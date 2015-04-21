package driver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.testng.Assert;

import common.Constants;
import common.KeywordBase;
import common.MyTestContext;
import common.Parameters;
import common.ReadExcelData;
import common.VariableStorage;
import common.WebdriverManager;

public class KeywordLauncher implements Constants {
	private static Logger log = Logger.getLogger(KeywordLauncher.class);
	public static String keyword;
	public static String target;
	public static String value;
	public static String index;
	ReadExcelData excelData;
	Parameters p = new Parameters();
	HashMap<Integer, ArrayList<String>> keyset;
	static KeywordBase keyWordBase;
	public static Method methods[];

	KeywordLauncher(String t, String d) {
		set(t, d);
	}

	public void set(String t, String d) {
		excelData = new ReadExcelData(p, t);
		keyset = new HashMap<Integer, ArrayList<String>>();
		keyset = excelData.getAllValues();
		log.info("Setting up browser : " + p.getBrowsers() + " " + t);
		WebdriverManager.setupDriver(p.getBrowsers());
		keyWordBase = new KeywordBase(p);
		methods = keyWordBase.getClass().getMethods();

		log.info("Now Executing : " + t + " Test Description : " + d);
		Set<Entry<Integer, ArrayList<String>>> set = keyset.entrySet();
		for (Entry<Integer, ArrayList<String>> me : set) {
			log.info(me.getKey() + " " + me.getValue() + " " + t);
			keyword = null;
			target = null;
			value = null;
			keyword = excelData.getCellValue(me.getKey(), TESTCASE_COMMAND);
			target = excelData.getCellValue(me.getKey(), TESTCASE_TARGET);
			value = excelData.getCellValue(me.getKey(), TESTCASE_VALUE);
			index = excelData.getCellValue(me.getKey(), TESTCASE_INDEX);
			MyTestContext.setStep(me.getValue());
			if (keyword != null) {
				if (keyword.contains("$")) {
					if (keyword.contains("store"))
						VariableStorage.setVar(target, value);
					else if (keyword.contains("|")) {
						String[] getVar = keyword.substring(1).split("\\|");
						value = getVar[0].trim();
						keyword = getVar[1].trim();
					}

				}
			}
			execute();
		}

	}

	private static void execute() {

		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(keyword)) {
				if (index == null) {
					executeIndexNull(i);
				} else
					executeIndex(i, getIndex(index));
			}
		}
	}

	public static void executeIndexNull(int i) {
		try {
			String[] index = new String[0];
			if (target != null && value != null) {
				if (methods[i].getParameterTypes().length == 3)
					methods[i].invoke(keyWordBase, target, value, index);
				else
					methods[i].invoke(keyWordBase, target, value);
			} else if (target != null && value == null) {
				if (methods[i].getParameterTypes().length == 2)
					methods[i].invoke(keyWordBase, target, index);
				else
					methods[i].invoke(keyWordBase, target);
			} else if (target == null && value != null) {
				methods[i].invoke(keyWordBase, value);
			} else if (value == null && target == null) {
				methods[i].invoke(keyWordBase);
			} else {
				Assert.fail("Empty target and values");
			}
		} catch (IllegalAccessException e) {
			Assert.fail("1. Check test case step for errors");
			keyword = null;
			target = null;
			value = null;
		} catch (IllegalArgumentException e) {
			Assert.fail("1. Check test case step for errors 2. Make sure valid keyword and arguments are passed");
			keyword = null;
			target = null;
			value = null;
		} catch (InvocationTargetException e) {
			Assert.fail("1. Check test case step for errors 2. Verify the behavior of the application manually");
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
		} catch (IllegalAccessException e) {
			Assert.fail("1. Check test case step for errors");
			keyword = null;
			target = null;
			value = null;
		} catch (IllegalArgumentException e) {
			Assert.fail("1. Check test case step for errors 2. Make sure valid keyword and arguments are passed");
			keyword = null;
			target = null;
			value = null;
		} catch (InvocationTargetException e) {
			Assert.fail("1. Check test case step for errors 2. Verify the behavior of the application manually");
			keyword = null;
			target = null;
			value = null;
		}
	}

	private static int getIndex(String i) {
		return Integer.parseInt(i);
	}

}
