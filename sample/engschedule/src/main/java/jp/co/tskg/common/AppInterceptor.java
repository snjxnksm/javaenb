package jp.co.example.common;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * メソッドの実行前と実行後にログを出力する。
 *
 * @author user01
 *
 */
@Slf4j
@Aspect
@Component
public class AppInterceptor {

  @Around("execution(* jp.co.example.controller..*.*(..))")
  public Object controllerInvoke(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Object ret = null;
    try {
	  // 実行**前**ログ出力
	  log.info(AppLogMessageConstants.BEFORE_INVOKE.getMessage(),
			  Arrays.toString(proceedingJoinPoint.getArgs()),
			  proceedingJoinPoint.getTarget().getClass() ,
			  proceedingJoinPoint.getSignature().getName());
	  // メソッド実行
	  ret = proceedingJoinPoint.proceed();
	  return ret;
    } finally {
      // 実行**後**ログ出力
      log.info(AppLogMessageConstants.AFTER_INVOKE.getMessage(),
    		  proceedingJoinPoint.getTarget().getClass(),
    		  proceedingJoinPoint.getSignature().getName());
    }
  }
}
