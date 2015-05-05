package at.ac.uibk.igwee.metadata.httpclient.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.uibk.igwee.metadata.httpclient.HttpClientService;
import at.ac.uibk.igwee.metadata.httpclient.ParameterPair;
import at.ac.uibk.igwee.metadata.httpclient.impl.HttpClientServiceImpl;

public class HttpClientServiceTest {

	private static HttpClientService service;
	
	private static final boolean EXECUTE_TEST = false;
	
	@BeforeClass
	public static void setUpHttpClientService() {
		Assume.assumeTrue(EXECUTE_TEST);
		service = new HttpClientServiceImpl();
	}
	
	@Test
	public void test01_httpGet() throws Exception {
		
		service.executeHttpGet("www.uibk.ac.at", "/index.html", null);
		
	}
	
	@Test
	public void test02_httpGetWithParams() throws Exception {
		List<ParameterPair> ppList = Arrays.asList(
				new ParameterPair("action", "wbgetentities"),
				new ParameterPair("ids", "Q1735|Q42"),
				new ParameterPair("format", "xml")
				);
		
		service.executeHttpGet("www.wikidata.org", "/w/api.php", ppList);
		
	}
	
	@Test
	public void test03_httpPost() throws Exception {
		List<ParameterPair> ppList = Arrays.asList(
				new ParameterPair("queryString", "Glaube")
				);
		
		service.executeHttpPost("webapp.uibk.ac.at", 
				"/brenner-archiv/BustaSearch/search/ajax/simpleSearch.action", ppList);
		
	}
	
}
