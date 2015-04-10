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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
/**
 * 
 * @author Vishshady
 *
 */
public class ReadExcelData {
	private static Logger log = Logger.getLogger(ReadExcelData.class);
	FileInputStream fileInputStream = null;
	HSSFWorkbook workbook;
	HSSFSheet sheet;
	Row row;
	Cell cell;
	public String sheetName;

	public ReadExcelData(Parameters p, String sheetName) {
		this.sheetName = sheetName;
		try {
			fileInputStream = new FileInputStream(p.getTestcasePath());
			workbook = new HSSFWorkbook(fileInputStream);
		} catch (IOException e) {
			log.error("File not found!!" + e);
			System.exit(1);
		}
	}
	
	public ReadExcelData(Parameters p) {
		try {
			fileInputStream = new FileInputStream(p.getTestcasePath());
			workbook = new HSSFWorkbook(fileInputStream);
		} catch (IOException e) {
			log.error("File not found!!" + e);
			System.exit(1);
		}
	}
	
	public String setSheet(int i){
		this.sheetName = workbook.getSheetName(i);
		return this.sheetName;
	}
	
	public Boolean isSheetPresent(int index,String sheetName) {
		boolean present = false;
		present = workbook.getSheetAt(index).getSheetName().equalsIgnoreCase(sheetName);
		return present;
	}
	

	public int getRowNumber() {
		int num = 0;
		try {
			num = workbook.getSheet(sheetName).getLastRowNum();
		} catch (Exception e) {
			log.error("Sheet not exist", e);
		}
		return num;
	}
	
	public int getSheetCount() {
		return workbook.getNumberOfSheets();
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
				if (!isRowEmpty(row)) {
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
						case Cell.CELL_TYPE_BOOLEAN:
							data.add(String.valueOf(cell.getBooleanCellValue()));
							break;
						}
						if (cell == null
								|| cell.getCellType() == Cell.CELL_TYPE_BLANK)
							data.add(null);
					}
					readValue.put(count++, new ArrayList<String>(data));
					data.clear();
				}
			}
		} catch (NullPointerException e) {
			log.error(e);
		}
		return readValue;
	}

	public static boolean isRowEmpty(Row row) {
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
				return false;
		}
		return true;
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
	
	public ArrayList<String> getColumnValue(String sheetName, String header) {
		HSSFSheet sheet = workbook.getSheet(sheetName);
		ArrayList<String> list = new ArrayList<String>();
		int index = 0;
		for(Row r : sheet) {
			for(Cell c : r) {
				if (c.getCellType() != Cell.CELL_TYPE_NUMERIC)
				if(c.getStringCellValue().equals(header))
					index = c.getColumnIndex();
			}
			list.add(r.getCell(index).getStringCellValue());
		}
		return list;
	}
	
	public ArrayList<String> getRowValue(String sheetName, int row) {
		HSSFSheet sheet = workbook.getSheet(sheetName);
		ArrayList<String> list = new ArrayList<String>();
		Row r = sheet.getRow(row);
		for(Cell c: r) {
			if(c.getCellType()==Cell.CELL_TYPE_STRING)
				list.add(c.getStringCellValue());
			else if (c.getCellType()==Cell.CELL_TYPE_NUMERIC)
				list.add(String.valueOf(c.getNumericCellValue()));
		}
		return list;
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

//	public static void main(String[] args) throws IOException {
//		Parameters p = new Parameters();
//		ReadExcelData read = new ReadExcelData(p, "TestSuite");
//	
//		MyTestContext.checkExcel();
//	
//	}

}
