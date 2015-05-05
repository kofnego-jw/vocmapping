package at.ac.uibk.igwee.metadata.vocabulary;

/**
 * Generalized Exception class. Careful, does not call super() 
 * to prevent problems when used in webservices.
 * @author joseph
 *
 */
public class VocabularyException extends Exception {
	
	private static final long serialVersionUID = 201503241210L;
	
	/**
	 * Message
	 */
	private String message;
	
	public VocabularyException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

}
