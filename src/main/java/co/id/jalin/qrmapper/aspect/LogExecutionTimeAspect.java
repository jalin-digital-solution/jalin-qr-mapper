package co.id.jalin.qrmapper.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class LogExecutionTimeAspect {

    @Around("@annotation(co.id.jalin.qrmapper.aspect.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String fullMethodName = className + "." + methodName;
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("{}() - {} ms", fullMethodName, duration);
            return result;
        } catch (Throwable t) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("{}() - {} ms - Cause: {}", fullMethodName, duration, t.getMessage());
            throw t;
        }
    }
}
