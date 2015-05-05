package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller.test;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({
	PreQueryControllerTest.class,
	EditControllerTest.class
})
public class ControllerTestSuite {
	
	private static final boolean RUN_TEST = false;
	
	@BeforeClass
	public static void runTest() {
		Assume.assumeTrue(RUN_TEST);
	}

}
