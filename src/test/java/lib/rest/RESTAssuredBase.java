package lib.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestLogSpecification;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;
import pojo.OAuth;

public class RESTAssuredBase extends PreAndTest{

	public static RequestSpecification setLogs() {
		
		
		RequestSpecification log = null;
		
			
			log = RestAssured
					.given()
					.auth()
					.oauth2(getAccessToken())
					.log().all().contentType(ContentType.JSON);
			return log;
		
	}
	
	
public static String getAccessToken() {	
		
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
		
		return response.jsonPath().getString("access_token");
		
		
		
		
		
		
		
	}

public static String getID() {	
	File fileData = new File("./data/data.json");

	Response response =postWithBodyAsFileAndUrl(fileData,"https://testleaf30-dev-ed.develop.my.salesforce.com/services/data/v58.0/sobjects/Lead/");
	enableResponseLog(response);
	
	return response.jsonPath().getString("id");
	
	
}
	
	public static RequestSpecification setLogsWithOauth(String access_token) {
		
		
		 RequestSpecification	log = null;
		
			
		 	  log = RestAssured
					.given()
					.auth()
					.oauth2(access_token)
					.log().all().contentType(ContentType.JSON);
		
		return log;
	}
	
	

	public static Response get(String URL) {
		return setLogs()
				.get(URL);
	}
	
	public static Response generateBearTokenWithPost(Map<String,Object> map,String url) {
		return RestAssured
				.given()
				.log()
				.all()
				.contentType("application/x-www-form-urlencoded")
				.formParams(map)
				.post(url);
				

	}
	
	

	

	public static Response get() {
		return setLogs()
				.get();
	}

	public static Response getWithHeader(Map<String, String> headers, String URL) {

		return setLogs()
				.when()
				.headers(headers)
				.get(URL);
	}
	
	public static Response getWithHeader(Header header, String URL) {

		return setLogs()
				.when()
				.header(header)
				.get(URL);
	}

	public static Response post() {

		return setLogs()
				.post();
	}

	public static Response post(String URL,ContentType type) {

		return setLogs()
				.contentType(type)
				.post(URL);
	}

	public static Response postWithBodyAsFile(File bodyFile) {

		return setLogs()
				.body(bodyFile)
				.post();
	}

	public static Response postWithBodyAsFileAndUrl(File bodyFile, String URL) {

		return setLogs()
				.body(bodyFile)
				.post(URL);
	}
	
	public static Response postWithHeaderAndForm(Map<String, String> headers,
			JSONObject jsonObject, String URL) {

		return setLogs()
				.headers(headers)
				.body(jsonObject)
				.post(URL);
	}

	public static Response postWithJsonAsBody(String jsonObject, String URL) {

		return setLogs()
				.body(jsonObject)
				.post(URL);
	}


	public static Response postWithHeaderAndJsonBody(Map<String, String> headers,
			String jsonObject, String URL) {

		return setLogs()
				.when()
				.headers(headers)
				.body(jsonObject)
				.post(URL);
	}


	public static Response postWithHeaderParam(Map<String, String> headers, String URL) {

		return setLogs()
				.when()
				.headers(headers)
				.post(URL);
	}


	public static Response postWithHeaderAndPathParam(Map<String, String> headers,Map<String, String> pathParms, String URL) {

		return setLogs()
				.when()
				.headers(headers)
				.pathParams(pathParms)
				.post(URL);
	}

	public static Response postWithHeaderAndPathParam(Map<String, String> pathParms, String URL) {

		return setLogs()
				.when()
				.pathParams(pathParms)
				.post(URL);
	}

	public static Response delete(String URL) {
		return setLogs()
				.when()
				.delete(URL);
	}

	public static Response deleteWithHeaderAndPathParam(Map<String, String> headers,
			JSONObject jsonObject, String URL) {

		return setLogs()
				.when()
				.headers(headers)
				.body(jsonObject)
				.delete(URL);
	}

	public static Response deleteWithHeaderAndPathParamWithoutRequestBody(
			Map<String, String> headers, String URL) throws Exception {
		return setLogs()
				.when()
				.headers(headers)
				.delete(URL);
	}

	public static Response putWithHeaderAndBodyParam(Map<String, String> headers,
			JSONObject jsonObject, String URL) {

		return RestAssured.given().headers(headers).contentType(getContentType()).request()
				.body(jsonObject).when().put(URL);
	}
	
	public static Response putWithBodyParam(File file, String URL) {

		return RestAssured.given().contentType(getContentType()).request()
				.body(file).when().put(URL);
	}

	public static ValidatableResponse enableResponseLog(Response response) {
		return response.then().log().all();
	}

	private static ContentType getContentType() {
		return ContentType.JSON;
	}

	public static void verifyContentType(Response response, String type) {
		if(response.getContentType().toLowerCase().contains(type.toLowerCase())) {
			reportRequest("The Content type "+type+" matches the expected content type", "PASS");
		}else {
			reportRequest("The Content type "+type+" does not match the expected content type "+response.getContentType(), "FAIL");	
		}
	}
	
	public static void verifyResponseCode(Response response, int code) {
		if(response.statusCode() == code) {
			reportRequest("The status code "+code+" matches the expected code", "PASS");
		}else {
			reportRequest("The status code "+code+" does not match the expected code"+response.statusCode(), "FAIL");	
		}
	}

	public static void verifyResponseTime(Response response, long time) {
		if(response.time() <= time) {
			reportRequest("The time taken "+response.time() +" with in the expected time", "PASS");
		}else {
			reportRequest("The time taken "+response.time() +" is greater than expected SLA time "+time,"FAIL");		
		}
	}

	public static void verifyContentWithKey(Response response, String key, String expVal) {
		if(response.getContentType().contains("json")) {
			JsonPath jsonPath = response.jsonPath();
			String actValue = jsonPath.get(key);
			if(actValue.equalsIgnoreCase(expVal)) {
				reportRequest("The JSON response has value "+expVal+" as expected. ", "PASS");
			}else {
				reportRequest("The JSON response :"+actValue+" does not have the value "+expVal+" as expected. ", "FAIL");		
			}
		}
	}

	public static void verifyContentsWithKey(Response response, String key, String expVal) {
		if(response.getContentType().contains("json")) {
			JsonPath jsonPath = response.jsonPath();
			List<String> actValue = jsonPath.getList(key);
			if(actValue.get(0).equalsIgnoreCase(expVal)) {
				reportRequest("The JSON response has value "+expVal+" as expected. ", "PASS");
			}else {
				reportRequest("The JSON response :"+actValue+" does not have the value "+expVal+" as expected. ", "FAIL");		
			}
		}
	}

	public static List<String> getContentsWithKey(Response response, String key) {
		if(response.getContentType().contains("json")) {
			JsonPath jsonPath = response.jsonPath();
			return jsonPath.getList(key);			
		}else {
			return null;
		}
	}

	public static String getContentWithKey(Response response, String key) {
		if(response.getContentType().contains("json")) {
			JsonPath jsonPath = response.jsonPath();
			return (String) jsonPath.get(key);			
		}else {
			return null;
		}
	}

}
