package net.sf.classmock;

public abstract class MockClassLoader extends ClassLoader {
	public abstract Class defineClass(String name, byte[] b);
}
