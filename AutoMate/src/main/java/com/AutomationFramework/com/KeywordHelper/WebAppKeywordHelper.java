package com.AutomationFramework.com.KeywordHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cucumber.listener.Reporter;
import com.AutomationFramework.com.DriverEngine.Driver;

public class WebAppKeywordHelper {
	public static final Logger logger = LoggerFactory.getLogger(WebAppKeywordHelper.class);
	public WebDriver webdriver=(WebDriver) Driver.driverMapObj.get("WEBDRIVER");
	public static String PageText=null;
	
	
	public boolean Click_Button(String webDriveraction,String Object_Field_Name,String Object_Field_Type,String value)
	{
		boolean flag=true;
		try
		{
			WebElement element;
			
			element=getElementLocation(Object_Field_Name,Object_Field_Type);
			element.click();
			logger.info("Button is clicked");
			
		}
		catch(Exception e)
		{
			logger.error("Error: Exception in method"+webDriveraction);
			//Reporter.addStepLog("Error: Exception in method"+webDriveraction);
			flag=false;
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean Hover(String webDriveraction,String Object_Field_Name,String Object_Field_Type,String value)
	{
		boolean flag=true;
		try
		{
			WebElement element;
			
			element=webdriver.findElement(this.getObject(Object_Field_Name,Object_Field_Type));
			//element = new WebDriverWait(webdriver, 20).until(ExpectedConditions.visibilityOfElementLocated(this.getObject(Object_Field_Name,Object_Field_Type)));
			
			Actions hover=new Actions(webdriver);
			hover.moveToElement(element).perform();
			logger.info("Hover is Success");
			
		}
		catch(Exception e)
		{
			logger.error("Error: Exception in method"+webDriveraction);
			flag=false;
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean KeyPress(String webDriveraction,String Object_Field_Name,String Object_Field_Type,String value)
	{
		boolean flag=true;
		try
		{
			Actions Action=new Actions(webdriver);
			
			if(value.equalsIgnoreCase("TAB"))
			{
				Action.sendKeys(Keys.TAB).build().perform();
			}
			else if(value.equalsIgnoreCase("ENTER"))
			{
				Action.sendKeys(Keys.ENTER).build().perform();
			}
			else if(value.equalsIgnoreCase("PAGE DOWN"))
			{
				Action.sendKeys(Keys.PAGE_DOWN).build().perform();
			}
			else if(value.equalsIgnoreCase("PAGE UP"))
			{
				Action.sendKeys(Keys.PAGE_UP).build().perform();
			}
			else if(value.equalsIgnoreCase("HOME"))
			{
				Action.sendKeys(Keys.HOME).build().perform();
			}
			else if(value.equalsIgnoreCase("END"))
			{
				Action.sendKeys(Keys.END).build().perform();
			}
			else if(value.equalsIgnoreCase("DOWN"))
			{
				Action.sendKeys(Keys.DOWN).build().perform();
			}
			else if(value.equalsIgnoreCase("UP"))
			{
				Action.sendKeys(Keys.UP).build().perform();
			}
			else if(value.equalsIgnoreCase("CTRL+END"))
			{
				Action.sendKeys(Keys.CONTROL,Keys.END).build().perform();
			}
			else if(value.equalsIgnoreCase("CTRL+HOME"))
			{
				Action.sendKeys(Keys.CONTROL,Keys.HOME).build().perform();
			}
			else
			{
				logger.info("No Key selected");
			}
			logger.info("Key Press successfully");
			
		}
		catch(Exception e)
		{
			logger.error("Error: Exception in method"+webDriveraction);
			Reporter.addStepLog("Error: Exception in method"+webDriveraction);
			flag=false;
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean Enter_Text(String webDriveraction,String Object_Field_Name,String Object_Field_Type,String value)
	{
		boolean flag=true;
		try
		{
			WebElement element;
			
			if(value!=null && !value.isEmpty())
			{
				element=getElementLocation(Object_Field_Name,Object_Field_Type);
				element.clear();
				element.sendKeys(value);
			logger.info("Data enterted Successfully");
			}
			else
			{
				logger.info("Error: Value to Enter the Data is empty");
				flag=false;
			}
			
		}
		catch(Exception e)
		{
			logger.error("Error: Exception in method"+webDriveraction);
			Reporter.addStepLog("Error: Exception in method"+webDriveraction);
			flag=false;
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean Get_Text(String webDriveraction,String Object_Field_Name,String Object_Field_Type,String value)
	{
		boolean flag=true;
		PageText=null;
		try
		{
			WebElement element;
			
			if(value!=null && !value.isEmpty())
			{
				element=getElementLocation(Object_Field_Name,Object_Field_Type);
				PageText=element.getText();
			logger.info("Data enterted Successfully");
			}
			else
			{
				logger.info("Error: Value to Enter the Data is empty");
				flag=false;
			}
			
		}
		catch(Exception e)
		{
			logger.error("Error: Exception in method"+webDriveraction);
			Reporter.addStepLog("Error: Exception in method"+webDriveraction);
			flag=false;
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean Click_Radio_Button(String webDriveraction,String Object_Field_Name,String Object_Field_Type,String value)
	{
		boolean flag=true;
		try
		{
			WebElement element;
			
			element=getElementLocation(Object_Field_Name,Object_Field_Type);
			element.click();
			logger.info("Raido button is clicked");
			
		}
		catch(Exception e)
		{
			logger.error("Error: Exception in method"+webDriveraction);
			Reporter.addStepLog("Error: Exception in method"+webDriveraction);
			flag=false;
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean Clear_Text(String webDriveraction,String Object_Field_Name,String Object_Field_Type,String value)
	{
		boolean flag=true;
		try
		{
			WebElement element;
			
			element=getElementLocation(Object_Field_Name,Object_Field_Type);
			element.clear();
			logger.info("Element data is cleared correctly");
			
		}
		catch(Exception e)
		{
			logger.error("Error: Exception in method"+webDriveraction);
			Reporter.addStepLog("Error: Exception in method"+webDriveraction);
			flag=false;
			e.printStackTrace();
		}
		
		return flag;
	}
	
	
	public boolean Select_List_Value(String webDriveraction,String Object_Field_Name,String Object_Field_Type,String value)
	{
		boolean flag=true;
		try
		{
	
			Select dropdown=new Select(webdriver.findElement(this.getObject(Object_Field_Name,Object_Field_Type)));
			
			if(value.startsWith("index="))
			{
				int index=Integer.parseInt(value.split("=")[1]);
				dropdown.selectByIndex(index);
			}
			else
			{
				dropdown.selectByVisibleText(value);
			}
			
			logger.info("Value Selected");
			
		}
		catch(Exception e)
		{
			logger.error("Error: Exception in method"+webDriveraction);
			//Reporter.addStepLog("Error: Exception in method"+webDriveraction);
			flag=false;
			e.printStackTrace();
		}
		
		return flag;
	}

	public boolean Pause(String webDriveraction,String Object_Field_Name,String Object_Field_Type,String value)
	{
		boolean flag=true;
		try
		{
			int n=Integer.parseInt(value)*1000;
			Thread.sleep(n);
			logger.info("Script in Sleep for"+n+" Seconds");
			
		}
		catch(Exception e)
		{
			logger.error("Error: Exception in method"+webDriveraction);
			Reporter.addStepLog("Error: Exception in method"+webDriveraction);
			flag=false;
			e.printStackTrace();
		}
		
		return flag;
	}

	private WebElement getElementLocation(String object, String object_Field_Type) {
		
		WebElement element=null;
		try
		{
			if(object_Field_Type.equalsIgnoreCase("XPATH"))
			{
				element=(new WebDriverWait(webdriver,10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(object)));
			}
			else if(object_Field_Type.equalsIgnoreCase("ID"))
			{
				element=(new WebDriverWait(webdriver,10)).until(ExpectedConditions.presenceOfElementLocated(By.id(object)));
			}
			else if(object_Field_Type.equalsIgnoreCase("NAME"))
			{
				element=(new WebDriverWait(webdriver,10)).until(ExpectedConditions.presenceOfElementLocated(By.name(object)));
			}
			else if(object_Field_Type.equalsIgnoreCase("CLASSNAME"))
			{
				element=(new WebDriverWait(webdriver,10)).until(ExpectedConditions.presenceOfElementLocated(By.className(object)));
			}
			else if(object_Field_Type.equalsIgnoreCase("CSS"))
			{
				element=(new WebDriverWait(webdriver,10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(object)));
			}
			else if(object_Field_Type.equalsIgnoreCase("LINK") || object_Field_Type.equalsIgnoreCase("LINKTEXT"))
			{
				element=(new WebDriverWait(webdriver,10)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(object)));
			}
			else if(object_Field_Type.equalsIgnoreCase("PARTIALLINK") || object_Field_Type.equalsIgnoreCase("PARTIALLINKTEXT"))
			{
				element=(new WebDriverWait(webdriver,10)).until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(object)));
			}
			
		}
		catch(Exception e)
		{
			logger.error("Error: Exception in object location "+object);
			Reporter.addStepLog("Error: Exception in object location "+object);
			e.printStackTrace();
		}
		return element;
	}
	
	private By getObject(String object, String object_Field_Type) throws Exception {
			
			
				if(object_Field_Type.equalsIgnoreCase("XPATH"))
				{
					return By.xpath(object);
				}
				else if(object_Field_Type.equalsIgnoreCase("ID"))
				{
					return By.id(object);
				}
				else if(object_Field_Type.equalsIgnoreCase("NAME"))
				{
					return By.name(object);
				}
				else if(object_Field_Type.equalsIgnoreCase("CLASSNAME"))
				{
					return By.className(object);
				}
				else if(object_Field_Type.equalsIgnoreCase("CSS"))
				{
					return By.cssSelector(object);
				}
				else if(object_Field_Type.equalsIgnoreCase("LINK") || object_Field_Type.equalsIgnoreCase("LINKTEXT"))
				{
					return By.linkText(object);
				}
				else if(object_Field_Type.equalsIgnoreCase("PARTIALLINK") || object_Field_Type.equalsIgnoreCase("PARTIALLINKTEXT"))
				{
					return By.partialLinkText(object);
				}
				else
				{
					throw new Exception("Wrong object Type");
				}
				
			
		}

}
