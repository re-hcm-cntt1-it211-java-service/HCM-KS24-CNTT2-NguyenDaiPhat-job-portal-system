package com.ptit.jobportalsystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Around("""
    execution(* ..service..*(..))
    && !execution(* ..CurrentUserService.*(..))
    && !execution(* ..TokenBlacklistService.isBlacklisted(..))
    """)
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long duration =
                    System.currentTimeMillis() - start;

            log.info(
                    "METHOD_EXECUTION method={} duration={}ms",
                    joinPoint.getSignature().getName(),
                    duration
            );
        }
    }

}
