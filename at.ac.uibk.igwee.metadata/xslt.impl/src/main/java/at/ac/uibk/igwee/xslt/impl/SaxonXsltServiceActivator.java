package at.ac.uibk.igwee.xslt.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import at.ac.uibk.igwee.xslt.XPathService;
import at.ac.uibk.igwee.xslt.XQueryService;
import at.ac.uibk.igwee.xslt.XsltService;

/**
 * BundleActivator for XsltService Implementation Package using Saxon. This activator ensures
 * that only one instance of SaxonXsltServiceImpl and SaxonXPathServiceImpl is activated at any
 * time.
 * @author Apple
 *
 */
public class SaxonXsltServiceActivator implements BundleActivator {
	
	/**
	 * XsltService Instance
	 */
	private volatile SaxonXsltServiceImpl xsltInstance;
	/**
	 * XPathService instance
	 */
	private volatile SaxonXPathServiceImpl xpathInstance;
	/**
	 * XQueryService instance
	 */
	private volatile SaxonXQueryServiceImpl xqueryInstance;
	
	@Override
	public void start(BundleContext context) throws Exception {
		if (xsltInstance==null)
			xsltInstance = new SaxonXsltServiceImpl();
		context.registerService(XsltService.class.getName(), xsltInstance, null);
		
		if (xpathInstance==null)
			xpathInstance = new SaxonXPathServiceImpl();
		context.registerService(XPathService.class.getName(), xpathInstance, null);
		
		if (xqueryInstance==null)
			xqueryInstance = new SaxonXQueryServiceImpl();
		context.registerService(XQueryService.class.getName(), xqueryInstance, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		xsltInstance.close();
		xsltInstance=null;
		xpathInstance = null;
		xqueryInstance = null;
	}

}
