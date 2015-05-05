package at.ac.uibk.igwee.metadata.wikidata;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.uibk.igwee.metadata.httpclient.HttpClientService;
import at.ac.uibk.igwee.metadata.httpclient.impl.HttpClientServiceImpl;
import at.ac.uibk.igwee.metadata.query.Query;
import at.ac.uibk.igwee.metadata.query.QueryResult;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;
import at.ac.uibk.igwee.metadata.wikidata.impl.HttpQueryHelper;
import at.ac.uibk.igwee.metadata.wikidata.impl.WikidataQueryServiceImpl;
import at.ac.uibk.igwee.xslt.XPathService;
import at.ac.uibk.igwee.xslt.XsltService;
import at.ac.uibk.igwee.xslt.impl.SaxonXPathServiceImpl;
import at.ac.uibk.igwee.xslt.impl.SaxonXsltServiceImpl;

public class WikidataQueryServiceImplTest {

	private static final boolean RUNTEST = false;

	private static WikidataQueryServiceImpl service;
	
	@BeforeClass
	public static void setUpHttpQueryHelper() throws Exception {
		Assume.assumeTrue(RUNTEST);
		HttpQueryHelper helper = new HttpQueryHelper();
		XsltService xs = new SaxonXsltServiceImpl();
		helper.setXsltService(xs);
		XPathService xps = new SaxonXPathServiceImpl();
		helper.setXPathService(xps);
		HttpClientService hs = new HttpClientServiceImpl();
		helper.setHttpClientService(hs);
		
		service = new WikidataQueryServiceImpl();
		service.setHttpQueryHelper(helper);
	}
	
	
	@Test
	public void test_query() throws Exception {
		
		Query query = new Query(VocabularyType.PERSONAL_NAME, "London", null, 1, 125);
		
		QueryResult result = service.query(query);
		
		output(result);
	}
	

	@Test
	public void test05_queryId() throws Exception {
		WikidataVocabulary result = (WikidataVocabulary) service.queryId("Q42");
		System.out.println(result.getURI());
		System.out.println("  " + result.getInternalID());
		System.out.println("  Name: " + result.getName());
	}
	
	public static void output(QueryResult r) {
		System.out.println("QueryResult: ");
		System.out.println("  Query: " + r.getQuery().toString());
		System.out.println("  Totalhits: " + r.getTotalhits());
		System.out.println("  Results: "  + r.getResults().size());
		r.getResults().forEach(v -> {
			System.out.println("    " + v.getName() + " (" + v.getURI() + ")");
		});
	}

}
