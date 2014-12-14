package keywordutilities;

import org.apache.log4j.Logger;

import common.ReadExcelData;

public class DriverUtilities {
	private static Logger log = Logger.getLogger(DriverUtilities.class);
	ReadExcelData excelData;
	public DriverUtilities() {
		excelData = new ReadExcelData("TestCase.xlxs");
	}
	

	
}
