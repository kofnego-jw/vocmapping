package at.ac.uibk.igwee.metadata.metaquery;

import java.util.List;

import at.ac.uibk.igwee.metadata.query.QueryService;

/**
 * Interface definition for a MetaQueryService.
 * A MetaQueryService searches for multiple queryServices and puts all results together.
 * @author Joseph
 *
 */
public interface MetaQueryService {
	
	public static final int DEFAULT_MAX_ROWS = 30;
	
	/**
	 * Main Query method for one query
	 * @param query
	 * @return
	 * @throws QueryServiceException
	 */
	public VocabularyQueryResult processQuery(VocabularyQuery query) throws QueryServiceException;
	
	/**
	 * Main method for querying a whole queue of queries
	 * @param queue
	 * @param requeryFixed set to true if all in QueryQueue should be requeried
	 * @return
	 * @throws QueryServiceException
	 */
	public QueryQueue processQueries(QueryQueue queue, boolean requeryFixed) throws QueryServiceException;
	
	/**
	 * Uses processQueries(queue, false)
	 * @param queue
	 * @return
	 */
	default QueryQueue processQueries(QueryQueue queue) throws QueryServiceException {
		return processQueries(queue, false);
	}
	
	/**
	 * Registers a queryService
	 * @param qs
	 */
	public void registerQueryService(QueryService qs);
	
	/**
	 * Deregisters a queryservice. if it is not registered, nothing should happen.
	 * @param qs
	 */
	public void deregisterQueryService(QueryService qs);
	
	/**
	 * 
	 * @return a list of String containing the name of the query services.
	 */
	public List<String> getQueryServiceNames();

}
