package at.ac.uibk.igwee.metadata.gnd.impl;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import at.ac.uibk.igwee.metadata.gnd.GndException;
import at.ac.uibk.igwee.metadata.gnd.GndQueryResult;
import at.ac.uibk.igwee.metadata.gnd.GndVocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.AbstractVocabulary;
import at.ac.uibk.igwee.xslt.XsltService;

import com.thoughtworks.xstream.XStream;

@Component
public class GndResultConverter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GndResultConverter.class);
	
	private static final String XML_PROLOGUE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	
	private static final XStream XSTREAM;
	static {
		XSTREAM = new XStream();
		XSTREAM.setClassLoader(GndQueryResult.class.getClassLoader());
		XSTREAM.alias("gndVocabulary", GndVocabulary.class);
		XSTREAM.alias("gndQueryResult", GndQueryResult.class);
		XSTREAM.omitField(AbstractVocabulary.class, "authority");
	}
	
	public static XStream xstream() {
		return XSTREAM;
	}
	
	public static final String GND_RESP_TO_RESULT_XSLT = "/xsl/gndResp2Result.xsl";
	
	private XsltService xsltService;
	
	@Reference
	public void setXsltService(XsltService service) {
		this.xsltService = service;
	}
	
	public GndQueryResult convertToGndQueryResult(InputStream gndResponse) throws GndException {
		
		InputStream xml;
		
		try {
			InputStream xsl = getClass().getResourceAsStream(GND_RESP_TO_RESULT_XSLT);
			xml = xsltService.doXslt(gndResponse, xsl, null);
		} catch (Exception e) {
			LOGGER.error("Cannot convert GND response to XStream XML.", e);
			throw new GndException("Cannot convert GND response to XStream XML: " + e.getMessage());
		}
		
		try {
			return (GndQueryResult) XSTREAM.fromXML(xml);
		} catch (Exception e) {
			LOGGER.error("Cannot deserialize XML to GndQueryResult.", e);
			throw new GndException("Cannot deserialize XML to GndQueryResult: " + e.getMessage());
		}
	}
	
	public String toXML(GndQueryResult result) {
		return XML_PROLOGUE + XSTREAM.toXML(result);
	}

}
