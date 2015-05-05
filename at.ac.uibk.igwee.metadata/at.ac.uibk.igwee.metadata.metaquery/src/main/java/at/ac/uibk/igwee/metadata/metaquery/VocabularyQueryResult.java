package at.ac.uibk.igwee.metadata.metaquery;

import java.util.List;

import at.ac.uibk.igwee.metadata.query.QueryResult;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;

/**
 * DataObject for the result of a single VocabularyQuery
 * @author Joseph
 *
 */
public class VocabularyQueryResult extends QueryResult {
	
	/**
	 * The vocabularyQuery used for creating this result
	 */
	private VocabularyQuery vocabularyQuery;
	
	/**
	 * The vocabulary which is the hit of the query. Mostly hand-picked.
	 */
	private Vocabulary fixedResult;
	
	public VocabularyQueryResult() {
		super();
	}

	public VocabularyQueryResult(VocabularyQuery vocabularyQuery,
			List<Vocabulary> results) {
		super(vocabularyQuery, -1, results);
		this.vocabularyQuery = vocabularyQuery;
	}

	/**
	 * @return the vocabularyQuery
	 */
	public VocabularyQuery getVocabularyQuery() {
		return vocabularyQuery;
	}

	/**
	 * @param vocabularyQuery the vocabularyQuery to set
	 */
	public void setVocabularyQuery(VocabularyQuery vocabularyQuery) {
		this.vocabularyQuery = vocabularyQuery;
	}

	/**
	 * @return the fixedResult
	 */
	public Vocabulary getFixedResult() {
		return fixedResult;
	}

	/**
	 * @param fixedResult the fixedResult to set
	 */
	public void setFixedResult(Vocabulary fixedResult) {
		this.fixedResult = fixedResult;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((vocabularyQuery == null) ? 0 : vocabularyQuery.hashCode());
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
		VocabularyQueryResult other = (VocabularyQueryResult) obj;
		if (vocabularyQuery == null) {
			if (other.vocabularyQuery != null)
				return false;
		} else if (!vocabularyQuery.equals(other.vocabularyQuery))
			return false;
		return true;
	}

	
	
}
