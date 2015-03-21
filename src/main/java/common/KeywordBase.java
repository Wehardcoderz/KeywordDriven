package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import common.Constants;
import common.Parameters;
import common.WebdriverManager;

public class KeywordBase implements Constants {
	Parameters parameters;
	private static Properties OR;
	private static Logger log = Logger.getLogger(KeywordBase.class);
	private static RemoteWebDriver d;
	private static WebDriverWait wait;

	/**
	 * @param d
	 */
	public KeywordBase(RemoteWebDriver d) {
		KeywordBase.d = d;
		parameters = new Parameters();
	}

	/**
	 * 
	 */
	private static void initOR() {
		String file = new File(OBJECT_REPOSITORY_PATH).getAbsolutePath();
		try {
			FileInputStream fis = new FileInputStream(file);
			OR = new Properties();
			OR.load(fis);
		} catch (IOException e1) {
			log.error("Error in initilizing the OR.properties file", e1);
		}
	}

	/**
	 * @param locator
	 * @return
	 */
	private static By getBy(String locator) {
		By by = null;
		String[] a = locator.split(">");
		String locType = a[0];
		String locValue = a[1];
		if (locType.equals("NAME")) {
			by = By.name(locValue);
		} else if (locType.equals("ID")) {
			by = By.id(locValue);
		} else if (locType.equals("XPATH")) {
			by = By.xpath(locValue);
		} else if (locType.equals("CSS")) {
			by = By.cssSelector(locValue);
		} else if (locType.equals("LINKTEXT")) {
			by = By.linkText(locValue);
		} else if (locType.equals("TEXT")) {
			// not implemented
		}
		log.info("Fetching element for :" + locType + " value :" + locValue);
		return by;
	}

	/**
	 * @param locator
	 * @param index
	 * @return
	 */
	private static WebElement getElement(String locator, String... index) {
		WebElement element = null;
		try {
			initOR();
			String loc = OR.getProperty(locator);
			By by = getBy(loc);
			if (index.length != 0)
				element = d.findElements(by).get(Integer.parseInt(index[0]));
			else
				element = d.findElement(by);
		} catch (NullPointerException n) {
			log.error("NullPointerException for : " + locator);
		}
		return element;
	}

	/**
	 * 
	 * @param locator
	 * @return
	 */
	public static By getElement(String locator) {
		By by = null;
		try {
			initOR();
			String loc = OR.getProperty(locator);
			by = getBy(loc);
		} catch (NullPointerException n) {
			log.error("NullPointerException for : " + locator);
		}
		return by;
	}

	/**
	 * @param browser
	 */
	private static void startDriver(String browser) {
		WebdriverManager.setupDriver(browser);
		log.info("Starting browser : " + browser);
		WebdriverManager.startDriver();
		d = WebdriverManager.getDriverInstance();
		wait = new WebDriverWait(d, WEBDRIVERWAIT_TIMEOUT);
	}

	/**
	 * @param locator
	 * @param index
	 */
	public static void click(String locator, String... index) {
		log.info("Clicking on " + locator);
		getElement(locator, index).click();
	}

	/**
	 * @param locator
	 * @param val
	 * @param index
	 */
	public static void type(String locator, String val, String... index) {
		log.info("Typing " + val);
		WebElement e = getElement(locator, index);
		e.clear();
		e.sendKeys(val);
	}

	/**
	 * @param locator
	 * @param index
	 */
	public static void clear(String locator, String... index) {
		getElement(locator, index).clear();
	}

	/**
	 * @param locator
	 * @param i
	 * @param index
	 */
	public static void selectIndex(String locator, int i, String... index) {
		log.info("Select by index " + locator);
		WebElement e = getElement(locator, index);
		Select select = new Select(e);
		select.selectByIndex(i);
	}

	/**
	 * @param locator
	 * @param value
	 * @param index
	 */
	public static void selectValue(String locator, String value,
			String... index) {
		log.info("Select by value " + locator + " value " + value);
		WebElement e = getElement(locator, index);
		Select select = new Select(e);
		select.selectByValue(value);
	}

	/**
	 * @param locator
	 * @param value
	 * @param index
	 */
	public static void selectText(String locator, String value, String... index) {
		log.info("Select by text " + locator + " value " + value);
		WebElement e = getElement(locator, index);
		Select select = new Select(e);
		select.selectByVisibleText(value);
	}

