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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

public class k implements IReporter{

	public static void main(String[] args) throws InterruptedException {
//		int start = (int) System.currentTimeMillis();
//		System.out.println("Start "+start);
//		Thread.sleep(5000);
//		int end = (int) System.currentTimeMillis();
//		System.out.println("End "+end);
//		int time = (end-start)/1000;
//		System.out.println("Time "+time);
//		int secs = time % 60;
//		int totalMins = time/60;
//		int mins = totalMins % 60;
//		int hrs = totalMins/60;
//		System.out.println(hrs+"H : "+mins+"M : "+secs+"S");
		
		System.out.println(System.getProperty("os.name"));
		
		TimeZone ind= TimeZone.getTimeZone("IST");
		TimeZone la = TimeZone.getTimeZone("America/Los_Angeles");
		DateFormat d = new SimpleDateFormat("hh:mm a");
		//Calendar c = new GregorianCalendar();
		d.setTimeZone(ind);
		Date date = new Date();
		System.out.println("Hours in India "+d.format(date));
		
		//c.setTimeZone(la);
		d.setTimeZone(la);
		 date = new Date();
		System.out.println("Hours in LA "+d.format(date));
		
		System.out.println( new Exception().getMessage());
//		
//		k k1 = new k();
//		Method m[];
//		try {
//			String[] s = new String[]{"ok"};
//			m = k1.getClass().getMethods();
//			for (int i = 0; i < m.length; i++) {
//			System.out.println(m[i].getName());
//			if(m[i].getName().equals("a"))
//				System.out.println(m[i].getParameterTypes().length);
//			}
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
	}
	
	public static void a(String... index) {
	System.out.println(index);
		
	}

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {
		// TODO Auto-generated method stub
		
	}

}
