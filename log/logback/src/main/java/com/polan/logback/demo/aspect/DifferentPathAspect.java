package com.polan.logback.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <p>指定目录打印日志</p>
 * @author panliyong  2019-07-25 15:26
 */
@Aspect
@Component
@Slf4j(topic = "slowSql")
public class DifferentPathAspect {

    @Around("repositoryOps()")
    public Object logPerformance(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        String name = "-";
        String result = "Y";
        String targetClassName = "-";
        try {

            Signature signature = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method targetMethod = methodSignature.getMethod();

            Object target = pjp.getTarget();
            targetClassName = target.getClass().getSimpleName();
            // name = targetClassName + "." + targetMethod.getName();
            // return pjp.proceed();

            name = pjp.getSignature().toShortString();
            return pjp.proceed();
        } catch (Throwable t) {
            result = "N";
            throw t;
        } finally {
            long endTime = System.currentTimeMillis();
            if (!name.contains("CrudRepository")) {
                log.info("{};{};{}ms", name, result, endTime - startTime);
            }
        }
    }

    @Pointcut("execution(* com.polan.logback.demo..*(..))")
    private void repositoryOps() {
    }
}
