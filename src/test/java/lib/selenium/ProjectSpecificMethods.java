package lib.selenium;

import java.io.IOException;   
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;


import lib.utils.DataInputProvider;





/**
 * @author Gangadaran - Aequalis
 * @version 1
 * @copyright Copyright (c) 2019 The Land Administration Company Inc. All rights
 *            reserved.
 * @project LAVA-Automation-Selenium
 * @description TThis class is designed to contain all the common methods that
 *              can be used across multiple classes in the application. The
 *              purpose of this class is to reduce code duplication and improve
 *              code organization by consolidating commonly used methods in a
 *              single location. The methods in this class are intended to be
 *              generic and reusable. They should not contain any
 *              application-specific logic and should be independent of any
 *              particular class or context.
 */

public class ProjectSpecificMethods extends SeleniumBase {

	public String dataSheetName, application;
	
	public static String enquiryId, titleID, appId,rrrNumber, documentId;
	
	public static String effDateL, expDateL, paymentPeriodL, amountL;
	public static String effDateM, expDateM, paymentPeriodM, amountM;
	 


	@DataProvider(name = "fetchData")
	public Object[][] fetchData() throws IOException {
		
		return DataInputProvider.getSheet(dataSheetName);
		}

	@BeforeMethod()
	public void beforeMethod() { 
		
		startApp(amountL);

	}

	@AfterMethod
	public void afterMethod() {
		quit();

	}

}
