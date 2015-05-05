package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;
import at.ac.uibk.igwee.metadata.metaquery.VocabularyQueryResult;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.application.Application;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.EditController;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.SessionHolder;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.VocMapperSerializer;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.model.QueryQueueData;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.model.VocabularyData;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.model.VocabularyQueryResultData;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={Application.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditControllerTest {
	
	private static SessionHolder sessionHolder;
	
	private static ObjectMapper om;
	
	private static Random random;
	
	private static final int POSITION = 0;
	
	@Autowired
	private EditController ec;
	
	@Autowired
	private VocMapperSerializer vms;
	
	@BeforeClass
	public static void setupSessionHolder() {
		sessionHolder = new SessionHolder();
		om = new ObjectMapper();
		random = new Random();
	}
	
	@Before
	public void setUpEditController() {
		ec.setSessionHolder(sessionHolder);
	}
	
	
	
	@Test
	public void test01_loadQueryQueue() throws Exception {
		QueryQueue qq = vms.getQueryQueueFactory()
				.loadQueryQueue(new FileInputStream("./src/test/resources/prequery/forEditing.xml"));
		sessionHolder.setQueryQueue(qq);
	}

	@Test
	public void test01a_filterQueue() throws Exception {
		QueryQueue qq = sessionHolder.getQueryQueue();
		VocabularyQueryResult result = qq.getResults().get(0);
		String chosen = "http://geonames.org/7782556";
		System.out.println("Chosen: " + chosen);
		result.getResults().stream().filter(voc -> voc.getURI().toString().equals(chosen))
				.forEach(voc -> System.out.println(voc));
	}
	
	@Test
	public void test02_getQueryQueueData() throws Exception {
		QueryQueueData qqd = ec.quickInfo();
		String content = om.writeValueAsString(qqd);
		write(content, "./src/test/resources/edit/queryQueueData.json");
	}
	
	@Test
	public void test03_loadAResult() throws Exception {
		VocabularyQueryResultData data = ec.view(POSITION);
		write(om.writeValueAsString(data), "./src/test/resources/edit/viewPosition.json");
	}
	
	@Test
	public void test04_removeAResult() throws Exception {
		
		VocabularyData voc = null;
		
		int pos = 0;
		
		do {
			VocabularyQueryResultData data = ec.view(pos);
			pos++;
			if (data.getFixedResult()!=null) continue;
			if (data.getResults().isEmpty()) continue;
			voc = data.getResults().get(random.nextInt(data.getResults().size()));
		} while (voc==null && pos < sessionHolder.getQueryQueue().getResults().size());
		
		String url = voc.getUrl();
		
		VocabularyQueryResultData toWrite = ec.removeOneHit(pos-1, url);
		
		write(om.writeValueAsString(toWrite), "./src/test/resources/edit/removeOne.json");
	}
	
	@Test
	public void test05_fixAResult() throws Exception {
		VocabularyData voc = null;
		int pos = 0;
		do {
			VocabularyQueryResultData data = ec.view(pos);
			pos++;
			if (data.getFixedResult()!=null) continue;
			if (data.getResults().isEmpty()) continue;
			voc = data.getResults().get(random.nextInt(data.getResults().size()));
		} while (voc==null && pos < sessionHolder.getQueryQueue().getResults().size());
		
		String url = voc.getUrl();
		
		VocabularyQueryResultData toWrite = ec.fixTo(pos-1, url);
		
		write(om.writeValueAsString(toWrite), "./src/test/resources/edit/fixTo.json");
		
	}
	
	@Test
	public void test06_fixAResultAndClear() throws Exception {
		VocabularyData voc = null;
		int pos = 0;
		do {
			VocabularyQueryResultData data = ec.view(pos);
			pos++;
			if (data.getFixedResult()!=null) continue;
			if (data.getResults().isEmpty()) continue;
			voc = data.getResults().get(random.nextInt(data.getResults().size()));
		} while (voc==null && pos < sessionHolder.getQueryQueue().getResults().size());
		
		String url = voc.getUrl();
		
		VocabularyQueryResultData toWrite = ec.fixToAndClear(pos-1, url);
		
		write(om.writeValueAsString(toWrite), "./src/test/resources/edit/fixToAndClear.json");
		
	}
	
//	@Test
	public void test07_fixACustomResult() throws Exception {
		VocabularyQueryResultData result = null;
		int pos = 0;
		do {
			VocabularyQueryResultData data = ec.view(pos);
			pos++;
			if (data.getFixedResult()!=null) continue;
			if (data.getResults().isEmpty()) continue;
			result = data;
		} while (result==null && pos < sessionHolder.getQueryQueue().getResults().size());
		
		VocabularyQueryResultData toWrite = ec.fixToCustom(pos-1, "geonames", "1234567");
		
		write(om.writeValueAsString(toWrite), "./src/test/resources/edit/fixToCustom.json");
		
	}
	
//	@Test
	public void test08_writeFinalResult() throws Exception {
		write(vms.getQueryQueueFactory().fromQueryQueueToXML(sessionHolder.getQueryQueue()),
				"./src/test/resources/edit/queryQueueAfter.xml");
	}
	
	
	
	private static void write(String content, String filename) throws Exception {
		
		FileUtils.write(new File(filename), content, "utf-8");
		
	}
}
