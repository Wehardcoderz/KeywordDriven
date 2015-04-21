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

import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;

/**
 * 
 * @author Vishshady
 *
 */
public class WebdriverManager {
	private static ThreadLocal<RemoteWebDriver> driverThread = new ThreadLocal<RemoteWebDriver>();
	private static String browserType;
	private static Parameters p = new Parameters();

	public synchronized static void startDriver() {
		RemoteWebDriver d = driverThread.get();
		if (d == null) {
			if (p.getTestType().equals("local")) {

				switch (Browser.valueOf(browserType.toUpperCase())) {
				case FIREFOX:
					FirefoxProfile fp = new FirefoxProfile();
					fp.setPreference("browser.privatebrowsing.autostart", true);
					d = new FirefoxDriver(fp);
					break;
				case GOOGLECHROME:
					if (System.getProperty("os.name").contains("windows"))
						System.setProperty("webdriver.chrome.driver",
								Constants.CHROME_DRIVERPATH + ".exe");
					else
						System.setProperty("webdriver.chrome.driver",
								Constants.CHROME_DRIVERPATH);
					ChromeOptions ch = new ChromeOptions();
					ch.addArguments("--incognito");
					d = new ChromeDriver(ch);
					break;
				case INTERNETEXPLORER:
					System.setProperty("webdriver.ie.driver",
							Constants.IE_DRIVERPATH);
					d = new InternetExplorerDriver();
					break;
				case SAFARI:
					Assert.assertTrue(isSupportedPlatform());
					d = new SafariDriver();
					break;
				default:
					Assert.fail("No browsers specified");
					break;
				}

			} else if (p.getTestType().equals("grid")) {
				DesiredCapabilities capabilities = null;

				switch (Browser.valueOf(browserType.toUpperCase())) {
				case FIREFOX:
					capabilities = DesiredCapabilities.firefox();
					capabilities.setBrowserName("firefox");
					FirefoxProfile fp = new FirefoxProfile();
					fp.setPreference("browser.privatebrowsing.autostart", true);
					capabilities.setCapability(FirefoxDriver.PROFILE, fp);
					break;
				case GOOGLECHROME:
					if (System.getProperty("os.name").contains("windows"))
						System.setProperty("webdriver.chrome.driver",
								Constants.CHROME_DRIVERPATH + ".exe");
					else
						System.setProperty("webdriver.chrome.driver",
								Constants.CHROME_DRIVERPATH);
					capabilities = DesiredCapabilities.chrome();
					capabilities.setBrowserName("googlechrome");
					ChromeOptions ch = new ChromeOptions();
					ch.addArguments("--incognito");
					capabilities.setCapability(ChromeOptions.CAPABILITY, ch);
					break;
				case INTERNETEXPLORER:
					System.setProperty("webdriver.ie.driver",
							Constants.IE_DRIVERPATH);
					capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setBrowserName("internetexplorer");
					break;
				case SAFARI:
					Assert.assertTrue(isSupportedPlatform());
					capabilities = DesiredCapabilities.safari();
					capabilities.setBrowserName("safari");
					break;
				default:
					Assert.fail("No browsers specified");
					break;
				}

				capabilities = setPlatform(capabilities);
				try {
					d = new RemoteWebDriver(new URL(p.getHub()), capabilities);
					d.setFileDetector(new LocalFileDetector());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		driverThread.set(d);
	}

	private static boolean isSupportedPlatform() {
		Platform current = Platform.getCurrent();
		return Platform.MAC.is(current) || Platform.WINDOWS.is(current);
	}

	public static void loadHomePage() {
		RemoteWebDriver d = driverThread.get();
		d.get(p.getURL());
		d.manage().window().maximize();
		if (p.getImplicitWait() == 0)
			d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		else
			d.manage().timeouts()
					.implicitlyWait(p.getImplicitWait(), TimeUnit.SECONDS);
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

	enum Browser {
		FIREFOX, GOOGLECHROME, SAFARI, INTERNETEXPLORER
	}
}
