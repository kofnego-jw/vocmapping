package at.ac.uibk.igwee.metadata.wikidata;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;
import at.ac.uibk.igwee.metadata.wikidata.impl.XStreamSerializer;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VocabularyImplTest {
	
	private static final boolean RUNTEST = true;
	
	private static final File XML_FILE = new File("./src/test/resources/wikidataQueryResult.xml");
	
	private static WikidataQueryResult intertestResult;
	
	@BeforeClass
	public static void prepareClass() {
		Assume.assumeTrue(RUNTEST);
	}

	@Test
	public void test01_authorityImplTest() throws Exception {
		OrgWikidata auth = OrgWikidata.getInstance();

		String url = "http://www.wikidata.org/wiki/Q1375";

		Assert.assertEquals(url, auth.createURI("Q1375").toString());

	}

	@Test
	public void test02_vocabularyImplTest() throws Exception {

		Map<String, String> labels = new HashMap<>();

		labels.put("de", "Innsbruck");
		labels.put("en", "Innsbruck");

		WikidataVocabulary ibk = new WikidataVocabulary("Q1375", labels,
				VocabularyType.PLACE_NAME);

		URI uri = new URI("http://www.wikidata.org/wiki/Q1375");

		Assert.assertEquals(ibk.getURI(), uri);

	}

	@Test
	public void test03_vocabularyQueryResultSerialization() throws Exception {

		Map<String, String> labels = new HashMap<>();

		labels.put("de", "Innsbruck");
		labels.put("en", "Innsbruck");

		Map<String, String> labels2 = new HashMap<>();

		labels2.put("de", "Douglas Adams");
		labels2.put("en", "Douglas Adams");
		
		List<WikidataVocabulary> list = Arrays.asList(
				new WikidataVocabulary("Q1735", labels, VocabularyType.PLACE_NAME),
				new WikidataVocabulary("Q42", labels2, VocabularyType.PERSONAL_NAME)
				);
		
		WikidataQueryResult result = new WikidataQueryResult(2, 1, 2, 3, "dc", list);
		
		intertestResult = result;
		
		String xml = XStreamSerializer.toXML(result);
		
		System.out.println(xml);
		
		FileUtils.write(XML_FILE, xml, "utf-8");
		
	}
	
	@Test
	public void test04_vocabularyQueryResultDeserialization() throws Exception {
		InputStream in = new FileInputStream(XML_FILE);
		
		WikidataQueryResult result = XStreamSerializer.fromXML(in);
		
		Assert.assertEquals(intertestResult, result);
		
	}
	

}
