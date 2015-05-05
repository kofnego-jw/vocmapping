package at.ac.uibk.igwee.metadata.wikidata;

import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;

/**
 * Wrapper Exception Class
 * @author joseph
 *
 */
public class WikidataException extends VocabularyException {
	
	private static final long serialVersionUID = 201503311720L;
	
	public WikidataException(String msg) {
		super(msg);
	}

}
