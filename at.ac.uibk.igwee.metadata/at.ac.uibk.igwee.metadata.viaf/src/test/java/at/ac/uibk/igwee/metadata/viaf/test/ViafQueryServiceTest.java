package at.ac.uibk.igwee.metadata.viaf.test;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import at.ac.uibk.igwee.metadata.httpclient.HttpClientService;
import at.ac.uibk.igwee.metadata.httpclient.impl.HttpClientServiceImpl;
import at.ac.uibk.igwee.metadata.viaf.ViafQueryResult;
import at.ac.uibk.igwee.metadata.viaf.ViafVocabulary;
import at.ac.uibk.igwee.metadata.viaf.impl.ViafQueryServiceImpl;
import at.ac.uibk.igwee.metadata.viaf.impl.ViafRespToResult;
import at.ac.uibk.igwee.xslt.XsltService;
import at.ac.uibk.igwee.xslt.impl.SaxonXsltServiceImpl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ViafQueryServiceTest {

	private static final boolean RUN_TESTS = false;
	
	
	private static ViafQueryServiceImpl service;

	@BeforeClass
	public static void setUpService() {
		
		
		Assume.assumeTrue(RUN_TESTS);
		
		service = new ViafQueryServiceImpl();

		XsltService xs = new SaxonXsltServiceImpl();

		ViafRespToResult r2r = new ViafRespToResult();
		
		HttpClientService httpClientService = new HttpClientServiceImpl(); 

		r2r.setXsltService(xs);

		service.setViafRespToResult(r2r);
		
		service.setHttpClientService(httpClientService);
	}

	@Test
	public void test01_queryForHorvat() throws Exception {

		ViafQueryResult result = service
				.queryForPersonalName("Ödön von Horvath");

		ViafXStreamTest.output(result);

	}

	@Test
	public void test02_queryForJaneAusten() throws Exception {
		ViafQueryResult result = service
				.queryForPersonalName("Jane Austen", 1, 20, null);

		ViafXStreamTest.output(result);
	}
	
	@Test
	public void test03_queryForBrennerArchives() throws Exception {
		ViafQueryResult result = service
				.queryForCorporateName("Brenner Archiv");

		ViafXStreamTest.output(result);
	}
	
	@Test
	public void test04_queryForInnsbruck() throws Exception {
		ViafQueryResult result = service
				.queryForGeographicalName("Innsbruck");

		ViafXStreamTest.output(result);
	}
	
	@Test
	public void test05_queryForLondon() throws Exception {
		ViafQueryResult result = service
				.queryForAll("Krakauer", 1, 20, "DNB");

		ViafXStreamTest.output(result);
	}
	
	@Test
	public void test06_queryID() throws Exception {
		ViafVocabulary voc = service.queryId("95147466");
		
		System.out.println(voc);
		System.out.println(voc.getName());
		System.out.println(voc.getURI());
		
	}
	

}
