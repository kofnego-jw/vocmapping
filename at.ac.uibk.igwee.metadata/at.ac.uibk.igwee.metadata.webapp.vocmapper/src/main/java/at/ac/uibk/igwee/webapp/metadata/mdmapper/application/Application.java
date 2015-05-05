package at.ac.uibk.igwee.webapp.metadata.mdmapper.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import at.ac.uibk.igwee.metadata.geonames.GeonamesQueryService;
import at.ac.uibk.igwee.metadata.geonames.impl.GeonamesHttpQueryServiceImpl;
import at.ac.uibk.igwee.metadata.gnd.GndQueryService;
import at.ac.uibk.igwee.metadata.gnd.impl.GndQueryServiceImpl;
import at.ac.uibk.igwee.metadata.gnd.impl.GndResultConverter;
import at.ac.uibk.igwee.metadata.httpclient.HttpClientService;
import at.ac.uibk.igwee.metadata.httpclient.impl.HttpClientServiceImpl;
import at.ac.uibk.igwee.metadata.query.QueryService;
import at.ac.uibk.igwee.metadata.viaf.ViafQueryService;
import at.ac.uibk.igwee.metadata.viaf.impl.ViafQueryServiceImpl;
import at.ac.uibk.igwee.metadata.viaf.impl.ViafRespToResult;
import at.ac.uibk.igwee.metadata.wikidata.WikidataQueryService;
import at.ac.uibk.igwee.metadata.wikidata.impl.HttpQueryHelper;
import at.ac.uibk.igwee.metadata.wikidata.impl.WikidataQueryServiceImpl;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.MetaQueryServiceFactory;
import at.ac.uibk.igwee.xslt.XPathService;
import at.ac.uibk.igwee.xslt.XsltService;
import at.ac.uibk.igwee.xslt.impl.SaxonXPathServiceImpl;
import at.ac.uibk.igwee.xslt.impl.SaxonXsltServiceImpl;

@SpringBootApplication
@ComponentScan(basePackages={"at.ac.uibk.igwee.webapp.metadata.mdmapper.controller"})
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
		
		System.out.println("Beans:");
		Stream.of(ctx.getBeanDefinitionNames())
			.sorted()
			.forEach(s -> System.out.println("  " + s));
		
	}
	
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder appBuilder) {
		return appBuilder.sources(Application.class);
	}
	
	
	@Bean
	public XsltService xsltService() {
		return new SaxonXsltServiceImpl();
	}
	
	public XPathService xpathService() {
		return new SaxonXPathServiceImpl();
	}
	
	@Bean
	public HttpClientService httpService() {
		return new HttpClientServiceImpl();
	}
	
	@Bean
	public GeonamesQueryService geonamesQueryService() {
		GeonamesHttpQueryServiceImpl geonames = new GeonamesHttpQueryServiceImpl();
		geonames.setHttpClientService(httpService());
		geonames.setXsltService(xsltService());
		return geonames;
	}
	
	@Bean
	public GndQueryService gndQueryService() {
		GndQueryServiceImpl gnd = new GndQueryServiceImpl();
		GndResultConverter converter = new GndResultConverter();
		converter.setXsltService(xsltService());
		gnd.setGndResultConverter(converter);
		gnd.setHttpClientService(httpService());
		return gnd;
	}
	
	@Bean
	public WikidataQueryService wikidataQueryService() {

		
		WikidataQueryServiceImpl wikidata = new WikidataQueryServiceImpl();
		
		HttpQueryHelper httpQueryHelper = new HttpQueryHelper();
		httpQueryHelper.setHttpClientService(httpService());
		httpQueryHelper.setXsltService(xsltService());
		httpQueryHelper.setXPathService(xpathService());

		wikidata.setHttpQueryHelper(httpQueryHelper);
		
		return wikidata;
	}
	
	@Bean
	public ViafQueryService viafQueryService() {
		ViafQueryServiceImpl viaf = new ViafQueryServiceImpl();
		ViafRespToResult viafR2R = new ViafRespToResult();
		viafR2R.setXsltService(xsltService());
		
		viaf.setHttpClientService(httpService());
		viaf.setViafRespToResult(viafR2R);
		return viaf;
	}
	
	@Bean
	public MetaQueryServiceFactory metaQueryServiceFactory() {
		
		List<QueryService> qss = new ArrayList<>(3);
		qss.add(geonamesQueryService());
		qss.add(gndQueryService());
		qss.add(viafQueryService());
		qss.add(wikidataQueryService());
		
		MetaQueryServiceFactory factory = new MetaQueryServiceFactory();
		factory.setQueryServices(qss);
		
		return factory;
		
	}
	
	
}
