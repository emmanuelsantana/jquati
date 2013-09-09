package net.sf.jquati.executioncontext.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.classmock.ClassMock;
import net.sf.jquati.context.PreparedContext;

public class ClassCreationPreparedContext implements PreparedContext {
	Map<String, ClassMock> classMockMap = new HashMap<String, ClassMock>();

	public void addPublicMethodToAll(String method) {
		for (Iterator<String> iterator = classMockMap.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			ClassMock cm = addPublicMethod(key, method);
			classMockMap.put(key, cm);
		}
	}
	public ClassMock addPublicMethod(String className, String method) {
		ClassMock classMock;
		String[] modifiers = method.split(" ");
		String signature = "";
		Class<?>[] params = new Class[0];
		Class<?> returnType = Void.class;
		try {
			if(modifiers.length > 1) {
				signature = modifiers[1].substring(0,modifiers[1].indexOf("("));
				String returnTypeName = modifiers[0];
				if(!returnTypeName.equals("void")) {
					returnTypeName = fixTypeName(returnTypeName);
					returnType = Class.forName(returnTypeName);
				}
			} else {
				signature = modifiers[0].substring(0,modifiers[0].indexOf("("));
			}
			params = getMethodParams(method);
		} catch (Exception e) {	}
		
		classMock = classMockMap.get(className).addMethod(returnType, signature, params);
		return classMock;
	}
	private String fixTypeName(String returnTypeName) {
		if(returnTypeName.indexOf(".") == -1) {
			returnTypeName = "java.lang." + returnTypeName;
		}
		return returnTypeName;
	}
	private Class<?>[] getMethodParams(String method) throws ClassNotFoundException {
		String paramsString = method.substring(method.indexOf("(")+1, method
				.indexOf(")"));
		List<Class<?>> paramList = new ArrayList<Class<?>>();
		if (paramsString.length() > 0) {
			String[] params = paramsString.split(",");
			for (String p : params) {
				p = p.trim();
				Class<?> clazz = Class.forName(fixTypeName(p));
				paramList.add(clazz);
			}
		}
		return paramList.toArray(new Class[0]);		
	}
	public void createClassMockMap(String[] classNames) {
		for (String className : classNames) {
			createClassMock(className);
		}
	}
	public void createClassMock(String className) {
		ClassMock cm = new ClassMock(className);
		cm.setRealName(className);
		classMockMap.put(className, cm);
	}
	public ClassMock getClassMock(String className) {
		return classMockMap.get(className);
	}
	public List<String> getClassesList() {
		List<String> classList = new ArrayList<String>();
		for (Iterator<String> iterator = classMockMap.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			classList.add(key);
		}
		return classList;
	}
}