	/**
	 * 
	 */
	public static void stopDriver() {
		WebdriverManager.stopDriver();
	}

	/**
	 * @param value
	 */
	public static void executeScript(String value) {
		JavascriptExecutor js = (JavascriptExecutor) d;
		js.executeScript(value);
	}

	/********************* Custom Waits ************************/

	/**
	 * @param locator
	 * @param time
	 * @param index
	 */
	public static void waitForElementEnabled(final String locator, String time,
			final String... index) {
		log.info("customWaitForElementEnabled for time " + time);
		(new WebDriverWait(d, Integer.parseInt(time)))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return (getElement(locator, index)).isEnabled();
					}
				});
	}

	/**
	 * @param locator
	 * @param index
	 */
	public static void waitForElementEnabled(final String locator,
			final String... index) {
		log.info("customWaitForElementEnabled for time "
				+ WEBDRIVERWAIT_TIMEOUT);
		(new WebDriverWait(d, WEBDRIVERWAIT_TIMEOUT))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return (getElement(locator, index)).isEnabled();
					}
				});
	}

	/**
	 * @param locator
	 * @param time
	 * @param index
	 */
	public static void waitForElementBy(final String locator, String time,
			final String... index) {
		log.info("waitForElement for time " + time);
		(new WebDriverWait(d, Integer.parseInt(time)))
				.until(new ExpectedCondition<WebElement>() {
					public WebElement apply(WebDriver d) {
						return getElement(locator, index);
					}
				});
	}

	/**
	 * @param locator
	 * @param index
	 */
	public static void waitForElement(final String locator,
			final String... index) {
		log.info("waitForElement for time " + WEBDRIVERWAIT_TIMEOUT);
		(new WebDriverWait(d, WEBDRIVERWAIT_TIMEOUT))
				.until(new ExpectedCondition<WebElement>() {
					public WebElement apply(WebDriver d) {
						return getElement(locator, index);
					}
				});

	}

	/**
	 * @param locator
	 * @param time
	 * @param index
	 */
	public static void waitForElementDisplayedBy(final String locator,
			String time, final String... index) {
		log.info("waitForElementByDisplayed " + time);
		(new WebDriverWait(d, Integer.parseInt(time)))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return getElement(locator, index).isDisplayed();
					}
				});
	}

	/**
	 * @param locator
	 * @param index
	 */
	public static void waitForElementDisplayed(final String locator,
			final String... index) {
		log.info("waitForElementByDisplayed " + WEBDRIVERWAIT_TIMEOUT);
		(new WebDriverWait(d, WEBDRIVERWAIT_TIMEOUT))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return getElement(locator, index).isDisplayed();
					}
				});
	}

	/**
	 * @param title
	 */
	public static void waitForElementByTitle(String title) {
		wait.until(ExpectedConditions.titleContains(title));
	}

	/**
	 * 
	 * @param locator
	 */
	public static void waitForElementInvisible(final String locator) {
		wait.until(ExpectedConditions
				.invisibilityOfElementLocated(getElement(locator)));
	}

	/********************* Assert Statements ************************/
	/**
	 * @param s
	 * @return
	 */
	public static String[] splitValueMsg(String s) {
		String[] m = s.split("\\|");
		return m;
	}

	/**
	 * @param locator
	 * @param value
	 * @param index
	 */
	public static void assertText(String locator, String value, String... index) {
		log.info("Assert by Text " + locator + " value " + value);
		if (splitValueMsg(value).length == 1)
			Assert.assertEquals(getElement(locator, index).getText(),
					splitValueMsg(value)[0]);
		else
			Assert.assertEquals(getElement(locator, index).getText(),
					splitValueMsg(value)[0], splitValueMsg(value)[1]);
	}

	/**
	 * @param locator
	 * @param value
	 * @param index
	 */
	public static void assertElementPresent(String locator, String value,
			String... index) {
		log.info("Assert by element " + locator + " value " + value);
		if (splitValueMsg(value).length == 1)
			Assert.assertEquals(getElement(locator, index).isDisplayed(),
					Boolean.parseBoolean(splitValueMsg(value)[0]));
		else
			Assert.assertEquals(getElement(locator, index).isDisplayed(),
					Boolean.parseBoolean(splitValueMsg(value)[0]),
					splitValueMsg(value)[1]);
	}

	/********************** JSOUP Functions under construction ******************************/

}
