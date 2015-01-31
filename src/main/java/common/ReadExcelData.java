package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import keywordutilities.TestSuiteDriver;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.testng.Assert;

public class ReadExcelData {
	private static Logger log = Logger.getLogger(ReadExcelData.class);
	FileInputStream fileInputStream = null;
	HSSFWorkbook workbook;
	HSSFSheet sheet;
	Row row;
	Cell cell;
	public String sheetName;


	public ReadExcelData(String path, String sheetName) {
		log.info("started");
		this.sheetName = sheetName;
		try {
			fileInputStream = new FileInputStream(path);
			workbook = new HSSFWorkbook(fileInputStream);
		} catch (IOException e) {
			System.out.println("File not found!!" + e);
		}
	}
	
	public int getRowNumber() {
		int num=0;
		try {
			num =workbook.getSheet(sheetName).getLastRowNum();
		}catch(Exception e) {
			log.error("Sheet not exist",e);
		}
		return num;
	}

	public HashMap<Integer, ArrayList<String>> getAllValues() {
		HashMap<Integer, ArrayList<String>> readValue = new HashMap<Integer, ArrayList<String>>();
		ArrayList<String> data = new ArrayList<String>();
		try {

			sheet = workbook.getSheet(sheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			int count = 0;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.iterator();
				while (cellIterator.hasNext()) {
					cell = cellIterator.next();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						data.add(String.valueOf(((long) cell
								.getNumericCellValue())));
						break;
					case Cell.CELL_TYPE_STRING:
						data.add(cell.getStringCellValue());
						break;
					}
					if(cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK)
						data.add(null);
				}
				readValue.put(count++, new ArrayList<String>(data));
				data.clear();
			}
		} catch (NullPointerException e) {
			log.error(e);
		}
		return readValue;
	}

	public Integer findRow(HSSFSheet sheet, int cellContent) {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					Double d = cell.getNumericCellValue();
					if (d.intValue() == cellContent) {
						return row.getRowNum();
					}
				}
			}
		}
		return null;
	}

	public HashMap<Integer, ArrayList<String>> getValues() {
		HashMap<Integer, ArrayList<String>> readValue = new HashMap<Integer, ArrayList<String>>();
		String value = "";
		ArrayList<String> data = new ArrayList<String>();
		try {
			sheet = workbook.getSheet("Data");
			for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
				row = sheet.getRow(i);
				for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
					cell = row.getCell(j);
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						long l = (long) cell.getNumericCellValue();
						value = String.valueOf((l));
						data.add(value);
						break;
					case Cell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						data.add(value);
						break;
					}
				}
				readValue.put(i, new ArrayList<String>(data));
				data.clear();
			}
		} catch (NullPointerException e) {
			System.out.println(e);
		}
		return readValue;
	}

	public String getCellValue(int index, String heading) {
		String cellValue = "";
		try {
			sheet = workbook.getSheet(sheetName);
			row = sheet.getRow(0);
			int cellNumber = 0;
			for (Cell cell : row) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (cell.getRichStringCellValue().getString().trim()
							.equals(heading)) {
						cellNumber = cell.getColumnIndex();
					}
				}
			}
			row = sheet.getRow(findRow(sheet, index));
			cell = row.getCell(cellNumber);
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				cellValue = String.valueOf(((long) cell.getNumericCellValue()));
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BLANK:
				cellValue = null;
			}
		} catch (NullPointerException e) {
			cellValue = null;
		}
		return cellValue;
	}

	public static void main(String[] args) throws IOException {
		ReadExcelData read = new ReadExcelData("TestCase.xls","");
		HashMap<Integer, ArrayList<String>> keyset = new HashMap<Integer, ArrayList<String>>();
		keyset = read.getAllValues();
		Set<Entry<Integer, ArrayList<String>>> set = keyset.entrySet();

		for (Entry<Integer, ArrayList<String>> me : set)
			System.out.println(me.getKey() + " " + me.getValue());

		System.out.println(read.getCellValue( 1, "Command"));
	}

}
