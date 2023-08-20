package lib.utils;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

public class ReadPropertiesData {
	
	   public static Properties prob;
	   
	   
	   public ReadPropertiesData() {
		   prob = new Properties();
		   try {
			prob.load(new FileInputStream("src/test/resources/config.properties"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	   }
	   
	   public static String getDataFromProb(String type) {
		   return prob.getProperty(type);
		   

	}
	    
	    
	    
	   
}
