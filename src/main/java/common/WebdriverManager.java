package common;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebdriverManager {
	public static ThreadLocal<RemoteWebDriver> driverThread = new ThreadLocal<RemoteWebDriver>();
	public static String browserType = "firefox";

	public static void startDriver() {
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
		d.get("https://developer.vuforia.com/");
		d.manage().window().maximize();
		d.manage().timeouts().implicitlyWait(Constants.IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
		driverThread.set(d);
	}
	
	public static RemoteWebDriver getDriverInstance() {
		return driverThread.get();
	}
	
	public static void setupDriver(String browser){
		browserType=browser;
		}	

	public static void stopDriver() {
		if(!(WebdriverManager.getDriverInstance()==null)) {
		WebdriverManager.getDriverInstance().quit();
		 driverThread.set(null);
		}
	}
}
