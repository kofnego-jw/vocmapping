package at.ac.uibk.igwee.metadata.wikidata;

import at.ac.uibk.igwee.metadata.query.QueryService;

public interface WikidataQueryService extends QueryService {
	
	public static final int STARTROW_OFFSET = -1;
	public static final int DEFAULT_ROWSIZE = 30;
	
	default public WikidataQueryResult query(String queryString) throws WikidataException {
		return query(queryString, 1, DEFAULT_ROWSIZE);
	}
	
	public WikidataQueryResult query(String queryString, int startRow, int maxRows) 
			throws WikidataException;

}
