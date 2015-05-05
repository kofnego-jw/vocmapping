package kofnego.editing.xslt.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

import at.ac.uibk.igwee.xslt.XsltService;
import at.ac.uibk.igwee.xslt.impl.SaxonXsltServiceImpl;

public class XsltTestSuite extends TestCase {
	
	static final File inputFile = new File("./src/test/resources/input.xml");
	
	static final File xsltFile = new File("./src/test/resources/sample.xsl");
	
	static final File outputFile = new File("./src/test/resources/output.xml");
	
	private XsltService xsltService;
	
	public void setUp() {
		this.xsltService = new SaxonXsltServiceImpl();
	}
	
	public void testSampleTransformation() throws Exception {
		InputStream in = xsltService.doXslt(new FileInputStream(inputFile), 
				new FileInputStream(xsltFile), null);
		FileUtils.copyInputStreamToFile(in, outputFile);
	}

}
