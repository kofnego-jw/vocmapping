package at.ac.uibk.igwee.metadata.wikidata;

import java.util.List;

public class WikidataQueryResult {
	
	/**
	 * totalhits, visible from the SRU
	 */
	private int totalhits;
	
	/**
	 * Starting row
	 */
	private int startRow;
	
	/**
	 * Maximum row.
	 */
	private int maxRows;
	
	/**
	 * position of the next row, if exists
	 */
	private int nextRow;
	
	/**
	 * the query string
	 */
	private String queryString;
	
	/**
	 * Results
	 */
	private List<WikidataVocabulary> results;
	
	public WikidataQueryResult() {
		super();
	}

	public WikidataQueryResult(int totalhits, int startRow, int maxRows,
			int nextRow, String queryString,
			List<WikidataVocabulary> results) {
		super();
		this.totalhits = totalhits;
		this.startRow = startRow;
		this.maxRows = maxRows;
		this.nextRow = nextRow;
		this.queryString = queryString;
		this.results = results;
	}

	/**
	 * @return the totalhits
	 */
	public int getTotalhits() {
		return totalhits;
	}

	/**
	 * @param totalhits the totalhits to set
	 */
	public void setTotalhits(int totalhits) {
		this.totalhits = totalhits;
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
	 * @return the maxRows
	 */
	public int getMaxRows() {
		return maxRows;
	}

	/**
	 * @param maxRows the maxRows to set
	 */
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	/**
	 * @return the nextRow
	 */
	public int getNextRow() {
		return nextRow;
	}

	/**
	 * @param nextRow the nextRow to set
	 */
	public void setNextRow(int nextRow) {
		this.nextRow = nextRow;
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
	 * @return the results
	 */
	public List<WikidataVocabulary> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<WikidataVocabulary> results) {
		this.results = results;
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WikidataQueryResult [totalhits=" + totalhits + ", startRow="
				+ startRow + ", maxRows=" + maxRows + ", nextRow=" + nextRow
				+ ", queryString=" + queryString + ", results=" + results + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maxRows;
		result = prime * result + nextRow;
		result = prime * result
				+ ((queryString == null) ? 0 : queryString.hashCode());
		result = prime * result + ((results == null) ? 0 : results.hashCode());
		result = prime * result + startRow;
		result = prime * result + totalhits;
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
		WikidataQueryResult other = (WikidataQueryResult) obj;
		if (maxRows != other.maxRows)
			return false;
		if (nextRow != other.nextRow)
			return false;
		if (queryString == null) {
			if (other.queryString != null)
				return false;
		} else if (!queryString.equals(other.queryString))
			return false;
		if (results == null) {
			if (other.results != null)
				return false;
		} else if (!results.equals(other.results))
			return false;
		if (startRow != other.startRow)
			return false;
		if (totalhits != other.totalhits)
			return false;
		return true;
	}

	

}
