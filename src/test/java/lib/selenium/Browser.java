package lib.selenium;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Gangadaran - Aequalis
 * @version 1
 * @copyright Copyright (c) 2019 The Land Administration Company Inc. All rights
 *            reserved.
 * @project LAVA-Automation-Selenium
 * @description This interface defines methods related to browser actions in web
 *              automation. It provides a set of common operations that can be
 *              performed on a web browser, such as navigating, maximizing, and
 *              closing. To use this interface, implement its methods in a class
 *              that interacts with a web browser. The methods can be used to
 *              perform various browser-related actions, such as opening a new
 *              window, switching to a different tab, and getting the page
 *              title.
 */

public interface Browser {
	/**
	 * This method will launch the chrome browser and maximize the browser and set
	 * the wait for 30 seconds and load the url
	 * 
	 * @param url - This will load the specified url
	 * @author Gangadaran - Aequalis
	 * @return
	 * @throws MalformedURLException
	 */
	public RemoteWebDriver startApp(String url);

	/**
	 * This method will launch the any browser and maximize the browser and set the
	 * wait for 30 seconds and load the url
	 * 
	 * @param browser - This will load the specified browser
	 * @param url     - This will load the specided URL
	 * @author Gangadaran - Aequalis
	 * @return
	 * @throws MalformedURLException
	 */

	public RemoteWebDriver startApp(String browser, String url);

	/**
	 * This method will locate the element using any given locator
	 * 
	 * @param locatorType - The locator by which the element to be found
	 * @param value       - The locator value by which the element to be found
	 * @author Gangadaran - Aequalis
	 * @throws NoSuchElementException
	 * @return The first matching element on the current context
	 */
	public WebElement locateElement(Locators locatorType, String value);

	/**
	 * This method will locate element using id
	 * 
	 * @param locValue - The locator value by which the element to be found
	 * @author Gangadran - Aequalis
	 * @throws NoSuchElementException
	 * @return The first matching element on the current context
	 */
	public WebElement locateElement(String value);

	/**
	 * This method will locate all matching element using any given locator
	 * 
	 * @param type  - The locatorType by which the element to be found
	 * @param value - The LocatorValue by which the element to be found
	 * @author Gangadaran - Aequalis
	 * @throws NoSuchElementException
	 * @return A list of all WebElements, or an empty list if nothing matches.
	 */
	public List<WebElement> locateElements(Locators type, String value);

	/**
	 * This method will switch to alert
	 * 
	 * @author Gangadaran - Aequalis
	 * @throws NoAlerPresentException
	 */
	public void switchToAlert();

	/**
	 * This method will accept the alert opened
	 * 
	 * @author Gangadaran - Aequalis
	 * @throws NoAlerPresentException
	 */
	public void acceptAlert();

	/**
	 * This method will dismiss the alert opened
	 * 
	 * @author Gangadaran - Aequalis
	 * @throws NoAlerPresentException
	 */
	public void dismissAlert();

	/**
	 * This method will return the text of the alert
	 * 
	 * @author Gangadaran - Aequalis
	 * @throws NoAlertPresentException
	 */
	public String getAlertText();

	/**
	 * This method will enter the value in the alert
	 * 
	 * @param data - The data to be entered in the alert
	 * @author Gangadaran - Aequalis
	 * @throws NoAlertPresentException
	 */
	public void typeAlert(String data);

	/**
	 * This method will switch to the window of interest
	 * 
	 * @param index - The window index to be switched to. 0 -> first window
	 * @author Gangadaran - Aequalis
	 * @throws NoSuchWindowException
	 */
	public void switchToWindow(int index);

	/**
	 * This method will switch to the Window of interest using its title
	 * 
	 * @param title The window title to be switched to first window
	 * @author Gangadaran - Aequalis
	 * @throws NoSuchWindowException
	 */
	public boolean switchToWindow(String title);

	/**
	 * This method will switch to the specific frame using index
	 * 
	 * @param index - The index(frame) to be switched
	 * @author Gangadaran - Aequalis
	 * @throws NoSuchFrameException
	 */
	public void switchToFrame(int index);

	/**
	 * This method will switch to the specific frame
	 * 
	 * @param ele - The Webelement (frame) to be switched
	 * @author Gangadaran - Aequalis
	 * @throws NoSuchFrameException, StaleElementReferenceException
	 */
	public void switchToFrame(WebElement ele);

	/**
	 * This method will switch to the specific frame using Id (or) Name
	 * 
	 * @param idOrName - The String (frame) to be switched
	 * @author Gangadaran - Aequalis
	 * @throws NoSuchFrameException
	 */
	public void switchToFrame(String idOrName);

	/**
	 * This method will switch to the first frame on the page
	 * 
	 * @author Gangadaran - Aequalis
	 * @return This driver focused on the top window/first frame.
	 */
	public void defaultContent();

	/**
	 * This method will verify browser actual url with expected
	 * 
	 * @param url - The url to be checked
	 * @author Gangadaran - Aequalis
	 * @return true if the given object represents a String equivalent to this url,
	 *         false otherwise
	 */
	public boolean verifyUrl(String url);

	/**
	 * This method will verify browser actual title with expected
	 * 
	 * @param title - The expected title of the browser
	 * @author Gangadaran - Aequalis
	 * @return true if the given object represents a String equivalent to this
	 *         title, false otherwise
	 */
	public boolean verifyTitle(String title);

	/**
	 * This method will close the active browser
	 * 
	 * @author Gangadaran - Aequalis
	 */
	public void close();

	/**
	 * This method will close all the browsers
	 * 
	 * @author Gangadaran - Aequalis
	 */
	public void quit();

}
