package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.test;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Assume;
import org.junit.Test;

import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;
import at.ac.uibk.igwee.metadata.metaquery.QueryQueueFormat;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.application.Application;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.AuthoritiesHolder;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.MetaQueryServiceFactory;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.PreQueryWorker;
import at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.VocMapperSerializer;

public class PreQueryWorkerTest {

	private static final boolean RUN_TEST = false;
	

	@Test
	public void test_query() throws Exception {
		
		Assume.assumeTrue(RUN_TEST);
		
		Application app = new Application();

		MetaQueryServiceFactory mqs = app.metaQueryServiceFactory();
		PreQueryWorker worker = new PreQueryWorker(mqs);
		
		AuthoritiesHolder holder = new AuthoritiesHolder();
		
		worker.setAuthoritiesHolder(holder);
		
		VocMapperSerializer serializer = new VocMapperSerializer(
				app.xsltService());

		QueryQueue qq = serializer.getQueryQueueFactory().loadQueryQueue(
				new FileInputStream(new File(
						"./src/test/resources/excelcsv.csv")),
				QueryQueueFormat.EXCEL_CSV, "macroman");

		worker.prequery(qq, true);
		
		while(!worker.isFinished()) {
			System.out.println("Waiting...");
			Thread.sleep(1000);
		}
		
		System.out.println("Is finished: " + worker.isFinished());
		
		QueryQueue finished = worker.getResult(0);
		System.out.println(finished.getPendingQueries().size() + " / " + finished.getResults().size());

		QueryQueue again = worker.getResult(0);
		System.out.println(again);
		System.out.println(finished);
		
		worker.close();
		
	}

}
