package keywordutilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import common.Constants;
import common.ReadExcelData;

public class TestSuiteDriver implements Constants{
	private static Logger log = Logger.getLogger(TestSuiteDriver.class);

	public static void main(String[] args) throws IOException {
		
	}

	public void prepareData() {

		ReadExcelData excelData = new ReadExcelData(TEST_XLSPATH,
				TEST_SUITESHEET);
		LinkedList<String> testName = new LinkedList<String>();
		LinkedList<String> testDescription = new LinkedList<String>();
		LinkedList<String> testEnabled = new LinkedList<String>();
		HashMap<Integer, ArrayList<String>> keyset = new HashMap<Integer, ArrayList<String>>();
		keyset = excelData.getAllValues();
		Set<Entry<Integer, ArrayList<String>>> set = keyset.entrySet();

		for (Entry<Integer, ArrayList<String>> me : set) {
			if(me.getKey()!=0) {
				testName.add(excelData.getCellValue(me.getKey(), TEST_NAME));
				testDescription.add(excelData.getCellValue(me.getKey(), TEST_DESCRIPTION));
				testEnabled.add(excelData.getCellValue(me.getKey(), TEST_ENABLED));
			}
		}
		System.out.println(testName+"\n"+testDescription+"\n"+testEnabled);
	
	}
}
