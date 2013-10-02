package org.github.ggeorgovassilis.compensation.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.github.ggeorgovassilis.compensation.api.CompensationAdvice;

public class AdviceProxy implements InvocationHandler {

    protected CompensationAdvice advice;

    private Method findMethodOnAdvice(Method method, CompensationAdvice advice)
	    throws Exception {
	Class<?>[] argumentTypes = method.getParameterTypes();
	Method adviceMethod = advice.getClass().getMethod(method.getName(),
		argumentTypes);
	return adviceMethod;
    }

    public AdviceProxy(CompensationAdvice advice) {
	this.advice = advice;
    }
    
    @Override
    public Object invoke(Object target, Method method, Object[] arguments)
	    throws Throwable {
	Method adviceMethod = findMethodOnAdvice(method, advice);
	return adviceMethod.invoke(advice, arguments);
    }

}
