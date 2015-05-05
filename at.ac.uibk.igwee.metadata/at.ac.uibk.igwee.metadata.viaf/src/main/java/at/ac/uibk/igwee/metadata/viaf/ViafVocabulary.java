package at.ac.uibk.igwee.metadata.viaf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import at.ac.uibk.igwee.metadata.vocabulary.AbstractVocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

/**
 * DataObject for a VIAF vocabulary.
 * @author Joseph
 *
 */
public class ViafVocabulary extends AbstractVocabulary {
	
	/**
	 * Prefix for internal ID: "viaf:"
	 */
	public static final String VIAF_PREFIX = "viaf:";
	
	/**
	 * Name of the vocabulary
	 */
	private String name;
	
	/**
	 * Linked sources, e.g. "GND":"12345", "LNB":"LNC10-000204134"
	 */
	private Map<String,String> linkedSources = new HashMap<>();
	
	protected ViafVocabulary() {
		super();
	}
	
	protected ViafVocabulary(String viafId, String name, VocabularyType type) {
		super(VIAF_PREFIX + viafId, OrgViaf.getInstance().createURI(viafId),
				OrgViaf.getInstance(), type, null);
		this.name = name;
	}
	
	protected ViafVocabulary(String viafId, String name, VocabularyType type, 
			Collection<Vocabulary> sameAs, Map<String,String> linked) {
		super(VIAF_PREFIX + viafId, OrgViaf.getInstance().createURI(viafId),
				OrgViaf.getInstance(), type, sameAs);
		this.name = name;
		this.linkedSources.putAll(linked);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * @return the linkedSources
	 */
	public Map<String, String> getLinkedSources() {
		return linkedSources;
	}

	/**
	 * @param linkedSources the linkedSources to set
	 */
	public void setLinkedSources(Map<String, String> linkedSources) {
		this.linkedSources = linkedSources;
	}
	
	/**
	 * 
	 * @param sourceID
	 * @return the ID (not the URL) for the given sourceID or null if there is no ID recorded.
	 */
	public String getLinkedID(String sourceID) {
		return this.linkedSources.get(sourceID);
	}

	private Object readResolve() {
		this.setAuthority(OrgViaf.getInstance());
		if (this.linkedSources==null) 
			this.linkedSources = new HashMap<>();
		return this;
	}

	
}
