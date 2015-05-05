package at.ac.uibk.igwee.metadata.gnd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.uibk.igwee.metadata.gnd.impl.GndResultConverter;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

public class CreateGndQueryResultTest {
	
	private static final boolean RUNTEST = false;
	
	private static final File OUTPUT = new File("./src/test/resources/gndQueryResult.xml");
	
	@BeforeClass
	public static void setUpTest() {
		Assume.assumeTrue(RUNTEST);
	}
	
	@Test
	public void writeAnArbitraryGndResult() throws Exception {
		
		GndVocabulary voc1 = new GndVocabulary("123", "EinZweiDrei", VocabularyType.PERSONAL_NAME);
		GndVocabulary voc2 = new GndVocabulary("456", "Innsbruck", VocabularyType.PLACE_NAME);
		GndVocabulary voc3 = new GndVocabulary("789", "ÖAMTC", VocabularyType.INSTITUTION_NAME);
		
		List<GndVocabulary> results = new ArrayList<>();
		results.add(voc1);
		results.add(voc2);
		results.add(voc3);
		
		GndQueryResult result = new GndQueryResult(3, 1, 20, 21, results);
		
		String text = GndResultConverter.xstream().toXML(result);
		
		FileUtils.write(OUTPUT, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + text, "utf-8");
		
	}

}
