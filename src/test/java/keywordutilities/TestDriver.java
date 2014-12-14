package keywordutilities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import common.ReadExcelData;

public class TestDriver {
	private static Logger log = Logger.getLogger(TestDriver.class);
	ReadExcelData excelData;
	HashMap<Integer, ArrayList<String>> keyset;
	static KeywordBase keyWord;
	public static String keyword;
	public static String target;
	public static String value;
	public static Method methods[];

	public TestDriver() {
		excelData = new ReadExcelData("TestCase.xls");
		keyset = new HashMap<Integer, ArrayList<String>>();
		keyset = excelData.getAllValues("LoginTest");
		keyWord = new KeywordBase();
		methods = keyWord.getClass().getMethods();
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
			keyword = excelData.getCellValue("LoginTest", me.getKey(),
					"Command");
			target = excelData.getCellValue("LoginTest", me.getKey(), "Target");
			value = excelData.getCellValue("LoginTest", me.getKey(), "Value");
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
					if (target == null) {
						if (value != null) {
							methods[i].invoke(keyWord, value);
							break;
						}
					} else if (value == null && target != null) {
						methods[i].invoke(keyWord, target);
						break;
					}
					else if (value != null && target != null) {
						methods[i].invoke(keyWord, target, value);
						break;
					}
					else if (value == null && target == null) {
						methods[i].invoke(keyWord);
						break;
					}
					else {
						Assert.fail("Empty target and values");
						break;
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
