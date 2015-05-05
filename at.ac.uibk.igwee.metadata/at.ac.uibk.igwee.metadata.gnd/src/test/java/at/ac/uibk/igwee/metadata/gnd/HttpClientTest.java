package at.ac.uibk.igwee.metadata.gnd;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.uibk.igwee.metadata.httpclient.HttpClientService;
import at.ac.uibk.igwee.metadata.httpclient.ParameterPair;
import at.ac.uibk.igwee.metadata.httpclient.impl.HttpClientServiceImpl;

public class HttpClientTest {
	
	private static final boolean RUNTEST = false;
	
	@BeforeClass
	public static void setUpTest() throws Exception {
		
		Assume.assumeTrue(RUNTEST);
	}
	
	@Test
	public void test_makeQuery() throws Exception {
		
		HttpClientService http = new HttpClientServiceImpl();
		
		List<ParameterPair> params = Arrays.asList(
				new ParameterPair("version", "1.1"),
				new ParameterPair("operation", "searchRetrieve"),
				new ParameterPair("recordSchema", "RDFxml"),
				new ParameterPair("query", "PER=\"Einstein\" and BBG=\"Tp*\""),
				new ParameterPair("maximumRecords", "30"),
				new ParameterPair("startRecord", "5")
				);
		
		
		InputStream answer = http.executeHttpGet("services.dnb.de", "/sru/authorities", params);
		
		FileUtils.copyInputStreamToFile(answer, new File("./src/test/resources/queryEinstein.xml"));
		
	}

}
