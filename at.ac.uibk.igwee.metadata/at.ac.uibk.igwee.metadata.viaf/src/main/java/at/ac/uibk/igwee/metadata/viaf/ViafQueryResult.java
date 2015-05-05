package at.ac.uibk.igwee.metadata.viaf;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DataObject for a ViafQueryResult.
 * @author Joseph
 *
 */
public class ViafQueryResult {
	
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
	 * Results
	 */
	private List<ViafVocabulary> results;
	
	public ViafQueryResult() {
		super();
	}

	public ViafQueryResult(int totalhits, int startRow, int maxRows,
			int nextRow,
			List<ViafVocabulary> results) {
		super();
		this.totalhits = totalhits;
		this.startRow = startRow;
		this.maxRows = maxRows;
		this.nextRow = nextRow;
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
	 * @return the results
	 */
	public List<ViafVocabulary> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<ViafVocabulary> results) {
		this.results = results;
	}
	
	/**
	 * Get a sorted result list using a comparator
	 * @param comparator
	 * @return
	 */
	public List<ViafVocabulary> getSortedResults(Comparator<ViafVocabulary> comparator) {
		return this.results.stream().sorted(comparator).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @return a result list sorted descending by the size of linked sources.
	 */
	public List<ViafVocabulary> getResultsOrderDescByLinkedSources() {
		return getSortedResults((v1,v2) -> v2.getLinkedSources().size() - v1.getLinkedSources().size());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ViafQueryResult [totalhits=" + totalhits + ", startRow="
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
		ViafQueryResult other = (ViafQueryResult) obj;
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
