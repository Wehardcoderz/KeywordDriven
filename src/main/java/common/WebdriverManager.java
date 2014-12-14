package common;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebdriverManager {
	public static ThreadLocal<RemoteWebDriver> driverThread = new ThreadLocal<RemoteWebDriver>();
	public static String browserType;

	public static RemoteWebDriver getDriverInstance() {
		RemoteWebDriver d = driverThread.get();
		if (d == null) {
			if (browserType.contains("firefox")) {
				d = new FirefoxDriver();
				driverThread.set(d);
			} else if (browserType.contains("googlechrome")) {
				System.setProperty("webdriver.chrome.driver",
						"D:/Selenium/chromedriver.exe");
			d = new ChromeDriver();
			driverThread.set(d);
		} else
			Assert.fail("No browsers specified");
		d.get("https://www.google.co.in/");
		d.manage().window().maximize();
		d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		}
		return d;
	}
	
	public static void setupDriver(String browser){
		browserType=browser;
		}	

	public static void stopDriver() {
		WebdriverManager.getDriverInstance().close();
		WebdriverManager.getDriverInstance().quit();
		 driverThread.set(null);
	}
}
