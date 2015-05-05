package at.ac.uibk.igwee.webapp.metadata.mdmapper.model;

import java.util.List;
import java.util.stream.Collectors;

import at.ac.uibk.igwee.metadata.metaquery.VocabularyQueryResult;

public class VocabularyQueryResultData {
	
	private QueryData query;
	private List<VocabularyData> results;
	private VocabularyData fixedResult;
	private int totalhits;
	
	public VocabularyQueryResultData(VocabularyQueryResult vocQR) {
		this.query = new QueryData(vocQR.getVocabularyQuery());
		this.fixedResult = vocQR.getFixedResult() != null ? 
				new VocabularyData(vocQR.getFixedResult()) :
					null;
		this.totalhits = vocQR.getTotalhits();
		this.results = vocQR.getResults().stream()
				.map(voc -> new VocabularyData(voc))
				.collect(Collectors.toList());
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
	 * @return the results
	 */
	public List<VocabularyData> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<VocabularyData> results) {
		this.results = results;
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
