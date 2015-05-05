package at.ac.uibk.igwee.metadata.gnd;

import at.ac.uibk.igwee.metadata.query.QueryService;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

public interface GndQueryService extends QueryService {
	
	public static final int DEFAULT_MAX_ROWS = 30;
	public static final int DEFAULT_START_ROW = 1;
	
	public GndQueryResult query(String queryString, VocabularyType type, 
			int startRow, int maxRows) throws GndException;
	
	default public GndQueryResult query(String queryString, VocabularyType type) throws GndException {
		return query(queryString, type, DEFAULT_START_ROW, DEFAULT_MAX_ROWS);
	}
	
	default public GndQueryResult queryForAll(String queryString) throws GndException {
		return query(queryString, VocabularyType.UNKNOWN);
	}
	
	default public GndQueryResult queryForPerson(String name) throws GndException {
		return query(name, VocabularyType.PERSONAL_NAME);
	}
	
	default public GndQueryResult queryForPlace(String loc) throws GndException {
		return query(loc, VocabularyType.PLACE_NAME);
	}
	
	default public GndQueryResult queryForCorporate(String inst) throws GndException {
		return query(inst, VocabularyType.INSTITUTION_NAME);
	}
	
	

}
