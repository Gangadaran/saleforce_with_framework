package lib.selenium;

import java.io.File; 

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import io.github.bonigarcia.wdm.WebDriverManager;
import lib.utils.HTMLReporter;


/**
 * @author Gangadaran - Aequalis
 * @version 1
 * @copyright Copyright (c) 2019 The Land Administration Company Inc. All rights
 *            reserved.
 * @project LAVA-Automation-Selenium
 * @descrpition This class provides implementation for the Element interface and
 *              Browser interface. It contains methods for interacting with web
 *              page elements and performing browser-related actions in web
 *              automation.
 */

public class SeleniumBase extends HTMLReporter implements Browser, Element {
	public RemoteWebDriver driver;
	public WebDriverWait wait;
	private static final Logger logger = LoggerFactory.getLogger(SeleniumBase.class);
	private static final String WEBDRIVER_EXCEPTION = "WebDriverException : {} ";

	@Override
	public RemoteWebDriver startApp(String url) {

		return startApp("chrome", url);
	}

	@Override
	public RemoteWebDriver startApp(String browser, String url) {
		try {
			if (browser.equalsIgnoreCase("chrome")) {
				ChromeOptions options = new ChromeOptions();
				options.addArguments("headless");
				options.addArguments("--start-maximized"); 
				options.addArguments("--window-size=2560,969"); 
				driver = new ChromeDriver();
			} else if (browser.equalsIgnoreCase("firefox")) {

				driver = new FirefoxDriver();
			} else if (browser.equalsIgnoreCase("edge")) {

				driver = new EdgeDriver();
			}

			driver.navigate().to(url);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		} catch (Exception e) {
			reportStep("The browser could not be launched. Hence failed", "fail");
			throw new RuntimeException(); // to indicate that an unexpected error occurred during the test execution.

		}
		return driver;
	}

	@Override
	public WebElement locateElement(Locators locatorType, String value) {

		try {
			switch (locatorType) {
			case ID:
				return driver.findElement(By.id(value));
			case NAME:
				return driver.findElement(By.name(value));
			case CLASS_NAME:
				return driver.findElement(By.className(value));
			case LINK:
				return driver.findElement(By.linkText(value));
			case XPATH:
				return driver.findElement(By.xpath(value));

			}
		} catch (NoSuchElementException e) {
			reportStep("The Element with locator:" + locatorType + " Not Found with value: " + value, "fail");
			throw new RuntimeException();
		} catch (Exception e) {
			reportStep("The Element with locator:" + locatorType + " Not Found with value: " + value, "fail");

		}
		return null;
	}



	@Override
	public WebElement locateElement(String value) {
		WebElement findElementById;
		try {
			findElementById = driver.findElement(By.id(value));
			reportStep("The Element found with value: " + value, "pass");
		} catch (NoSuchElementException e) {
			reportStep("The Element not found with value: " + value, "fail");
			throw new RuntimeException();
		}

		return findElementById;
	}

	@Override
	public List<WebElement> locateElements(Locators type, String value) {
		try {
			switch (type) {
			case ID:
				return driver.findElements(By.id(value));
			case NAME:
				return driver.findElements(By.name(value));
			case CLASS_NAME:
				return driver.findElements(By.className(value));
			case LINK:
				return driver.findElements(By.linkText(value));
			case XPATH:
				return driver.findElements(By.xpath(value));
			}
		} catch (NoSuchElementException e) {
			logger.error("The Element with locator: {} Not Found with value: {} ", type, value);
			throw new RuntimeException();
		}
		return null;
	}

	@Override
	public void switchToAlert() {
		try {
			driver.switchTo().alert();
			reportStep("Focus has been switched to Alert", "info", false);
		} catch (NoAlertPresentException e) {
			reportStep("There is no alert present.", "fail", false);
		} catch (WebDriverException e) {
			reportStep("WebDriverException : " + e.getMessage(), "fail", false);
		}

	}

	@Override
	public void acceptAlert() {
		String text = "";
		try {
			wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			text = alert.getText();
			alert.accept();
			reportStep("The alert " + text + " is accepted.", "pass");
		} catch (NoAlertPresentException e) {
			reportStep("There is no alert present", "fail");

		} catch (WebDriverException e) {
			logger.error(WEBDRIVER_EXCEPTION, e.getMessage());
		}

	}

