package com.goalglo.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

   @Before("execution(* com.goalglo..*(..))")
   public void logMethodEntry(JoinPoint joinPoint) {
      logMethod(joinPoint, "Entering", joinPoint.getArgs(), null);
   }

   @AfterReturning(pointcut = "execution(* com.goalglo..*(..))", returning = "result")
   public void logMethodExit(JoinPoint joinPoint, Object result) {
      logMethod(joinPoint, "Exiting", result, null);
   }

   @AfterThrowing(pointcut = "execution(* com.goalglo..*(..))", throwing = "ex")
   public void logMethodException(JoinPoint joinPoint, Throwable ex) {
      logMethod(joinPoint, "Exception", null, ex);
   }

   private void logMethod(JoinPoint joinPoint, String action, Object obj, Throwable ex) {
      String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
      String methodName = joinPoint.getSignature().getName();

      Logger log = LoggerFactory.getLogger(className);

      if ("Exception".equals(action)) {
         log.error("Exception in method: {}() with cause = {}",methodName, ex != null && ex.getCause() != null ? ex.getCause() : "NULL");
      } else {
         log.info("{}() : {} with {} = {}", methodName, action, action.equals("Entering") ? "arguments" : "result", obj);
      }
   }
}