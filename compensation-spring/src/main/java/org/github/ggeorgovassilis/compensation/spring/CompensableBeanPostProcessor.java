package org.github.ggeorgovassilis.compensation.spring;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.github.ggeorgovassilis.compensation.api.CompensationAdvice;
import org.github.ggeorgovassilis.compensation.api.CompensationManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

public class CompensableBeanPostProcessor implements BeanPostProcessor,
		ApplicationContextAware {

	protected CompensationManager compensationManager;
	protected Logger logger = Logger
			.getLogger(CompensableBeanPostProcessor.class);
	protected ApplicationContext context;

	protected Annotation getShallowCompensableAnnotationFromClass(Class c) {
		for (Annotation a : c.getAnnotations())
			if (Compensable.class.isAssignableFrom(a.annotationType()))
				return a;
		return null;
	}

	protected Set<Class> getAllClassesOrInterfacesThatHaveTheCompensableAnnotation(
			Class c) {
		Set<Class> classes = new HashSet<Class>();
		if (getShallowCompensableAnnotationFromClass(c) != null)
			classes.add(c);
		for (Class cTmp : c.getClasses())
			classes.addAll(getAllClassesOrInterfacesThatHaveTheCompensableAnnotation(cTmp));
		for (Class cTmp : c.getInterfaces())
			classes.addAll(getAllClassesOrInterfacesThatHaveTheCompensableAnnotation(cTmp));
		return classes;
	}

	protected Set<Class> getInterfaces(Collection<Class> classes) {
		Set<Class> interfaces = new HashSet<Class>();
		for (Class c : classes) {
			interfaces.addAll(getInterfaces(c));
		}
		return interfaces;
	}

	protected Set<Class> getInterfaces(Class c) {
		if (c == null)
			return new HashSet<Class>();
		Set<Class> interfaces = new HashSet<Class>();
		if (c.isInterface())
			interfaces.add(c);
		for (Class ci : c.getInterfaces()) {
			interfaces.addAll(getInterfaces(ci));
		}
		interfaces.addAll(getInterfaces(c.getSuperclass()));
		return interfaces;
	}

	protected Annotation getCompensableAnnotation(Collection<Class> classes) {
		for (Class c : classes) {
			Annotation a = getShallowCompensableAnnotationFromClass(c);
			if (a != null)
				return a;
		}
		return null;
	}

	public void setCompensationManager(CompensationManager compensationManager) {
		this.compensationManager = compensationManager;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		Set<Class> classes = getAllClassesOrInterfacesThatHaveTheCompensableAnnotation(bean
				.getClass());
		Set<Class> interfaces = getInterfaces(classes);
		Class[] ainterfaces = new Class[classes.size()];
		int i = 0;
		for (Class c : interfaces)
			ainterfaces[i++] = c;
		Annotation compensable = getCompensableAnnotation(classes);
		if (compensable == null)
			return bean;
		String adviceBeanName = ((Compensable) compensable).value();
		CompensationAdvice advice = (CompensationAdvice) context
				.getBean(adviceBeanName);
		return compensationManager.createProxyFor(bean, ainterfaces, advice);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
	}

}