	@Override
	public void dismissAlert() {
		String text = "";
		try {
			Alert alert = driver.switchTo().alert();
			text = alert.getText();
			alert.dismiss();
			reportStep("The alert " + text + " is accepted", "pass");
		} catch (NoAlertPresentException e) {
			reportStep("There is no alert present", "fail");
		}

	}

	@Override
	public String getAlertText() {
		String text = "";
		try {
			Alert alert = driver.switchTo().alert();
			text = alert.getText();
		} catch (NoAlertPresentException e) {
			logger.info("There is no alert peresent.");
		} catch (WebDriverException e) {
			logger.error(WEBDRIVER_EXCEPTION, e.getMessage());
		}
		return text;
	}

	@Override
	public void typeAlert(String data) {
		try {
			driver.switchTo().alert().sendKeys(data);
		} catch (NoAlertPresentException e) {
			reportStep("There is no alert present.", "fail", false);
		} catch (WebDriverException e) {
			reportStep("WebDriverException : \n" + e.getMessage(), "fail", false);
		}

	}

	@Override
	public void switchToWindow(int index) {
		try {
			Set<String> allWindows = driver.getWindowHandles();
			List<String> allhandles = new ArrayList<String>(allWindows);
			driver.switchTo().window(allhandles.get(index));
			reportStep("The Window With index: " + index + " switched successfully", "info", false);
			reportStep(driver.getTitle(), "info");
		} catch (NoSuchWindowException e) {
			reportStep("The Window With index: " + index + " not found\n" + e.getMessage(), "fail", false);
		} catch (Exception e) {
			reportStep("The Window With index: " + index + " not found\n" + e.getMessage(), "fail", false);
		}

	}

	@Override
	public boolean  switchToWindow(String title) {
		try {
			Set<String> allWindows = driver.getWindowHandles();
			for (String eachWindow : allWindows) {
				driver.switchTo().window(eachWindow);
				if (driver.getTitle().equals(title)) {
					break;
				}
			}
			reportStep("The Window With Title: " + title + "is switched ", "info");
			return true;
		} catch (NoSuchWindowException e) {
			reportStep("The Window With Title: " + title + " not found", "fail", false);
		}
		
		return false;
	}

	@Override
	public void switchToFrame(int index) {
		try {
			Thread.sleep(1000);
			driver.switchTo().frame(index);
		} catch (NoSuchFrameException e) {
			reportStep("No such frame " + e.getMessage(), "warning", false);
		} catch (Exception e) {
			reportStep("No such frame " + e.getMessage(), "fail", false);
		};

	}

	@Override
	public void switchToFrame(WebElement element) {
		try {
		    driver.switchTo().frame(element);
		} catch (NoSuchFrameException e) {
			reportStep("No such frame " + e.getMessage(), "fail", false);
		} catch (Exception e) {
			reportStep("No such frame " + e.getMessage(), "fail", false);
		}

	}

	@Override
	public void switchToFrame(String idOrName) {
		try {
			driver.switchTo().frame(idOrName);
		} catch (NoSuchFrameException e) {
			reportStep("No such frame " + e.getMessage(), "fail", false);
		} catch (Exception e) {
			reportStep("No such frame " + e.getMessage(), "fail", false);
		}

	}

	@Override
	public void defaultContent() {
		driver.switchTo().defaultContent();

	}

	@Override
	public boolean verifyUrl(String url) {
		String currentUrl = driver.getCurrentUrl();

		if (currentUrl.equals(url)) {

			
			reportStep("The " + url + " matched successfully", "pass");

			return true;

		}
		reportStep("The " + url + " not matched successfully", "fail");
		
		return false;

	}

	@Override
	public boolean verifyTitle(String title) {
		if (driver.getTitle().equals(title)) {
			logger.info("Page title: {} matched succcessfulley", title);
			return true;
		}
		logger.info("Page title: {} not matched", title);
		return false;
	}

	@Override
	public void close() {
		driver.close();

	}

	@Override
	public void quit() {
		driver.quit();

	}

