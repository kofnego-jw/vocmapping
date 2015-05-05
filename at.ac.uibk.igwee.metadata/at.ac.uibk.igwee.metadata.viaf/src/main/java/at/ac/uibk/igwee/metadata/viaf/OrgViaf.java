package at.ac.uibk.igwee.metadata.viaf;

import java.net.URI;

import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

/**
 * Singleton Class, implementation of Authority for www.viaf.org 
 * @author Joseph
 *
 */
public final class OrgViaf extends Authority {
	
	/**
	 * Base URI = http://www.viaf.org/viaf/
	 */
	protected static final URI BASEURI;
	static {
		URI test;
		try {
			test = new URI("http://www.viaf.org/viaf/");
		} catch (Exception e) {
			test = null;
		}
		BASEURI = test;
	}
	
	/**
	 * Instance
	 */
	private static final OrgViaf INSTANCE = new OrgViaf();
	/**
	 * 
	 * @return the only instance of OrgViaf
	 */
	public static OrgViaf getInstance() {
		return INSTANCE;
	}
	
	private OrgViaf() {
		super("org.viaf", "VIAF", BASEURI);
	}
	
	@Override
	public URI getVocabularyBaseUri(VocabularyType type) {
		return BASEURI;
	}
	
	@Override
	public URI getVocabularyUri(Vocabulary voc) throws VocabularyException {
		if (!(voc instanceof ViafVocabulary))
			throw new VocabularyException("Can only process ViafVocabulary.");
		return ((ViafVocabulary)voc).getURI();
	}
	
	/**
	 * Given an Viaf-ID, returns the url
	 * @param id the VIAF ID ("123456778")
	 * @return the URI to the resource
	 */
	public URI createURI(String id) {
		return BASEURI.resolve(id);
	}
	
	private Object readResolve() {
		return getInstance();
	}

}
