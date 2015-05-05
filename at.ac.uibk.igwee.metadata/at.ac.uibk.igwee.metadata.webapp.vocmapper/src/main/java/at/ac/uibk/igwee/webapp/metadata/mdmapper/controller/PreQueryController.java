package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;
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

import at.ac.uibk.igwee.metadata.geonames.OrgGeonames;
import at.ac.uibk.igwee.metadata.gnd.DeDnb;
import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;
import at.ac.uibk.igwee.metadata.metaquery.VocabularyQuery;
import at.ac.uibk.igwee.metadata.viaf.OrgViaf;
import at.ac.uibk.igwee.metadata.vocabulary.Authority;
import at.ac.uibk.igwee.metadata.vocabulary.VocabularyType;
import at.ac.uibk.igwee.metadata.wikidata.OrgWikidata;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.model.QueryQueueData;

@RestController
@RequestMapping("/app/prequery")
@Scope("session")
public class PreQueryController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PreQueryController.class);
	
	@Autowired
	private SessionHolder sessionHolder;
	
	@Autowired 
	private PreQueryWorker worker;
	
	@Autowired
	private AuthoritiesHolder authoritiesHolder;
	
	
	public void setSessionHolder(SessionHolder sh) {
		this.sessionHolder = sh;
	}
	
	public void setPreQueryWorker(PreQueryWorker w) {
		this.worker = w;
	}
	
	@RequestMapping(value="/start", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void startPrequery(@RequestParam("requery") boolean requery)
			throws Exception {
		
		QueryQueue qq = sessionHolder.getQueryQueue();
		
		if (qq==null) {
			throw new Exception("No QueryQueue in Session.");
		}
		
		if (qq.getPendingQueries().isEmpty() && !requery) {
			throw new Exception("No pending query in queue.");
		}
		
		LOGGER.debug("Start mass query.");
		
		try {
			worker.prequery(qq, requery);
		} catch (Exception e) {
			throw new Exception("Cannot start mass query.", e);
		}
		
	}
	
	@RequestMapping("/finished")
	public boolean isFinished() {
		return worker.isFinished();
	}
	
	@RequestMapping("/previewAvailable")
	public boolean previewAvailable() {
		try {
			return worker.getResult(500)!=null;
		} catch (Exception e) {
			return false;
		}
	}
	
	@RequestMapping("/preview")
	public QueryQueueData previewResult() throws Exception {
		
		QueryQueue qq; 
		try {
			qq = worker.getResult(500);
		} catch (Exception e) {
			return null;
		}
		return new QueryQueueData(qq);
	}
	
	@RequestMapping(value="/accept", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void acceptResult() throws Exception {
		QueryQueue qq = worker.getResult(500);
		if (qq!=null) {
			sessionHolder.setQueryQueue(qq);
		}
		worker.close();
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addRequest(
			@RequestParam("name") String name,
			@RequestParam(value="queryString", required=false) String qs,
			@RequestParam(value="info", required=false) String info,
			@RequestParam(value="type", required=false) String type,
			@RequestParam(value="authorities", required=false) List<String> auths
			) throws Exception {
		
		QueryQueue qq = sessionHolder.getQueryQueue();
		if (qq==null) {
			qq = new QueryQueue();
			qq.setName("QueryQueue");
			qq.setAdditionalInfo("created by " + getClass().getName());
			sessionHolder.setQueryQueue(qq);
		}
		
		String queryString = (qs==null || qs.isEmpty() ) ? name : qs;
		String addInfo = (info==null || info.isEmpty())? name : info;
		VocabularyType vType = guessType(type); 
		Set<Authority> authSet = createAuthSet(auths);
		System.out.println(authSet);
		VocabularyQuery vq = new VocabularyQuery(name, queryString, addInfo, vType, authSet);
		
		qq.addPendingQuery(vq);
		
	}
	
	@RequestMapping(value="/remove/{pos}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void removeAPendingQuery(@PathVariable("pos") int pos) {
		
		QueryQueue qq = sessionHolder.getQueryQueue();
		qq.getPendingQueries().remove(pos);
	}
	
	@RequestMapping(value="/reject", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void rejectResult() throws Exception {
		worker.close();
	}
	
	@RequestMapping(value="/authorities", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void setupAuthorities(@RequestParam(value="authorities", required=false) List<String> authorities) {
		Set<Authority> authList = createAuthSet(authorities);
		authoritiesHolder.setAuthorities(authList);
	}
	
	@RequestMapping(value="/authorities", method=RequestMethod.GET)
	public Set<Authority> getSetupAuthorities() {
		return authoritiesHolder.getUsedAuthorities();
	}

	
	private static Set<Authority> createAuthSet(List<String> list) {
		if (list==null || list.isEmpty()) return Collections.emptySet();
		return list.stream().map(s -> getAuth(s))
				.filter(a -> a!=null)
				.collect(Collectors.toSet());
	}
	
	private static Authority getAuth(String s) {
		String test = s.toLowerCase();
		if (test.contains("geoname")) return OrgGeonames.getInstance();
		if (test.contains("viaf")) return OrgViaf.getInstance();
		if (test.contains("wikidata")) return OrgWikidata.getInstance();
		if (test.contains("gnd")) return DeDnb.getInstance();
		return null;
	}
	
	private static VocabularyType guessType(String type) {
		if (type==null || type.isEmpty()) return VocabularyType.UNKNOWN;
		String test = type.toLowerCase();
		if (test.contains("pers")) return VocabularyType.PERSONAL_NAME;
		if (test.contains("corp") || test.contains("inst")) 
			return VocabularyType.INSTITUTION_NAME;
		if (test.contains("place")) return VocabularyType.PLACE_NAME;
		return VocabularyType.UNKNOWN;
	}
	
}
