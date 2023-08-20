package runner;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import lib.selenium.PreAndPost;

@CucumberOptions(features = { "src/test/java/features" }, 
glue = { "pages.selenium","pages.rest" }, 
monochrome = true 
//dryRun = false, 
//strict = true,
//plugin = { "html:reports/cucumber",	"json:reports/cucumber.json" }
		)


public class RunCucumberUsingTestNG extends PreAndPost{


}
