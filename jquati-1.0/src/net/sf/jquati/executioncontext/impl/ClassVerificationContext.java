package net.sf.jquati.executioncontext.impl;

import java.util.Map;

import net.sf.jquati.context.ExecutionContext;
import net.sf.jquati.context.PreparedContext;
import net.sf.jquati.context.VerificationContext;


public class ClassVerificationContext extends VerificationContext<ClassExecutionContext> {

	@Override
	public ClassExecutionContext execute(PreparedContext preparedContext) {
		ClassExecutionContext classExecutionContext = new ClassExecutionContext();
		classExecutionContext.setClassCreationPreparedContext((ClassCreationPreparedContext)preparedContext);
		return classExecutionContext;
	}

	@Override
	public ClassCreationPreparedContext prepareContext(Map<String, String[]> params) {
		ClassCreationPreparedContext preparedContext = new ClassCreationPreparedContext();
		preparedContext.createClassMockMap(params.get("classNames"));
		String[] methods = params.get("methods");
		for (String method : methods) {
			preparedContext.addPublicMethodToAll(method);
		}
		
		return preparedContext;
	}

}
