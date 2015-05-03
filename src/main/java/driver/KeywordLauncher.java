package driver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import common.Constants;
import common.IDriver;
import common.KeywordBase;
import common.MyTestContext;
import common.Parameters;
import common.ReadExcelData;
import common.VariableStorage;

public class KeywordLauncher implements Constants, IDriver {
	private static Logger log = Logger.getLogger(KeywordLauncher.class);
	public static String keyword;
	public static String target;
	public static String value;
	public static String index;
	ReadExcelData excelData;
	HashMap<Integer, ArrayList<String>> keyset;
	// public static KeywordBase keyWordBase;
	KeywordBase keyWordBase = null;
	public static Method methods[];
	String t;
	String d;
	Parameters p;

	KeywordLauncher(String t, String d, Parameters p) {
		this.t = t;
		this.d = d;
		this.p = p;
	}

	public void set() throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		excelData = new ReadExcelData(p, t);
		keyset = new HashMap<Integer, ArrayList<String>>();
		keyset = excelData.getAllValues();
		log.info("Setting up browser : " + p.getBrowsers() + " " + t);

		keyWordBase = KeywordBase.class.getConstructor(Parameters.class).newInstance(p);

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

	private void execute() {

		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(keyword)) {
				if (index == null) {
					executeIndexNull(i);
				} else
					executeIndex(i, getIndex(index));
			}
		}
	}

	public void executeIndexNull(int i) {
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
			StringWriter s = new StringWriter();
			PrintWriter p = new PrintWriter(s, true);
			e.printStackTrace(p);
			log.error(s.getBuffer().toString());
			Assert.fail("1. Check test case step for errors");
			keyword = null;
			target = null;
			value = null;
		} catch (IllegalArgumentException e) {
			StringWriter s = new StringWriter();
			PrintWriter p = new PrintWriter(s, true);
			e.printStackTrace(p);
			log.error(s.getBuffer().toString());
			Assert.fail("1. Check test case step for errors 2. Make sure valid keyword and arguments are passed");
			keyword = null;
			target = null;
			value = null;
		} catch (InvocationTargetException e) {
			StringWriter s = new StringWriter();
			PrintWriter p = new PrintWriter(s, true);
			e.printStackTrace(p);
			log.error(s.getBuffer().toString());
			Assert.fail("1. Check test case step for errors 2. Verify the behavior of the application manually");
			keyword = null;
			target = null;
			value = null;
		}
	}

	public void executeIndex(int i, int index) {
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
			StringWriter s = new StringWriter();
			PrintWriter p = new PrintWriter(s, true);
			e.printStackTrace(p);
			log.error(s.getBuffer().toString());
			Assert.fail("1. Check test case step for errors");
			keyword = null;
			target = null;
			value = null;
		} catch (IllegalArgumentException e) {
			StringWriter s = new StringWriter();
			PrintWriter p = new PrintWriter(s, true);
			e.printStackTrace(p);
			log.error(s.getBuffer().toString());
			Assert.fail("1. Check test case step for errors 2. Make sure valid keyword and arguments are passed");
			keyword = null;
			target = null;
			value = null;
		} catch (InvocationTargetException e) {
			StringWriter s = new StringWriter();
			PrintWriter p = new PrintWriter(s, true);
			e.printStackTrace(p);
			log.error(s.getBuffer().toString());
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
