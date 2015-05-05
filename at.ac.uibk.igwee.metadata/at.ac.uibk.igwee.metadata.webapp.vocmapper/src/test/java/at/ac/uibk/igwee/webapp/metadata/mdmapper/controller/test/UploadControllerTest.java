package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.test;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.io.FileInputStream;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={at.ac.uibk.igwee.webapp.metadata.mdmapper.application.Application.class})
@WebAppConfiguration
public class UploadControllerTest {

	private static final File XML = new File("./src/test/resources/queryQueue.xml");
	
	private static final File EXCELCSV = new File("./src/test/resources/excelcsv.csv");
	
	@Autowired
	private WebApplicationContext context;
	
	@Test
	public void test_uploadXmlFile() throws Exception {
		
		MockMultipartFile xml = new MockMultipartFile("file", XML.getName(), "application/xml", new FileInputStream(XML));
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/app/upload/xml").file(xml))
			.andExpect(status().is(200));
		
		
	}
	
	@Test
	public void test_uploadExcelCsvFile() throws Exception {
		
		MockMultipartFile xml = new MockMultipartFile("file", EXCELCSV.getName(), "text/csv", new FileInputStream(EXCELCSV));
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/app/upload/excelcsv").file(xml).param("encoding", "macroman"))
			.andExpect(status().is(200));
		
		
	}
	
	
}
