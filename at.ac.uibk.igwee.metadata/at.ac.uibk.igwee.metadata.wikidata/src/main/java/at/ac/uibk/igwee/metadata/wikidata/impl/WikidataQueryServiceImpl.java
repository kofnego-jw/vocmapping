package at.ac.uibk.igwee.metadata.wikidata.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import at.ac.uibk.igwee.metadata.query.Query;
import at.ac.uibk.igwee.metadata.query.QueryResult;
import at.ac.uibk.igwee.metadata.query.QueryService;
import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;
import at.ac.uibk.igwee.metadata.wikidata.OrgWikidata;
import at.ac.uibk.igwee.metadata.wikidata.WikidataException;
import at.ac.uibk.igwee.metadata.wikidata.WikidataQueryResult;
import at.ac.uibk.igwee.metadata.wikidata.WikidataQueryService;
import at.ac.uibk.igwee.metadata.wikidata.WikidataVocabulary;

/**
 * Basic implementation, needs a HttpQueryHelper
 * @author totoro
 *
 */
@Component
public class WikidataQueryServiceImpl implements WikidataQueryService, QueryService {

	protected static final String NAME = "QueryService wikidata.org";

	protected static final List<Authority> QUERIED_AUTHORITY = Arrays.asList(OrgWikidata.getInstance());
	
	private HttpQueryHelper httpQueryHelper;
	
	@Reference
	public void setHttpQueryHelper(HttpQueryHelper service) {
		this.httpQueryHelper = service;
	}
	
	public WikidataQueryServiceImpl() {
		super();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public WikidataQueryResult query(String queryString, int startRow,
			int maxRows) throws WikidataException {
		return httpQueryHelper.query(queryString, startRow, maxRows);
	}

	@Override
	public QueryResult query(Query query) throws VocabularyException {
		if (!query.getIncludedAuthority().isEmpty() 
				&& !query.getIncludedAuthority().contains(OrgWikidata.getInstance()))
			return null;
		WikidataQueryResult result = 
				query(query.getQueryString(), query.getStartRow(), query.getMaxRow());
		return convert(result);
	}
	
	@Override
	public WikidataVocabulary queryId(String id) throws VocabularyException {
		return httpQueryHelper.queryTitle(id);
	}
	
	@Override
	public Collection<? extends Authority> getQueriedAuthority() {
		return QUERIED_AUTHORITY;
	}
	
	protected static QueryResult convert(WikidataQueryResult result) {
		
		Query q = new Query(VocabularyType.UNKNOWN, result.getQueryString(), 
				null, result.getStartRow(), result.getMaxRows());
		
		return new QueryResult(q, result.getTotalhits(), result.getResults());
	}

}
