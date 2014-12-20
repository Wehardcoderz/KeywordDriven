package keywordutilities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import common.ReadExcelData;
import common.WebdriverManager;

public class TestDriver {
	private static Logger log = Logger.getLogger(TestDriver.class);
	LinkedList<String> testName = new LinkedList<String>();
	LinkedList<String> testDescription = new LinkedList<String>();
	LinkedList<String> testEnabled = new LinkedList<String>();
	public static String keyword;
	public static String target;
	public static String value;
	ReadExcelData excelData;
	HashMap<Integer, ArrayList<String>> keyset;
	static KeywordBase keyWordBase;
	public static Method methods[];
	RemoteWebDriver d;

	@BeforeSuite
	public void prepareData() {
		
	}
	
	@BeforeMethod
	public void setUp(String sheetName) {
		excelData = new ReadExcelData(sheetName, "TestCase.xls");
		keyset = new HashMap<Integer, ArrayList<String>>();
		keyset = excelData.getAllValues();
		WebdriverManager.startDriver();
		d = WebdriverManager.getDriverInstance();
		keyWordBase = new KeywordBase(d);
		methods = keyWordBase.getClass().getMethods();
	}

	@Test
	public void test() {
		log.info("Now Executiong " + "LoginTest");
		Set<Entry<Integer, ArrayList<String>>> set = keyset.entrySet();

		for (Entry<Integer, ArrayList<String>> me : set) {
			System.out.println(me.getKey() + " " + me.getValue());
			keyword = null;
			target = null;
			value = null;
			keyword = excelData.getCellValue(me.getKey(), "Command");
			target = excelData.getCellValue(me.getKey(), "Target");
			value = excelData.getCellValue(me.getKey(), "Value");
			execute();
		}
	}

	private static void execute() {
		// This is a loop which will run for the number of actions in the Action
		// Keyword class
		// method variable contain all the method and method.length returns the
		// total number of methods
		for (int i = 0; i < methods.length; i++) {
			// This is now comparing the method name with the ActionKeyword
			// value got from excel
			try {
				if (methods[i].getName().equals(keyword)) {
					// In case of match found, it will execute the matched
					// method
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
					// Once any method is executed, this break statement will
					// take
					// the flow outside of for loop

				}
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail(e.getCause().toString());
				keyword = null;
				target = null;
				value = null;
			}
		}
	}
}
