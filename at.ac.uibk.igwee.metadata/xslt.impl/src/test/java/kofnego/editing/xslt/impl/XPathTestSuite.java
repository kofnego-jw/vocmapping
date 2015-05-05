package kofnego.editing.xslt.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import junit.framework.TestCase;
import at.ac.uibk.igwee.xslt.XPathService;
import at.ac.uibk.igwee.xslt.impl.SaxonXPathServiceImpl;

public class XPathTestSuite extends TestCase {
	
	private static final File inputFile = new File("./src/test/resources/input.xml");
	
	private static final String XPATH = "//@key";
	
	private XPathService xpathService;
	
	public void setUp() {
		this.xpathService = new SaxonXPathServiceImpl();
	}
	
	public void testXPath() throws Exception {
		List<String> result = 
				this.xpathService.evaluateAsStringList(new FileInputStream(inputFile), XPATH, null);
		assert(result!=null);
		assert(result.size()==1);
		assert(result.get(0).equals("value"));
		
	}

}