	@Override
	public void click(WebElement element) {
		try {
			element.isDisplayed(); // @FindBy return the proxy even if it does not exist !!
		} catch (NoSuchElementException e) {
			reportStep("The Element " + element + " is not found", "fail");
			
		}

		String text = "";
		try {
			try {
				Thread.sleep(500);
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				
				wait.until(ExpectedConditions.elementToBeClickable(element));
				text = element.getAttribute("value");
				if (element.isEnabled()) {
					element.click();
					reportStep("The Element " + element + " is clicked", "pass");
				} else {
					driver.executeScript("arguments[0].click()", element);
				}
			} catch (Exception e) {
				boolean bFound = false;
				int totalTime = 0;
				while (!bFound && totalTime < 10000) {
					try {
						Thread.sleep(500);
						element.click();
						bFound = true;

					} catch (Exception e1) {
						bFound = false;
					}
					totalTime = totalTime + 500;
				}
				if (!bFound)
					element.click();
			}
		} catch (StaleElementReferenceException e) {
			System.err.println(e);
			reportStep("The Element " + text + " could not be clicked due to:" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			System.err.println(e);
			reportStep("The Element " + element + " could not be clicked due to: " + e.getMessage(), "fail");
		} catch (Exception e) {
			System.err.println(e);
			reportStep("The Element " + element + " could not be clicked due to: " + e.getMessage(), "fail");
		}

	}
	

	

	@Override
	public void append(WebElement element, String data) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOf(element));
			element.clear();
			element.sendKeys(data);
			reportStep("The Element " + element + " is entered", "pass");
		} catch (ElementNotInteractableException e) {
			reportStep("The Element " + element + " is not Interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("The Element " + element + " is not Interactable \n" + e.getMessage(), "fail");
		}

	}

	@Override
	public void clear(WebElement element) {
		try {
			element.clear();
			reportStep("The field is cleared successfully", "pass");
		} catch (ElementNotInteractableException e) {
			reportStep("The field is not Interactable", "fail");
			throw new RuntimeException();
		}

	}

	@Override
	public void clearAndType(WebElement element, String data) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOf(element));
			element.clear();
			element.sendKeys(data);
			reportStep("The date is entered: " + data, "pass");
		} catch (ElementNotInteractableException e) {
			reportStep("The Element not found with value: " + element, "fail");
		} catch (WebDriverException e) { // retry - 1
			pause(500);
			try {
				element.sendKeys(data);
			} catch (Exception e1) {
				reportStep("The Element " + element + " did not allow to clear / type \n" + e.getMessage(), "fail");
			}
		}

	}

	@Override
	public String getElementText(WebElement element) {
		try {
			pause(3000);
			String text = element.getText();
			reportStep("Text has been retrieved " + text, "info");
			return text;
		} catch (WebDriverException e) {
			reportStep("Sorry! text is not available \n" + e.getMessage(), "fail");
		} catch (Exception e) {
			reportStep("Sorry! text is not available \n" + e.getMessage(), "fail");
		}
		return null;
	}

	@Override
	public String getBackgroundColor(WebElement element) {
		String cssValue = null;
		try {
			cssValue = element.getCssValue("color");
			reportStep("The background color is " + cssValue, "info");
		} catch (WebDriverException e) {
			reportStep("Not able to get the background color \n" + e.getMessage(), "fail");
		} catch (Exception e) {
			reportStep("Not able to get the background color \n" + e.getMessage(), "fail");
		}
		return cssValue;
	}

	@Override
	public String getTypedText(WebElement element) {
		String attributeValue = null;
		try {
			attributeValue = element.getAttribute("value");
			reportStep("The attribute value is " + attributeValue, "info");
		} catch (WebDriverException e) {
			reportStep("Not able to find attribute value \n" + e.getMessage(), "fail");
		}
		return attributeValue;
	}

	@Override
	public void selectDropDownUsingText(WebElement element, String value) {
		try {
			Select sel = new Select(element);
			sel.selectByVisibleText(value);
		} catch (WebDriverException e) {
			reportStep("Not able to select the drop down with text \n" + value, "fail");
		}

	}

	@Override
	public void selectDropDownUsingvalue(WebElement element, String value) {
		new Select(element).selectByValue(value);

	}

	@Override
	public boolean verifyExactText(WebElement element, String expectedText) {
		try {
			if (element.getText().equals(expectedText)) {
				reportStep("The expected text contains the actual " + expectedText, "pass");
				return true;
			} else {
				reportStep("The expected text doen't contain the actula " + expectedText, "fail");
			}
		} catch (WebDriverException e) {
			logger.error("Unknown exception occured while verifying the Text");
		}
		return false;
	}

	@Override
	public boolean verifyPartialText(WebElement element, String expectedText) {
		try {
			if (element.getText().contains(expectedText)) {
				reportStep("The expected text contains the actual " + expectedText, "pass");
				return true;
			} else {
				reportStep("The expected text doesn't contain the actual " + expectedText, "fail");
			}
		} catch (WebDriverException e) {
			logger.error("Unknown exception occured while verifying the Text");
		}
		return false;
	}

	@Override
	public boolean verifyExactAttribute(WebElement element, String attribute, String value) {
		try {
			if (element.getAttribute(attribute).equals(value)) {
				reportStep("The expected attribute :" + attribute + "value contains the actual " + value, "pass");
			} else {
				reportStep("The expected attribute :" + attribute + "value doesn't contains the actual " + value,
						"fail");
			}
		} catch (WebDriverException e) {
			logger.error("Unknown exception occured while verifying the attribute Text");
		}
		return false;
	}

	@Override
	public void verifyPartialAttribute(WebElement element, String attribute, String value) {
		try {
			if (element.getAttribute(attribute).contains(value)) {
				reportStep("The expected attribute :" + attribute + "value contains the actual " + value, "pass");
			} else {
				reportStep("The expected attribute :" + attribute + "value doesn't contains the actual " + value,
						"fail");
			}
		} catch (WebDriverException e) {
			logger.error("Unknown exception occured while verifying the attribute Text");
		}

	}

	@Override
	public boolean verifyDisplayed(WebElement element) {
		String text = "";
		try {
			if (element.isDisplayed()) {
				text = element.getText();
				
				reportStep("The Elelment " + text + " is visible", "pass");
				return true;
			} else {
				reportStep("The Element " + text + " is not visible", "warnings");
			}
		} catch (WebDriverException e) {

			logger.error(WEBDRIVER_EXCEPTION, e.getMessage());
		}
		return false;
	}

	

	@Override
	public boolean verifyDisappeared(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			Boolean until = wait.until(ExpectedConditions.invisibilityOf(element));
			reportStep(element+" is disappeared successfully", "pass");
			return until;
		} catch (org.openqa.selenium.TimeoutException e) {
			reportStep("Element is disappeared \n", "pass");
		} catch (Exception e) {
			reportStep("Element is disappeared \n", "pass");
		}
		return false;
	}

	@Override
	public boolean verifyEnabled(WebElement element) {
		try {
			if (element.isEnabled()) {
				reportStep("The element " + element + " is enabled", "pass");
				return true;
			} else {
				reportStep("The element " + element + "is not enabled", "fail");
			}
		} catch (WebDriverException e) {
			logger.error(WEBDRIVER_EXCEPTION, e.getMessage());
		}
		return false;
	}

	@Override
	public void verifySelected(WebElement element) {
		try {
			if (element.isSelected()) {
				reportStep("The element " + element + " is selected", "pass");
			} else {
				reportStep("The element " + element + " is not selected", "fail");
			}
		} catch (WebDriverException e) {
			logger.error(WEBDRIVER_EXCEPTION, e.getMessage());
		}

	}

	@Override
	public long takeSnap() {
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;

		try {
			FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE), new File(
					getDataFromProb("TakeScreenshotPath") + number + getDataFromProb("ImageFormat")));
		} catch (WebDriverException e) {
			logger.error("The browser has been closed");
		} catch (IOException e) {
			logger.error("The snapshot could not be taken");
		}
		return number;
	}

	@Override
	public void selectDropDownUsingIndex(WebElement element, int index) {
		try {
			Select sel = new Select(element);
			sel.selectByIndex(index);
		} catch (WebDriverException e) {
			reportStep("Not able to select the drop down with index " + index + " \n" + e.getMessage(), "fail");
		}

	}

