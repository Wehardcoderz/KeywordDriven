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
package driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import common.Constants;
import common.Parameters;

/**
 * 
 * @author Vishshady
 *
 */
public class SuiteDriver implements Constants {
	private static Logger log = Logger.getLogger(SuiteDriver.class);

	public static void main(String[] args) throws IOException {
		Parameters p = new Parameters();
		TestNG tng = new TestNG();
		XmlSuite suite = new XmlSuite();
		log.info("Setting suite name " + p.getSuiteName());
		suite.setName(p.getSuiteName());
		if (p.getThreadCount() == 0) {
			log.info("Setting thread count to 1");
			suite.setDataProviderThreadCount(1);
		} else {
			log.info("Setting thread count to " + p.getThreadCount());
			suite.setParallel("methods");
			suite.setDataProviderThreadCount(p.getThreadCount());
		}
		XmlTest test = new XmlTest(suite);
		test.setName("Default Test");
		List<XmlClass> classes = new ArrayList<XmlClass>();
		classes.add(new XmlClass("driver.TestDriver"));
		test.setXmlClasses(classes);
		List<XmlTest> tests = new ArrayList<XmlTest>();
		tests.add(test);
		suite.setTests(tests);
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);
		TestListenerAdapter tla = new TestListenerAdapter();
		tng.setXmlSuites(suites);
		tng.addListener(tla);
		tng.run();

	}

}
