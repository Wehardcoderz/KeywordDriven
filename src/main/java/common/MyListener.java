package common;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.internal.TestResult;

public class MyListener extends TestListenerAdapter {
	LinkedHashMap<String, String> testPassed;
	LinkedHashMap<String, String> testFailed;
	LinkedHashMap<String, String> testSkipped;
	private ITestContext testContext = null;
	String[] s;

	@Override
	public void onStart(ITestContext context) {
		testContext = context;
		testPassed = new LinkedHashMap<>();
		testFailed = new LinkedHashMap<>();
		testSkipped = new LinkedHashMap<>();
	}

	@Override
	public void onFinish(ITestContext context) {
		CustomReport reporter = new CustomReport();

		try {
			reporter.startReport();
			reporter.writeExecutionSummary(testContext);
			reporter.writeExecutionDetail(testPassed, testFailed, testSkipped);
			reporter.finishReport();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
System.out.println(CheckStatic.getAdded());
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		s = tr.getTestName().split(" ");
		testFailed.put(s[0], s[1]);
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		s = tr.getTestName().split(" ");
		testSkipped.put(s[0], s[1]);
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		s = tr.getTestName().split(" ");
		testPassed.put(s[0], s[1]);
		for(String s : Reporter.getOutput(tr))
			System.out.println(s);
	}

//	@Override
//	public void onConfigurationFailure(ITestResult itr) {
//
//	}

}
