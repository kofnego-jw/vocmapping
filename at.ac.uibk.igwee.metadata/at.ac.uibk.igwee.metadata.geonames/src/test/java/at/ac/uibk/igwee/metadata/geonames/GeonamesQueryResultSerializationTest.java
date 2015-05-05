package at.ac.uibk.igwee.metadata.geonames;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import at.ac.uibk.igwee.metadata.geonames.impl.GeonamesHttpQueryServiceImpl;
import at.ac.uibk.igwee.metadata.geonames.impl.XStreamSerializer;
import at.ac.uibk.igwee.metadata.httpclient.impl.HttpClientServiceImpl;
import at.ac.uibk.igwee.xslt.impl.SaxonXsltServiceImpl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GeonamesQueryResultSerializationTest {
	
	private static final boolean RUNTEST = false;
	
	private static final File OUTPUT_FILE = new File("./src/test/resources/result.xml");
	
	private static GeonamesQueryResult interTestResult;
	
	private static GeonamesHttpQueryServiceImpl service;
	
	@BeforeClass
	public static void setUpGeonamesQueryService() throws Exception {
		Assume.assumeTrue(RUNTEST);
		
		service = new GeonamesHttpQueryServiceImpl();
		HttpClientServiceImpl http = new HttpClientServiceImpl();
		service.setHttpClientService(http);
		service.setXsltService(new SaxonXsltServiceImpl());
	}
	
	@Test
	public void test01_createAResultObject() throws Exception {
		GeonamesQueryResult result = service.queryLocation("Innsbruck", 1, 1000);
		interTestResult = result;
	}
	
	@Test
	public void test02_saveResult() throws Exception {
		String data = XStreamSerializer.toXML(interTestResult);
		FileUtils.write(OUTPUT_FILE, data, "UTF-8");
	}
	
	@Test
	public void test03_readResult() throws Exception {
		InputStream in = new FileInputStream(OUTPUT_FILE);
		GeonamesQueryResult r = XStreamSerializer.fromXML(in);
		Assert.assertEquals(interTestResult, r);
	}
	
//	@Test
	public void test04_geonameIdResult() throws Exception {
		InputStream in = new FileInputStream(new File("./src/test/resources/geonameIdResult.xml"));
		
		SaxonXsltServiceImpl xsl = new SaxonXsltServiceImpl();
		InputStream result = xsl.doXslt(in, 
				getClass().getResourceAsStream("/xsl/geonames2data.xsl"), null);
		
		GeonameData data = XStreamSerializer.toGeonameData(result);
		Assert.assertEquals(2775220, data.getGeonameId());
		xsl.close();
	}

}
