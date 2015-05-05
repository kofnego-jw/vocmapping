package at.ac.uibk.igwee.metadata.query;

import java.util.Collection;

import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;

/**
 * Defines a query service
 * @author Joseph
 *
 */
public interface QueryService {
	
	/**
	 * 
	 * @return a unique name for the query service.
	 */
	public String getName();
	
	/**
	 * Main query method.
	 * @param query
	 * @return a query result
	 * @throws VocabularyException
	 */
	public QueryResult query(Query query) throws VocabularyException;
	
	/**
	 * Query for one ID
	 * @param id
	 * @return
	 * @throws VocabularyException
	 */
	public Vocabulary queryId(String id) throws VocabularyException;
	
	/**
	 * 
	 * @return the authority (authorities) that are queried by this service
	 */
	public Collection<? extends Authority> getQueriedAuthority();

}
