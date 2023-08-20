package tests.selenium;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import lib.selenium.PreAndPost;
import pages.selenium.LoginPage;

public class Salesforce_CreateLeadandConverttoOpportunity extends PreAndPost {
	
	@BeforeTest
	public void setValues() {

		testCaseName = "Create Leadand Convert to Opportunity";
		testDescription = "Create Lead";
		nodes = "Incident Management";
		authors = "Sarath";
		category = "UI";
		dataSheetName = "TC002";

	}
	
	@Test(dataProvider = "fetchData")
	public void createLead(String filter, String user, String short_desc) throws InterruptedException {
		
		new LoginPage(driver, test)
		.loginApp()
		.searchUsingFilter(filter)
		.clickCreateNew()
		.getIncidentNumber()
		.selectUser(user)
		.typeShortDescription(short_desc)
		.clickSubmit();
		
	}

}
