package at.ac.uibk.igwee.webapp.metadata.mdmapper.model;

import java.util.stream.Collectors;

import at.ac.uibk.igwee.metadata.metaquery.VocabularyQuery;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

public class QueryData {

	private String name;
	private String additionalInfo;
	private String queryString;
	private VocabularyType type;
	private String authorities;
	
	public QueryData(VocabularyQuery vocQ) {
		this.name = vocQ.getName();
		this.additionalInfo = vocQ.getAdditionalInfo();
		this.queryString = vocQ.getQueryString();
		this.type = vocQ.getType();
		this.authorities = vocQ.getIncludedAuthority() == null ? "" :
			vocQ.getIncludedAuthority().stream()
				.map(auth -> auth.getName()).collect(Collectors.joining("|"));
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

	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * @return the type
	 */
	public VocabularyType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(VocabularyType type) {
		this.type = type;
	}

	/**
	 * @return the authorities
	 */
	public String getAuthorities() {
		return authorities;
	}

	/**
	 * @param authorities the authorities to set
	 */
	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}
	
	
	
}
