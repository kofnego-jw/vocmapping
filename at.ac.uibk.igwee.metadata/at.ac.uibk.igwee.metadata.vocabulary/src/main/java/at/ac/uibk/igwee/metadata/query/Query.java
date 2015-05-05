package at.ac.uibk.igwee.metadata.query;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;

/**
 * DataObject for VocabularyQuery
 * @author totoro
 *
 */
public class Query {

	/**
	 * Default start row = 1
	 */
	public static final int DEFAULT_START_ROW = 1;
	/**
	 * Default Max Rows = 30
	 */
	public static final int DEFAULT_MAX_ROW = 30;
	
	/**
	 * The type
	 */
	private VocabularyType type;
	/**
	 * Querystring
	 */
	private String queryString;
	/**
	 * Included authority. if empty then the query doesn't care about authority
	 */
	private Set<Authority> includedAuthority = new HashSet<>();
	/**
	 * startRow, counting starts with 1
	 */
	private int startRow = DEFAULT_START_ROW;
	/**
	 * Maximum rows returned.
	 */
	private int maxRow = DEFAULT_MAX_ROW;
	
	public Query() {
		super();
	}
	

	public Query(VocabularyType type, String queryString,
			Collection<Authority> includedAuthority) {
		super();
		this.type = type;
		this.queryString = queryString;
		if (includedAuthority!=null) this.includedAuthority.addAll(includedAuthority);
	}
	
	public Query(VocabularyType type, String queryString,
			Set<Authority> includedAuthority, int startRow, int maxRow) {
		super();
		this.type = type;
		this.queryString = queryString;
		if (includedAuthority!=null) 
			this.includedAuthority.addAll(includedAuthority);
		this.startRow = startRow;
		this.maxRow = maxRow;
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
	 * @return the includedAuthority
	 */
	public Set<Authority> getIncludedAuthority() {
		return includedAuthority;
	}

	/**
	 * @param includedAuthority the includedAuthority to set
	 */
	public void setIncludedAuthority(Set<Authority> includedAuthority) {
		if (includedAuthority==null) {
			this.includedAuthority = new HashSet<>();
		} else {
			this.includedAuthority = includedAuthority;
		}
	}
	
	


	/**
	 * @return the startRow
	 */
	public int getStartRow() {
		return startRow;
	}


	/**
	 * @param startRow the startRow to set
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}


	/**
	 * @return the maxRow
	 */
	public int getMaxRow() {
		return maxRow;
	}


	/**
	 * @param maxRow the maxRow to set
	 */
	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maxRow;
		result = prime * result
				+ ((queryString == null) ? 0 : queryString.hashCode());
		result = prime * result + startRow;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Query other = (Query) obj;
		if (maxRow != other.maxRow)
			return false;
		if (queryString == null) {
			if (other.queryString != null)
				return false;
		} else if (!queryString.equals(other.queryString))
			return false;
		if (startRow != other.startRow)
			return false;
		if (type != other.type)
			return false;
		return true;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VocabularyQuery [type=" + type + ", queryString=" + queryString
				+ ", includedAuthority=" + includedAuthority + ", startRow="
				+ startRow + ", maxRow=" + maxRow + "]";
	}

	
	
	
}
