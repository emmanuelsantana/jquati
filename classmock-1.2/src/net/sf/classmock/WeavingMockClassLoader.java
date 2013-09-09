package net.sf.classmock;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.loadtime.Aj;
import org.aspectj.weaver.loadtime.ClassPreProcessor;

public class WeavingMockClassLoader extends MockClassLoader {
	private static String aspectUnderTest;
	private static String xmlPath;
	private static ClassPreProcessor classPreProcessor;
	private static List<ClassData> byteCodeList = new ArrayList<ClassData>();
	
	private String xml = "<?xml version='1.0' encoding='UTF-8'?>" +
	"<aspectj>" +
		"<aspects>" +
			"<aspect name=\"AspectX\"/>" + 
			"<aspect name=\"net.sf.jquati.adviceinspector.AdviceInspector\"/>" + 
		"</aspects>" + 
		"<weaver options=\"-Xreweavable -debug -verbose -showWeaveInfo\">" + 
			"<include within=\"X\" />" + 
			"<include within=\"Y\" />" +
			"<include within=\"AspectX\" />" +
		"</weaver>" +
	"</aspectj>";
	
	static {
		try {
			classPreProcessor = new Aj();
			classPreProcessor.initialize();
		} catch (Exception e) {
			throw new ExceptionInInitializerError(
					"Error initializing classPreProcessor");
		}
	}

	public Class defineClass(String name, byte[] b) {
		buildAOPXml();
		b = classPreProcessor.preProcess(name, b, this);
		return defineClass(name, b, 0, b.length);
	}
	
	private void buildAOPXml() {
		try {
			File file = new File(xmlPath + "/aop-ajc.xml");
			if (!file.exists()) file.createNewFile();
			FileWriter fw = new FileWriter(file);
			// write the aspects part
			// write the weaver part
//			fw.append(xml);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void setAspectUnderTest(String aspectUnderTest) {
		WeavingMockClassLoader.aspectUnderTest = aspectUnderTest;
	}
	
	public static void setXmlPath(String path) {
		WeavingMockClassLoader.xmlPath = path;
	}
}
