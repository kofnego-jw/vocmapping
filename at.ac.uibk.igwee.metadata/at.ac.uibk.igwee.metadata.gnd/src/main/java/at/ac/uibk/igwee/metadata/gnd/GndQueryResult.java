package at.ac.uibk.igwee.metadata.gnd;

import java.util.List;

public class GndQueryResult {
	
	private int maxRows;
	
	private int startRow;
	
	private int nextRow;
	
	private int totalhits;
	
	private List<GndVocabulary> results;
	
	public GndQueryResult() {
		super();
	}
	
	public GndQueryResult(int totalhits, int startrow, int maxrows, int nextrow, List<GndVocabulary> results) {
		super();
		this.totalhits = totalhits;
		this.startRow = startrow;
		this.maxRows = maxrows;
		this.nextRow = nextrow;
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
	 * @return the results
	 */
	public List<GndVocabulary> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<GndVocabulary> results) {
		this.results = results;
	}
	
	

}
