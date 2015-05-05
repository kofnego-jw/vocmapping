package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.ac.uibk.igwee.metadata.metaquery.MetaQueryService;
import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;

@Component
@Scope("session")
public class PreQueryWorker implements AutoCloseable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PreQueryWorker.class);
	
	@Autowired
	private MetaQueryServiceFactory metaQueryServiceFactory;
	
	@Autowired
	private AuthoritiesHolder authoritiesHolder;
	
	private ExecutorService es;
	
	private volatile Future<QueryQueue> result;
	
	public PreQueryWorker() {
		super();
	}
	
	public PreQueryWorker(MetaQueryServiceFactory mqs) {
		super();
		this.metaQueryServiceFactory = mqs;
	}
	
	public void setAuthoritiesHolder(AuthoritiesHolder holder) {
		this.authoritiesHolder = holder;
	}
	
	
	public void prequery(QueryQueue qq, boolean requery) throws Exception {
		
		if (this.result!=null && !isFinished()) {
			throw new Exception("A progress is running.");
		}
		if (this.es!=null) {
			try {
				es.shutdownNow();
			} catch (Exception ignored) {}
		}
		
		PreQueryRunnable r = new PreQueryRunnable(qq, 
				metaQueryServiceFactory.getMetaQueryService(authoritiesHolder.getUsedAuthorities()), requery);
		es = Executors.newFixedThreadPool(1);
		this.result = es.submit(r);
	}
	
	public boolean isFinished() {
		if (this.result!=null)
			return this.result.isDone();
		return true;
	}
	
	public QueryQueue getResult(int millis) throws Exception{
		if (this.result==null) {
			throw new Exception("No query queue process.");
		}
		if (!isFinished()) {
			return this.result.get(millis, TimeUnit.MILLISECONDS);
		}
		return this.result.get();
	}
	
	@Override
	public void close() throws Exception {
		if (this.result!=null) {
			if (!this.result.isDone() && !this.result.isCancelled()) {
				try {
					this.result.get(500, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					LOGGER.warn("Exception while trying to stop the prequery process.", e);
				}
			}
		}
		this.result = null;
		if (es!=null) {
			try {
				es.shutdownNow();
			} catch (Exception e) {
				LOGGER.warn("Exception while trying to shutdown the ExecutorService.", e);
			}
		}
	}
	
		
	
	protected class PreQueryRunnable implements Callable<QueryQueue> {
		
		private QueryQueue qq;
		
		private MetaQueryService mqs;
		
		private boolean queryFixed;

		private volatile boolean terminated = true;
		
		protected PreQueryRunnable(QueryQueue qq, MetaQueryService mqs, boolean queryFixed) {
			super();
			this.qq = qq;
			this.mqs = mqs;
			this.queryFixed = queryFixed;
		}
		
		public boolean isTerminated() {
			return this.terminated;
		}
		
		@Override
		public QueryQueue call() throws Exception {
			terminated = false;
			try {
				qq = mqs.processQueries(qq, queryFixed);
			} catch (Exception e) {
				terminated = true;
				LOGGER.warn("Exception while prequery.", e);
			}
			return qq;
		}
				
		
	}

}
