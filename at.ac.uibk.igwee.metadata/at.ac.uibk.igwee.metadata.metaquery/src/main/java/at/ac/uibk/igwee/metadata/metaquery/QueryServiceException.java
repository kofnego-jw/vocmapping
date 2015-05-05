package at.ac.uibk.igwee.metadata.metaquery;

import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;

/**
 * Exception wrapper for queryService
 * @author Joseph
 *
 */
public class QueryServiceException extends VocabularyException {
	
	private static final long serialVersionUID = 201503271452L;
	
	public QueryServiceException(String msg) {
		super(msg);
	}

}
