package at.ac.uibk.igwee.metadata.geonames;

import java.net.URI;

import org.junit.Test;

import at.ac.uibk.igwee.metadata.vocabulary.AbstractVocabulary;


public class OrgGeonamesTest {
	
	
	@Test
	public void test() {
		URI test = OrgGeonames.getInstance().createURI(2775220);
		System.out.println(test);
	}
	
	@Test
	public void testGeodataAsVocabulary() {
		GeonameData ibk = new GeonameData("Innsbruck", "Austria", "AT", "EU", 47.11, 12.11, 2775220);
		
		System.out.println( ((AbstractVocabulary)ibk).toString());
		System.out.println(ibk.toString());
		
	}

}
