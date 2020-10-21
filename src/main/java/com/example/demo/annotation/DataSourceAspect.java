package com.example.demo.annotation;

import com.example.demo.config.DataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
//优先级设置到最高
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
public class DataSourceAspect {


    @Pointcut("@annotation(SwitchSource)")
    public void pointcut() {
    }

    /**
     * 在方法执行之前切换到指定的数据源
     * @param joinPoint
     */
    @Before(value = "pointcut()")
    public void beforeOpt(JoinPoint joinPoint) {
        /*因为是对注解进行切面，所以这边无需做过多判定，直接获取注解的值，进行环绕，将数据源设置成远方，然后结束后，清楚当前线程数据源*/
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        SwitchSource switchSource = method.getAnnotation(SwitchSource.class);
        log.info("[Switch DataSource]:" + switchSource.value());
        DataSourceHolder.setDataSource(switchSource.value());
    }

    /**
     * 方法执行之后清除掉ThreadLocal中存储的KEY，这样动态数据源会使用默认的数据源
     */
    @After(value = "pointcut()")
    public void afterOpt() {
        DataSourceHolder.clearDataSource();
        log.info("[Switch Default DataSource]");
    }

}
