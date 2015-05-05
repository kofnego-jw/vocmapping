package at.ac.uibk.igwee.metadata.geonames;

import java.util.List;

/**
 * Modelling of a search result in geonames
 * @author joseph
 *
 */
public class GeonamesQueryResult {
	
	/**
	 * total hits
	 */
	private int totalhits;
	
	/**
	 * start row
	 */
	private int startRow;
	
	/**
	 * maximum rows
	 */
	private int maxRows;
	/**
	 * the results
	 */
	private List<GeonameData> results;
	
	public GeonamesQueryResult() {
		super();
	}
	
	public GeonamesQueryResult(int totalhits, int startRow, int maxRows, List<GeonameData> results) {
		super();
		this.totalhits = totalhits;
		this.startRow = startRow;
		this.maxRows = maxRows;
		this.results = results;
	}

	/**
	 * @return the totalhits
	 */
	public int getTotalhits() {
		return totalhits;
	}

	/**
	 * @return the results
	 */
	public List<GeonameData> getResults() {
		return results;
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
	 * @param totalhits the totalhits to set
	 */
	public void setTotalhits(int totalhits) {
		this.totalhits = totalhits;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<GeonameData> results) {
		this.results = results;
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
	 * 
	 * @return true if no results are available after this set.
	 */
	public boolean isLastResult() {
		return this.startRow + this.maxRows >= this.totalhits;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GeonamesQueryResult [totalhits=" + totalhits + ", startRow="
				+ startRow + ", maxRows=" + maxRows + ", results=" + results
				+ "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maxRows;
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
		GeonamesQueryResult other = (GeonamesQueryResult) obj;
		if (maxRows != other.maxRows)
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
