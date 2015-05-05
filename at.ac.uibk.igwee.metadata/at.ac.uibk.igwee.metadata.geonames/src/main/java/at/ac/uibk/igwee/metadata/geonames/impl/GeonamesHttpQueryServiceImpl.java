package at.ac.uibk.igwee.metadata.geonames.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import at.ac.uibk.igwee.metadata.geonames.GeonameData;
import at.ac.uibk.igwee.metadata.geonames.GeonamesQueryException;
import at.ac.uibk.igwee.metadata.geonames.GeonamesQueryResult;
import at.ac.uibk.igwee.metadata.geonames.GeonamesQueryService;
import at.ac.uibk.igwee.metadata.geonames.OrgGeonames;
import at.ac.uibk.igwee.metadata.httpclient.HttpClientException;
import at.ac.uibk.igwee.metadata.httpclient.HttpClientService;
import at.ac.uibk.igwee.metadata.httpclient.HttpMethod;
import at.ac.uibk.igwee.metadata.httpclient.ParameterPair;
import at.ac.uibk.igwee.metadata.query.Query;
import at.ac.uibk.igwee.metadata.query.QueryResult;
import at.ac.uibk.igwee.metadata.query.QueryService;
import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;
import at.ac.uibk.igwee.xslt.XsltService;

