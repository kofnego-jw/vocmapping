package at.ac.uibk.igwee.webapp.metadata.mdmapper.model;

import java.util.List;
import java.util.stream.Collectors;

import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;

public class QueryQueueData {
	
	private String name;
	
	private String additionalInfo;
	
	private List<QueryData> pendingQueries;
	
	private List<VocabularyQueryResultDataFlyweight> results;
	
	public QueryQueueData(QueryQueue qq) {
		if (qq==null)
			qq = new QueryQueue();
		this.name = qq.getName();
		this.additionalInfo = qq.getAdditionalInfo();
		this.pendingQueries = qq.getPendingQueries()
				.stream()
				.map(vq -> new QueryData(vq))
				.collect(Collectors.toList());
		this.results = qq.getResults().stream()
				.map(vocQR -> new VocabularyQueryResultDataFlyweight(vocQR))
				.collect(Collectors.toList());
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
	 * @return the pendingQueries
	 */
	public List<QueryData> getPendingQueries() {
		return pendingQueries;
	}

	/**
	 * @param pendingQueries the pendingQueries to set
	 */
	public void setPendingQueries(List<QueryData> pendingQueries) {
		this.pendingQueries = pendingQueries;
	}

	/**
	 * @return the results
	 */
	public List<VocabularyQueryResultDataFlyweight> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<VocabularyQueryResultDataFlyweight> results) {
		this.results = results;
	}
	
	

}
