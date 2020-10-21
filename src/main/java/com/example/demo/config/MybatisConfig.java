package com.example.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.demo.annotation.SwitchSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MybatisConfig {

    /**
     * @Bean：向IOC容器中注入一个Bean
     * @ConfigurationProperties：使得配置文件中以spring.datasource为前缀的属性映射到Bean的属性中
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean("dataSource")
    public DataSource dataSource(){
        return new DruidDataSource();
    }

    /**
     * 向IOC容器中注入另外一个数据源
     * 全局配置文件中前缀是spring.datasource.his
     */
    @Bean(name = SwitchSource.DEFAULT_NAME)
    @ConfigurationProperties(prefix = "spring.datasource.his")
    public DataSource hisDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 构建一个动态数据源，用以数据源的切换
     * @param dataSource
     * @param hisDataSource
     * @return
     */
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dynamicDataSource(@Qualifier("dataSource") DataSource dataSource, @Qualifier("hisDataSource") DataSource hisDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        //放置目标数据源
        targetDataSources.put(SwitchSource.DEFAULT_NAME, hisDataSource);
        //构建动态数据源
        return new DynamicDataSource(dataSource, targetDataSources);
    }

    /**
     * 注入一个SqlSessionFactory（这个是单一数据源的配置，整合多数据源后可以去掉）
     */
    @Bean("sqlSessionFactory1")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        // 自动将数据库中的下划线转换为驼峰格式
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setDefaultFetchSize(100);
        configuration.setDefaultStatementTimeout(30);
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean.getObject();
    }


    /**
     * 创建动态数据源的SqlSessionFactory，传入的是动态数据源
     * @Primary这个注解很重要，如果项目中存在多个SqlSessionFactory，这个注解一定要加上
     */
    @Primary
    @Bean("sqlSessionFactory2")
    public SqlSessionFactory sqlSessionFactoryBean(DynamicDataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        // 自动将数据库中的下划线转换为驼峰格式
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setDefaultFetchSize(100);
        configuration.setDefaultStatementTimeout(30);
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 重写事务管理器，管理动态数据源
     */
    @Primary
    @Bean(value = "transactionManager2")
    public PlatformTransactionManager annotationDrivenTransactionManager(DynamicDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }



}
