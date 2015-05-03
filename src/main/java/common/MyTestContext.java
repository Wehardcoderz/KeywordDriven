/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
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
package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * Stores and verifies all test data related to tests
 * 
 * @author Vishshady
 *
 */
public class MyTestContext {
	private static Logger log = Logger.getLogger(MyTestContext.class);
	static ThreadLocal<LinkedHashMap<String, String>> passed = new ThreadLocal<LinkedHashMap<String, String>>();
	static ThreadLocal<LinkedHashMap<String, String>> failed = new ThreadLocal<LinkedHashMap<String, String>>();
	static ThreadLocal<LinkedHashMap<String, String>> skipped = new ThreadLocal<LinkedHashMap<String, String>>();
	static Parameters p = new Parameters();
	static ThreadLocal<LinkedHashMap<String, String>> testStats = new ThreadLocal<LinkedHashMap<String, String>>();
	static ThreadLocal<StringBuilder> messages = new ThreadLocal<StringBuilder>();
	private static ThreadLocal<ArrayList<String>> step = new ThreadLocal<ArrayList<String>>();
	static ReadExcelData excelData;

	public static void setMessage(String s) {
		String m = "";
		m = m + "\n" + s;
		log.info("Test Log : " + m);
		if (messages.get() == null)
			messages.set(new StringBuilder().append(s
					+ System.getProperty("line.separator")));
		else
			messages.get().append(s + System.getProperty("line.separator"));
	}

	/**
	 * Get log message
	 * 
	 * @return
	 */
	public static StringBuilder getMessages() {
		return messages.get();
	}

	/**
	 * Set Test as passed with test details
	 * 
	 * @param stat
	 */
	public static void setPassed(LinkedHashMap<String, String> stat) {
		LinkedHashMap<String, String> l = new LinkedHashMap<String, String>();
		l.putAll(stat);
		passed.set(l);
	}

	/**
	 * Set Test as failed with test details
	 * 
	 * @param stat
	 */
	public static void setFailed(LinkedHashMap<String, String> stat) {
		LinkedHashMap<String, String> l = new LinkedHashMap<String, String>();
		l.putAll(stat);
		failed.set(l);
	}

	/**
	 * Set Test as skipped with test details
	 * 
	 * @param stat
	 */
	public static void setSkipped(LinkedHashMap<String, String> stat) {
		LinkedHashMap<String, String> l = new LinkedHashMap<String, String>();
		l.putAll(stat);
		skipped.set(l);
	}

	/**
	 * Get passed test details
	 * 
	 * @return
	 */
	public static LinkedHashMap<String, String> getPassed() {
		return passed.get();
	}

	/**
	 * Get failed test details
	 * 
	 * @return
	 */
	public static LinkedHashMap<String, String> getFailed() {
		return failed.get();
	}

	/**
	 * Get skipped test details
	 * 
	 * @return
	 */
	public static LinkedHashMap<String, String> getSkipped() {
		return skipped.get();
	}

	/**
	 * Set test name and description
	 * 
	 * @param testName
	 * @param description
	 */
	public static void setTestStats(String testName, String description) {
		LinkedHashMap<String, String> l = new LinkedHashMap<String, String>();
		l.put(testName, description);
		testStats.set(l);
	}

	/**
	 * Get test name and description
	 * 
	 * @return
	 */
	public static LinkedHashMap<String, String> getTestStats() {
		return testStats.get();
	}

	/**
	 * Get test cases from excel
	 * 
	 * @return
	 */
	public static Object[][] prepareData() {

		ReadExcelData excelData = new ReadExcelData(p,
				Constants.TEST_SUITESHEET);
		HashMap<Integer, ArrayList<String>> keyset = new HashMap<Integer, ArrayList<String>>();
		keyset = excelData.getAllValues();
		Set<Entry<Integer, ArrayList<String>>> set = keyset.entrySet();
		String[][] store = new String[excelData.getRowNumber()][3];
		int startRow = 0;

		for (Entry<Integer, ArrayList<String>> me : set) {
			if (me.getKey() != 0) {
				store[startRow][0] = excelData.getCellValue(me.getKey(),
						Constants.TEST_NAME);
				store[startRow][1] = excelData.getCellValue(me.getKey(),
						Constants.TEST_DESCRIPTION);
				store[startRow][2] = excelData.getCellValue(me.getKey(),
						Constants.TEST_ENABLED);
				startRow++;
			}
		}
		return store;

	}

