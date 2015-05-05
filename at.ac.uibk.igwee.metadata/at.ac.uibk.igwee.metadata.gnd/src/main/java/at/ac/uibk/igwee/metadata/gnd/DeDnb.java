package at.ac.uibk.igwee.metadata.gnd;

import java.net.URI;

import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

public class DeDnb extends Authority {
	
	private static final URI BASEURI;
	static {
		URI uri;
		try {
			uri = new URI("http://d-nb.info/gnd/");
		} catch (Exception shouldNeverHappen) {
			uri = null;
		}
		BASEURI = uri;
	}
	
	private static final DeDnb INSTANCE = new DeDnb();
	
	public static DeDnb getInstance() {
		return INSTANCE;
	}
	
	private DeDnb() {
		super("info.d-nb", "GND", BASEURI);
	}
	
	@Override
	public URI getVocabularyBaseUri(VocabularyType type)
			throws VocabularyException {
		return getBaseUri();
	}
	
	@Override
	public URI getVocabularyUri(Vocabulary voc) throws VocabularyException {
		
		if (voc.getURI()!=null) return voc.getURI();
		
		if (! (voc instanceof GndVocabulary))
			throw new VocabularyException("Cannot create Uri from not GND vocabulary.");
		
		GndVocabulary gnd = (GndVocabulary) voc;
		
		return createURI(gnd.getGndId());
	}
	
	public URI createURI(String gndId) {
		return BASEURI.resolve(gndId);
	}

}
