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
	public static WebDriverWait wait;

	/**
	 * @param d
	 */
	public KeywordBase(RemoteWebDriver d) {
		KeywordBase.d = d;
		parameters = new Parameters();
		wait = new WebDriverWait(d, WEBDRIVERWAIT_TIMEOUT);
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
		initOR();
		String loc = OR.getProperty(locator);
		WebElement element = null;
		By by = getBy(loc);
		if (index.length != 0)
			element = d.findElements(by).get(Integer.parseInt(index[0]));
		else
			element = d.findElement(by);
		return element;
	}

	/**
	 * @param browser
	 */
	private static void startDriver(String browser) {
		WebdriverManager.setupDriver(browser);
		log.info("Starting browser : " + browser);
		WebdriverManager.getDriverInstance();
	}

	/**
	 * @param s
	 * @param index
	 */
	public static void click(String s, String... index) {
		log.info("Clicking on " + s);
		getElement(s, index).click();
	}

	/**
	 * @param locator
	 * @param val
	 * @param index
	 */
	private static void type(String locator, String val, String... index) {
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
	private static void stopDriver() {
		WebdriverManager.stopDriver();
	}

	/**
	 * @param locator
	 * @param index
	 * @return
	 */
	public static String getText(String locator, String... index) {
		log.info("Get text " + locator);
		return getElement(locator, index).getText();
	}

	/**
	 * @param value
	 */
	public void executeScript(String value) {
		JavascriptExecutor js = (JavascriptExecutor) d;
		js.executeScript(value);
	}

	/********************* Custom Waits ************************/

	/**
	 * @param locator
	 * @param value
	 * @param index
	 */
	public static void waitForElementByElement(String locator, String value,
			String... index) {
		wait.until(ExpectedConditions.visibilityOf(getElement(locator, index)));
	}

	/**
	 * @param locator
	 * @param value
	 * @param index
	 */
	public void customWaitForElementEnabled(final String locator, String value,
			final String... index) {
		log.info("customWaitForElementEnabled for time " + value);
		(new WebDriverWait(d, Integer.parseInt(value)))
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
	public void waitForElementEnabled(final String locator,
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
	 * @param value
	 * @param index
	 */
	public void waitForElement(final String locator, String value,
			final String... index) {
		log.info("waitForElement for time " + value);
		(new WebDriverWait(d, Integer.parseInt(value)))
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
	public void waitForElement(final String locator, final String... index) {
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
	 * @param value
	 * @param index
	 */
	public void waitForElementByDisplayed(final String locator, String value,
			final String... index) {
		log.info("waitForElementByDisplayed " + value);
		(new WebDriverWait(d, Integer.parseInt(value)))
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
	public void waitForElementByDisplayed(final String locator,
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
	public void waitForElementByTitle(String title) {
		wait.until(ExpectedConditions.titleContains(title));
	}

	/********************* Assert Statements ************************/
	/**
	 * @param s
	 * @return
	 */
	public static String[] splitValueMsg(String s) {
		return s.split("|");
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
			Assert.assertEquals(getElement(locator, index).getText(),
					Boolean.parseBoolean(splitValueMsg(value)[0]),
					splitValueMsg(value)[1]);
	}

	public static void main(String[] args) throws IOException {
		startDriver("firefox");
		type("searchBox", "hehe");
		stopDriver();
	}

	/********************** JSOUP Functions under construction ******************************/

	// private Document getSource() {
	// return Jsoup.parse(d.getPageSource());
	// }
	//
	// /**
	// * Get WebElement id by text
	// *
	// * @param key
	// * - Text name
	// * @param index
	// * - Element index. Default 0 (optional)
	// * @return WebElement
	// */
	// public WebElement getElementId(String key, int... index) {
	// String id = null;
	// String text = null;
	// int i = 0;
	// try {
	// if (index.length == 0) {
	// while (true) {
	// text = getSource().getElementsContainingOwnText(key).get(i)
	// .ownText();
	// if (text.equals(key.toString())) {
	// id = getSource().getElementsContainingOwnText(key)
	// .get(i).id();
	// break;
	// }
	// i++;
	// }
	// } else
	// id = getElementEqualsText(key, index[0]).id();
	// if (id.isEmpty()) {
	// Assert.fail("Element is changed or not found for : " + key);
	// }
	// } catch (java.lang.NullPointerException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// } catch (java.lang.IndexOutOfBoundsException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// }
	// return d.findElement(By.id(id));
	// }
	//
	// /**
	// * Get next WebElement id by text
	// *
	// * @param key
	// * - Text name
	// * @param index
	// * - Next element sibling index
	// * @param child
	// * - Child element index if any (optional)
	// * @return WebElement
	// */
	// public WebElement getNextElementId(String key, int index, int... child) {
	// String id = null;
	// Element next = null;
	// try {
	// next = getElementEqualsText(key);
	// for (int j = 0; j <= index; j++) {
	// if (next.nextElementSibling() == null)
	// next = next.parent().nextElementSibling();
	// else
	// next = next.nextElementSibling();
	// if (j == index)
	// id = next.id();
	// }
	// if (id != null && child.length != 0) {
	// id = next.children().get(child[0]).id();
	// }
	// if (id.isEmpty()) {
	// Assert.fail("Element is changed or not found for : " + key);
	// }
	// } catch (java.lang.NullPointerException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// } catch (java.lang.IndexOutOfBoundsException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// }
	// return d.findElement(By.id(id));
	// }
	//
	// /**
	// * Get previous WebElement id by text
	// *
	// * @param key
	// * - Text name
	// * @param index
	// * - Next element sibling index
	// * @param child
	// * - Child element index if any (optional)
	// * @return WebElement
	// */
	// public WebElement getPreviousElementId(String key, int index, int...
	// child) {
	// String id = null;
	// Element next = null;
	// try {
	// next = getElementEqualsText(key);
	// for (int j = 0; j <= index; j++) {
	// if (next.nextElementSibling() == null)
	// next = next.parent().previousElementSibling();
	// else
	// next = next.previousElementSibling();
	// if (j == index)
	// id = next.id();
	// }
	// if (id != null && child.length != 0) {
	// id = next.children().get(child[0]).id();
	// }
	// if (id.isEmpty()) {
	// Assert.fail("Element is changed or not found for : " + key);
	// }
	// } catch (java.lang.NullPointerException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// } catch (java.lang.IndexOutOfBoundsException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// }
	// return d.findElement(By.id(id));
	// }
	//
	// /**
	// * Get WebElement css selector by text
	// *
	// * @param key
	// * - Text name
	// * @param index
	// * - Element index. Default 0 (optional)
	// * @return WebElement
	// */
	// public WebElement getElementCssSelector(String key, int... index) {
	// String css = "";
	// String text = "";
	// int i = 0;
	// try {
	// if (index.length == 0) {
	// while (true) {
	// text = getSource().getElementsContainingOwnText(key).get(i)
	// .ownText();
	// if (text.equals(key.toString())) {
	// css = getSource().getElementsContainingOwnText(key)
	// .get(i).cssSelector();
	// break;
	// }
	// i++;
	// }
	// } else
	// css = getElementEqualsText(key, index[0]).cssSelector();
	// if (css.isEmpty()) {
	// Assert.fail("Element is changed or not found for : " + key);
	// }
	// } catch (java.lang.NullPointerException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// } catch (java.lang.IndexOutOfBoundsException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// }
	// return d.findElement(By.cssSelector(css));
	// }
	//
	// /**
	// * Get next WebElement css selector by text
	// *
	// * @param key
	// * - text name
	// * @param index
	// * - Next element sibling index
	// * @param child
	// * - Child element index if any (optional)
	// * @return WebElement
	// */
	// public WebElement getNextElementCssSelector(String key, int index,
	// int... child) {
	// String css = null;
	// Element next = null;
	// try {
	//
	// next = getElementEqualsText(key);
	// for (int j = 0; j <= index; j++) {
	// if (next.nextElementSibling() == null)
	// next = next.parent().nextElementSibling();
	// else
	// next = next.nextElementSibling();
	// if (j == index)
	// css = next.cssSelector();
	// }
	// if (css != null && child.length != 0) {
	// css = next.children().get(child[0]).cssSelector();
	// }
	// if (css.isEmpty()) {
	// Assert.fail("Element is changed or not found for : " + key);
	// }
	// } catch (java.lang.NullPointerException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// } catch (java.lang.IndexOutOfBoundsException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// }
	// return d.findElement(By.cssSelector(css));
	// }
	//
	// /**
	// * Get next WebElement css selector string by text
	// *
	// * @param key
	// * - text name
	// * @param index
	// * - Next element sibling index
	// * @param child
	// * - Child element index if any (optional)
	// * @return String
	// */
	// public String getNextCssSelector(String key, int index, int... child) {
	// String css = null;
	// Element next = null;
	// try {
	//
	// next = getElementEqualsText(key);
	// for (int j = 0; j <= index; j++) {
	// if (next.nextElementSibling() == null)
	// next = next.parent().nextElementSibling();
	// else
	// next = next.nextElementSibling();
	// if (j == index)
	// css = next.cssSelector();
	// }
	// if (css != null && child.length != 0) {
	// css = next.children().get(child[0]).cssSelector();
	// }
	// if (css.isEmpty()) {
	// Assert.fail("Element is changed or not found for : " + key);
	// }
	// } catch (java.lang.NullPointerException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// } catch (java.lang.IndexOutOfBoundsException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// }
	// return css;
	// }
	//
	// /**
	// * Get next WebElements css selector by text. Can select element matching
	// * the same criteria.
	// *
	// * @param key
	// * - text name
	// * @param index
	// * - Next element sibling index
	// * @param elements
	// * - index of the WebElements
	// * @param child
	// * - Child element index if any (optional)
	// * @return WebElement
	// */
	// public WebElement getNextElementsCssSelector(String key, int index,
	// int elements, int... child) {
	// String css = null;
	// Element next = null;
	// try {
	//
	// next = getElementEqualsText(key, elements);
	// for (int j = 0; j <= index; j++) {
	// if (next.nextElementSibling() == null)
	// next = next.parent().nextElementSibling();
	// else
	// next = next.nextElementSibling();
	// if (j == index)
	// css = next.cssSelector();
	// }
	// if (css != null && child.length != 0) {
	// css = next.children().get(child[0]).cssSelector();
	// }
	// if (css.isEmpty()) {
	// Assert.fail("Element is changed or not found for : " + key);
	// }
	// } catch (java.lang.NullPointerException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// } catch (java.lang.IndexOutOfBoundsException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// }
	// return d.findElement(By.cssSelector(css));
	// }
	//
	// private Element getElementEqualsText(String key, int... i) {
	// String text = null;
	// int index = 0;
	// Element next = null;
	// try {
	// while (true) {
	// text = getSource().getElementsContainingOwnText(key).get(index)
	// .ownText().trim();
	// if (text.equals(key.toString())) {
	// next = getSource().getElementsContainingOwnText(key).get(
	// index);
	// break;
	// }
	// index++;
	// }
	// if (i.length != 0) {
	// next = getSource().getElementsContainingOwnText(key).get(
	// index + i[0]);
	// }
	// } catch (java.lang.IndexOutOfBoundsException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// }
	// return next;
	// }
	//
	// /**
	// * Get WebElement ID by value
	// *
	// * @param key
	// * - text
	// * @param index
	// * - index (optional)
	// * @return
	// */
	// public WebElement getElementIdByValue(String key, int... index) {
	// String id = null;
	// try {
	// id = getSource().getElementsByAttributeValue("value", key).first()
	// .id();
	// if (index.length != 0) {
	// id = getSource().getElementsByAttributeValue("value", key)
	// .get(index[0]).id();
	// }
	// } catch (java.lang.NullPointerException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// } catch (java.lang.IndexOutOfBoundsException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// }
	// return d.findElement(By.id(id));
	// }
	//
	// /**
	// * Get WebElement CSS Selector by value
	// *
	// * @param key
	// * - text
	// * @param index
	// * - index (optional)
	// * @return
	// */
	// public WebElement getElementCSSByValue(String key, int... index) {
	// String id = null;
	// try {
	// id = getSource().getElementsByAttributeValue("value", key).first()
	// .cssSelector();
	// if (index.length != 0) {
	// id = getSource().getElementsByAttributeValue("value", key)
	// .get(index[0]).cssSelector();
	// }
	// } catch (java.lang.NullPointerException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// } catch (java.lang.IndexOutOfBoundsException e) {
	// Assert.fail("Element is not diaplayed in current page for :" + key);
	// }
	// return d.findElement(By.cssSelector(id));
	// }//
}
