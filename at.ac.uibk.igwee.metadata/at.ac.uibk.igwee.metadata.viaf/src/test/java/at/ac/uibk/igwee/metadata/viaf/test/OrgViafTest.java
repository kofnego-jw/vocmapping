package at.ac.uibk.igwee.metadata.viaf.test;

import java.net.URI;

import org.junit.Assert;
import org.junit.Test;

import at.ac.uibk.igwee.metadata.viaf.OrgViaf;

public class OrgViafTest {
	
	@Test
	public void test() throws Exception {
		OrgViaf viaf = OrgViaf.getInstance();
		URI created = viaf.createURI("102333412");
		Assert.assertEquals(created, new URI("http://www.viaf.org/viaf/102333412"));
	}

}
