package at.ac.uibk.igwee.metadata.vocabulary;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base abstract class for vocabularies. Maybe useful
 * @author joseph
 *
 */
public abstract class AbstractVocabulary implements Vocabulary {

	/**
	 * id
	 */
	private String id;
	
	/**
	 * uri
	 */
	private URI uri;
	
	/**
	 * Authority
	 */
	private Authority authority;
	
	/**
	 * Type
	 */
	private VocabularyType vocabularyType;
	
	/**
	 * SameAs set
	 */
	private Set<Vocabulary> sameAsSet = new HashSet<>();
	
	
	
	public AbstractVocabulary() {
		super();
	}
	
	public AbstractVocabulary(String id, URI uri, Authority authority,
			VocabularyType type, Collection<Vocabulary> sameAsSet) {
		super();
		this.id = id;
		this.uri = uri;
		this.authority = authority;
		this.vocabularyType = type;
		if (sameAsSet!=null) this.sameAsSet.addAll(sameAsSet);
	}



	/**
	 * @return the id
	 */
	@Override
	public String getInternalID() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setInternalID(String id) {
		this.id = id;
	}

	/**
	 * @return the uri
	 */
	@Override
	public URI getURI() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setURI(URI uri) {
		this.uri = uri;
	}

	/**
	 * @return the authority
	 */
	@Override
	public Authority getAuthority() {
		return authority;
	}

	/**
	 * @param authority the authority to set
	 */
	public void setAuthority(Authority authority) {
		this.authority = authority;
	}
	
	public Set<Vocabulary> getSameAsSet() {
		return this.sameAsSet;
	}

	/**
	 * @return the sameAsSet
	 */
	@Override
	public Set<Vocabulary> getKnownSameAs() {
		return sameAsSet.stream().collect(Collectors.toSet());
	}

	/**
	 * @param sameAsSet the sameAsSet to set
	 */
	public void setSameAsSet(Set<Vocabulary> sameAsSet) {
		this.sameAsSet = sameAsSet;
	}

	/**
	 * @return the vocabularyType
	 */
	@Override
	public VocabularyType getVocabularyType() {
		return vocabularyType;
	}

	/**
	 * @param vocabularyType the vocabularyType to set
	 */
	public void setVocabularyType(VocabularyType vocabularyType) {
		this.vocabularyType = vocabularyType;
	}
	
	@Override
	public void addSameAs(Vocabulary uri) {
		this.sameAsSet.add(uri);
	}
	
	@Override
	public void removeSameAs(Vocabulary uri) {
		this.sameAsSet.remove(uri);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AbstractVocabulary [id=" + id + ", uri=" + uri + ", authority="
				+ authority + ", vocabularyType=" + vocabularyType
				+ ", sameAsSet=" + sameAsSet + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authority == null) ? 0 : authority.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractVocabulary other = (AbstractVocabulary) obj;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	
	
	
}
