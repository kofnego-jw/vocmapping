package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.test;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.FileUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.application.Application;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.VocMapperSerializer;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.model.QueryQueueData;

import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration(classes={Application.class})
public class JsonOutputTest {
	
	private static QueryQueue qq;
	
	@Autowired
	private VocMapperSerializer serializer;
	
	@Test
	public void test01_readQueryQueue() throws Exception {
		qq = serializer.getQueryQueueFactory().loadQueryQueue(
				new FileInputStream(new File("./src/test/resources/queryQueue.xml")));
	}
	
	@Test
	public void test02_jsonOutput() throws Exception {
		
		ObjectMapper om = new ObjectMapper();
		
		String json = om.writeValueAsString(new QueryQueueData(qq));
		
		FileUtils.write(new File("./src/test/resources/queryQueueData.json"), json, "utf-8");
	}
	
	
}
