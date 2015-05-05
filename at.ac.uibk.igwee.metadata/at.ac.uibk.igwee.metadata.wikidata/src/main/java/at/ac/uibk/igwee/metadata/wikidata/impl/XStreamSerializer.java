package at.ac.uibk.igwee.metadata.wikidata.impl;

import java.io.InputStream;

import at.ac.uibk.igwee.metadata.vocabulary.AbstractVocabulary;
import at.ac.uibk.igwee.metadata.wikidata.WikidataQueryResult;
import at.ac.uibk.igwee.metadata.wikidata.WikidataVocabulary;

import com.thoughtworks.xstream.XStream;

public class XStreamSerializer {
	
	private static final String XML_PROLOGUE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	
	public static final XStream XSTREAM;
	static {
		XSTREAM = new XStream();
		XSTREAM.setClassLoader(WikidataVocabulary.class.getClassLoader());
		XSTREAM.alias("wikidataVocabulary", WikidataVocabulary.class);
		XSTREAM.alias("wikidataQueryResult", WikidataQueryResult.class);
		XSTREAM.addImplicitArray(WikidataQueryResult.class, "results", WikidataVocabulary.class);
		XSTREAM.omitField(AbstractVocabulary.class, "authority");
	}

	public static String toXML(WikidataQueryResult result) {
		return XML_PROLOGUE + XSTREAM.toXML(result);
	}
	
	public static WikidataQueryResult fromXML(InputStream xml) {
		return (WikidataQueryResult) XSTREAM.fromXML(xml);
	}
	
}
