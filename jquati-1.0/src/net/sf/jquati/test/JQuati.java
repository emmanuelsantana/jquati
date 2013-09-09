package net.sf.jquati.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import net.sf.classmock.WeavingMockClassLoader;
import net.sf.jquati.adviceinspector.AdviceInspector;
import net.sf.jquati.adviceinspector.AdviceInspectorElement;
import net.sf.jquati.annotations.ExecutionContextCreation;
import net.sf.jquati.annotations.ExecutionContextElement;
import net.sf.jquati.annotations.MustExecute;
import net.sf.jquati.annotations.MustNotExecute;
import net.sf.jquati.annotations.TestedAspect;
import net.sf.jquati.contextcreator.VerificationContextCreator;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;


public class JQuati extends BlockJUnit4ClassRunner {
	ExecutionContextCreation ecc;
	//Class<?> testedAspect;
	AdviceInspector adviceInspector = AdviceInspector.getInstance();

	public JQuati(Class<?> klass) throws InitializationError {
		super(klass);
		ecc = klass.getAnnotation(ExecutionContextCreation.class);
//		TestedAspect testedAspectAnnotation = klass.getAnnotation(TestedAspect.class);
//		testedAspect = testedAspectAnnotation.value();
		WeavingMockClassLoader.setXmlPath("/DESENVOLVIMENTO/WORKSPACES/ASPECTJ/ExampleCollection/src/META-INF");
	}

	@Override
	protected Object createTest() throws Exception {
		Object o = super.createTest();
		
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(ExecutionContextElement.class)) {
				HashMap<String, String[]> params = new HashMap<String, String[]>();
				params.put("methods", ecc.methods());
				params.put("classNames", ecc.classNames());
				//field.set(o, VerificationContextCreator.createContext(params));
				field.setAccessible(true);
				field.set(o, VerificationContextCreator.createContext(params));
			}
			if (field.isAnnotationPresent(AdviceInspectorElement.class)) {
				field.setAccessible(true);
				field.set(o, adviceInspector);
			}
		}
		return o;
	}

	@Override
	protected Statement methodInvoker(FrameworkMethod m, Object test) {
		final Statement s = super.methodInvoker(m, test);
		
		Method method = m.getMethod();
		if(method.isAnnotationPresent(MustExecute.class)) {
			MustExecute mustExecute = method.getAnnotation(MustExecute.class);
			adviceInspector.addMustExecuteAdvices(mustExecute.value());
		}
		
		if(method.isAnnotationPresent(MustNotExecute.class)) {
			MustNotExecute mustNotExecute = method.getAnnotation(MustNotExecute.class);
			adviceInspector.addMustNotExecuteAdvices(mustNotExecute.value());
		}
		
		Statement returnStatement = new Statement() {
			@Override
			public void evaluate() throws Throwable {
				s.evaluate();
				adviceInspector.verify();
			}
		};
		
		return returnStatement;
	}
}
