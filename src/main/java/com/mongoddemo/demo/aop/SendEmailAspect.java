package com.mongoddemo.demo.aop;

import com.mongoddemo.demo.service.MailService;
import com.mongoddemo.demo.util.UserIdentity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@Aspect
@Component
public class SendEmailAspect {

	private static final Map<ActionType, String> SUBJECT_TEMPLATE_MAP;
	private static final Map<ActionType, String> MESSAGE_TEMPLATE_MAP;

	static {
		SUBJECT_TEMPLATE_MAP = new EnumMap<ActionType, String>(ActionType.class);
		SUBJECT_TEMPLATE_MAP.put(ActionType.CREATE, "New %s");
		SUBJECT_TEMPLATE_MAP.put(ActionType.UPDATE, "Update %s");
		SUBJECT_TEMPLATE_MAP.put(ActionType.DELETE, "Delete %s");

		MESSAGE_TEMPLATE_MAP = new EnumMap<ActionType, String>(ActionType.class);
		MESSAGE_TEMPLATE_MAP.put(ActionType.CREATE, "Hi, %s. There's a new %s (%s) created.");
		MESSAGE_TEMPLATE_MAP.put(ActionType.UPDATE, "Hi, %s. There's a %s (%s) updated.");
		MESSAGE_TEMPLATE_MAP.put(ActionType.DELETE, "Hi, %s. A %s (%s) is just deleted.");
	}

	@Autowired
	private UserIdentity userIdentity;
	@Autowired
	private MailService mailService;

	@Pointcut("@annotation(com.mongoddemo.demo.aop.SendEmail)")
	public void pointCut() {

	}

	@AfterReturning(pointcut = "pointCut()", returning = "result")
	public void sendEmail(JoinPoint joinPoint, Object result) throws NoSuchFieldException, IllegalAccessException {
		System.out.println("=====after returning advice starts=====");
		System.out.println("Method: " + getMethodName(joinPoint));
		System.out.println("Args: " + Arrays.toString(joinPoint.getArgs()));
		System.out.println("result: " + result);
		if (userIdentity.isAnonymous()) {
			return;
		}
		SendEmail annotation = getAnnotation(joinPoint);
		String subject = composeSubject(annotation);
		String content = composeContent(annotation, joinPoint, result);
		mailService.sendMail(subject, content, Collections.singletonList(userIdentity.getEmail()));
		System.out.println("=====after returning advice ends=====");
	}

	private String getMethodName(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getName();
	}

	private String composeContent(SendEmail annotation, JoinPoint joinPoint, Object result) throws NoSuchFieldException, IllegalAccessException {
		String template = MESSAGE_TEMPLATE_MAP.get(annotation.action());
		int idParamIndex = annotation.idParamIndex();
		String entityId = idParamIndex == -1 ? getEntityId(result) : (String) joinPoint.getArgs()[idParamIndex];
		return String.format(template, userIdentity.getName(), annotation.entity().toString().toLowerCase(), entityId);
	}

	private String getEntityId(Object result) throws NoSuchFieldException, IllegalAccessException {
		Field field = result.getClass().getDeclaredField("id");
		field.setAccessible(true);
		return (String) field.get(result);
	}

	private String composeSubject(SendEmail annotation) {
		String template = SUBJECT_TEMPLATE_MAP.get(annotation.action());
		return String.format(template, annotation.entity().toString().toLowerCase());
	}

	private SendEmail getAnnotation(JoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		return methodSignature.getMethod().getAnnotation(SendEmail.class);
	}
}
