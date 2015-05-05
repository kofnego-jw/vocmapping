package at.ac.uibk.igwee.metadata.metaquery;

import java.util.Set;

import at.ac.uibk.igwee.metadata.query.Query;
import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

/**
 * DataObject for a query for a vocabulary.
 * Sadly, because this is also used in webservices, we cannot enforce immutability.
 * @author Joseph
 *
 */
public class VocabularyQuery extends Query {
	
	/**
	 * Name of the query, e.g. "Horvath, Ödön"; can also be used for "KEY-Value" 
	 * in a Metadata-Mapping
	 */
	private String name;
	/**
	 * Any additional info. Useful for displaying purposes. For certain authority services, this
	 * can be used, but mostly this will be ignored. 
	 */
	private String additionalInfo;
	
	public VocabularyQuery() {
		super();
	}

	public VocabularyQuery(String name, String queryString,
			String additionalInfo, VocabularyType type,
			Set<Authority> usedAuthorities) {
		super(type, queryString, usedAuthorities);
		this.name = name;
		this.additionalInfo = additionalInfo;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the additionalInfo
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	/**
	 * @param additionalInfo the additionalInfo to set
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VocabularyQuery [name=" + name + ", additionalInfo="
				+ additionalInfo + "]";
	}

	
	
	
	
	

}