@Component
public class GeonamesHttpQueryServiceImpl implements GeonamesQueryService,
		QueryService {

	/**
	 * Service name
	 */
	protected static final String QUERYSERVICE_NAME = "QueryService geonames.org";
	
	/**
	 * Queried Authority
	 */
	protected static final List<Authority> QUERIED_AUTHORITY = Arrays.asList(OrgGeonames.getInstance());

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GeonamesHttpQueryServiceImpl.class);

	/**
	 * Username_info_location = /userinfo.properties
	 */
	private static final String USERNAME_INFO_LOCATION = "/userinfo.properties";

	/**
	 * org.geonames.username
	 */
	private static final String USERNAME_PROPERTY_KEY = "org.geonames.username";

	/**
	 * demo
	 */
	private static final String DEFAULT_USERNAME = "demo";

	/**
	 * startRow
	 */
	public static final String STARTROW_PARAMNAME = "startRow";

	/**
	 * maxRows
	 */
	public static final String MAXROW_PARAMNAME = "maxRows";

	/**
	 * q
	 */
	public static final String QUERY_PARAMNAME = "q";

	/**
	 * username
	 */
	public static final String USERNAME_PARAMNAME = "username";

	/**
	 * country
	 */
	public static final String COUNTRYCODE_PARAMNAME = "country";
	
	/**
	 * = /xsl/geonames2queryResult.xsl
	 */
	public static final String XSLT_STYLESHEET = "/xsl/geonames2queryResult.xsl";
	
	/**
	 * = /xsl/geonames2data.xsl
	 */
	public static final String ID_XSLT_STYLESHEET = "/xsl/geonames2data.xsl";

	/**
	 * Username, using static method to read the properties file
	 */
	private static final String USERNAME;
	static {
		Properties p = new Properties();
		try {
			p.load(GeonamesHttpQueryServiceImpl.class
					.getResourceAsStream(USERNAME_INFO_LOCATION));
		} catch (Exception e) {
			LOGGER.warn(
					"Cannot load the userinfo.properties file. Use default username instead.",
					e);
		}
		Object prop = p.get(USERNAME_PROPERTY_KEY);
		if (prop == null)
			prop = DEFAULT_USERNAME;
		USERNAME = prop.toString();
	}

	/**
	 * Host api.geonames.org
	 */
	public static final String GEONAMES_HOST = "api.geonames.org";
	/**
	 * path /search
	 */
	public static final String GEONAMES_PATH = "/search";

	/**
	 * predefined params: type=xml, style=FULL, username= username
	 */
	public static final List<ParameterPair> PREDEFINED_PARAMS = Arrays.asList(
			new ParameterPair("type", "xml"),
			new ParameterPair("style", "FULL"), 
			new ParameterPair(USERNAME_PARAMNAME, USERNAME)
			);

	/**
	 * The path for id query
	 */
	public static final String IDQUERY_PATH = "/get";
	
	/**
	 * Paramname for id
	 */
	public static final String IDQUERY_PARAMNAME = "geonameId";
	
	
	private HttpClientService httpClient;

	@Reference
	public void setHttpClientService(HttpClientService s) {
		this.httpClient = s;
	}
	
	private XsltService xsltService;
	
	@Reference
	public void setXsltService(XsltService xls) {
		this.xsltService = xls;
	}

	/**
	 * 
	 * @param name
	 * @param countryCode
	 * @param startRow
	 * @param maxRow
	 * @return the result of the http call as raw inputstream
	 * @throws GeonamesQueryException
	 */
	protected ByteArrayInputStream httpCall(String name, String countryCode,
			int startRow, int maxRow) throws GeonamesQueryException {
		try {

			List<ParameterPair> params = new ArrayList<>();
			params.addAll(PREDEFINED_PARAMS);
			params.add(new ParameterPair(QUERY_PARAMNAME, name));
			if (countryCode != null && !countryCode.isEmpty()) {
				Stream.of(countryCode.split("\\|")).forEach(
						s -> params.add(new ParameterPair(
								COUNTRYCODE_PARAMNAME, s)));
			}
			if (startRow > 1)
				params.add(new ParameterPair(STARTROW_PARAMNAME, Integer
						.toString(startRow)));
			if (maxRow > 1)
				params.add(new ParameterPair(MAXROW_PARAMNAME, Integer
						.toString(maxRow)));

			return httpClient.executeHttpGet(GEONAMES_HOST, GEONAMES_PATH,
					params);
		} catch (Exception e) {
			LOGGER.error("Cannot make http call to geonames.", e);
			throw new GeonamesQueryException(
					"Cannot make a http call to Geonames: " + e.getMessage());
		}
	}

	@Override
	public GeonamesQueryResult queryLocationInCountry(String name,
			String countryCode, int startRow, int maxRow)
			throws GeonamesQueryException {

		ByteArrayInputStream result = httpCall(name, countryCode, startRow,
				maxRow);
		
		InputStream xml;
		try {
			
			Map<String,String> params = new HashMap<>();
			params.put("startRow", Integer.toString(startRow));
			params.put("maxRows", Integer.toString(maxRow));
			xml = xsltService.doXslt(result, getClass().getResourceAsStream(XSLT_STYLESHEET), params);
		} catch (Exception e) {
			LOGGER.error("Cannot transform geonames xml to queryResult xml.", e);
			throw new GeonamesQueryException("Cannot transform geonames xml to queryResult xml: " + e.getMessage());
		}

		return XStreamSerializer.fromXML(xml);
	}

	@Override
	public QueryResult query(Query q) throws VocabularyException {

		if (q.getType() != null && q.getType() != VocabularyType.PLACE_NAME
				&& q.getType() != VocabularyType.UNKNOWN) {
			LOGGER.warn("Can only query place names with GeonamesQueryService.");
			return QueryResult.emptyResult(q);
		}
		
		if (q.getIncludedAuthority()!=null && !q.getIncludedAuthority().contains(OrgGeonames.getInstance()))
			return null;
		
		GeonamesQueryResult result = queryLocation(q.getQueryString(),
				q.getStartRow(), q.getMaxRow());

		return new QueryResult(q, result.getTotalhits(), result.getResults());
	}
	
	@Override
	public GeonameData queryId(String id) throws VocabularyException {
		
		ByteArrayInputStream queryResult = httpCallQueryId(id); 
		
		InputStream xsl = null;
		
		InputStream result = null;
		
		try {
			xsl = getClass().getResourceAsStream(ID_XSLT_STYLESHEET);
			result = xsltService.doXslt(queryResult, xsl, null);
			
		} catch (Exception e) {
			throw new GeonamesQueryException("Cannot do xslt to get the id query result: " 
					+ e.getMessage());
		} finally {
			if (xsl!=null) {
				try {
					xsl.close();
				} catch (Exception e2) {
					// Ignored
				}
			}
		}
		
		return XStreamSerializer.toGeonameData(result);
	}
	
	protected ByteArrayInputStream httpCallQueryId(String id) 
			throws GeonamesQueryException {
		
		LOGGER.debug("Geonames query id for {}.", id);
		
		List<ParameterPair> params = Arrays.asList(
				new ParameterPair(IDQUERY_PARAMNAME, id),
				new ParameterPair(USERNAME_PARAMNAME, USERNAME)
				);
		
		try {
			return httpClient.executeHttp(GEONAMES_HOST, IDQUERY_PATH, params, HttpMethod.GET);
		} catch (HttpClientException e) {
			LOGGER.error("Cannot do the id query.", e);
			throw new GeonamesQueryException("Cannot do the id query for geonames: " 
					+ e.getMessage());
		}
		
	}

	@Override
	public String getName() {
		return QUERYSERVICE_NAME;
	}
	
	@Override
	public Collection<? extends Authority> getQueriedAuthority() {
		return QUERIED_AUTHORITY;
	}

}
