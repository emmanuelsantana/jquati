import org.aspectj.lang.annotation.AdviceName;


public aspect AspectX {
	
	@AdviceName("doX")
	after() : execution(* *.doX(..)) {
		System.out.println("AspectX.doX execution");
	}
}
