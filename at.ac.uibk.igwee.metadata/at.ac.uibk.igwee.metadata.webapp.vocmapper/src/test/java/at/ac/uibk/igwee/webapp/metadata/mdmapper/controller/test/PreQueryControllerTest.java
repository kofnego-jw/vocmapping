package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.test;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.application.Application;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.PreQueryController;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.PreQueryWorker;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.SessionHolder;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.VocMapperSerializer;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.model.QueryQueueData;

import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes={Application.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PreQueryControllerTest {
	
	private static SessionHolder sh;
	
	private static PreQueryWorker worker = null;
	
	private static final boolean RUN_TEST = false;
	
	
	@Autowired
	private PreQueryController pc;
	
	@Autowired
	private VocMapperSerializer serializer;
	
	@Autowired
	private ApplicationContext ctx;
	
	@BeforeClass
	public static void createSessionHolder() {
		Assume.assumeTrue(RUN_TEST);
		
		sh = new SessionHolder();
	}
	
	@Before
	public void setSessionHolder() {
		pc.setSessionHolder(sh);
		if (worker!=null)
			pc.setPreQueryWorker(worker);
	}
	
	@Test
	public void test00_getWorker() {
		worker = ctx.getBean(PreQueryWorker.class);
	}
	
	@Test
	public void test01_addRequests() throws Exception {
		pc.addRequest("Einstein, Albert", "Albert Einstein", "Physicist", "person", null);
		pc.addRequest("Bauer, Otto", "Otto Bauer", "Catholic", "person", Arrays.asList("viaf"));
		pc.addRequest("Mühlviertel", null, null, "place", Arrays.asList("geonames"));
	}
	
	@Test
	public void test02_writeFile() throws Exception {
		QueryQueue qq = sh.getQueryQueue();
		Assert.assertNotEquals(qq.getPendingQueries().size(), 0);
		String xml = serializer.getQueryQueueFactory().fromQueryQueueToXML(qq);
		FileUtils.write(new File("./src/test/resources/prequery/pending.xml"), xml, "utf-8");
	}
	
	@Test
	public void test03_doQuery() throws Exception {
		pc.startPrequery(false);
		while(!pc.isFinished()) {
			System.out.println("Still querying...");
			Thread.sleep(1000);
		}
		System.out.println("Finished.");
	}
	
	@Test
	public void test04_preview() throws Exception {
		QueryQueueData qqd = pc.previewResult();
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(qqd);
		FileUtils.write(new File("./src/test/resources/prequery/preview.json"), content, "utf-8");
	}
	
	@Test
	public void test05_accept() throws Exception {
		pc.acceptResult();
	}
	
	@Test
	public void test06_saveFile() throws Exception {
		QueryQueue qq = sh.getQueryQueue();
//		Assert.assertNotEquals(qq.getResults().size(), 0);
		String xml = serializer.getQueryQueueFactory().fromQueryQueueToXML(qq);
		FileUtils.write(new File("./src/test/resources/prequery/forEditing.xml"), xml, "utf-8");
	}

}
