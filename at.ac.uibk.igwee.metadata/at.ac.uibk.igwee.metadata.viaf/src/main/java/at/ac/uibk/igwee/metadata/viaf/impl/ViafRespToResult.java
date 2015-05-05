package at.ac.uibk.igwee.metadata.viaf.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import at.ac.uibk.igwee.metadata.viaf.ViafQueryResult;
import at.ac.uibk.igwee.metadata.viaf.ViafVocabulary;
import at.ac.uibk.igwee.metadata.viaf.ViafVocabularyException;
import at.ac.uibk.igwee.metadata.vocabulary.AbstractVocabulary;
import at.ac.uibk.igwee.xslt.XsltService;

import com.thoughtworks.xstream.XStream;

/**
 * Helper class to convert Viaf-Responses to ViafQueryResult.
 * @author Joseph
 *
 */
@Component(provide=ViafRespToResult.class)
public class ViafRespToResult {
	
	/**
	 * XStream class object, used for creating the ViafQueryResult from XML.
	 * NB: it is set to public, because for the tests this is necessary....
	 * TODO: Change the visibility?
	 */
	public static final XStream XSTREAM;
	static {
		XSTREAM = new XStream();
		XSTREAM.setClassLoader(ViafRespToResult.class.getClassLoader());
		// TODO: Make adjustments
		XSTREAM.alias("viafQueryResult", ViafQueryResult.class);
		XSTREAM.alias("viafVocabulary", ViafVocabulary.class);
		XSTREAM.addImplicitCollection(ViafQueryResult.class, "results", ViafVocabulary.class);
		XSTREAM.omitField(AbstractVocabulary.class, "authority");
	}
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ViafRespToResult.class);
	
	/**
	 * Location of the xsl file, embedded in the jar file.
	 */
	protected static final String XSLT_LOCATION = "/xsl/viafResp2Result.xsl";
	
	/**
	 * Location of the xsl file for converting id response to XSTREAM
	 */
	protected static final String ID_XSLT_LOCATION = "/xsl/viafId2Voc.xsl";
	
	
	/**
	 * XsltService
	 */
	private XsltService xsltService;
	
	@Reference
	public void setXsltService(XsltService serv) {
		this.xsltService = serv;
	}
	
	/**
	 * Converts an InputStream to ViafVocabulary
	 * @param viafIdResp
	 * @return
	 * @throws ViafVocabularyException
	 */
	public ViafVocabulary convertToViafVocabulary(InputStream viafIdResp)
			throws ViafVocabularyException {
		LOGGER.debug("Converting viaf id response to viafVocabulary.");
		
		InputStream xml;
		try {
			InputStream xsl = getClass().getResourceAsStream(ID_XSLT_LOCATION);
			xml = xsltService.doXslt(viafIdResp, xsl, null);
		} catch (Exception e) {
			LOGGER.error("Cannot perform XSLT from ViafResponse.", e);
			throw new ViafVocabularyException("Cannot perform XSLT from ViafResponse: " 
					+ e.getMessage());
		}
		
		ViafVocabulary voc;
		try {
			voc = (ViafVocabulary) XSTREAM.fromXML(xml);
		} catch (Exception e) {
			LOGGER.error("Cannot convert XML to ViafVocabulary.", e);
			throw new ViafVocabularyException("Cannot convert XML to ViafVocabulary: " 
					+ e.getMessage());
		}
		return voc;
	}
	
	
	/**
	 * Convert an InputStream to ViafQueryResult
	 * @param viafResponse
	 * @return The result from one viafResponse
	 * @throws ViafVocabularyException
	 */
	public ViafQueryResult convertToQueryResult(InputStream viafResponse)
			throws ViafVocabularyException {
		
		LOGGER.debug("Conversion to ViafQueryResult requested.");
		
		InputStream xml;
		
		try {
			InputStream xsl = getClass().getResourceAsStream(XSLT_LOCATION);
			
			xml = xsltService.doXslt(viafResponse, xsl, null);
		} catch (Exception e) {
			
			LOGGER.error("Cannot do the XSLT.", e);
			
			throw new ViafVocabularyException("Cannot perform XSLT for conversion: " + e.getMessage());
		}
		
		ViafQueryResult result;
		try {
			result = (ViafQueryResult) XSTREAM.fromXML(xml);
		} catch (Exception e) {
			LOGGER.error("Cannot convert XML to ViafQueryResult.", e);
			e.printStackTrace();
			throw new ViafVocabularyException("Cannot convert XML to ViafQueryResult: " + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * Helper method to merge two results
	 * @param one must be the prior result
	 * @param two must be the following result
	 * @return
	 * @throws ViafVocabularyException
	 */
	public ViafQueryResult mergeResults(ViafQueryResult one, ViafQueryResult two) throws ViafVocabularyException {
		
		// If the totalhits are not the same, then the merging will not funtion
		if (one.getTotalhits()!=two.getTotalhits())
			throw new ViafVocabularyException("Cannot merge two results with different totalhits.");
		
		// Merge only if the next row of one is the starting row of two.
		if (one.getNextRow()==two.getStartRow()) {
			
			List<ViafVocabulary> results = new ArrayList<>(one.getResults().size() + two.getResults().size());
			results.addAll(one.getResults());
			results.addAll(two.getResults());
			
			return new ViafQueryResult(one.getTotalhits(), one.getStartRow(), one.getMaxRows(), two.getNextRow(), results);
			
			
		} else if (two.getStartRow() == one.getNextRow()) {
			return mergeResults(two, one);
		} else {
			throw new ViafVocabularyException("Cannot merge two results that are not connected.");
		}
	}

}
