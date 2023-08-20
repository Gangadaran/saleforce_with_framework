package tests.rest;

import java.io.File;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;
import lib.rest.RESTAssuredBase;
import pojo.OAuth;

public class Salesforce_GenerateTokenAndCreateLeadAndGetDeatails extends RESTAssuredBase {
	
	
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
		
		OAuth obj = new OAuth();
		obj.setGrant_type(getDataFromProb("grant_type"));
		obj.setClient_id(getDataFromProb("client_id"));
		obj.setClient_secret(getDataFromProb("client_secret"));
		obj.setUsername(getDataFromProb("username"));
		obj.setPassword(getDataFromProb("password"));
		
		
		
        // Create ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();
        // Converting POJO to Map
        Map<String, Object> map = mapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
				
		Response response =RESTAssuredBase
		.generateBearTokenWithPost(map,"https://testleaf30-dev-ed.develop.my.salesforce.com/services/oauth2/token");
		
		enableResponseLog(response);
		
		access_token = response.jsonPath().getString("access_token");
		
		System.out.println(access_token);
		
		
		
		
		
	}

}