	/**
	 * Verifies Test suite and test cases sheet headers.
	 */
	public static void checkExcel() {
		excelData = new ReadExcelData(p);
		String sheeName = excelData.setSheet(0);
		ArrayList<String> verify = new ArrayList<String>();
		boolean flag = true;

		if (!sheeName.contains(Constants.TEST_SUITESHEET)) {
			log.error("Test Suite sheet name should be : TestSuite");
			flag = false;
		}
		try {
			verify = excelData.getRowValue(sheeName, 0);
		} catch (NullPointerException e) {
			verify.add("");
		}
		if (!verify.contains(Constants.TEST_NAME)) {
			log.error("Column header missing : " + Constants.TEST_NAME);
			flag = false;
		}
		if (!verify.contains(Constants.TEST_DESCRIPTION)) {
			log.error("Column header missing : " + Constants.TEST_DESCRIPTION);
			flag = false;
		}
		if (!verify.contains(Constants.TEST_ENABLED)) {
			log.error("Column header missing : " + Constants.TEST_ENABLED);
			flag = false;
		}

		if (flag == false) {
			System.exit(1);
		}

		verifySheets();

	}

	public static Boolean verifyTestHeaders(String sheetName) {

		excelData = new ReadExcelData(p);
		ArrayList<String> verify = new ArrayList<String>();
		boolean flag = true;

		try {
			verify = excelData.getRowValue(sheetName, 0);
		} catch (NullPointerException e) {
			verify.add("");
		}
		if (!verify.contains(Constants.TESTCASE_DESCRIPTION)) {
			log.error("Column header missing : "
					+ Constants.TESTCASE_DESCRIPTION);
			flag = false;
		}
		if (!verify.contains(Constants.TESTCASE_COMMAND)) {
			log.error("Column header missing : " + Constants.TESTCASE_COMMAND);
			flag = false;
		}
		if (!verify.contains(Constants.TESTCASE_TARGET)) {
			log.error("Column header missing : " + Constants.TESTCASE_TARGET);
			flag = false;
		}
		if (!verify.contains(Constants.TESTCASE_VALUE)) {
			log.error("Column header missing : " + Constants.TESTCASE_VALUE);
			flag = false;
		}
		if (!verify.contains(Constants.TESTCASE_INDEX)) {
			log.error("Column header missing : " + Constants.TESTCASE_INDEX);
			flag = false;
		}
		return flag;
	}

	public static void verifySheets() {
		boolean flag = true;
		boolean testFlag = true;
		ArrayList<String> list = excelData.getColumnValue(
				Constants.TEST_SUITESHEET, Constants.TEST_NAME);
		excelData = new ReadExcelData(p);
		int sheetCount = excelData.getSheetCount();

		for (int i = 1; i < list.size(); i++) {
			log.info("Verifying for " + list.get(i));
			boolean present = true;
			for (int j = 1; j <= sheetCount; j++) {
				try {
					if (!excelData.isSheetPresent(j, list.get(i)))
						present = false;
					else {
						if (!verifyTestHeaders(list.get(i))) {
							testFlag = false;
							present = true;
						}
						break;
					}
				} catch (IllegalArgumentException a) {
					present = false;
					flag = false;
				}
			}

			if (present == false)
				log.error("Test Case name " + list.get(i)
						+ " is not found in any of the sheets");

		}

		if (flag == false && testFlag == false) {
			System.exit(1);
		}
	}

	/**
	 * Get test step being executed
	 * 
	 * @return
	 */
	public static ArrayList<String> getStep() {
		return step.get();
	}

	/**
	 * Set test step being executed
	 * 
	 * @param s
	 */
	public static void setStep(ArrayList<String> s) {
		step.set(s);
	}

}