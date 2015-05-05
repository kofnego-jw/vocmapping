package at.ac.uibk.igwee.webapp.metadata.mdmapper.model;

import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;

public class VocabularyData {
	
	private String url;
	private String authority;
	private String id;
	private String name;
	
	public VocabularyData(Vocabulary voc) {
		super();
		this.url = voc.getURI() == null ? "" : voc.getURI().toString();
		this.authority = voc.getAuthority().getName();
		this.id = voc.getInternalID();
		this.name = voc.getName();
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the authority
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * @param authority the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
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
	
	

}
