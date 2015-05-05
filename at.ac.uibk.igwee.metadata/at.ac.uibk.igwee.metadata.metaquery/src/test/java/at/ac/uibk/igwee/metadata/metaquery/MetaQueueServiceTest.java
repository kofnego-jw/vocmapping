package at.ac.uibk.igwee.metadata.metaquery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import at.ac.uibk.igwee.metadata.geonames.OrgGeonames;
import at.ac.uibk.igwee.metadata.geonames.impl.GeonamesHttpQueryServiceImpl;
import at.ac.uibk.igwee.metadata.gnd.DeDnb;
import at.ac.uibk.igwee.metadata.gnd.impl.GndQueryServiceImpl;
import at.ac.uibk.igwee.metadata.gnd.impl.GndResultConverter;
import at.ac.uibk.igwee.metadata.httpclient.impl.HttpClientServiceImpl;
import at.ac.uibk.igwee.metadata.metaquery.impl.MetaQueryServiceImpl;
import at.ac.uibk.igwee.metadata.metaquery.impl.QueryQueueFactoryImpl;
import at.ac.uibk.igwee.metadata.viaf.impl.ViafQueryServiceImpl;
import at.ac.uibk.igwee.metadata.viaf.impl.ViafRespToResult;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;
import at.ac.uibk.igwee.metadata.wikidata.OrgWikidata;
import at.ac.uibk.igwee.metadata.wikidata.impl.HttpQueryHelper;
import at.ac.uibk.igwee.metadata.wikidata.impl.WikidataQueryServiceImpl;
import at.ac.uibk.igwee.xslt.XPathService;
import at.ac.uibk.igwee.xslt.XsltService;
import at.ac.uibk.igwee.xslt.impl.SaxonXPathServiceImpl;
import at.ac.uibk.igwee.xslt.impl.SaxonXsltServiceImpl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MetaQueueServiceTest {
	
	private static final boolean RUNTEST = true;
	
	private static ViafQueryServiceImpl viafQS;
	private static GeonamesHttpQueryServiceImpl geoQS;
	private static WikidataQueryServiceImpl wdQS;
	private static GndQueryServiceImpl gndQS;
	
	private static MetaQueryServiceImpl service;
	
	private static QueryQueueFactoryImpl qqFactory;
	
	private static QueryQueue interTestQueryQueue;
	
	private static final File QUERYQUEUE_XML = new File("./src/test/resources/queryQueue.xml");
	
	@BeforeClass
	public static void setUpServices() throws Exception {
		Assume.assumeTrue(RUNTEST);
		
		viafQS = new ViafQueryServiceImpl();
		ViafRespToResult r2r = new ViafRespToResult();
		XsltService xs = new SaxonXsltServiceImpl();
		XPathService xps = new SaxonXPathServiceImpl();
		r2r.setXsltService(xs);
		viafQS.setViafRespToResult(r2r);
		
		HttpClientServiceImpl httpClient = new HttpClientServiceImpl();
		
		viafQS.setHttpClientService(httpClient);
		
		
		
		service = new MetaQueryServiceImpl();
		
		qqFactory = new QueryQueueFactoryImpl();
		
		wdQS = new WikidataQueryServiceImpl();
		
		
		
		HttpQueryHelper helper = new HttpQueryHelper();
		helper.setHttpClientService(httpClient);
		helper.setXPathService(xps);
		helper.setXsltService(xs);
		wdQS.setHttpQueryHelper(helper);
		
		
		geoQS = new GeonamesHttpQueryServiceImpl();
		geoQS.setXsltService(xs);
		geoQS.setHttpClientService(httpClient);
		
		
		gndQS = new GndQueryServiceImpl();
		GndResultConverter gndConv = new GndResultConverter();
		gndConv.setXsltService(xs);
		gndQS.setGndResultConverter(gndConv);
		gndQS.setHttpClientService(httpClient);
		
		
		service.registerQueryService(geoQS);
		service.registerQueryService(gndQS);
		service.registerQueryService(viafQS);
		service.registerQueryService(wdQS);
		
		
	}
	
	@Test
	public void test02_queryForOnePlace() throws Exception {
		VocabularyQuery q = new VocabularyQuery("Rom", "Rom", "Eine Stadt in Italien", VocabularyType.PLACE_NAME, null);
		VocabularyQueryResult r = service.processQuery(q);
		r.getResults().forEach(voc -> System.out.println(voc.getURI()));
	}
	
	@Test
	public void test03_queryForQueue() throws Exception {
		
		List<VocabularyQuery> list = new ArrayList<>(5);
		
		VocabularyQuery q1 = new VocabularyQuery("Rom", "Rom", "Eine Stadt in Italien", VocabularyType.PLACE_NAME, null);
		VocabularyQuery q2 = new VocabularyQuery("Horvath", "Ödön von Horvath", "Ödön", VocabularyType.PERSONAL_NAME, null);
		VocabularyQuery q3 = new VocabularyQuery("Jane Austen", "Austen, Jane", "Jane", VocabularyType.PERSONAL_NAME, null);
		VocabularyQuery q4 = new VocabularyQuery("FIBA", "Brenner Archiv", "BA", VocabularyType.INSTITUTION_NAME, null);
		VocabularyQuery q5 = new VocabularyQuery("London", "London", "London", VocabularyType.UNKNOWN, null);
		
		q1.getIncludedAuthority().add(OrgGeonames.getInstance());
		
		q3.getIncludedAuthority().add(DeDnb.getInstance());
		
		q5.getIncludedAuthority().add(OrgWikidata.getInstance());
		
		list.add(q1);
		list.add(q2);
		list.add(q3);
		list.add(q4);
		list.add(q5);
		
		QueryQueue qq = new QueryQueue("test", "test queue", list);
		
		QueryQueue result = service.processQueries(qq);
		
		Assert.assertTrue(result.getPendingQueries().isEmpty());
		
		Assert.assertEquals(5, result.getResults().size());
		
		qqFactory.writeQueryQueue(result, new FileOutputStream(QUERYQUEUE_XML));
		
		output(result);
		
		interTestQueryQueue = result;
	}
	
	@Test
	public void test04_deserializeQueryQueue() throws Exception {
		QueryQueue qq = qqFactory.loadQueryQueue(new FileInputStream(QUERYQUEUE_XML));
		Assert.assertEquals(qq, interTestQueryQueue);
	}
	
	@Test
	public void test05_editing() throws Exception {
		int counter = 0;
		while(interTestQueryQueue.hasNext()) {
			counter++;
			VocabularyQueryResult result = interTestQueryQueue.next();
			if (result.getResults().isEmpty()) continue;
			Vocabulary voc = result.getResults().get(0);
			result.setFixedResult(voc);
		}
		Assert.assertEquals(counter, 5);
	}
	
	@Test
	public void test06_readFromCSV() throws Exception {
		QueryQueue qq = qqFactory.loadQueryQueue(new FileInputStream(new File("./src/test/resources/queryQueue.csv")), 
				QueryQueueFormat.CSV, "utf-8");
		
		output(qq);
	}
	
	public static void output(QueryQueue q) {
		System.out.println("QueryQueue: " + q.getName());
		System.out.println("  Info: " + q.getAdditionalInfo());
		System.out.println("  Pending: " + q.getPendingQueries().size());
		q.getPendingQueries().forEach(vq -> {
			System.out.println("    Query for: " + vq);
			System.out.println("      Type: " + vq.getType());
			System.out.println("      Restricted to: " + vq.getIncludedAuthority());
			});
		System.out.println();
		System.out.println("  Results: ");
		q.getResults().stream().forEach(vq -> {
			System.out.println("    Query for: " + vq.getVocabularyQuery().getName() );
			System.out.println("    Fixed: " + vq.getFixedResult());
			System.out.println("    Results: " + vq.getResults().size());
			vq.getResults().forEach(v -> {
				System.out.println("      " + v.getInternalID() + ": " + v.getURI());
			});
		});
	}
	
}
