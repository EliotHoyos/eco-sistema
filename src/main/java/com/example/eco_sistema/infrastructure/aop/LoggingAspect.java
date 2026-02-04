package com.example.eco_sistema.infrastructure.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(com.example.eco_sistema.infrastructure.controller..*)")
    public void controllerLayer() {}

    @Pointcut("within(com.example.eco_sistema.application.service..*)")
    public void serviceLayer() {}

    @Pointcut("within(com.example.eco_sistema.infrastructure.repository.adapter..*)")
    public void repositoryLayer() {}

    @Around("controllerLayer() || serviceLayer() || repositoryLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info(">>> Entrando al método: {} con argumentos: {}", methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;

            log.info("<<< Saliendo del método: {} | Tiempo de ejecución: {} ms", methodName, elapsedTime);

            return result;
        } catch (Exception e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.error("!!! Excepción en el método: {} | Tiempo antes de excepción: {} ms | Error: {}",
                    methodName, elapsedTime, e.getMessage());
            throw e;
        }
    }

    @AfterThrowing(pointcut = "controllerLayer() || serviceLayer() || repositoryLayer()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Excepción en {}.{}() con mensaje: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }
}
