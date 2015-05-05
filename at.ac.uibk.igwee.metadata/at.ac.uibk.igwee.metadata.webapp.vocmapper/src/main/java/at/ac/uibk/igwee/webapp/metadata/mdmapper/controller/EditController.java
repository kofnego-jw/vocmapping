package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import at.ac.uibk.igwee.metadata.geonames.GeonamesQueryService;
import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;
import at.ac.uibk.igwee.metadata.metaquery.VocabularyQueryResult;
import at.ac.uibk.igwee.metadata.viaf.ViafQueryService;
import at.ac.uibk.igwee.metadata.vocabulary.Vocabulary;
import at.ac.uibk.igwee.metadata.wikidata.WikidataQueryService;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.model.QueryQueueData;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.model.VocabularyQueryResultData;

@Scope("session")
@RestController
@RequestMapping("/app/edit")
public class EditController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EditController.class);

	@Autowired
	private GeonamesQueryService geonamesQueryService;
	
	@Autowired
	private WikidataQueryService wikidataQueryService;
	
	@Autowired
	private ViafQueryService viafQueryService;
	
	
	@Autowired
	private SessionHolder sessionHolder;
	
	public void setSessionHolder(SessionHolder sh) {
		this.sessionHolder = sh;
	}
	
	protected QueryQueue getQQ() {
		return this.sessionHolder.getQueryQueue();
	}
	
	protected VocabularyQueryResult getVQ(int pos) {
		return getQQ().getResults().get(pos);
	}
	
	@RequestMapping("/")
	public QueryQueueData quickInfo() {
		LOGGER.debug("quickInfo() called.");
		return new QueryQueueData(getQQ());
	}
	
	@RequestMapping("/{pos}")
	public VocabularyQueryResultData view(@PathVariable("pos") int pos) {
		LOGGER.debug("view() at {} called.", pos);
		return new VocabularyQueryResultData(getVQ(pos));
	}
	
	@RequestMapping(value="/{pos}/fixTo", method=RequestMethod.POST)
	public VocabularyQueryResultData fixTo(@PathVariable("pos") int pos, @RequestParam("url") String url) throws Exception {
		
		LOGGER.debug("fixTo() called with position {} and url {}.", Integer.valueOf(pos), url);
		
		VocabularyQueryResult result = getVQ(pos);
		Vocabulary v = result.getResults().stream().filter(voc -> voc.getURI().toString().equals(url)).findAny().orElse(null);
		if (v==null) {
			LOGGER.error("Cannot find vocabulary with url {}.", url);
			throw new Exception("Cannot find a vocabulary that matches the url '" + url + "'.");
		}
		result.setFixedResult(v);
		LOGGER.debug("FixTo() success.");
		return new VocabularyQueryResultData(result);
	}
	
	@RequestMapping(value="/{pos}/fixToAndClear", method=RequestMethod.POST)
	public VocabularyQueryResultData fixToAndClear(@PathVariable("pos") int pos, @RequestParam("url") String url) throws Exception {
		
		LOGGER.debug("fixToAndClear() called with position {} and url {}.", Integer.valueOf(pos), url);
		
		
		VocabularyQueryResult result = getVQ(pos);
		Vocabulary v = result.getResults().stream().filter(voc -> voc.getURI().toString().equals(url)).findAny().orElse(null);
		if (v==null) {
			LOGGER.error("Cannot find vocabulary with url {}.", url);
			throw new Exception("Cannot find a vocabulary that matches the url '" + url + "'.");
		}
		result.setFixedResult(v);
		result.getResults().clear();
		
		LOGGER.debug("fixToAndClear() success.");
		
		return new VocabularyQueryResultData(result);
	}
	
	@RequestMapping(value="/{pos}/fixToCustom", method=RequestMethod.POST)
	public VocabularyQueryResultData fixToCustom(
			@PathVariable("pos") int pos,
			@RequestParam("authority") String authority,
			@RequestParam("id") String id
			) throws Exception {
		
		LOGGER.debug("fixToCustom() called with {}, {}, {}.", Integer.valueOf(pos), authority, id);
		
		VocabularyQueryResult r = getVQ(pos);
		
		Vocabulary voc = guessVocabulary(r, authority, id);
		
		r.setFixedResult(voc);
		
		LOGGER.debug("Vocabulary fixed to {}.", voc );
		
		return new VocabularyQueryResultData(r);
	}
	
	protected Vocabulary guessVocabulary(VocabularyQueryResult r, String a, String id) throws Exception {
		String test = a.toLowerCase();
		if (test.contains("viaf")) {
			return createViafVocabulary(id);
		}
		if (test.contains("wikidata"))
			return createWikidataVocabulary(id);
		if (test.contains("geonames"))
			return createGeonamesData(id);
		
		LOGGER.error("Cannot recognize authority {}.", a);
		
		throw new Exception("Cannot recognize authority '" + a + "'.");
	}
	
	protected Vocabulary createViafVocabulary(String id) throws Exception {
		return viafQueryService.queryId(id);
	}
	
	protected Vocabulary createWikidataVocabulary(String id) throws Exception {
		return wikidataQueryService.queryId(id);
	}
	
	protected Vocabulary createGeonamesData(String id) throws Exception {
		return geonamesQueryService.queryId(id);
	}
	
	@RequestMapping(value="/{pos}/remove", method=RequestMethod.POST)
	public VocabularyQueryResultData removeOneHit(@PathVariable("pos") int pos, 
			@RequestParam("url") String url) {
		VocabularyQueryResult r = getVQ(pos);
		
		List<Vocabulary> toRemove = 
				r.getResults().stream().filter(voc -> voc.getURI().toString().equals(url))
				.collect(Collectors.toList());
		
		toRemove.forEach(voc -> r.removeResult(voc));
		
		return new VocabularyQueryResultData(r);
	}
	
	@RequestMapping(value="/removeResult/{pos}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void removeAResult(@PathVariable("pos") int position ) throws Exception {
		LOGGER.info("Remove result at position {}.", position);
		QueryQueue qq = getQQ();
		qq.removeResult(position);
	}
	
	
	
}
