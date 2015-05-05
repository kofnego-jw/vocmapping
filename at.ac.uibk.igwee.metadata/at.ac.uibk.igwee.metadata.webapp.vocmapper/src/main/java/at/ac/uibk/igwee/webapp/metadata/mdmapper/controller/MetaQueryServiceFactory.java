package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import at.ac.uibk.igwee.metadata.metaquery.MetaQueryService;
import at.ac.uibk.igwee.metadata.metaquery.impl.MetaQueryServiceImpl;
import at.ac.uibk.igwee.metadata.query.QueryService;
import at.ac.uibk.igwee.metadata.vocabulary.Authority;

public class MetaQueryServiceFactory {
	
	private List<QueryService> qqs;
	
	public MetaQueryServiceFactory() {
		
	}
	
	public void setQueryServices(List<QueryService> qqs) {
		this.qqs = qqs;
	}
	
	public MetaQueryService getMetaQueryService() {
		MetaQueryServiceImpl impl = new MetaQueryServiceImpl();
		
		qqs.stream().forEach(qs -> impl.addQueryService(qs));
		
		return impl;
	}
	
	public MetaQueryService getMetaQueryService(Collection<Authority> includedAuthorities) {
		
		Predicate<QueryService> filterPredicate;
		
		if (includedAuthorities==null || includedAuthorities.isEmpty()) {
			filterPredicate = qs -> true;
		} else {
			filterPredicate = qs -> {
				return qs.getQueriedAuthority().stream()
						.filter(auth -> includedAuthorities.contains(auth))
						.findAny().orElse(null) != null;
				
			};
		}
			
		MetaQueryServiceImpl impl = new MetaQueryServiceImpl();
		qqs.stream().filter(filterPredicate).forEach(qs -> impl.addQueryService(qs));
		return impl;
		
	}
	
	
}
