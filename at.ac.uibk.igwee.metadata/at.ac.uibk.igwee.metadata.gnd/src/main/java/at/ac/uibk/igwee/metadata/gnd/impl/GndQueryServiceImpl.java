package at.ac.uibk.igwee.metadata.gnd.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import at.ac.uibk.igwee.metadata.gnd.DeDnb;
import at.ac.uibk.igwee.metadata.gnd.GndException;
import at.ac.uibk.igwee.metadata.gnd.GndQueryResult;
import at.ac.uibk.igwee.metadata.gnd.GndQueryService;
import at.ac.uibk.igwee.metadata.httpclient.HttpClientService;
import at.ac.uibk.igwee.metadata.httpclient.ParameterPair;
import at.ac.uibk.igwee.metadata.query.Query;
import at.ac.uibk.igwee.metadata.query.QueryResult;
import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

@Component
public class GndQueryServiceImpl implements GndQueryService {
	
	public static final String SERVICE_NAME = "QueryService d-nb.info";
	
	protected static final List<Authority> QUERIED_AUTHORITY = Arrays.asList(DeDnb.getInstance());
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GndQueryServiceImpl.class);
	
	protected static final String GND_HOST = "services.dnb.de";
	
	protected static final String GND_QUERY_PATH = "/sru/authorities";
	
	protected static final List<ParameterPair> DEFAULT_PARAMS_QUERY = Arrays.asList(
			new ParameterPair("version", "1.1"),
			new ParameterPair("operation", "searchRetrieve"),
			new ParameterPair("recordSchema", "RDFxml")
			);
	
	protected static final String STARTROW_KEY = "startRecord";
	
	protected static final String MAXROWS_KEY = "maximumRecords";
	
	protected static final String QUERY_KEY = "query";
	
	private HttpClientService httpService;
	
	@Reference
	public void setHttpClientService(HttpClientService service) {
		this.httpService = service;
	}
	
	private GndResultConverter converter;
	
	@Reference
	public void setGndResultConverter(GndResultConverter conv) {
		this.converter = conv;
	}
	
	@Override
	public String getName() {
		return SERVICE_NAME;
	}
	
	@Override
	public QueryResult query(Query query) throws VocabularyException {
		if (!query.getIncludedAuthority().isEmpty() && !query.getIncludedAuthority().contains(DeDnb.getInstance()))
			return null;
		String queryString = query.getQueryString();
		VocabularyType type = query.getType();
		return convertToQueryResult(query, query(queryString,type, query.getStartRow(), query.getMaxRow()));
	}
	
	@Override
	public GndQueryResult query(String queryString, VocabularyType type,
			int startRow, int maxRows) throws GndException {
		
		LOGGER.debug("Querying gnd with qs={} and type={}.", queryString, type.toString() );
		
		List<GndQueryParameter> gndParams = new ArrayList<>(2);
		
		switch(type) {
		case PERSONAL_NAME:
			gndParams.add(new GndQueryParameter(GndIndex.PER, queryString));
			gndParams.add(new GndQueryParameter(GndIndex.BBG, GndBbgValue.PERSON.getGndValue()));
			break;
		case PLACE_NAME:
			gndParams.add(new GndQueryParameter(GndIndex.GEO, queryString));
			gndParams.add(new GndQueryParameter(GndIndex.BBG, GndBbgValue.PLACE.getGndValue()));
			break;
		case INSTITUTION_NAME:
			gndParams.add(new GndQueryParameter(GndIndex.KOE, queryString));
			gndParams.add(new GndQueryParameter(GndIndex.BBG, GndBbgValue.INSTITUTION.getGndValue()));
			break;
		default:
			gndParams.add(new GndQueryParameter(GndIndex.WOE, queryString));
		}
		
		return query(gndParams, startRow, maxRows);
	}
	
	@Override
	public Collection<? extends Authority> getQueriedAuthority() {
		return QUERIED_AUTHORITY;
	}
	
	
	
	public GndQueryResult query(List<GndQueryParameter> gndParams, int startRow, int maxRows)
			throws GndException {
		
		LOGGER.debug("Querying gnd with {} GndQueryParams.", Integer.toString(gndParams.size()));
		
		List<ParameterPair> params = new ArrayList<>();
		params.addAll(DEFAULT_PARAMS_QUERY);
		if (startRow!=1) {
			params.add(new ParameterPair(STARTROW_KEY, Integer.toString(startRow)));
		}
		
		if (maxRows!=10) {
			params.add(new ParameterPair(MAXROWS_KEY, Integer.toString(maxRows)));
		}
		
		params.add(new ParameterPair(QUERY_KEY, GndQueryBuilder.combineGndQueries(gndParams)));
		
		InputStream xml = null;
		try {
			xml = httpService.executeHttpGet(GND_HOST, GND_QUERY_PATH, params);
		} catch (Exception e) {
			LOGGER.error("Exception while querying the host.", e);
			throw new GndException("Exception while querying the host: " + e.getMessage());
		}
		
		return converter.convertToGndQueryResult(xml);
	}
	
	@Override
	public Vocabulary queryId(String id) throws VocabularyException {
		
		LOGGER.debug("Query GND ID {}.", id);
		
		List<GndQueryParameter> gndParams = new ArrayList<>();
		gndParams.add(new GndQueryParameter(GndIndex.IDN, id));
		
		GndQueryResult result = query(gndParams, 1, 1);
		
		if (result.getResults()==null || result.getResults().isEmpty()) {
			LOGGER.warn("Cannot find an GND entity with id {}.", id);
			throw new GndException("Cannot find GND ID " + id + "!");
		}
		
		assert(result.getResults().size()==1);
		
		return result.getResults().get(0);
	}

	
	
	protected static QueryResult convertToQueryResult(Query q, GndQueryResult r) {
		return new QueryResult(q, r.getTotalhits(), r.getResults());
	}
	
}
