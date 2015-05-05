package at.ac.uibk.igwee.metadata.geonames.impl;

import java.io.InputStream;

import at.ac.uibk.igwee.metadata.geonames.GeonameData;
import at.ac.uibk.igwee.metadata.geonames.GeonamesQueryException;
import at.ac.uibk.igwee.metadata.geonames.GeonamesQueryResult;
import at.ac.uibk.igwee.metadata.vocabulary.AbstractVocabulary;

import com.thoughtworks.xstream.XStream;

public class XStreamSerializer {
	
	protected static final String XML_PROLOGUE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	
	public static final XStream XSTREAM;
	static {
		XSTREAM = new XStream();
		XSTREAM.setClassLoader(GeonamesQueryResult.class.getClassLoader());
		XSTREAM.alias("geonameData", GeonameData.class);
		XSTREAM.alias("geonamesQueryResult", GeonamesQueryResult.class);
		XSTREAM.addImplicitCollection(GeonamesQueryResult.class, "results", GeonameData.class);
		XSTREAM.omitField(AbstractVocabulary.class, "authority");
	}
	
	
	public static String toXML(GeonamesQueryResult result) {
		return XML_PROLOGUE + XSTREAM.toXML(result);
	}
	
	public static GeonamesQueryResult fromXML(InputStream in) throws GeonamesQueryException {
		try {
			return (GeonamesQueryResult) XSTREAM.fromXML(in);
		} catch (Exception e) {
			throw new GeonamesQueryException("Cannot deserialize GeonamesQueryResult: " + e.getMessage());
		}
	}
	
	public static GeonameData toGeonameData(InputStream in) throws GeonamesQueryException {
		try {
			return (GeonameData) XSTREAM.fromXML(in);
		} catch (Exception e) {
			throw new GeonamesQueryException("Cannot deserialize GeonameData: " + e.getMessage());
		}
	}

}
