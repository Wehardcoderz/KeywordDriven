package common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class MyListener extends TestListenerAdapter {
	LinkedHashMap<String, String> testPassed;
	LinkedHashMap<String,Object[]> testFailed;
	LinkedHashMap<String,Object[]> testSkipped;
	LinkedHashMap<String, String> testLogs;
	private ITestContext testContext = null;
	String t = null;

	@Override
	public void onStart(ITestContext context) {
		testContext = context;
		testPassed = new LinkedHashMap<String, String>();
		testFailed = new LinkedHashMap<String, Object[]>();
		testSkipped = new LinkedHashMap<String, Object[]>();
		testLogs = new LinkedHashMap<String, String>();
	}

	@Override
	public void onFinish(ITestContext context) {
		CustomReport reporter = new CustomReport();

		try {
			reporter.startReport();
			reporter.writeExecutionSummary(testContext);
			reporter.writeExecutionDetail(testPassed, testFailed, testSkipped,testLogs);
			reporter.finishReport();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		for(Map.Entry<String, String> entry: MyTestContext.getTestStats().entrySet()){
		testFailed.put(entry.getKey(),new Object[]{entry.getValue(),tr.getThrowable()});
		t = entry.getKey();
		}
		addTestLogs(t);
		MyTestContext.testStats.remove();
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		for(Map.Entry<String, String> entry: MyTestContext.getTestStats().entrySet()) {
		testSkipped.put(entry.getKey(),new Object[]{entry.getValue(),tr.getThrowable()});
		t=entry.getKey();
		}
		addTestLogs(t);
		MyTestContext.testStats.remove();}

	@Override
	public void onTestSuccess(ITestResult tr) {
		for(Map.Entry<String, String> entry: MyTestContext.getTestStats().entrySet()) {
			testPassed.put(entry.getKey(), entry.getValue());
			t=entry.getKey();
		}
		addTestLogs(t);
		MyTestContext.testStats.remove();
	}
	
	public void addTestLogs(String s) {
		testLogs.put(s, MyTestContext.getMessages());
		MyTestContext.messages.remove();
	}

//	@Override
//	public void onConfigurationFailure(ITestResult itr) {
//
//	}

}
