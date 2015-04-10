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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelTest {
	static HSSFSheet sheet = null;
	static HSSFWorkbook workbook = null;
	static int row;
	
	public static void main(String[] args) {
		createSheet("Result");
	addRow("one value");
	addRow("a","b","c","go","on","common");
	addRow("a","b","c");
	addRow("okay");
	writeFile();
	System.out.println("Done");
	}
	
	public static void createSheet(String s) {
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet(s);
	}
	
	public static void addRow(String ...value) {
		Row r = sheet.createRow(row++);
		int cellnum = 0;
		for(String s :value) {
			Cell cell = r.createCell(cellnum++);
           cell.setCellValue(s);
		}
	}
	
	public static void writeFile() {
		try {
			FileOutputStream f = new FileOutputStream(new File("doc.xls"));
			workbook.write(f);
			f.close();
			f.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
