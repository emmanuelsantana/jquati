import net.sf.jquati.annotations.ExecutionContextCreation;
import net.sf.jquati.annotations.ExecutionContextElement;
import net.sf.jquati.annotations.MustExecute;
import net.sf.jquati.executioncontext.impl.ClassExecutionContext;
import net.sf.jquati.test.JQuatiRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@ExecutionContextCreation(
		classNames={"X","Y"},
		methods={
				"void doX()",
				"void doY()"
				}
)
@RunWith(JQuatiRunner.class)
public class AspectXTest {
	@ExecutionContextElement
	ClassExecutionContext cec;

	@Before
	public void setup() {
		cec.instanciateClasses();
	}
	
	@Test
	@MustExecute({"doX"})
	public void shouldBeOutOfSquareLimitArea() throws Exception {
		cec.executeMethodOnAll("doX");
	}
}
