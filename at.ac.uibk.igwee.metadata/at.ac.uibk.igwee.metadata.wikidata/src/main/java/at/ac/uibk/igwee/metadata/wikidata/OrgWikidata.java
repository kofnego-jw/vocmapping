package at.ac.uibk.igwee.metadata.wikidata;

import java.net.URI;

import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

public final class OrgWikidata extends Authority {
	
	public static final URI BASEURI;
	static {
		URI test;
		try {
			test = new URI("http://www.wikidata.org/wiki/");
		} catch (Exception ignored) {
			// should never happen.
			test = null;
		}
		BASEURI = test;
	}
	
	private static final OrgWikidata INSTANCE = new OrgWikidata();
	
	public static OrgWikidata getInstance() {
		return INSTANCE;
	}
	
	
	private URI vocabularyUri = BASEURI;
	
	private OrgWikidata() {
		super("org.wikidata", "Wikidata", BASEURI);
	}
	
	@Override
	public URI getVocabularyBaseUri(VocabularyType type) throws VocabularyException {
		return this.vocabularyUri;
	}
	
	@Override
	public URI getVocabularyUri(Vocabulary voc) throws VocabularyException {
		if (!(voc instanceof WikidataVocabulary)) 
			throw new VocabularyException("Can only process Wikidata.");
		return ((WikidataVocabulary)voc).getURI();
	}
	
	public URI createURI(String wikidataId) {
		return BASEURI.resolve(wikidataId);
	}
	
	private Object readResolve() {
		return getInstance();
	}
	

}
