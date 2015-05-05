package at.ac.uibk.igwee.metadata.metaquery;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;

/**
 * DataObject for a list of queries.
 * @author Joseph
 *
 */
public class QueryQueue implements Cloneable, Iterator<VocabularyQueryResult> {
	
	/**
	 * The name of the queryQueue
	 */
	private String name;
	/**
	 * Additional info
	 */
	private String additionalInfo;
	/**
	 * The queries that were not yet executed. This list can be empty.
	 */
	private LinkedList<VocabularyQuery> pendingQueries = new LinkedList<>();
	/**
	 * The results of queries that were executed.
	 */
	private LinkedList<VocabularyQueryResult> results = new LinkedList<>();
	/**
	 * The position of results which was edited last.
	 */
	private int lastEditingPosition = 0;
	
	public QueryQueue() {
		super();
	}

	public QueryQueue(String name, String additionalInfo,
			Collection<VocabularyQuery> pendingQueries) {
		super();
		this.name = name;
		this.additionalInfo = additionalInfo;
		this.pendingQueries.addAll(pendingQueries);
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
	public List<VocabularyQuery> getPendingQueries() {
		return pendingQueries;
	}

	/**
	 * @param pendingQueries the pendingQueries to set
	 */
	public void setPendingQueries(Collection<VocabularyQuery> pendingQueries) {
		this.pendingQueries.clear();
		this.pendingQueries.addAll(pendingQueries);
	}
	
	/**
	 * Adds a bunch of query.
	 * @param additionalQueries
	 */
	public synchronized void addPendingQueries(Collection<VocabularyQuery> additionalQueries) {
		
		List<VocabularyQuery> qList = additionalQueries.stream()
				.filter(vq -> !this.pendingQueries.contains(vq))
				.collect(Collectors.toList());
		
		this.pendingQueries.addAll(qList);
	}
	
	/**
	 * Adds a query
	 * @param vq
	 */
	public void addPendingQuery(VocabularyQuery vq) {
		this.addPendingQueries(Stream.of(vq).collect(Collectors.toList()));
	}

	/**
	 * @return the results
	 */
	public List<VocabularyQueryResult> getResults() {
		return results.stream().collect(Collectors.toList());
	}
	
	/**
	 * Clears all results
	 */
	public void clearResults() {
		this.results.clear();
	}
	
	/**
	 * Remove one result at postion x
	 * @param position starting with 0
	 */
	public void removeResult(int position) {
		if (this.results.isEmpty() || this.results.size()<=position)
			return;
		this.results.remove(position);
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(Collection<VocabularyQueryResult> results) {
		this.results.clear();
		this.results.addAll(results);
		this.lastEditingPosition = 0;
	}

	/**
	 * @return the lastEditingPosition
	 */
	public int getLastEditingPosition() {
		return lastEditingPosition;
	}

	/**
	 * @param lastEditingPosition the lastEditingPosition to set
	 */
	public void setLastEditingPosition(int lastEditingPosition) {
		this.lastEditingPosition = lastEditingPosition;
	}
	
	/**
	 * Adds a result. If the query of the result is in the pending list, it will be removed from the pending list
	 * @param res
	 */
	public void addResult(VocabularyQueryResult res) {
		this.results.add(res);
		VocabularyQuery q = res.getVocabularyQuery();
		this.pendingQueries.remove(q);
	}

	/**
	 * 
	 * @return the first element of the queue
	 * @throws QueryServiceException if the queue is empty
	 */
	public VocabularyQuery getNextQuery() throws QueryServiceException {
		if (this.pendingQueries.isEmpty())
			throw new QueryServiceException("PendingQueue is empty, no more queries.");
		return this.pendingQueries.getFirst();
	}
		
	@Override
	public QueryQueue clone() {
		
		QueryQueue clone = new QueryQueue(this.name, this.additionalInfo, this.pendingQueries.stream().collect(Collectors.toList()));
		
		clone.lastEditingPosition = this.lastEditingPosition;
		
		this.results.stream().forEach(res -> clone.addResult(res));
		
		return clone;
	}
	
	@Override
	public boolean hasNext() {
		return this.lastEditingPosition < getResults().size();
	}
	
	@Override
	public VocabularyQueryResult next() {
		return this.results.get(lastEditingPosition++);
	}
	
	/**
	 * 
	 * @return the current VocabularyQueryResult, without advancing the cursor
	 */
	public VocabularyQueryResult current() {
		return this.results.get(lastEditingPosition);
	}
	
	/**
	 * Sets the fixed vocabulary at the current editing position
	 * @param voc
	 */
	public void setVocabulary(Vocabulary voc) {
		current().setFixedResult(voc);
	}
	
	/**
	 * Removes a vocabulary from results
	 * @param voc
	 */
	public void removeVocabulary(Vocabulary voc) {
		current().getResults().remove(voc);
	}
	
	/**
	 * Adds a vocabulary to the current result
	 * @param voc
	 */
	public void addVocabulary(Vocabulary voc) {
		addVocabularies(Stream.of(voc).collect(Collectors.toList()));
	}
	
	/**
	 * Adds a bunch of vocabularies
	 * @param vocs
	 */
	public void addVocabularies(Collection<Vocabulary> vocs) {
		current().addResults(vocs);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
		result = prime * result + lastEditingPosition;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((pendingQueries == null) ? 0 : pendingQueries.hashCode());
		result = prime * result + ((results == null) ? 0 : results.hashCode());
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
		QueryQueue other = (QueryQueue) obj;
		if (additionalInfo == null) {
			if (other.additionalInfo != null)
				return false;
		} else if (!additionalInfo.equals(other.additionalInfo))
			return false;
		if (lastEditingPosition != other.lastEditingPosition)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pendingQueries == null) {
			if (other.pendingQueries != null)
				return false;
		} else if (!pendingQueries.equals(other.pendingQueries))
			return false;
		if (results == null) {
			if (other.results != null)
				return false;
		} else if (!results.equals(other.results))
			return false;
		return true;
	}

	
	
	
	

}
