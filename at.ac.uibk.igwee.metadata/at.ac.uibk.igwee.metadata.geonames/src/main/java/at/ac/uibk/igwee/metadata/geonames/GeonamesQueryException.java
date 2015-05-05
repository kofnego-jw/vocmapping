package at.ac.uibk.igwee.metadata.geonames;

import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;

/**
 * Exception wrapper for the service. Careful, the exception will not contain the 
 * cause to prevent errors with webservice. Therefore always use Logger to log the 
 * exceptions.
 * @author joseph
 *
 */
public class GeonamesQueryException extends VocabularyException {
	
	private static final long serialVersionUID = 201503231650L;
	
	public GeonamesQueryException(String message) {
		super(message);
	}	
	

}
