package at.ac.uibk.igwee.metadata.viaf.test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import at.ac.uibk.igwee.metadata.viaf.ViafQueryResult;
import at.ac.uibk.igwee.metadata.viaf.ViafVocabulary;
import at.ac.uibk.igwee.metadata.viaf.ViafVocabularyFactory;
import at.ac.uibk.igwee.metadata.viaf.impl.ViafRespToResult;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

import com.thoughtworks.xstream.XStream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ViafQueryResultTest {

	protected static final ViafQueryResult RESULT;
	static {
		ViafVocabularyFactory fac = new ViafVocabularyFactory();

		List<Vocabulary> uriList;
		try {
			uriList = Arrays.asList(
					
					fac.createViafVocabulary("123", "example", VocabularyType.PERSONAL_NAME),
					fac.createViafVocabulary("456", "example", VocabularyType.PERSONAL_NAME)
					);
		} catch (Exception ignored) {
			uriList = null;
		}
		Map<String,String> linked = new HashMap<>();
		linked.put("GND", "1234");
		linked.put("INB", "LVVN123123");
		List<ViafVocabulary> list = Arrays.asList(
				fac.createViafVocabulary("308191762", "Austen, Jane", VocabularyType.PERSONAL_NAME), 
				fac.createViafVocabulary("305434803", "Austen, Jane",
						VocabularyType.PERSONAL_NAME, uriList, linked));

		RESULT = new ViafQueryResult(1, 1, 30, 11, list);
	}

	protected static final File FILE = new File(
			"./src/test/resources/xstream/result.xml");

	@Test
	public void test01_write() throws Exception {

		XStream x = ViafRespToResult.XSTREAM;

		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ x.toXML(RESULT);

		FileUtils.write(FILE, xml, "utf-8");

	}

	@Test
	public void test02_read() throws Exception {

		XStream x = ViafRespToResult.XSTREAM;
		ViafQueryResult res = (ViafQueryResult) x.fromXML(FILE);
		Assert.assertEquals(res, RESULT);
		Assert.assertNotNull(res.getResults().get(0).getAuthority());
	}

}
