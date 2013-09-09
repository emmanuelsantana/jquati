package net.sf.jquati.context;

import java.util.Map;


public abstract class VerificationContext<T extends ExecutionContext> {
	/*
	 * A intencao expressa aqui 
	 * é de um contexto de verificacao que podera ser extendido apos o nosso trabalho
	 */

	
	public T getExecutionContext(Map<String,String[]> params) {
		return execute(prepareContext(params));
	}
	
	public abstract T execute(PreparedContext preparedContext);
	
	public abstract PreparedContext prepareContext(Map<String,String[]> params);
}
