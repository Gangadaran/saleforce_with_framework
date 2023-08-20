package tests.rest;

import java.io.File;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.rest.RESTAssuredBase;
import pojo.OAuth;

public class CreateLead extends RESTAssuredBase {
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
		File fileData = new File("./data/data.json");

		Response response =postWithBodyAsFileAndUrl(fileData,"https://testleaf30-dev-ed.develop.my.salesforce.com/services/data/v58.0/sobjects/Lead/");
		enableResponseLog(response);
		
		ID = response.jsonPath().getString("id");
		
		
	}

}
