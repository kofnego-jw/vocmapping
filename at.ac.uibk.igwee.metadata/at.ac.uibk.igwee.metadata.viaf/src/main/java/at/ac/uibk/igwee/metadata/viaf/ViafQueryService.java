package at.ac.uibk.igwee.metadata.viaf;

import at.ac.uibk.igwee.metadata.query.QueryService;


/**
 * Main Service interface for querying VIAF for any vocabulary.
 * @author joseph
 *
 */
public interface ViafQueryService extends QueryService {
	
	/**
	 * Default max row = 20, 
	 * as now (20150326 the max rows is limited to 10 by VIAF, the implementation should be 
	 * able to make multiple requests to gather the results)
	 */
	public static final int DEFAULT_MAXROWS = 20;
	/**
	 * Default start row = 1 (VIAF starts counting at 1)
	 */
	public static final int DEFAULT_STARTROW = 1;
	
	/**
	 * Query for everything, using the default max rows
	 * @param queryString
	 * @return A ViafQueryResult, should never be null.
	 * @throws ViafVocabularyException
	 */
	default public ViafQueryResult queryForAll(String queryString) 
			throws ViafVocabularyException {
		return queryForAll(queryString, DEFAULT_STARTROW, DEFAULT_MAXROWS, null);
	}
	
	/**
	 * Query for everything
	 * @param queryString
	 * @param startRow
	 * @param maxRows the maximal rows returned by 
	 * @param restrictToAuthority restrict to only one authority, e.g. "DNB"
	 * @return A ViafQueryResult, should never be null
	 * @throws ViafVocabularyException
	 */
	public ViafQueryResult queryForAll(String queryString, int startRow, int maxRows, 
			String restrictToAuthority) throws ViafVocabularyException;
	
	/**
	 * Query for personal names only
	 * @param queryString
	 * @return A ViafQueryResult, should never be null.
	 * @throws ViafVocabularyException
	 */
	default public ViafQueryResult queryForPersonalName(String queryString) 
			throws ViafVocabularyException {
		return queryForPersonalName(queryString, DEFAULT_STARTROW, DEFAULT_MAXROWS, null);
	}
	
	/**
	 * Query for Personal names.
	 * @param queryString
	 * @param startRow
	 * @param maxRows
	 * @param restrictToAuthority
	 * @return A ViafQueryResult, should never be null.
	 * @throws ViafVocabularyException
	 */
	public ViafQueryResult queryForPersonalName(String queryString, int startRow, int maxRows, 
			String restrictToAuthority) throws ViafVocabularyException;
	
	/**
	 * Query for corporate
	 * @param queryString
	 * @return a ViafQueryResult, should never be null
	 * @throws ViafVocabularyException
	 */
	default public ViafQueryResult queryForCorporateName(String queryString) 
			throws ViafVocabularyException {
		return queryForCorporateName(queryString, DEFAULT_STARTROW, DEFAULT_MAXROWS, null);
	}
	
	/**
	 * Query for corporate
	 * @param queryString
	 * @param startRow
	 * @param maxRows
	 * @param restrictToAuthority
	 * @return a ViafQueryResult, should never be null
	 * @throws ViafVocabularyException
	 */
	public ViafQueryResult queryForCorporateName(String queryString, int startRow, int maxRows, 
			String restrictToAuthority) throws ViafVocabularyException;

	/**
	 * Query for geographical names
	 * @param queryString
	 * @return a ViafQueryResult, should never be null
	 * @throws ViafVocabularyException
	 */
	default public ViafQueryResult queryForGeographicalName(String queryString) 
			throws ViafVocabularyException {
		return queryForGeographicalName(queryString, DEFAULT_STARTROW, DEFAULT_MAXROWS, null);
	}
	/**
	 * Query for geographical names
	 * @param queryString
	 * @param startRow
	 * @param maxRows
	 * @param restrictToAuthority
	 * @return A ViafQueryResult, should never be null.
	 * @throws ViafVocabularyException
	 */
	public ViafQueryResult queryForGeographicalName(String queryString, int startRow, int maxRows, 
			String restrictToAuthority) throws ViafVocabularyException;


}
