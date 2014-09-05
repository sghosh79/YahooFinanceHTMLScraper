package org.shu.test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestPropertyFile {
	
	 
	    public static void main( String[] args )
	    {
	    	Properties prop = new Properties();
	 
	    	try {
	               //load a properties file
	    		prop.load(new FileInputStream("batch.properties"));
	 
	               //get the property value and print it out
                System.out.println(prop.getProperty("emails"));
	 
	    	} catch (IOException ex) {
	    		ex.printStackTrace();
	        }
	 
	    }

}
