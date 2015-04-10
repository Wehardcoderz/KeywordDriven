/*******************************************************************************
 * Licensed to the Software Freedom Conservancy (SFC) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The SFC licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package keywordutilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import common.Constants;
import common.KeywordBase;
import common.MyTestContext;
import common.Parameters;
import common.ReadExcelData;
import common.WebdriverManager;

/**
 * 
 * @author Vishshady
 *
 */
@Listeners(common.MyListener.class)
public class TestDriver implements Constants {
	private static Logger log = Logger.getLogger(TestDriver.class);
	public static String keyword;
	public static String target;
	public static String value;
	public static String index;
	ReadExcelData excelData;
	Parameters p = new Parameters();
	HashMap<Integer, ArrayList<String>> keyset;
	static KeywordBase keyWordBase;
	public static Method methods[];
	public String d;
	public String t;
	public String e;

	@Factory(dataProvider = "dp")
	public TestDriver(String t, String d, String e) {
		this.d = d;
		this.t = t;
		this.e = e;
	}

	@DataProvider(name = "dp", parallel = true)
	public static Object[][] dp() {
		return MyTestContext.prepareData();
	}

	@BeforeClass(alwaysRun = true)
	public void setUp() {
		MyTestContext.setTestStats(t, d);
		excelData = new ReadExcelData(p, t);
		keyset = new HashMap<Integer, ArrayList<String>>();
		keyset = excelData.getAllValues();
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		WebdriverManager.stopDriver();
	}

	@Test()
	public void test() throws InterruptedException {
		if (e.equalsIgnoreCase("False"))
			throw new SkipException(
					"Skipping this test as Enables == False");
		log.info("Now Executiong : " + t + " Test Description : " + d);
		keyWordBase = new KeywordBase(p);
		methods = keyWordBase.getClass().getMethods();
		Set<Entry<Integer, ArrayList<String>>> set = keyset.entrySet();
		for (Entry<Integer, ArrayList<String>> me : set) {
			log.info(me.getKey() + " " + me.getValue());
			keyword = null;
			target = null;
			value = null;
			keyword = excelData.getCellValue(me.getKey(), TESTCASE_COMMAND);
			target = excelData.getCellValue(me.getKey(), TESTCASE_TARGET);
			value = excelData.getCellValue(me.getKey(), TESTCASE_VALUE);
			index = excelData.getCellValue(me.getKey(), TESTCASE_INDEX);
			MyTestContext.setStep(me.getValue());
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
			Assert.fail("Check excel for errors " + e);
			keyword = null;
			target = null;
			value = null;
		} catch (IllegalArgumentException e) {
			Assert.fail("Check excel for errors " + e);
			keyword = null;
			target = null;
			value = null;
		} catch (InvocationTargetException e) {
			Assert.fail("Check excel for errors " + e);
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
			Assert.fail("Check excel for errors " + e);
			keyword = null;
			target = null;
			value = null;
		} catch (IllegalArgumentException e) {
			Assert.fail("Check excel for errors " + e);
			keyword = null;
			target = null;
			value = null;
		} catch (InvocationTargetException e) {
			Assert.fail("Check excel for errors " + e);
			keyword = null;
			target = null;
			value = null;
		}
	}

	private static int getIndex(String i) {
		return Integer.parseInt(i);
	}
}
