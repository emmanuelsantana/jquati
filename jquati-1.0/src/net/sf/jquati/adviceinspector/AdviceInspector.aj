package net.sf.jquati.adviceinspector;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.jquati.executioncontext.impl.ClassExecutionContext;

import org.aspectj.lang.Aspects;
import org.aspectj.lang.annotation.AdviceName;

public aspect AdviceInspector {

	private List<String> mustExecuteList = new ArrayList<String>();
	private List<String> mustNotExecuteList = new ArrayList<String>();
	private List<String> hasExecuted = new ArrayList<String>();
	
	public ClassExecutionContext context;

	private AdviceInspector() {

	}

	public static AdviceInspector getInstance() {
		return Aspects.aspectOf(AdviceInspector.class);
	}

	pointcut adviceInspectorObject(Object a): adviceexecution() && this(a) && !within(AdviceInspector);

	before(Object a) : adviceInspectorObject(a) {
		System.out.println("* Advice Executed");
		StackTraceElement[] steList = Thread.currentThread().getStackTrace();
		StackTraceElement stackTraceElement = steList[2];
		String callName = stackTraceElement.getMethodName();
		Method[] methods = a.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equals(callName)) {
				AdviceName an = method.getAnnotation(AdviceName.class);
				if (an != null) {
					String adviceName = an.value();
					hasExecuted.add(adviceName);
				}
				break;
			}
		}
		System.out.println("* Advice Executed");
	}

	public void addMustExecuteAdvices(String[] advices) {
		for (String a : advices) {
			mustExecuteList.add(a);
		}
	}

	public void addMustNotExecuteAdvices(String[] advices) {
		for (String a : advices) {
			mustNotExecuteList.add(a);
		}
	}

	public void verify() {
		try {
				for (String a : hasExecuted) {
					System.out.println("has executed " + a);
					if (mustNotExecuteList.contains(a)) {
//						System.out.println("EXPECTATION FAIL: HAS EXECUTED : " + a);
						throw new AdviceInspectionException("Too strong pointcut descriptor, has executed : " + a);
					}
				}
				for (String a : mustExecuteList) {
					System.out.println("should execute " + a);
					if (!hasExecuted.contains(a) || (hasExecuted.size() > context.executionNumber)) {
						//System.out.println("EXPECTATION FAIL: HAS NOT EXECUTED : " + a);
						//throw new AdviceInspectionException("EXPECTATION FAIL: HAS NOT EXECUTED : " + a);
						throw new AdviceInspectionException("Too weak pointcut descriptor");
					}
			}
		} catch (AdviceInspectionException e) {
			throw e;
		} finally {
			clearLists();
		}
	}

	public void clearLists() {
		hasExecuted.clear();
		mustExecuteList.clear();
		mustNotExecuteList.clear();
		context.executionNumber = 0;
	}
}
