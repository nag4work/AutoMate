package com.AutomationFramework.BDD.Util;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cucumber.listener.Reporter;

import com.AutomationFramework.com.DriverEngine.Driver;
import com.AutomationFramework.com.KeywordHelper.WebAppKeywordHelper;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.custom.EnvironmentVariables;
import com.Wrapper.ResponseWrapper;

public class Utility {
	public static final Logger logger = LoggerFactory.getLogger(Utility.class);
	public Properties prop = null;
	public static boolean result;
	public static String Object_Field_Type=null;
	public static WebElement element=null;
	public static String Object_Field_Name=null;

	  
  
  public void Execute(String webDriveraction,String ObjectName,String Value)
  {
	  Utility Util_OBJ=new Utility();
	  //Driver Driver_OBJ=new Driver();
	 // WebAppKeywordHelper WebAppKeywordHelper_OBJ=new WebAppKeywordHelper();
	  try
	  {
		  result=Util_OBJ.KeywordRunner(webDriveraction,ObjectName,Value);
		  
		  if(result==false)
		  {
			  assertEquals(true, result);
			  //Driver_OBJ.getFailureScreenshot(WebAppKeywordHelper_OBJ.webdriver, AutomationMainClass.TestCase_Name, "");
			  logger.error(webDriveraction+"Failed"+((ObjectName.isEmpty()) ?"":" on Object "+ ObjectName+" for value"+ Value ));
			 // Reporter.addStepLog("Failed :"+webDriveraction+"Failed"+((ObjectName.isEmpty()) ?"":" on Object "+ ObjectName+" for value"+ Value ));
		  }
		  else
		  {
			  logger.info(webDriveraction+"Success"+((ObjectName.isEmpty()) ?"":" on Object "+ ObjectName+" for value"+ Value ));
			 // Reporter.addStepLog("Success :"+webDriveraction+" Passed"+((ObjectName.isEmpty()) ?"":" on Object "+ ObjectName+" for value"+ Value +" and action completed" ));  
		  }
		  
		  
	  } 
	  catch (Exception e) {
			// TODO Auto-generated catch block
		  ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
  }
  
  public void Execute(String webDriveraction,String ObjectName,String Value,boolean failure_run_flag)
  {
	  Utility Util_OBJ=new Utility();
	  //Driver Driver_OBJ=new Driver();
	 // WebAppKeywordHelper WebAppKeywordHelper_OBJ=new WebAppKeywordHelper();
	  try
	  {
		  result=Util_OBJ.KeywordRunner(webDriveraction,ObjectName,Value);
		  
		  if(failure_run_flag==true)
		  {
			  logger.info("Script failed at this step but running further as per the flag value is True");
		  }
		  else
		  {
			  if(result==false)
			  {
				   assertEquals(true, result);
				  //Driver_OBJ.getFailureScreenshot(WebAppKeywordHelper_OBJ.webdriver, AutomationMainClass.TestCase_Name, "");
				  logger.error(webDriveraction+"Failed"+((ObjectName.isEmpty()) ?"":" on Object "+ ObjectName+" for value"+ Value ));
				 // Reporter.addStepLog("Failed :"+webDriveraction+"Failed"+((ObjectName.isEmpty()) ?"":" on Object "+ ObjectName+" for value"+ Value ));
			  }
			  else
			  {
				  logger.info(webDriveraction+"Success"+((ObjectName.isEmpty()) ?"":" on Object "+ ObjectName+" for value"+ Value ));
				 // Reporter.addStepLog("Success :"+webDriveraction+" Passed"+((ObjectName.isEmpty()) ?"":" on Object "+ ObjectName+" for value"+ Value +" and action completed" ));  
			  }
		  }
		  
		  
	  } 
	  catch (Exception e) {
			// TODO Auto-generated catch block
		  ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
  }
  



public boolean Validate(String webDriveraction,String ObjectName,String Value)
  {

	  Utility Util_OBJ=new Utility();
	  try
	  {
		  result=Util_OBJ.KeywordRunner(webDriveraction,ObjectName,Value);
		  

		  
		  
	  } 
	  catch (Exception e) {
			// TODO Auto-generated catch block
		  ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			//e.printStackTrace();
		}
	  
	  return result;
  }
  


	private boolean KeywordRunner(String webDriveraction, String objectName, String value) 
	{
		boolean flag=false;
		ObjectLoader objerloader=new ObjectLoader();
		Driver Driver_Class_Obj=new Driver();
		
		try
		{
			logger.info("Executing the keyword with action " +webDriveraction);
			
			long startTime=System.currentTimeMillis();
			
			if(!objectName.isEmpty())
			{
				WebElement element=getObjectDetails(objectName,Thread.currentThread().getStackTrace()[3].getClassName().replace("StepDefinations", "POMClass"));
				
				if(element==null)
				{
					logger.error("Issue in finding the object for action"+webDriveraction +"and Object "+objectName);
					//Reporter.addStepLog("Failure: Issue in finding the object "+objectName); 
				}
				if(Object_Field_Type.isEmpty() || Object_Field_Name.isEmpty())
				{
					logger.error("Issue in finding the object properites in POM Class  for action "+webDriveraction+"and Object "+objectName);
					//Reporter.addStepLog("Issue in finding the object properites in POM Class ");
				}
				else
				{
					objerloader.LoadObject(Object_Field_Name,Object_Field_Type);
				}
				
						
			}
			else
			{
				Object_Field_Name="";
				Object_Field_Type="";
			}
			
			
			
			result=Driver_Class_Obj.invokeWebAppKeywordMethod(webDriveraction,Object_Field_Name,Object_Field_Type,value);	
			
			long duration=System.currentTimeMillis()-startTime;
			
			double Seconds=((double)duration)/1000;
			
			if(duration<1000)
			{
				  logger.info("Keyword "+webDriveraction+" executed in "+duration+ "ms");
				 // Reporter.addStepLog("Keyword "+webDriveraction+" executed in "+duration+ "ms");  
			}
			else
			{
				logger.info("Keyword "+webDriveraction+" executed in "+String.format("%.3f", Seconds)+ " Seconds");
				//Reporter.addStepLog("Keyword "+webDriveraction+" executed in "+String.format("%.3f", Seconds)+ " Seconds"); 
			}
			
		}
		catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean chkObjectPresence(String objectName) 
	{

		ObjectLoader objerloader=new ObjectLoader();

		
		try
		{
				WebElement elementpresence=null;
				
				WebElement element=getObjectDetails(objectName,Thread.currentThread().getStackTrace()[2].getClassName().replace("StepDefinations", "POMClass"));
				
				if(element==null)
				{
					logger.error("Issue in finding the object for action");
				}
				if(Object_Field_Type.isEmpty() || Object_Field_Name.isEmpty())
				{
					logger.error("Issue in finding the object properites in POM Class  for action ");
					return false;
				}
				
					elementpresence=objerloader.LoadObject(Object_Field_Name,Object_Field_Type);
					
				if(elementpresence!=null)
				{
					return true;
				}
				else
				{
					return false;
				}
		
			
		}
		catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
			return false;
		}
		//return result;
	}
	


	private WebElement getObjectDetails(String objectName, String ClassName) {
		
		try
		{
			Object_Field_Type="";
			Object_Field_Name="";
			
			Class<?> C1=Class.forName(ClassName);
			Constructor<?> Constructor=C1.getConstructor(WebDriver.class,int.class);
			Object obj=Constructor.newInstance((WebDriver) Driver.driverMapObj.get("WEBDRIVER"),60);
			Field f1=obj.getClass().getField(objectName);
			element =(WebElement)f1.get(obj);
			
			FindBy findByAnnotation=f1.getAnnotation(FindBy.class);
			
			if(!findByAnnotation.xpath().isEmpty())
			{
				Object_Field_Type="xpath";
				Object_Field_Name=findByAnnotation.xpath();
			}
			else if(!findByAnnotation.id().isEmpty())
			{
				Object_Field_Type="id";
				Object_Field_Name=findByAnnotation.id();
			}
			else if(!findByAnnotation.name().isEmpty())
			{
				Object_Field_Type="name";
				Object_Field_Name=findByAnnotation.name();
			}
			else if(!findByAnnotation.linkText().isEmpty())
			{
				Object_Field_Type="linkText";
				Object_Field_Name=findByAnnotation.linkText();
			}
			else if(!findByAnnotation.partialLinkText().isEmpty())
			{
				Object_Field_Type="partialLinkText";
				Object_Field_Name=findByAnnotation.partialLinkText();
			}
			else if(!findByAnnotation.css().isEmpty())
			{
				Object_Field_Type="css";
				Object_Field_Name=findByAnnotation.css();
			}
			else if(!findByAnnotation.className().isEmpty())
			{
				Object_Field_Type="className";
				Object_Field_Name=findByAnnotation.className();
			}

			return element;
			
			
			
		}
		catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		return element;
	}
  
}
