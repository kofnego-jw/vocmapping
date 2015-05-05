package kofnego.editing.xslt.impl;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import at.ac.uibk.igwee.xslt.impl.SaxonXQueryServiceImpl;

public class XQueryServiceTest {
	
	
	@Test
	public void test() throws Exception {
		SaxonXQueryServiceImpl xqImpl = new SaxonXQueryServiceImpl();
		File xq= new File("./src/test/resources/xqueryTest/xquery.xql");
		
		URI xqlUri = xq.toURI();
		System.out.println(xqlUri);
		InputStream result = xqImpl.doXQuery(xqlUri);
		IOUtils.copy(result, System.out);
	}

}
