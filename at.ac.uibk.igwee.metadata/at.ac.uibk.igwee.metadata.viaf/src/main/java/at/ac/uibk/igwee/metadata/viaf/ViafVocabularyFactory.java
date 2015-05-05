package at.ac.uibk.igwee.metadata.viaf;

import java.util.Collection;
import java.util.Map;

import aQute.bnd.annotation.component.Component;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

/**
 * Factory class for a VIAF vocabulary
 * @author Joseph
 *
 */
@Component(provide=ViafVocabularyFactory.class)
public class ViafVocabularyFactory {
	
	/**
	 * 
	 * @param viafId
	 * @param name
	 * @param type
	 * @return a ViafVocabulary
	 */
	public ViafVocabulary createViafVocabulary(String viafId, String name, VocabularyType type) {
		return new ViafVocabulary(viafId, name, type);
	}
	
	/**
	 * 
	 * @param viafId
	 * @param name
	 * @param type
	 * @param sameAs
	 * @param linked
	 * @return a ViafVocabulary
	 */
	public ViafVocabulary createViafVocabulary(String viafId, String name, 
			VocabularyType type, Collection<Vocabulary> sameAs, Map<String,String> linked) {
		return new ViafVocabulary(viafId, name, type, sameAs, linked);
	}

}
