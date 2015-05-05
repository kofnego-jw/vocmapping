package at.ac.uibk.igwee.metadata.viaf.impl;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import at.ac.uibk.igwee.metadata.httpclient.HttpClientService;
import at.ac.uibk.igwee.metadata.httpclient.ParameterPair;
import at.ac.uibk.igwee.metadata.query.Query;
import at.ac.uibk.igwee.metadata.query.QueryResult;
import at.ac.uibk.igwee.metadata.query.QueryService;
import at.ac.uibk.igwee.metadata.viaf.OrgViaf;
import at.ac.uibk.igwee.metadata.viaf.ViafQueryResult;
import at.ac.uibk.igwee.metadata.viaf.ViafQueryService;
import at.ac.uibk.igwee.metadata.viaf.ViafVocabulary;
import at.ac.uibk.igwee.metadata.viaf.ViafVocabularyException;
import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;

/**
 * Implementation
 * 
 * @author Joseph
 *
 */
@Component
public class ViafQueryServiceImpl implements ViafQueryService, QueryService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ViafQueryServiceImpl.class);
	
	/**
	 * Query service name
	 */
	protected static final String QUERYSERVICE_NAME = "QueryService viaf.org";
	
	/**
	 * Queried authority
	 */
	protected static final List<Authority> QUERIED_AUTHORITY = Arrays.asList(OrgViaf.getInstance());
	
	/**
	 * Default Charset= UTF-8
	 */
	protected static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");
	
	/**
	 * VIAF_HOST = www.viaf.org
	 */
	protected static final String VIAF_HOST = "www.viaf.org";
	
	/**
	 * VIAF_ID_PATH = /viaf/get
	 */
	protected static final String VIAF_ID_PATH = "/viaf/";
	
	/**
	 * VIAF_PATH = /viaf/search
	 */
	protected static final String VIAF_PATH = "/viaf/search";
	
	/**
	 * ViafQueryParamKey = query
	 */
	protected static final String VIAF_QUERY_PARAMKEY = "query";
	
	/**
	 * Fixed parameter: httpAccept=text/xml
	 */
	protected static final List<ParameterPair> FIXED_PARAMETERS = Arrays.asList(
			new ParameterPair("httpAccept", "text/xml")
			);

	protected static String viafIdQueryPath(String id) {
		return VIAF_ID_PATH + id + "/viaf.xml";
	}
	
	
	private HttpClientService httpClientService;
	
	@Reference
	public void setHttpClientService(HttpClientService service) {
		this.httpClientService = service;
	}
	
	
	/**
	 * ViafRespToResult, must be injected
	 */
	private ViafRespToResult r2r;

	@Reference
	public void setViafRespToResult(ViafRespToResult r) {
		this.r2r = r;
	}

	/**
	 * Creates a parameter list.
	 * 
	 * @param startRow
	 * @param maxRows
	 * @return
	 */
	protected static List<ParameterPair> createParameterPairs(int startRow,
			int maxRows) {
		return Arrays.asList(
				new ParameterPair("startRecord", Integer.toString(startRow)),
				new ParameterPair("maximumRecords", Integer.toString(maxRows)));
	}

	/**
	 * Creates a list of ViafQueryParameter
	 * 
	 * @param key
	 *            the query type
	 * @param queryString
	 * @param restrictToAuthority
	 * @return
	 */
	protected static List<ViafQueryParameter> createViafQueryParameters(
			ViafQueryKey key, String queryString, String restrictToAuthority) {

		List<ViafQueryParameter> viafQ = new ArrayList<>(3);
		if (queryString != null && !queryString.isEmpty()) {
			viafQ.add(new ViafQueryParameter(key, queryString));
			viafQ.add(new ViafQueryParameter(ViafQueryKey.MAIN_HEADING, queryString));
		}

		if (restrictToAuthority != null && !restrictToAuthority.isEmpty()) {
			viafQ.add(new ViafQueryParameter(ViafQueryKey.AUTHORITY,
					restrictToAuthority));
		}

		return viafQ;
	}
	
	/**
	 * Creates a NOT url-encoded ParameterString out of the ViafQueryParameters.
	 * @param params
	 * @return
	 */
	public static String combineViafQueryParameters(List<ViafQueryParameter> params) {
		
		return params.stream()
				.map(param -> param.getKey().getViafKey() + " = " + param.getValue())
				.collect(Collectors.joining(" and "));
		
		
	}

	/**
	 * Main query method, will query as much as it can... TODO: Refactor to use
	 * multiple thread?
	 * 
	 * @param key
	 * @param queryString
	 * @param startRow
	 * @param maxRows
	 * @param restrictToAuthority
	 * @return
	 * @throws ViafVocabularyException
	 */
	protected ViafQueryResult makeQuery(ViafQueryKey key, String queryString,
			int startRow, int maxRows, String restrictToAuthority)
			throws ViafVocabularyException {

		List<ViafQueryParameter> viafQ = createViafQueryParameters(key,
				queryString, restrictToAuthority);
		
		List<ParameterPair> pp = createParameterPairs(startRow, maxRows);

		InputStream xml = callHttpClientService(pp, viafQ);

		ViafQueryResult result = r2r.convertToQueryResult(xml);

		ViafQueryResult result2;

		while (result.getNextRow() != -1
				&& result.getMaxRows() != result.getResults().size()) {
			pp = createParameterPairs(result.getNextRow(), maxRows);
			xml = callHttpClientService(pp, viafQ);
			result2 = r2r.convertToQueryResult(xml);
			result = r2r.mergeResults(result, result2);
		}

		return result;
	}

	@Override
	public ViafQueryResult queryForAll(String queryString, int startRow,
			int maxRows, String restrictToAuthority)
			throws ViafVocabularyException {
		return makeQuery(ViafQueryKey.ANY, queryString, startRow, maxRows,
				restrictToAuthority);
	}

	@Override
	public ViafQueryResult queryForCorporateName(String queryString,
			int startRow, int maxRows, String restrictToAuthority)
			throws ViafVocabularyException {
		return makeQuery(ViafQueryKey.CORPORATE_NAME, queryString, startRow,
				maxRows, restrictToAuthority);
	}

	@Override
	public ViafQueryResult queryForGeographicalName(String queryString,
			int startRow, int maxRows, String restrictToAuthority)
			throws ViafVocabularyException {
		return makeQuery(ViafQueryKey.PLACE_NAME, queryString, startRow,
				maxRows, restrictToAuthority);
	}

	@Override
	public ViafQueryResult queryForPersonalName(String queryString,
			int startRow, int maxRows, String restrictToAuthority)
			throws ViafVocabularyException {
		return makeQuery(ViafQueryKey.PERSONAL_NAME, queryString, startRow,
				maxRows, restrictToAuthority);
	}

	@Override
	public QueryResult query(Query q) throws VocabularyException {
		
		if (!q.getIncludedAuthority().isEmpty() && 
				!q.getIncludedAuthority().contains(OrgViaf.getInstance()))
			return null;

		ViafQueryResult result;

		switch (q.getType()) {
		case INSTITUTION_NAME:
			result = queryForCorporateName(q.getQueryString(), q.getStartRow(),
					q.getMaxRow(), null);
			break;
		case PERSONAL_NAME:
			result = queryForPersonalName(q.getQueryString(), q.getStartRow(),
					q.getMaxRow(), null);
			break;
		case PLACE_NAME:
			result = queryForGeographicalName(q.getQueryString(), q.getStartRow(),
					q.getMaxRow(), null);
			break;
		default:
			result = queryForAll(q.getQueryString(), q.getStartRow(), q.getMaxRow(), null);
		}

		return new QueryResult(q, result.getTotalhits(), result.getResults());
	}

	@Override
	public ViafVocabulary queryId(String id) throws ViafVocabularyException {
		
		LOGGER.debug("ID Query for {}.", id);
		
		InputStream viafIdResponse;
		try {
			viafIdResponse = httpClientService.executeHttpGet(VIAF_HOST, viafIdQueryPath(id), null);
		} catch (Exception e) {
			LOGGER.error("Cannot perform the ID request on VIAF.", e);
			throw new ViafVocabularyException("Cannot perform the ID query: " + e.getMessage());
		}
		
		
		return r2r.convertToViafVocabulary(viafIdResponse);
	}
	
	@Override
	public String getName() {
		return QUERYSERVICE_NAME;
	}
	
	@Override
	public Collection<? extends Authority> getQueriedAuthority() {
		return QUERIED_AUTHORITY;
	}
	
	protected InputStream callHttpClientService(List<ParameterPair> additionalParams, List<ViafQueryParameter> viafParams)
			throws ViafVocabularyException {
		
		List<ParameterPair> allTogether = new ArrayList<>();
		allTogether.addAll(FIXED_PARAMETERS);
		allTogether.addAll(additionalParams);
		allTogether.add(new ParameterPair(VIAF_QUERY_PARAMKEY, combineViafQueryParameters(viafParams)));
		
		InputStream result;
		try {
			result = httpClientService.executeHttpGet(VIAF_HOST, VIAF_PATH, allTogether);
		} catch (Exception e) {
			LOGGER.error("Cannot perform the http call to VIAF.", e);
			throw new ViafVocabularyException("Cannot perform http call to VIAF: " + e.getMessage());
		}
		
		return result;
	}
	
	
	
	
}
