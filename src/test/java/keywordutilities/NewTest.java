package keywordutilities;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Factory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.ITest;
import org.testng.Reporter;
import org.testng.SkipException;

import common.CheckStatic;
import common.CustomReport;
import common.Parameters;
import common.WebdriverManager;

@Listeners(common.MyListener.class)
public class NewTest implements ITest {
	private static Logger log = Logger.getLogger(NewTest.class);
	Parameters p = new Parameters();
	CustomReport cus = new CustomReport();
	int time = 0;
	public String d;
	public String t;

	@Factory(dataProvider = "dp")
	public NewTest(String t, String d) {
		this.d = d;
		this.t = t;
	}

	@BeforeMethod
	public void setUp() {
		// WebdriverManager.setupDriver("firefox");
		log.info("setup for " + t);
	}

	@AfterMethod
	public void tearDown() {
		// WebdriverManager.stopDriver();
		log.info("teardown for " + t);
	}

	@Test()
	public void f() throws InterruptedException {
		WebdriverManager.getDriverInstance();
		// Thread.sleep(10000);
		// System.out.println("started " + n);
		if (t.equals("TestCase3"))
			throw new SkipException("Skipping " + t
					+ " - This is not ready for testing ");
		if (t.equals("TestCase1"))
			Assert.fail("Failed due to fucking problem");
		Reporter.log("now executing " + t + " " + d);
		CheckStatic.add(t);
	}

	@DataProvider(name = "dp", parallel = true)
	public static Object[][] dp() {
		return new Object[][] { new Object[] { "TestCase1", "TestDesc1" },
				new Object[] { "TestCase2", "TestDesc2" },
				new Object[] { "TestCase3", "TestDesc3" },
				new Object[] { "TestCase4", "TestDesc4" },
				new Object[] { "TestCase5", "TestDesc5" },
				new Object[] { "TestCase6", "TestDesc6" } };
	}

	@Override
	public String getTestName() {
		return t + " " + d;
	}
}
