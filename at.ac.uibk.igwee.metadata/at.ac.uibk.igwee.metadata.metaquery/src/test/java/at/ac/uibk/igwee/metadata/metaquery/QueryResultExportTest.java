package at.ac.uibk.igwee.metadata.metaquery;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;

import at.ac.uibk.igwee.metadata.metaquery.impl.QueryQueueFactoryImpl;
import at.ac.uibk.igwee.xslt.impl.SaxonXsltServiceImpl;

public class QueryResultExportTest {
	
	
	@Test
	public void test() throws Exception {
		
		QueryQueueFactoryImpl qqF = new QueryQueueFactoryImpl();
		qqF.setXsltService(new SaxonXsltServiceImpl());
		
		QueryQueue qq = qqF.loadQueryQueue(
				new FileInputStream(new File("./src/test/resources/queryQueueWithFixed.xml")));
		
		String export = qqF.exportResult(qq);
		System.out.println(export);
		
	}
	

}
