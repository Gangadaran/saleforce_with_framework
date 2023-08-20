package tests.rest;

import java.io.File;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.rest.RESTAssuredBase;

public class GetDetails extends RESTAssuredBase {
	
	
	@BeforeTest//Reporting
	public void setValues() {
		testCaseName = "Create a new Incident (REST)";
		testDescription = "Create a new Incident and Verify";
		nodes = "Incident";
		authors = "Sarath";
		category = "REST";
		//dataProvider
		dataFileName = "TC001";
		dataFileType = "JSON";
		
		
	}

	@Test(dataProvider = "fetchData")
	public void createIncident(File file) {	
		
		Response response =RESTAssuredBase
		.setLogs()
		.get("https://testleaf30-dev-ed.develop.my.salesforce.com/services/data/v58.0/sobjects/Lead/"+getID());

		enableResponseLog(response);
		
		
		
		
		
		
		
	}

}
