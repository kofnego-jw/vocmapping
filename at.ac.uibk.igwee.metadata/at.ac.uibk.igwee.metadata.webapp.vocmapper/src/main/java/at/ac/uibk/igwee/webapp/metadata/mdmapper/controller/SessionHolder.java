package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;

@Component
@Scope("session")
public class SessionHolder {

	private QueryQueue queryQueue;

	/**
	 * @return the queryQueue
	 */
	public QueryQueue getQueryQueue() {
		if (queryQueue == null)
			clear();
		return queryQueue;
	}

	/**
	 * @param queryQueue the queryQueue to set
	 */
	public void setQueryQueue(QueryQueue queryQueue) {
		this.queryQueue = queryQueue;
		
//		output(this.queryQueue);
		
	}
	

	public static void output(QueryQueue q) {
		System.out.println("QueryQueue: " + q.getName());
		System.out.println("  Info: " + q.getAdditionalInfo());
		System.out.println("  Pending: " + q.getPendingQueries().size());
		q.getPendingQueries().forEach(vq -> {
			System.out.println("    Query for: " + vq);
			System.out.println("      Type: " + vq.getType());
			System.out.println("      Restricted to: " + vq.getIncludedAuthority());
			});
		System.out.println();
		System.out.println("  Results: ");
		q.getResults().stream().forEach(vq -> {
			System.out.println("    Query for: " + vq.getVocabularyQuery().getName() );
			System.out.println("    Fixed: " + vq.getFixedResult());
			System.out.println("    Results: " + vq.getResults().size());
			vq.getResults().forEach(v -> {
				System.out.println("      " + v.getInternalID() + ": " + v.getURI());
			});
		});
	}

	public boolean isEmpty() {
		return this.queryQueue == null;
	}

	public void clear() {
		this.queryQueue = new QueryQueue("StandardQueue", "Created on " + new Date().toString(), 
				new ArrayList<>());
	}
	
	
	
}
