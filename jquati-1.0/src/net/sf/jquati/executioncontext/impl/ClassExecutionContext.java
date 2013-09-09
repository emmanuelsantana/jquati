package net.sf.jquati.executioncontext.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jquati.context.ExecutionContext;
import net.sf.jquati.exception.ClassExecutionException;


public class ClassExecutionContext extends ExecutionContext {
	ClassCreationPreparedContext classCreationPreparedContext;
	Map<String, Object> classInstanceMap = new HashMap<String, Object>();
	public int executionNumber = 0;
	
	public void instanciateClasses() {
		for (String className : classCreationPreparedContext.getClassesList()) {
			executionNumber++;
			instanciateClass(className);
		}
	}
	public void instanciateClass(String className) {
		Class<?> newClass = classCreationPreparedContext.getClassMock(className).createClass();
		try {
			Object newClassInstance = newClass.newInstance();
			classInstanceMap.put(className, newClassInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void executeMethodOnAll(String name, Object... pars) throws ClassExecutionException {
		for (Iterator<String> iterator = classInstanceMap.keySet().iterator(); iterator.hasNext();) {
			String className = iterator.next();
			executeMethodOnClass(className, name, pars);
		}
	}
	public void executeMethodOnClass(String className, String name, Object... pars) throws ClassExecutionException {
		try {
			List<Class<?>> parList = new ArrayList<Class<?>>();
			for (Object o : pars) {
				parList.add(o.getClass());
			}
			Object classInstance = classInstanceMap.get(className);
			Method execute = classInstance.getClass().getMethod(name, parList.toArray(new Class[0]));
			System.out.println("VCC - executing method: " + name + "(" + pars + ")");
			executionNumber++;
			execute.invoke(classInstance, pars);
		} catch (Exception e) {
			throw new ClassExecutionException(e);
		}
	}
	public ClassCreationPreparedContext getClassCreationPreparedContext() {
		return classCreationPreparedContext;
	}
	public void setClassCreationPreparedContext(
			ClassCreationPreparedContext classCreationPreparedContext) {
		this.classCreationPreparedContext = classCreationPreparedContext;
	}
	
	public String toString() {
		return "ClassExecutionContext.toString";
	}
}
