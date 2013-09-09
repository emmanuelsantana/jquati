package net.sf.jquati.contextcreator;

import java.util.HashMap;

import net.sf.jquati.executioncontext.impl.ClassExecutionContext;
import net.sf.jquati.executioncontext.impl.ClassVerificationContext;


public class VerificationContextCreator {
	public static ClassExecutionContext createContext(HashMap<String, String[]> params) {
		ClassVerificationContext cvc = new ClassVerificationContext();
		return cvc.getExecutionContext(params);
	}
}
