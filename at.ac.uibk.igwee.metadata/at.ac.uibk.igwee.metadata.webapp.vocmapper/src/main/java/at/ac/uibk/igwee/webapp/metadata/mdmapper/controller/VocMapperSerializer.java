package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.uibk.igwee.metadata.metaquery.QueryQueueFactory;
import at.ac.uibk.igwee.metadata.metaquery.impl.QueryQueueFactoryImpl;
import at.ac.uibk.igwee.xslt.XsltService;

@Component
public class VocMapperSerializer {
	
	private QueryQueueFactoryImpl qqFactoryImpl;
	
	@Autowired
	private XsltService xsltService;
	
	public VocMapperSerializer() {
		super();
	}
	
	public VocMapperSerializer(XsltService xs) {
		super();
		this.xsltService = xs;
	}
	
	public QueryQueueFactory getQueryQueueFactory() {
		
		if (this.qqFactoryImpl!=null) return this.qqFactoryImpl;
		
		this.qqFactoryImpl = new QueryQueueFactoryImpl();
		this.qqFactoryImpl.setXsltService(xsltService);
		
		return this.qqFactoryImpl;
	}

}
