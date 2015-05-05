package at.ac.uibk.igwee.metadata.geonames;

import java.io.File;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.uibk.igwee.metadata.geonames.impl.GeonamesHttpQueryServiceImpl;
import at.ac.uibk.igwee.metadata.httpclient.impl.HttpClientServiceImpl;
import at.ac.uibk.igwee.xslt.XsltService;
import at.ac.uibk.igwee.xslt.impl.SaxonXsltServiceImpl;


public class GeonamesHttpQueryServiceImplTest {
	
	private static final boolean RUNTEST = false;
	
	public static final File OUTPUT_FILE = new File("./src/test/resources/geonamesCall.xml");

	public static final String ID_INNSBRUCK = "2775220";
	
	private static GeonamesHttpQueryServiceImpl impl;
	
	@BeforeClass
	public static void setUpImpl() throws Exception {
		
		Assume.assumeTrue(RUNTEST);
		
		HttpClientServiceImpl hc = new HttpClientServiceImpl();
		
		XsltService xs = new SaxonXsltServiceImpl();
		
		impl = new GeonamesHttpQueryServiceImpl();
		impl.setHttpClientService(hc);
		impl.setXsltService(xs);
	}
	
	@Test
	public void test01_testHttpCall() throws Exception {
		
		GeonamesQueryResult r = impl.queryLocationInCountry("innsbruck", "AT", 1, 1000);
		
		output(r);
		
	}
	
	@Test
	public void test02_testIdQuery() throws Exception {
		GeonameData data = impl.queryId(ID_INNSBRUCK);
		Assert.assertEquals("Innsbruck", data.getName());
		
	}
	

	public static void output(GeonamesQueryResult res) {
		System.out.println();
		System.out.println("Totalhits:  " + res.getTotalhits());
		System.out.println("Start row:  " + res.getStartRow());
		System.out.println("Maximum row:" + res.getMaxRows());
		
		res.getResults().stream().forEach(data -> {
			System.out.println("  Name: " + data.getName());
			System.out.println("    Country " + data.getCountry() + " (" + data.getCountryCode() 
					+ "/" + data.getContinentCode() + ")");
			System.out.println("    URI: " + data.getURI());
		});
		
		
	}
}
