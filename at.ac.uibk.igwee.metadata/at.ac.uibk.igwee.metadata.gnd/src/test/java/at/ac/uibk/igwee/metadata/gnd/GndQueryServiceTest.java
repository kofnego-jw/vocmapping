package at.ac.uibk.igwee.metadata.gnd;

import java.io.File;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.uibk.igwee.metadata.gnd.impl.GndQueryServiceImpl;
import at.ac.uibk.igwee.metadata.gnd.impl.GndResultConverter;
import at.ac.uibk.igwee.metadata.httpclient.impl.HttpClientServiceImpl;
import at.ac.uibk.igwee.metadata.query.Query;
import at.ac.uibk.igwee.metadata.query.QueryResult;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;
import at.ac.uibk.igwee.xslt.impl.SaxonXsltServiceImpl;

public class GndQueryServiceTest {

	private static final boolean RUNTEST = false;
	
	private static final List<String> ALLOWED_IPS = Arrays.asList(
			"138.232.156.135",
			"138.232.156.163");
	
	private static GndQueryService service;
	
	private static GndResultConverter conv;
	
	@BeforeClass
	public static void setUpService() throws Exception {
		Assume.assumeTrue(RUNTEST);
		
		
		InetAddress addr = InetAddress.getLocalHost();
		Assume.assumeTrue("Not within the address range for GND.", 
				ALLOWED_IPS.contains(addr.getHostAddress()));
		
		
		GndQueryServiceImpl impl = new GndQueryServiceImpl();
		HttpClientServiceImpl http = new HttpClientServiceImpl();
		impl.setHttpClientService(http);
		SaxonXsltServiceImpl saxon = new SaxonXsltServiceImpl();
		conv = new GndResultConverter();
		conv.setXsltService(saxon);
		impl.setGndResultConverter(conv);
		service = impl;
	}
	
	@Test
	public void test_queryPerson() throws Exception {
		GndQueryResult result = service.queryForPerson("Otto Müller");
		String xml = conv.toXML(result);
		FileUtils.write(new File("./src/test/resources/output/queryPerson.xml"), xml, "utf-8");
	}
	
	@Test
	public void test_queryPlace() throws Exception {
		GndQueryResult result = service.queryForPlace("Olmütz");
		String xml = conv.toXML(result);
		FileUtils.write(new File("./src/test/resources/output/queryPlace.xml"), xml, "utf-8");
	}
	
	@Test
	public void test_queryCorporate() throws Exception {
		GndQueryResult result = service.queryForCorporate("Universität Salzburg");
		String xml = conv.toXML(result);
		FileUtils.write(new File("./src/test/resources/output/queryInsitute.xml"), xml, "utf-8");
	}
	
	@Test
	public void test_queryId() throws Exception {
		Vocabulary voc = service.queryId("140961615");
		output(voc);
	}
	
	@Test
	public void test_query() throws Exception {
		Query query = new Query(VocabularyType.UNKNOWN, "London", null, 1, 100);
		QueryResult qr = service.query(query);
		System.out.println(qr);
	}
	
	
	private static void output(Vocabulary voc) {
		System.out.println("Vocabulary:");
		System.out.println("   id: " + voc.getInternalID());
		System.out.println("  url: " + voc.getURI().toString());
		System.out.println(" type: " + voc.getVocabularyType());
		System.out.println(" name: " + voc.getName());
	}
	
}
