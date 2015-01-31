package common;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class MyTestContext {
	private static Logger log = Logger.getLogger(MyTestContext.class);
	static ThreadLocal<LinkedHashMap<String, String>> passed = new ThreadLocal<LinkedHashMap<String, String>>();
	static ThreadLocal<LinkedHashMap<String, String>> failed = new ThreadLocal<LinkedHashMap<String, String>>();
	static ThreadLocal<LinkedHashMap<String, String>> skipped = new ThreadLocal<LinkedHashMap<String, String>>();
	
	static ThreadLocal<LinkedHashMap<String, String>> testStats = new ThreadLocal<LinkedHashMap<String, String>>();
	static ThreadLocal<String> messages = new ThreadLocal<String>();
	
	public static void setMessage(String s) {
		String m = "";
		m = m+"\n"+s; 
		log.info("Test Log : "+m);
		messages.set(m);
	}
	
	public static String getMessages(){
		return messages.get();
	}

	public static void setPassed(LinkedHashMap<String, String> stat) {
		LinkedHashMap<String, String> l = new LinkedHashMap<String, String>();
		l.putAll(stat);
		passed.set(l);
	}

	public static void setFailed(LinkedHashMap<String, String> stat) {
		LinkedHashMap<String, String> l = new LinkedHashMap<String, String>();
		l.putAll(stat);
		failed.set(l);
	}

	public static void setSkipped(LinkedHashMap<String, String> stat) {
		LinkedHashMap<String, String> l = new LinkedHashMap<String, String>();
		l.putAll(stat);
		skipped.set(l);
	}
	
	public static LinkedHashMap<String, String> getPassed() {
		return passed.get();
	}
	
	public static LinkedHashMap<String, String> getFailed() {
		return failed.get();
	}
	
	public static LinkedHashMap<String, String> getSkipped() {
		return skipped.get();
	}
	
	public static void setTestStats(String testName, String description) {
		LinkedHashMap<String, String> l = new LinkedHashMap<String, String>();
		l.put(testName,description);
		testStats.set(l);
	}
	
	public static LinkedHashMap<String, String> getTestStats() {
		return testStats.get();
	}
	
	public static Object[][] prepareData() {

		ReadExcelData excelData = new ReadExcelData(Constants.TEST_XLSPATH,
				Constants.TEST_SUITESHEET);
		HashMap<Integer, ArrayList<String>> keyset = new HashMap<Integer, ArrayList<String>>();
		keyset = excelData.getAllValues();
		Set<Entry<Integer, ArrayList<String>>> set = keyset.entrySet();
		String [][] store = new String [excelData.getRowNumber()][2];
		int startRow = 0;

		for (Entry<Integer, ArrayList<String>> me : set) {
			if(me.getKey()!=0) {
				store[startRow][0] = excelData.getCellValue(me.getKey(), Constants.TEST_NAME);
				store[startRow][1]= excelData.getCellValue(me.getKey(), Constants.TEST_DESCRIPTION);
				//testEnabled.add(excelData.getCellValue(me.getKey(), TEST_ENABLED));
				startRow++;
			}
		}
		return store;
	
	}
	
	public static HashMap<Integer,ArrayList<String>> getTestData(String sheet) {
		ReadExcelData excelData = new ReadExcelData(Constants.TEST_XLSPATH,
				sheet);
		return excelData.getAllValues();
	}

}