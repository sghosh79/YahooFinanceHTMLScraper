package org.shu.util;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
	
	private static Properties prop = new Properties();
	
	public static String getEmail(){ 
		String emails = null;
		try {
	           //load a properties file			
			prop.load(PropertyUtil.class.getResourceAsStream("batch.properties"));	
	           //get the property value and print it out
			emails =  prop.getProperty("emails");
	
		} catch (IOException ex) {
			ex.printStackTrace();
	    }
		
		return emails;
	}

}
