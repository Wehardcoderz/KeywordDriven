package keywordutilities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import common.Constants;
import common.KeywordBase;
import common.MyTestContext;
import common.ReadExcelData;
import common.WebdriverManager;

@Listeners(common.MyListener.class)
public class TestDriver {
	private static Logger log = Logger.getLogger(TestDriver.class);
	public static String keyword;
	public static String target;
	public static String value;
	public static String index;
	ReadExcelData excelData;
	HashMap<Integer, ArrayList<String>> keyset;
	static KeywordBase keyWordBase;
	public static Method methods[];
	// RemoteWebDriver driver;
	public String d;
	public String t;

	@Factory(dataProvider = "dp")
	public TestDriver(String t, String d) {
		this.d = d;
		this.t = t;
	}

	@DataProvider(name = "dp", parallel = true)
	public static Object[][] dp() {
		return MyTestContext.prepareData();
	}

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		log.info("Set Up and Start driver");
		MyTestContext.setTestStats(t, d);
		excelData = new ReadExcelData(t, Constants.TEST_XLSPATH);
		keyset = new HashMap<Integer, ArrayList<String>>();
		keyset = excelData.getAllValues();
		WebdriverManager.startDriver();
		keyWordBase = new KeywordBase(WebdriverManager.getDriverInstance());
		methods = keyWordBase.getClass().getMethods();
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		log.info("Quitting Driver");
		WebdriverManager.stopDriver();
	}

	@Test
	public void test() {
		log.info("Now Executiong : " + t + " Test Description : " + d);
		Set<Entry<Integer, ArrayList<String>>> set = keyset.entrySet();

		for (Entry<Integer, ArrayList<String>> me : set) {
			log.info(me.getKey() + " " + me.getValue());
			keyword = null;
			target = null;
			value = null;
			keyword = excelData.getCellValue(me.getKey(), "Command");
			target = excelData.getCellValue(me.getKey(), "Target");
			value = excelData.getCellValue(me.getKey(), "Value");
			index = excelData.getCellValue(me.getKey(), "Index");
			execute();
		}
	}

	private static void execute() {

		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(keyword)) {
				// In case of match found, it will execute the matched
				// method
				if (index == null) {
					executeIndexNull(i);
				} else 
					executeIndex(i,getIndex(index));
			}
		}
	}

	public static void executeIndexNull(int i) {
		try {
			if (target != null && value != null) {
				methods[i].invoke(keyWordBase, target, value);
			} else if (target != null && value == null) {
				methods[i].invoke(keyWordBase, target);
			} else if (target == null && value != null) {
				methods[i].invoke(keyWordBase, value);
			} else if (value == null && target == null) {
				methods[i].invoke(keyWordBase);
			} else {
				Assert.fail("Empty target and values");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getCause().toString());
			keyword = null;
			target = null;
			value = null;
		}
	}
	
	public static void executeIndex(int i, int index) {
		try {
			if (target != null && value != null) {
				methods[i].invoke(keyWordBase, target, value, index);
			} else if (target != null && value == null) {
				methods[i].invoke(keyWordBase, target, index);
			} else if (target == null && value != null) {
				methods[i].invoke(keyWordBase, value);
			} else if (value == null && target == null) {
				methods[i].invoke(keyWordBase);
			} else {
				Assert.fail("Empty target and values");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getCause().toString());
			keyword = null;
			target = null;
			value = null;
		}
	}

	private static int getIndex(String i) {
		return Integer.parseInt(i);
	}
}
