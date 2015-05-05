package at.ac.uibk.igwee.metadata.wikidata.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import at.ac.uibk.igwee.metadata.httpclient.HttpClientService;
import at.ac.uibk.igwee.metadata.httpclient.ParameterPair;
import at.ac.uibk.igwee.metadata.wikidata.WikidataException;
import at.ac.uibk.igwee.metadata.wikidata.WikidataQueryResult;
import at.ac.uibk.igwee.metadata.wikidata.WikidataVocabulary;
import at.ac.uibk.igwee.xslt.XPathService;
import at.ac.uibk.igwee.xslt.XsltException;
import at.ac.uibk.igwee.xslt.XsltService;

/**
 * Helper Class for HTTPQuery for Wikidata
 * @author joseph
 *
 */
@Component(provide=HttpQueryHelper.class)
public class HttpQueryHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpQueryHelper.class);
	
	/**
	 * Row offset = -1
	 */
	protected static final int ROW_OFFSET = -1;
	/**
	 * Host: www.wikidata.org
	 */
	private static final String WIKIDATA_HOST = "www.wikidata.org";

	/**
	 * default path: /w/api.php
	 */
	private static final String WIKIDATA_PATH = "/w/api.php";

	/**
	 * Predefined Parameters for searching: action=query, list=search, format=xml, srprop=wordcount
	 */
	private static final List<ParameterPair> SEARCH_PREDEFINED_PARAMS = 
			Arrays.asList(
					new ParameterPair("action", "query"), 
					new ParameterPair("list", "search"), 
					new ParameterPair("format", "xml"),
					new ParameterPair("srprop", "wordcount")
					);

	/**
	 * SearchString paramKey = srsearch
	 */
	private static final String SEARCH_STRING_PARAMKEY = "srsearch";

	/**
	 * Startrow: sroffset
	 */
	private static final String START_ROW_PARAMKEY = "sroffset";

	/**
	 * maxros = srlimit
	 */
	private static final String MAX_ROW_PARAMKEY = "srlimit";
	
	/**
	 * IDParamkey = ids
	 */
	private static final String ID_PARAMKEY = "ids";
	
	/**
	 * Default max row per query
	 */
	private static final int DEFAULT_MAX_ROWS = 50;
	
	/**
	 * Predefined parameters for titles: action=wbgetentities, format=xml
	 */
	private static final List<ParameterPair> FIND_TITLES_PREDEFINED_PARAMS = 
			Arrays.asList(
					new ParameterPair("action","wbgetentities"),
					new ParameterPair("format", "xml"),
					new ParameterPair("languages", "de|en|de-at|de-ch|en-gb|en-ca"),
					new ParameterPair("props", "info|labels|claims")
					);

	/**
	 * XPath for titles: //@title
	 */
	private static final String XPATH_FOR_TITLES = "//@title";
	
	/**
	 * XPath for TotalHits: //searchinfo/@totalhits
	 */
	private static final String XPATH_FOR_TOTALHITS = "//searchinfo/@totalhits";
	
	/**
	 * XSLT_RESOURCESTREAM-NAME: /xsl/wikidataToVoc.xsl
	 */
	private static final String XSL_RESOURCENAME = "/xsl/wikidataToVoc.xsl";
	
	private HttpClientService httpClientService;
	
	@Reference
	public void setHttpClientService(HttpClientService service) {
		this.httpClientService = service;
	}
	
	private XPathService xpathService;
	@Reference
	public void setXPathService(XPathService service) {
		this.xpathService = service;
	}
	
	private XsltService xsltService;
	@Reference
	public void setXsltService(XsltService service) {
		this.xsltService = service;
	}
	
	/**
	 * 
	 * @param queryString
	 * @param startRow NB: It is the internal startRow, i.e. starts with 0
	 * @param maxRowsHere
	 * @return the result of httpClientService
	 * @throws WikidataException
	 */
	protected ByteArrayInputStream queryFirstPhase(String queryString, int startRow, int maxRows) 
			throws WikidataException {
		
		LOGGER.debug("Query Wikidata, phase 1.");
				
		List<ParameterPair> list = new ArrayList<>();
		list.addAll(SEARCH_PREDEFINED_PARAMS);
		list.add(new ParameterPair(SEARCH_STRING_PARAMKEY, queryString));
		list.add(new ParameterPair(START_ROW_PARAMKEY, Integer.toString(startRow)));
		list.add(new ParameterPair(MAX_ROW_PARAMKEY, Integer.toString(maxRows)));
		
		try {
			return httpClientService.executeHttpGet(WIKIDATA_HOST, WIKIDATA_PATH, list);
		} catch (Exception e) {
			LOGGER.error("Cannot perform first phase of search on Wikidata.", e);
			throw new WikidataException("Cannot perform first phase of search on Wikidata: " 
					+ e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param titles the Titles of wikidata (e.g. "Q42", "P31")
	 * @return The result of httpClientService
	 * @throws WikidataException
	 */
	public ByteArrayInputStream queryEntities(List<String> titles) 
			throws WikidataException {
		
		LOGGER.debug("Query Wikidata, phase 2.");
		
		List<ParameterPair> list = new ArrayList<>();
		list.addAll(FIND_TITLES_PREDEFINED_PARAMS);
		String title = titles.stream().collect(Collectors.joining("|"));
		list.add(new ParameterPair(ID_PARAMKEY, title));
		
		try {
			return httpClientService.executeHttpGet(WIKIDATA_HOST, WIKIDATA_PATH, list);
		} catch (Exception e) {
			LOGGER.error("Cannot query Wikidata for entities.", e);
			throw new WikidataException("Cannot query Wikidata for entities: " + e.getMessage());
		}
	}
	
	public WikidataVocabulary queryTitle(String title) throws WikidataException {
		List<String> titles = Arrays.asList(title);
		ByteArrayInputStream bis = queryEntities(titles);
		List<WikidataVocabulary> results = getVocabularies(bis);
		if (results.size()!=1) {
			LOGGER.error("Query for title {} ends with {} results.", title, Integer.toString(results.size()));
			throw new WikidataException("Query for title ends with results !=1.");
		}
		return results.get(0);
	}
	
	/**
	 * 
	 * @param queryString
	 * @param startRowByQueryService The start row according to QueryService, i.e. starts with 1
	 * @param maxRows
	 * @return the wikidataQueryResult
	 * @throws WikidataException
	 */
	public WikidataQueryResult query(String queryString, int startRowByQueryService, int maxRows) 
			throws WikidataException {
		
		int startRow = toInternalOffset(startRowByQueryService);
		
		// Phase 1: Do the searching
		ByteArrayInputStream searchResults = null;
		List<String> titles = new ArrayList<>(maxRows);
		int totalhits = -1;

		int nextRow = startRow;
		int endRow = startRow + maxRows;
		try {
			
			do {
				int maxRowsNow = nextRow + DEFAULT_MAX_ROWS < endRow ? 
						DEFAULT_MAX_ROWS : endRow - nextRow;
				searchResults = queryFirstPhase(queryString, nextRow, maxRowsNow);
				List<String> titlesNow = getTitles(searchResults);
				titles.addAll(titlesNow);
				if (titlesNow.size() < DEFAULT_MAX_ROWS) 
					break;
				nextRow += DEFAULT_MAX_ROWS;
			} while(nextRow < endRow);
			
			if (searchResults!=null) {
				searchResults.reset();
				List<String> list = 
						xpathService.evaluateAsStringList(searchResults, XPATH_FOR_TOTALHITS, null);
				assert(list.size()==1);
				totalhits = Integer.parseInt(list.get(0));
				LOGGER.debug("Totalhits: {}", Integer.toString(totalhits));
			} 
			
		} catch (Exception e) {
			throw new WikidataException("Exception while executing the first phase: " + e.getMessage());
		}
		
		LOGGER.debug("Found {} titles.", Integer.toString(titles.size()));
		
		// Create sublists
		
		List<List<String>> subLists = new ArrayList<>(titles.size() / DEFAULT_MAX_ROWS + 1);
		int subListStartNum = 0;
		int endNum;
		do {
			endNum = subListStartNum + DEFAULT_MAX_ROWS;
			if (endNum >= titles.size())
				endNum = titles.size();
			
			subLists.add(titles.subList(subListStartNum, endNum));
			
			subListStartNum = endNum;
			
		} while (subListStartNum < titles.size());
		
		LOGGER.debug("SubList {} for entities query created.", Integer.toString(subLists.size()));
		
		// Query phase 2: get the entities
		
		List<WikidataVocabulary> vocabularies = new ArrayList<>(titles.size());
		
		for (List<String> sublist: subLists) {
			ByteArrayInputStream entityStream = queryEntities(sublist);
			vocabularies.addAll(getVocabularies(entityStream));
		}
		
		return new WikidataQueryResult(totalhits, startRowByQueryService, 
				maxRows, nextRow+1, queryString, vocabularies);
	}
	
	/**
	 * 
	 * @param wdStream the result of first phase query
	 * @return a List of titles "Q42" etc.
	 * @throws WikidataException
	 */
	public List<String> getTitles(InputStream wdStream) throws WikidataException {
		try {
			return xpathService.evaluateAsStringList(wdStream, XPATH_FOR_TITLES, null);
		} catch (XsltException e) {
			LOGGER.error("Cannot perform XPath evaluation to extract titles.", e);
			throw new WikidataException("Cannot perform XPath evaluation to extract titles: " 
					+ e.getMessage());
		}
	}
	
	/**
	 * Converts the result of phase 2 query to WikidataQueryResult, and then gets the results
	 * of the result
	 * @param xml
	 * @return
	 * @throws WikidataException
	 */
	public List<WikidataVocabulary> getVocabularies(InputStream xml) throws WikidataException {
		
		try {
			
			InputStream xsl = getClass().getResourceAsStream(XSL_RESOURCENAME);
			
			InputStream queryResultXML = xsltService.doXslt(xml, xsl, null);
			
			WikidataQueryResult result = XStreamSerializer.fromXML(queryResultXML);
			return result.getResults();
		} catch (Exception e) {
			LOGGER.error("Cannot convert Wikidata answer to WikidataQueryResult.", e);
			throw new WikidataException("Cannot convert wikidata answer to WikidataQueryResult: " 
					+ e.getMessage());
		}
		
	}
	
	/**
	 * 
	 * @param startRowByQueryService the start row number starting with 1
	 * @return
	 */
	protected static int toInternalOffset(int startRowByQueryService) {
		return startRowByQueryService + ROW_OFFSET;
	}

}
