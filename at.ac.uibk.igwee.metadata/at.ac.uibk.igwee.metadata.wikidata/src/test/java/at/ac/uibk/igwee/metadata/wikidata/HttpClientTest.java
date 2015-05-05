package at.ac.uibk.igwee.metadata.wikidata;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.uibk.igwee.metadata.httpclient.HttpClientService;
import at.ac.uibk.igwee.metadata.httpclient.impl.HttpClientServiceImpl;
import at.ac.uibk.igwee.metadata.wikidata.impl.HttpQueryHelper;
import at.ac.uibk.igwee.xslt.XPathService;
import at.ac.uibk.igwee.xslt.XsltService;
import at.ac.uibk.igwee.xslt.impl.SaxonXPathServiceImpl;
import at.ac.uibk.igwee.xslt.impl.SaxonXsltServiceImpl;

public class HttpClientTest {
	
	private static final boolean RUNTEST = false;
	
	private static HttpQueryHelper helper;
	
	@BeforeClass
	public static void setUpHttpQueryHelper() throws Exception {
		Assume.assumeTrue(RUNTEST);
		helper = new HttpQueryHelper();
		XsltService xs = new SaxonXsltServiceImpl();
		helper.setXsltService(xs);
		XPathService xps = new SaxonXPathServiceImpl();
		helper.setXPathService(xps);
		HttpClientService hs = new HttpClientServiceImpl();
		helper.setHttpClientService(hs);
	}
	
	@Test
	public void test01_QueryForInnsbruck() throws Exception {
		
		helper.query("Innsbruck", 0, 10);
		
	}
	
	@Test
	public void test02_QueryForTitles() throws Exception {
		
		List<String> titles = IntStream.iterate(51, i -> i+1)
			.limit(50)
			.mapToObj(i -> "Q" + Integer.toString(i))
			.collect(Collectors.toList());
		
		InputStream in = helper.queryEntities(titles);
		
		FileUtils.copyInputStreamToFile(in, new File("./src/test/resources/getEntitiesResult3.xml"));
		
	}
	
	@Test
	public void test03_queryForReal() throws Exception {
		
		String qs = "London";
		
		WikidataQueryResult result = helper.query(qs, 0, 100);
		
		output(result);
	}
	
	public static void output(WikidataQueryResult result ) {
		System.out.println();
		System.out.println("WikidataQueryResult: ");
		System.out.println("  Totalhits: " + result.getTotalhits());
		System.out.println("  Start Row: " + result.getStartRow());
		System.out.println("  Maxim Row: " + result.getMaxRows());
		System.out.println("  Next Row : " + result.getNextRow());
		System.out.println("  Results: ");
		result.getResults().stream().forEach(
				w -> System.out.println("    " + w.getName() + " (" + w.getInternalID() + "/" + w.getVocabularyType() + ")")
				);
	}

}
