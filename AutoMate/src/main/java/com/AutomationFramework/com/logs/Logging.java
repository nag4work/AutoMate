package com.AutomationFramework.com.logs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import com.AutomationFramework.com.custom.EnvironmentVariables;



public class Logging {

	
	public static String LoggingPath=null;
	public static String LoggingProperties=null;
	
	public String Log_Name="log4j";
	public static boolean result;
	public PrintStream ps1=null;
	public PrintStream ps2=null;
	
	public void logaction() 
	{
		
		try
		{


			String ConsoleLog1=EnvironmentVariables.current_dir+"/Logs/Info.txt";
				String ConsoleLog2=EnvironmentVariables.current_dir+"/Logs/Err.txt";
				
				    File file1=new File(ConsoleLog1);
				    File file2=new File(ConsoleLog2);
				    
				    
				    FileOutputStream fos1=new FileOutputStream(file1);
				    FileOutputStream fos2=new FileOutputStream(file2);
				    
				    ps1=new PrintStream(fos1);
				    ps2=new PrintStream(fos2);
				    
				    
				    System.setOut(ps1);
				    System.setErr(ps2);

    	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public Properties readPropertiesFile(String fileName) throws IOException {
	      FileInputStream fis = null;
	      Properties prop = null;
	      try {
	         fis = new FileInputStream(fileName);
	         prop = new Properties();
	         prop.load(fis);
	      } catch(FileNotFoundException fnfe) {
	         fnfe.printStackTrace();
	      } catch(IOException ioe) {
	         ioe.printStackTrace();
	      } finally {
	         fis.close();
	      }
	      return prop;
	   }
	
	
	

}
