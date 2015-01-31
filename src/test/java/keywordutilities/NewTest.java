package keywordutilities;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import common.CustomReport;
import common.MyTestContext;
import common.Parameters;
import common.WebdriverManager;

@Listeners(common.MyListener.class)
public class NewTest {
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
		MyTestContext.setTestStats(t, d);
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
		MyTestContext.setMessage(("now executing " + t));
		if (t.equals("ss"))
			throw new SkipException("Skipping " + t
					+ " - This is not ready for testing ");
		if (t.equals("LoginTest"))
			Assert.fail("Failed due to fucking problem");
		
	}

	@DataProvider(name = "dp", parallel = true)
	public static Object[][] dp() {
		return MyTestContext.prepareData();
	}
}
