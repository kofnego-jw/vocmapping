package at.ac.uibk.igwee.metadata.metaquery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import at.ac.uibk.igwee.metadata.metaquery.impl.QueryQueueFactoryImpl;
import at.ac.uibk.igwee.xslt.XsltService;
import at.ac.uibk.igwee.xslt.impl.SaxonXsltServiceImpl;

@RunWith(BlockJUnit4ClassRunner.class)
public class SerializationTest {

	public static final File EXCEL_CSV = new File("./src/test/resources/excelcsv.csv");
	public static final File EXCEL_XML = new File("./src/test/resources/excelcsv.xml");
	public static final File CSV = new File("./src/test/resources/queryQueue.csv");
	
	public static final File QUERY_QUEUE_OUTPUT = new File("./src/test/resources/queryQueueOutput.xml");
	
	
	private static QueryQueueFactoryImpl qqf;
	
	@BeforeClass
	public static void setUpQueryQueueFactory() throws Exception {
		qqf = new QueryQueueFactoryImpl();
		XsltService xs = new SaxonXsltServiceImpl();
		qqf.setXsltService(xs);
	}
	
	@Test
	public void test_listAllCharsets() {
		Charset.availableCharsets().entrySet().forEach(entry -> System.out.println(entry.getKey()));
	}
	
	@Test
	public void test_convertExcelCSV() throws Exception {
		QueryQueue qq = qqf.loadQueryQueue(new FileInputStream(EXCEL_CSV), 
				QueryQueueFormat.EXCEL_CSV, "x-MacRoman");
		
		System.out.println("Excel CSV");
		MetaQueueServiceTest.output(qq);
		qqf.writeQueryQueue(qq, new FileOutputStream(QUERY_QUEUE_OUTPUT));
	}
	
	@Test
	public void test_convertExcelXML() throws Exception {
		QueryQueue qq = qqf.loadQueryQueue(new FileInputStream(EXCEL_XML), QueryQueueFormat.EXCEL_XML, 
				"UTF-8");
		
		System.out.println("Excel XML");
		MetaQueueServiceTest.output(qq);
	}
	
	@Test
	public void test_convertCSV() throws Exception {
		QueryQueue qq = qqf.loadQueryQueue(new FileInputStream(CSV), QueryQueueFormat.CSV, "utf-8");
		System.out.println("CSV");
		
		MetaQueueServiceTest.output(qq);
	}
	
}
