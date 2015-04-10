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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
/**
 * 
 * @author Vishshady
 *
 */
public class WebdriverManager {
	public static ThreadLocal<RemoteWebDriver> driverThread = new ThreadLocal<RemoteWebDriver>();
	public static String browserType;

	public synchronized static void startDriver() {
		RemoteWebDriver d = driverThread.get();
		Parameters p = new Parameters();
		if (d == null) {
			if (p.getTestType().equals("local")) {
				if (browserType.contains("firefox")) {
					d = new FirefoxDriver();
					driverThread.set(d);
				} else if (browserType.contains("googlechrome")) {
					System.setProperty("webdriver.chrome.driver",
							Constants.CHROME_DRIVERPATH);
					d = new ChromeDriver();
					driverThread.set(d);
				} else
					Assert.fail("No browsers specified");
			} else if (p.getTestType().equals("grid")) {
				DesiredCapabilities capabilities = DesiredCapabilities
						.firefox();
				capabilities.setBrowserName("firefox");
				capabilities = setPlatform(capabilities);
				try {
					d = new RemoteWebDriver(new URL(Constants.HUB_URL),
							capabilities);
					d.setFileDetector(new LocalFileDetector());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			d.get(p.getURL());
			d.manage().window().maximize();
			if (p.getImplicitWait() == 0)
				d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			else
				d.manage().timeouts()
						.implicitlyWait(p.getImplicitWait(), TimeUnit.SECONDS);
		}
		driverThread.set(d);
	}

	public static RemoteWebDriver getDriverInstance() {
		return driverThread.get();
	}

	public static void setupDriver(String browser) {
		browserType = browser;
	}

	public static void stopDriver() {
		if (!(WebdriverManager.getDriverInstance() == null)) {
			WebdriverManager.getDriverInstance().quit();
			driverThread.set(null);
		}
	}

	private static DesiredCapabilities setPlatform(DesiredCapabilities cap) {
		String platform = System.getProperty("os.name");
		if (platform.contains("mac"))
			cap.setPlatform(Platform.MAC);
		else if (platform.contains("windows"))
			cap.setPlatform(Platform.WINDOWS);
		else if (platform.contains("unix"))
			cap.setPlatform(Platform.UNIX);
		else if (platform.contains("linux"))
			cap.setPlatform(Platform.LINUX);
		return cap;
	}
}
