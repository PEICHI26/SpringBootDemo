package com.mongoddemo.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
	//此處意味著要切入點套用到 com.mongoddemo.demo.service 這個套件及子套件下所有元件類別的 public 方法
	@Pointcut("execution(* com.mongoddemo.demo.service..*(..))")
	public void pointCut() {

	}

	//它會在原方法執行前，先執行此處的程式
	//共通工作可看做是 Aspect 建議原方法額外執行的工作，因此被稱為「Advice」
	//	@Before("pointCut()")
	//	public void before(JoinPoint joinPoint) {
	//		System.out.println("=====before advice starts=====");
	//		System.out.println(getMethodName(joinPoint));
	//		System.out.println("=====before advice end=====");
	//	}
	//
	//	@AfterReturning(pointcut = "pointCut()", returning = "result")
	//	public void afterReturning(JoinPoint joinPoint, Object result) {
	//		System.out.println("=====after returning advice starts=====");
	//		System.out.println(getMethodName(joinPoint));
	//		System.out.println("Args: " + Arrays.toString(joinPoint.getArgs()));
	//		if (result != null) {
	//			System.out.println("result: " + result);
	//		}
	//		System.out.println("=====after returning advice ends=====");
	//	}

	//利用「Java Reflection」得到 Method 物件，再取出方法名稱。
	private String getMethodName(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getName();
	}

	//	@Around("pointCut()")
	//	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
	//		System.out.println("=====advice starts=====");
	//		long startTime = System.currentTimeMillis();
	//		Object result = joinPoint.proceed();
	//		System.out.println(getMethodName(joinPoint));
	//		long spentTime = System.currentTimeMillis() - startTime;
	//		System.out.println("Time spent: " + spentTime);
	//		System.out.println("=====advice ends=====");
	//
	//		return result;
	//	}

}
