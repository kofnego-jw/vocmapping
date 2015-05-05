package at.ac.uibk.igwee.metadata.metaquery.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import at.ac.uibk.igwee.metadata.metaquery.MetaQueryService;
import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;
import at.ac.uibk.igwee.metadata.metaquery.QueryServiceException;
import at.ac.uibk.igwee.metadata.metaquery.VocabularyQuery;
import at.ac.uibk.igwee.metadata.metaquery.VocabularyQueryResult;
import at.ac.uibk.igwee.metadata.query.QueryResult;
import at.ac.uibk.igwee.metadata.query.QueryService;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyException;

/**
 * Default implementation of MetaQueryService
 * 
 * @author Joseph
 *
 */
@Component
public class MetaQueryServiceImpl implements MetaQueryService {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MetaQueryServiceImpl.class);

	private volatile List<QueryService> queryServices = new ArrayList<>();

	@Reference(type='+')
	public void addQueryService(QueryService qs) {
		
		String serviceName = qs.getName();
		LOGGER.debug("Register a queryService: {}", serviceName);

		List<String> names = queryServices.stream().map(q -> q.getName())
				.collect(Collectors.toList());
		if (names.contains(serviceName)) {
			LOGGER.info("QueryService {} already registered.", serviceName);
			return;
		}

		synchronized (queryServices) {
			List<QueryService> qss = queryServices.stream().collect(
					Collectors.toList());
			qss.add(qs);
			this.queryServices = qss;
		}
		LOGGER.debug("QueryService {} registered.", serviceName);
	}

	public void removeQueryService(QueryService qs) {
		String serviceName = qs.getName();
		LOGGER.debug("Unregister a queryService: {}", serviceName);

		List<String> names = queryServices.stream().map(q -> q.getName())
				.collect(Collectors.toList());
		if (!names.contains(serviceName)) {
			LOGGER.info("QueryService {} was not registered.", serviceName);
			return;
		}

		synchronized (queryServices) {
			List<QueryService> qss = queryServices.stream()
					.filter(q -> !q.getName().equals(serviceName))
					.collect(Collectors.toList());
			this.queryServices = qss;
		}
	}

	@Override
	public void registerQueryService(QueryService qs) {
		addQueryService(qs);
	}

	@Override
	public void deregisterQueryService(QueryService qs) {
		removeQueryService(qs);
	}

	@Override
	public List<String> getQueryServiceNames() {
		if (this.queryServices==null || this.queryServices.isEmpty()) return Collections.emptyList();
		return this.queryServices.stream()
					.map(qs -> qs.getName())
					.collect(Collectors.toList());
	}
	
	@Override
	public VocabularyQueryResult processQuery(final VocabularyQuery query)
			throws QueryServiceException {
		
		LOGGER.info("MetaQuerying for {} using {} services.", query, Integer.toString(this.queryServices.size()));;
		
		List<QueryService> usedQss = this.queryServices.stream()
				.collect(Collectors.toList());
		
		if (usedQss.isEmpty())
			throw new QueryServiceException("No query service is available.");
		
		VocabularyQueryResult result = new VocabularyQueryResult(query, Collections.emptyList());
		
		List<QueryResult> qResults = usedQss.stream().map(qs -> {
			LOGGER.debug("MetaQueryService calling {}.", qs.getName());
			try {
				return qs.query(query);
			} catch (VocabularyException e) {
				LOGGER.warn("Exception while using a queryService: " + e.getMessage(), e);
				return QueryResult.emptyResult(query);
			}
		}).filter(qr -> qr!=null).collect(Collectors.toList());
		
		qResults.stream().forEach(qr -> result.addResults(qr.getResults()));
		
		return result;
	}
	
	@Override
	public QueryQueue processQueries(QueryQueue requestQueue, boolean requeryFixed)
			throws QueryServiceException {
		
		QueryQueue queue = requestQueue.clone();
		
		
		if (requeryFixed && queue.getResults()!=null && !queue.getResults().isEmpty()) {
			List<VocabularyQuery> formerQueries = queue.getResults()
					.stream()
					.map(vocQueryResult -> vocQueryResult.getVocabularyQuery())
					.collect(Collectors.toList());
			queue.addPendingQueries(formerQueries);
			queue.clearResults();
		}
		
//		System.out.println("Pending Queriews: " + queue.getPendingQueries().size() + 
//				" / Results: " + queue.getResults().size());
		
		if (queue==null || queue.getPendingQueries()==null || queue.getPendingQueries().isEmpty()) {
			LOGGER.info("Empty queue, no pending request available.");
			throw new QueryServiceException("Empty queue, no pending request available.");
		}
		
		while(!queue.getPendingQueries().isEmpty()) {
			VocabularyQuery q = queue.getNextQuery();
			VocabularyQueryResult result = processQuery(q);
			queue.addResult(result);
		}

//		System.out.println("Pending Queriews: " + queue.getPendingQueries().size() + 
//				" / Results: " + queue.getResults().size());

		
		return queue;
	}
	
	

}
