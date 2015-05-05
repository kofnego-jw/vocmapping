package at.ac.uibk.igwee.metadata.vocabulary;

import java.net.URI;

public abstract class Authority {
	
	private String id;
	
	private String name;
	
	private URI uri;
	
	private URI baseUri;
	
	public Authority() {
		super();
	}
	
	public Authority(String id, String name, URI baseURI) {
		super();
		this.id = id;
		this.name = name;
		this.uri = baseURI;
		this.baseUri = baseURI;
	}

	public Authority(String id, String name, URI uri, URI baseUri) {
		super();
		this.id = id;
		this.name = name;
		this.uri = uri;
		this.baseUri = baseUri;
	}
	
	

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the uri
	 */
	public URI getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(URI uri) {
		this.uri = uri;
	}

	/**
	 * @return the baseUri
	 */
	public URI getBaseUri() {
		return baseUri;
	}

	/**
	 * @param baseUri the baseUri to set
	 */
	public void setBaseUri(URI baseUri) {
		this.baseUri = baseUri;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 * @throws VocabularyException
	 */
	abstract public URI getVocabularyBaseUri(VocabularyType type) 
			throws VocabularyException;
	
	/**
	 * 
	 * @param voc
	 * @return
	 * @throws VocabularyException
	 */
	abstract public URI getVocabularyUri(Vocabulary voc) 
			throws VocabularyException;
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Authority other = (Authority) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Authority [id=" + id + ", name=" + name + ", uri=" + uri
				+ ", baseUri=" + baseUri + "]";
	}
	
	
	

}