//	public String referenceNumber() {
//		Faker faker = new Faker();
//		String refNumber = appProperty.getProperty("ReferenceNumberPrefix") + faker.number().digits(5);
//		return refNumber;
//
//	}

	public void chooseDate(WebElement ele, String data) {
		try {

			driver.executeScript("arguments[0].setAttribute('value', '" + data + "')", ele);
			reportStep("The Data :" + data + " entered Successfully", "pass");
		} catch (ElementNotInteractableException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		}

	}

	public void waitForDisapperance(WebElement element) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.invisibilityOf(element));
		} catch (Exception e) {
			reportStep("Element did not appear after 10 seconds", "fail", false);

		}

	}

	public void waitForApperance(WebElement element) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			reportStep("Element did not appear after 20 seconds", "fail", false);

		}

	}

	public void pause(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void clickWithNoSnap(WebElement ele) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			ele.click();
		} catch (StaleElementReferenceException e) {
			reportStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail", false);
		} catch (WebDriverException e) {
			reportStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail", false);
		} catch (Exception e) {
			reportStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail", false);
		}
	}
	public void clickUsingJs(WebElement ele) {
		try {
			ele.isDisplayed(); // @FindBy return the proxy even if it does not exist !! 
		}catch (NoSuchElementException e) {
			reportStep("The Element " + ele + " is not found", "fail");
		}

		String text = "";
		try {
			try {
				driver.executeScript("arguments[0].click()", ele);
			} catch (Exception e) {
				boolean bFound = false;
				int totalTime = 0;
				while(!bFound && totalTime < 10000) {
					try {
						Thread.sleep(500);
						driver.executeScript("arguments[0].click()", ele);
						bFound = true;

					} catch (Exception e1) {
						bFound = false;
					}
					totalTime = totalTime+500;						
				}
				if(!bFound)
					driver.executeScript("arguments[0].click()", ele);
			}
		} catch (StaleElementReferenceException e) {
			reportStep("The Element " + text + " could not be clicked due to:" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("The Element " + ele + " could not be clicked due to: " + e.getMessage(), "fail");
		} catch (Exception e) {
			reportStep("The Element " + ele + " could not be clicked due to: " + e.getMessage(), "fail");
		}
	}
	
	public void typeAndTab(WebElement ele, String data) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOf(ele));
			ele.clear();
			ele.sendKeys("", "", data, Keys.TAB);
		} catch (ElementNotInteractableException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		}

	}
	public void typeAndEnter(WebElement ele, String data) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOf(ele));
			ele.clear();
			ele.sendKeys("", "", data, Keys.ENTER);
		} catch (ElementNotInteractableException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		} catch (WebDriverException e) {
			reportStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
		}

	}
	
	public void openNewTapAndLoadGivenURL(String URL) {
		try {
			
			driver.switchTo().newWindow(WindowType.TAB);
			pause(2000);
			driver.get(URL);
			reportStep("Successfully open the new tap and loaded the given URL", "pass");
		} catch (Exception e) {
			reportStep("New tap is not able to open due to " + e, "fail", false);
		}
	}

	public void clickNormal(WebElement ele) {
		String text = "";
		try {
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			wait.until(ExpectedConditions.elementToBeClickable(ele));
			text = ele.getText();
			ele.click();
			reportStep("The Element " + text + " clicked", "pass");
		} catch (StaleElementReferenceException e) {
			reportStep("The Element " + text + " could not be clicked", "fail");
			throw new RuntimeException();
		}

	}
	public WebElement locateElementWithoutScreenshot(Locators locatorType, String value) {

		try {
			switch (locatorType) {
			case ID:
				return driver.findElement(By.id(value));
			case NAME:
				return driver.findElement(By.name(value));
			case CLASS_NAME:
				return driver.findElement(By.className(value));
			case LINK:
				return driver.findElement(By.linkText(value));
			case XPATH:
				return driver.findElement(By.xpath(value));

			}
		} catch (NoSuchElementException e) {
			reportStep("The Element with locator:" + locatorType + " Not Found with value: " + value, "info", false);
			throw new RuntimeException();
		} catch (Exception e) {
			reportStep("The Element with locator:" + locatorType + " Not Found with value: " + value, "info", false);

		}
		return null;
	}
	

}
