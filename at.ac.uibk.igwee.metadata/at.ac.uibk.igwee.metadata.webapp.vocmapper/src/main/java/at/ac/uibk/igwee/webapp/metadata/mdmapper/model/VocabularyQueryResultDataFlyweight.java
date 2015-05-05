package at.ac.uibk.igwee.webapp.metadata.mdmapper.model;

import at.ac.uibk.igwee.metadata.metaquery.VocabularyQueryResult;

public class VocabularyQueryResultDataFlyweight {

	private QueryData query;
	private int resultCounts;
	private VocabularyData fixedResult;
	private int totalhits;
	
	public VocabularyQueryResultDataFlyweight(VocabularyQueryResult r) {
		this.query = new QueryData(r.getVocabularyQuery());
		this.resultCounts = r.getResults() == null ? -1 : r.getResults().size();
		this.fixedResult = r.getFixedResult() == null ? 
				null : new VocabularyData(r.getFixedResult());
		this.totalhits = r.getTotalhits();
	}

	/**
	 * @return the query
	 */
	public QueryData getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(QueryData query) {
		this.query = query;
	}

	/**
	 * @return the resultCounts
	 */
	public int getResultCounts() {
		return resultCounts;
	}

	/**
	 * @param resultCounts the resultCounts to set
	 */
	public void setResultCounts(int resultCounts) {
		this.resultCounts = resultCounts;
	}

	/**
	 * @return the fixedResult
	 */
	public VocabularyData getFixedResult() {
		return fixedResult;
	}

	/**
	 * @param fixedResult the fixedResult to set
	 */
	public void setFixedResult(VocabularyData fixedResult) {
		this.fixedResult = fixedResult;
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
	
	
	
}
