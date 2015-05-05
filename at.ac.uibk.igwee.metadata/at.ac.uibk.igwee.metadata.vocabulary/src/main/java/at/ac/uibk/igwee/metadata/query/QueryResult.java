package at.ac.uibk.igwee.metadata.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;

/**
 * DataObject for a QueryResult
 * @author totoro
 *
 */
public class QueryResult {
	
	/**
	 * The query
	 */
	private Query query;
	
	/**
	 * Totalhits
	 */
	private int totalhits;
	/**
	 * The results
	 */
	private List<Vocabulary> results = new ArrayList<>();

	public QueryResult() {
		super();
	}

	public QueryResult(Query query, int totalhits,
			Collection<? extends Vocabulary> results) {
		super();
		this.query = query;
		this.totalhits = totalhits;
		if (results!=null) this.results.addAll(results);
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
	 * @return the query
	 */
	public Query getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(Query query) {
		this.query = query;
	}

	/**
	 * @return the results
	 */
	public List<Vocabulary> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<Vocabulary> results) {
		this.results = results;
	}
	
	
	/**
	 * Adds a bunch of results
	 * @param additionalResults
	 */
	public void addResults(Collection<? extends Vocabulary> additionalResults) {
		additionalResults.stream().forEach(voc -> {
			if (!this.results.contains(voc)) this.results.add(voc);
		});
	}
	
	/**
	 * removes one result
	 * @param voc
	 */
	public void removeResult(Vocabulary voc) {
		this.results.remove(voc);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QueryResult [query=" + query + ", totalhits=" + totalhits
				+ ", results=" + results + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((query == null) ? 0 : query.hashCode());
		result = prime * result + ((results == null) ? 0 : results.hashCode());
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
		QueryResult other = (QueryResult) obj;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		if (results == null) {
			if (other.results != null)
				return false;
		} else if (!results.equals(other.results))
			return false;
		if (totalhits != other.totalhits)
			return false;
		return true;
	}
	
	public static QueryResult emptyResult(Query q) {
		return new QueryResult(q, 0, null);
	}

}
