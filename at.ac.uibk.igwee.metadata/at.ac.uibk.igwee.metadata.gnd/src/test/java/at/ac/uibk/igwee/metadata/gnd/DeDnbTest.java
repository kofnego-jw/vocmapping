package at.ac.uibk.igwee.metadata.gnd;

import java.net.URI;

import org.junit.Assert;
import org.junit.Test;

public class DeDnbTest {
	
	DeDnb authority = DeDnb.getInstance();
	
	@Test
	public void test_getName() throws Exception {
		Assert.assertEquals(authority.getId(), "info.d-nb");
		Assert.assertEquals(authority.getName(), "GND");
	}
	
	@Test
	public void test_createUri() throws Exception {
		URI relU = authority.createURI("140961615");
		Assert.assertEquals("http://d-nb.info/gnd/140961615", relU.toString());
	}

}
