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

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * @author Vishshady
 *
 */
public class Parameters {
	private String reportTitle = null;
	private String suiteName = null;
	private String browsers = null;
	private String testcasePath = null;
	private int threadCount = 0;
	private int explicitWait = 0;
	private int implicitWait = 0;
	private String URL;
	private String hub = null;
	private String testType;
	private static Logger log = Logger.getLogger(Parameters.class);

	public Parameters() {

		PropertiesConfiguration config;
		File file = new File(Constants.LOG4J_PATH);
		if (!file.getAbsoluteFile().exists()) {
			log.error("Invalid log properties file!");
			System.exit(1);
		}
		file = new File(Constants.OBJECT_REPOSITORY_PATH);
		if (!file.getAbsoluteFile().exists()) {
			log.error("Invalid Object properties file!");
			System.exit(1);
		}
		// BasicConfigurator.configure();
		PropertyConfigurator.configure(Constants.LOG4J_PATH);

		try {
			config = new PropertiesConfiguration(Constants.CONFIG_PATH);
			config.getSubstitutor().setEnableSubstitutionInVariables(true);
			setConfig(config);
			checkConfig();
		} catch (ConfigurationException e) {
			log.error("Unable to load config. Config file possibly missing."
					+ e);
			System.exit(1);
		}

	}

	void setConfig(PropertiesConfiguration c) {
		setReportTitle(c.getString("reportTitle"));
		setSuiteName(c.getString("suiteName"));
		setBrowsers(c.getString("browsers"));
		setTestcasePath(c.getString("testcasePath"));
		setThreadCount(c.getInt("threadCount"));
		setExplicitWait(c.getInt("expWait"));
		setImplicitWait(c.getInt("impWait"));
		setURL(c.getString("URL"));
		setTestType(c.getString("testType"));
		setHub(c.getString("hub"));
	}

	void checkConfig() {
		verifyNotNull(reportTitle,
				"reportTitle is null in config.properties file");
		verifyNotNull(suiteName, "suiteName is null in config.properties file");
		verifyNotNull(browsers, "browsers is null in config.properties file");
		verifyNotNull(testcasePath,
				"testcasePath is null in config.properties file");
		verifyNotNull(URL, "URL is null in config.properties file");
		verifyNotNull(testType, "testType is null in config.properties file");
		if (!(browsers.equalsIgnoreCase("googlechrome")
				|| browsers.equalsIgnoreCase("firefox")
				|| browsers.equalsIgnoreCase("internetexplorer") || browsers
					.equalsIgnoreCase("safari")))
			log.error("Any of the browser name should be specified : firefox/googlechrome/internetexplorer/safari");
	}

	void verifyNotNull(Object ob, String message) {
		if (ob == null) {
			log.error(message);
			System.exit(1);
		} else if (ob.equals("")) {
			log.error(message);
			System.exit(1);
		}

	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getBrowsers() {
		return browsers;
	}

	public void setBrowsers(String browsers) {
		this.browsers = browsers;
	}

	public String getTestcasePath() {
		return testcasePath;
	}

	public void setTestcasePath(String testcasePath) {
		this.testcasePath = testcasePath;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public int getExplicitWait() {
		return explicitWait;
	}

	public void setExplicitWait(int explicitWait) {
		this.explicitWait = explicitWait;
	}

	public int getImplicitWait() {
		return implicitWait;
	}

	public void setImplicitWait(int implicitWait) {
		this.implicitWait = implicitWait;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getHub() {
		return hub;
	}

	public void setHub(String hub) {
		this.hub = hub;
	}

}
