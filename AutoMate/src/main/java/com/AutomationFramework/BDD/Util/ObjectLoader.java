package com.AutomationFramework.BDD.Util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cucumber.listener.Reporter;

import com.AutomationFramework.com.DriverEngine.Driver;
import com.Wrapper.ResponseWrapper;

public class ObjectLoader {
	public static final Logger logger = LoggerFactory.getLogger(ObjectLoader.class);
	
	
	
	
	public WebElement LoadObject(String object_Field_Name, String object_Field_Type) 
	{
		WebElement element=null;
		try
		{
			Thread.sleep(1000);
			WebDriver driver=(WebDriver) Driver.driverMapObj.get("WEBDRIVER");
			
			
			By by=getByObject(object_Field_Name,object_Field_Type);
			
			waitForJavaScriptToLoad(driver);
			waitForJQueryToLoad(driver);
			
			WebDriverWait wait=new WebDriverWait(driver,10);
			
			element=wait.until(ExpectedConditions.presenceOfElementLocated(by));
			
			if(!waitForElementToBeVisisble(element,wait))
			 return null;
			 
			waitForElementToBeClickable(element,wait);
			
			boolean f1=false;
			
			for(int i=0;i<30;i++)
			{
				try {
					element.getTagName();
					f1=true;
				}
				catch(StaleElementReferenceException e)
				{
					f1=false;
				}
				
				if(f1=true)
					break;
				else
					Thread.sleep(1000);
					
			}
			
			
			
		}
		catch(NullPointerException e)
		{
			logger.error("Error: Issue in fiding the object "+object_Field_Name +":"+object_Field_Type);
			//Reporter.addStepLog("Error: Issue in fiding the object "+object_Field_Name +":"+object_Field_Type);
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e)
		{
			logger.error("Error: Issue in fiding the object "+object_Field_Name +":"+object_Field_Type);
			//Reporter.addStepLog("Error: Issue in fiding the object "+object_Field_Name +":"+object_Field_Type);
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		return element; 
	}

	private void waitForElementToBeClickable(WebElement element, WebDriverWait wait) {
		// TODO Auto-generated method stub
		
	}

	private boolean waitForElementToBeVisisble(WebElement element, WebDriverWait wait) {
		
		boolean result=false;
			try
			{
				Actions hover=new Actions((WebDriver) Driver.driverMapObj.get("WEBDRIVER"));
				hover.moveToElement(element).perform();
			}
			catch(Exception e)
			{
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			}
			
			WebDriver driver=(WebDriver) Driver.driverMapObj.get("WEBDRIVER");
			
			try
			{
				wait.until(ExpectedConditions.visibilityOf(element));
				result=true;
			}
			catch(StaleElementReferenceException e)
			{
				
				logger.info("Error: Element is Stale");
				Reporter.addStepLog("Error: Element is Stale");
				
				try
				{
					new WebDriverWait(driver,30).until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
					result=true;
				}
				catch(Exception e1)
				{
					logger.error(e1.getLocalizedMessage());
					
					Reporter.addStepLog("Exception occured"+e1.getStackTrace());
					e1.printStackTrace();
				}
			}
			catch(Exception e)
			{
				logger.error(e.getLocalizedMessage());
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				//Reporter.addStepLog("Exception occured"+e.getStackTrace());
				//e.printStackTrace();
			}
		
		return result;
	}

	private void waitForJQueryToLoad(WebDriver driver) {
		

		
		try
		{
			WebDriverWait wait=new WebDriverWait(driver,30);
			ExpectedCondition<Boolean> jQueryLoad=new ExpectedCondition<Boolean>()
			{
				public Boolean apply(WebDriver driver)
				{
					try
					{
					return ((Long)((JavascriptExecutor)driver).executeScript("return jQuery.active")==0);
					}
					catch(Exception e)
					{
						return true;
					}
				}
			};
			
			wait.until(jQueryLoad);
			
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
		}
		
	
		
	}

	private void waitForJavaScriptToLoad(WebDriver driver) {
		
		try
		{
			WebDriverWait wait=new WebDriverWait(driver,30);
			ExpectedCondition<Boolean> jsLoad=new ExpectedCondition<Boolean>()
			{
				public Boolean apply(WebDriver driver)
				{
					return ((JavascriptExecutor)driver).executeScript("return document.readyState").toString().equals("complete");
				}
			};
			
			wait.until(jsLoad);
			
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
	}

	private By getByObject(String object_Field_Name, String object_Field_Type) {
		
		By by=null;
		
		if(object_Field_Type.equalsIgnoreCase("XPATH"))
		{
			by=By.xpath(object_Field_Name);
		}
		else if(object_Field_Type.equalsIgnoreCase("ID"))
		{
			by=By.id(object_Field_Name);
		}
		else if(object_Field_Type.equalsIgnoreCase("NAME"))
		{
			by=By.name(object_Field_Name);
		}
		else if(object_Field_Type.equalsIgnoreCase("CLASSNAME"))
		{
			by=By.className(object_Field_Name);
		}
		else if(object_Field_Type.equalsIgnoreCase("CSS"))
		{
			by=By.cssSelector(object_Field_Name);
		}
		else if(object_Field_Type.equalsIgnoreCase("LINK") || object_Field_Type.equalsIgnoreCase("LINKTEXT"))
		{
			by=By.linkText(object_Field_Name);
		}
		else if(object_Field_Type.equalsIgnoreCase("PARTIALLINK") || object_Field_Type.equalsIgnoreCase("PARTIALLINKTEXT"))
		{
			by=By.partialLinkText(object_Field_Name);
		}
		else 
		{
			logger.info("Error: Invalid Object Type");
			Reporter.addStepLog("Error: Invalid Object Type");
		}
		
		return by;
	}

}
