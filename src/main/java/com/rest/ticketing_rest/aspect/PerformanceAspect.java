package com.rest.ticketing_rest.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class PerformanceAspect {
    @Pointcut("@annotation(com.rest.ticketing_rest.annotation.ExecutionTime)")
    public void executionTimePC(){

    }

    @Around("executionTimePC()")
    public Object aroundAnyExecutionTimeAdvice(ProceedingJoinPoint proceedingJoinPoint){

        long beforeTime= System.currentTimeMillis();
        Object result = null;
        log.info("Execution starts:");

        try{
            result = proceedingJoinPoint.proceed();
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

        long afterTime = System.currentTimeMillis();

        log.info("Time taken to execute: {} ms - Method: {}",
                (afterTime-beforeTime), proceedingJoinPoint.getSignature().toShortString());

        return result;
    }

}
