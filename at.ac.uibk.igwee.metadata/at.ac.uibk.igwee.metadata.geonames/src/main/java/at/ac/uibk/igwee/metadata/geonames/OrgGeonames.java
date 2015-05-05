package at.ac.uibk.igwee.metadata.geonames;

import java.net.URI;

import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

public class OrgGeonames extends Authority {
	
	public static final URI BASEURI;
	static {
		URI test;
		try {
			test = new URI("http://geonames.org/");
		} catch (Exception ignored) {
			// should never happen.
			test = null;
		}
		BASEURI = test;
	}
	
	private static final OrgGeonames INSTANCE = new OrgGeonames();
	
	public static OrgGeonames getInstance() {
		return INSTANCE;
	}
	
	
	private URI vocabularyUri = BASEURI;
	
	private OrgGeonames() {
		super("org.geonames", "Geonames", BASEURI);
	}
	
	@Override
	public URI getVocabularyBaseUri(VocabularyType type) throws VocabularyException {
		if (type!=VocabularyType.PLACE_NAME) 
			throw new VocabularyException("Geonames contain only placename vocabularies.");
		return this.vocabularyUri;
	}
	
	@Override
	public URI getVocabularyUri(Vocabulary voc) throws VocabularyException {
		if (!(voc instanceof GeonameData)) 
			throw new VocabularyException("Can only process GeonameData.");
		return ((GeonameData)voc).getURI();
	}
	
	public URI createURI(int geonamesId) {
		return BASEURI.resolve(Integer.toString(geonamesId));
	}
	
	private Object readResolve() {
		return getInstance();
	}

}
