package at.ac.uibk.igwee.metadata.wikidata;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.uibk.igwee.metadata.vocabulary.AbstractVocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

/**
 * DataObject for Wikidata Vocabulary
 * @author joseph
 *
 */
public class WikidataVocabulary extends AbstractVocabulary {
	
	public static final String WIKIDATA_PREFIX = "wikidata:";
	
	public static final List<String> LANGUAGE_PRIORITY = Arrays.asList("de", "en");
	
	private String wikidataId;
	
	private Map<String,String> labels = new HashMap<>();
	
	public WikidataVocabulary() {
		super();
	}
	
	public WikidataVocabulary(String wikidataId, Map<String,String> names, VocabularyType type) {
		this(wikidataId, names, type, null);
	}
	
	public WikidataVocabulary(String wikidataId, Map<String,String> names, VocabularyType type, 
			Collection<Vocabulary>sameAsSet) {
		super(WIKIDATA_PREFIX + wikidataId, OrgWikidata.getInstance().createURI(wikidataId), 
				OrgWikidata.getInstance(), type, sameAsSet);
		this.labels = names;
		this.wikidataId = wikidataId;
	}
	
	@Override
	public String getName() {
		
		if (labels.isEmpty()) return "";
		
		return LANGUAGE_PRIORITY.stream()
				.map(lang -> labels.get(lang))
				.filter(label -> label!=null)
				.findFirst().orElse(labels.entrySet().stream().findFirst().get().getValue());
		
	}

	/**
	 * @return the wikidataId
	 */
	public String getWikidataId() {
		return wikidataId;
	}

	/**
	 * @param wikidataId the wikidataId to set
	 */
	public void setWikidataId(String wikidataId) {
		this.wikidataId = wikidataId;
	}

	/**
	 * @param name the name to set
	 */
	public void setLabels(Map<String,String> labels) {
		this.labels.clear();
		this.labels.putAll(labels);
	}
	
	private Object readResolve() {
		if (this.getAuthority()==null)
			this.setAuthority(OrgWikidata.getInstance());
		if (this.labels==null)
			this.labels = new HashMap<>();
		return this;
	}
	

}
