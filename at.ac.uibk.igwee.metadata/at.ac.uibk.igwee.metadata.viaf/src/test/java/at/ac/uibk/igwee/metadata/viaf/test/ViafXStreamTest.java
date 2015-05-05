package at.ac.uibk.igwee.metadata.viaf.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import at.ac.uibk.igwee.metadata.viaf.ViafQueryResult;
import at.ac.uibk.igwee.metadata.viaf.impl.ViafRespToResult;
import at.ac.uibk.igwee.xslt.XsltService;
import at.ac.uibk.igwee.xslt.impl.SaxonXsltServiceImpl;

public class ViafXStreamTest {

	private static final ViafRespToResult vr2r;
	static {
		vr2r = new ViafRespToResult();
		XsltService serv = new SaxonXsltServiceImpl();
		vr2r.setXsltService(serv);
	}
	
	@Test
	public void test01_convertResponseToViafQueryResult() throws Exception {
		
		InputStream in = new FileInputStream(new File("./src/test/resources/viafresponses/viaf_person.xml"));
		
		ViafQueryResult result = vr2r.convertToQueryResult(in);
		output(result);
		
		InputStream in2 = new FileInputStream(new File("./src/test/resources/viafresponses/viaf_person2.xml"));
		
		ViafQueryResult result2 = vr2r.convertToQueryResult(in2);
		
		output(result2);
		
		ViafQueryResult resultAll = vr2r.mergeResults(result, result2);
		
		output(resultAll);
		
	}
	
	
	public static void output(ViafQueryResult r) {
		System.out.println("ViafQueryResult: ");
		System.out.println("Totalhits: " + r.getTotalhits());
		System.out.println("StartRow: " + r.getStartRow());
		System.out.println("MaxRows: " + r.getMaxRows());
		System.out.println("NextRow: " + r.getNextRow());
		System.out.println("Results: ");
		r.getResultsOrderDescByLinkedSources().forEach(v -> {
			System.out.println("  " + v.getInternalID());
			System.out.println("    " + v.getName());
			System.out.println("    " + v.getURI());
			System.out.println("    " + v.getVocabularyType());
			System.out.println("    LinkedSources: " + v.getLinkedSources().size());
			v.getLinkedSources().forEach((key,vid) -> System.out.println("      " + key + " | " + vid));
		});
		System.out.println();
	}
	
}
