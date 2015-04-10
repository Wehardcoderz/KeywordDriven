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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
/**
 * 
 * @author Vishshady
 *
 */
public class MyListener extends TestListenerAdapter {
	Parameters p = new Parameters();
	LinkedHashMap<String, Object[]> testPassed;
	LinkedHashMap<String, Object[]> testFailed;
	LinkedHashMap<String, Object[]> testSkipped;
	LinkedHashMap<String, String> testLogs;
	private ITestContext testContext = null;
	String t = null;
	long time = 0;
	int totalTime;

	@Override
	public void onStart(ITestContext context) {
		totalTime = (int) System.currentTimeMillis();
		testContext = context;
		testPassed = new LinkedHashMap<String, Object[]>();
		testFailed = new LinkedHashMap<String, Object[]>();
		testSkipped = new LinkedHashMap<String, Object[]>();
		testLogs = new LinkedHashMap<String, String>();
	}

	@Override
	public void onFinish(ITestContext context) {
		totalTime = (int) (System.currentTimeMillis() - totalTime);
		totalTime = totalTime / 1000;
		CustomReport reporter = new CustomReport(p);
		try {
			reporter.startReport();
			reporter.writeExecutionSummary(testContext,
					timeConvertor(totalTime));
			reporter.writeExecutionDetail(testPassed, testFailed, testSkipped,
					testLogs);
			reporter.finishReport();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		time = tr.getEndMillis() - tr.getStartMillis();
		time = time / 1000;
		String testName = null;
		for (Map.Entry<String, String> entry : MyTestContext.getTestStats()
				.entrySet()) {
			String[] err = null;
			try {
				err = tr.getThrowable().getMessage().split("\\n");
			} catch (NullPointerException n) {
				err = new String[] { "Fail" };
				n.printStackTrace();
			}
			testName = entry.getKey();
			testFailed.put(testName, new Object[] { entry.getValue(), err[0],
					MyTestContext.getStep(), time });
			t = entry.getKey();
		}
		addTestLogs(t);
		MyTestContext.testStats.remove();
		WebDriver driver = new Augmenter().augment(WebdriverManager
				.getDriverInstance());
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ssaa");
		String destDir = "screenshots";
		new File(destDir).mkdirs();
		String destFile = testName + dateFormat.format(new Date()) + ".png";

		try {
			FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		addTestLogs("Screen shot saved at /screenshots/" + destFile);
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		time = tr.getEndMillis() - tr.getStartMillis();
		time = time / 1000;
		for (Map.Entry<String, String> entry : MyTestContext.getTestStats()
				.entrySet()) {
			testSkipped.put(entry.getKey(),
					new Object[] { entry.getValue(), tr.getThrowable(), time });
			t = entry.getKey();
		}
		addTestLogs(t);
		MyTestContext.testStats.remove();
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		time = tr.getEndMillis() - tr.getStartMillis();
		time = time / 1000;
		for (Map.Entry<String, String> entry : MyTestContext.getTestStats()
				.entrySet()) {
			testPassed.put(entry.getKey(), new Object[] { entry.getValue(),
					time });
			t = entry.getKey();
		}
		addTestLogs(t);
		MyTestContext.testStats.remove();
	}

	public void addTestLogs(String s) {
		testLogs.put(s, MyTestContext.getMessages());
		MyTestContext.messages.remove();
	}

	public String timeConvertor(int time) {
		int secs = time % 60;
		int totalMins = time / 60;
		int mins = totalMins % 60;
		int hrs = totalMins / 60;
		return (hrs + "H : " + mins + "M : " + secs + "S");
	}

}
