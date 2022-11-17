package com.AutomationFramework.com.custom;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import com.AutomationFramework.com.DriverEngine.Driver;
import com.AutomationFramework.com.KeywordHelper.WebAppKeywordHelper;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.Wrapper.ResponseWrapper;


public class CustomFunctions {
	public static final Logger logger = LoggerFactory.getLogger(CustomFunctions.class);
	private static final boolean flag = true;
	public static WebDriver driver=null;
	public static Properties prop = null;
	public static boolean Remote_Browser_Opened=false;
	private static SecretKeySpec secretKey;
	private static byte[] key;
	
public class ScreenShotRemoteWebDriver extends RemoteWebDriver implements TakesScreenshot
{
	public ScreenShotRemoteWebDriver(URL url,ChromeOptions options)
	{
		super(url,options);
	}
}
	
	public void getURL(String URL)
	{
		CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		try
		{   
			
			prop=CustomFunctions_OBJ.ApplicationCommonProperties();
			
			System.out.println("prop: " + prop.getProperty("Selenium_Remote_Execution").toString().trim());
			
			if(prop.getProperty("Selenium_Remote_Execution").toString().trim().compareToIgnoreCase("true")==0)
			{
				//new DesiredCapabilities();
				DesiredCapabilities cap=DesiredCapabilities.chrome();
				cap.setBrowserName("chrome");
				
				cap.setPlatform(Platform.WINDOWS);
				cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				
				ChromeOptions options = new ChromeOptions();
				options.setHeadless(true);
				//option.add_argument("headless");
				options.merge(cap);
				//WebDriverManager.chromedriver().setup();
				
				
				
				String Remote_URL=prop.getProperty("Selenium_Grid_URL").trim();
				WebDriver driver=new ScreenShotRemoteWebDriver(new URL(Remote_URL),options);
				driver.manage().window().maximize();
				driver.get(URL);
				System.out.println("Page title without browser: " + driver.getTitle());
				Remote_Browser_Opened=true;
				Driver.driverMapObj.put("WEBDRIVER", driver);

			}
			else
			{
			
			logger.info("Setting up the chrome Driver");
			//System.setProperty("webdriver.chrome.driver", EnvironmentVariables.current_dir+"/Drivers/chromedriver.exe");
			
			DesiredCapabilities cap=DesiredCapabilities.chrome();
			cap.setBrowserName("chrome");
			
			cap.setPlatform(Platform.WINDOWS);
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			
			
			//option.add_argument("headless");
			

			ChromeOptions options = new ChromeOptions();
			//options.setHeadless(true);
			options.merge(cap);
			
			try {
				driver=WebDriverManager.chromedriver().capabilities(options).create();
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				;
			}
			

			/*driver = new ChromeDriver(chromeOptions);
			DesiredCapabilities cap=DesiredCapabilities.chrome();
			cap.setBrowserName("chrome");
			
			cap.setPlatform(Platform.WINDOWS);
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			
			
			//option.add_argument("headless");
			

			ChromeOptions options = new ChromeOptions();
			options.setHeadless(true);
			options.merge(cap);
			driver = new ChromeDriver(options);*/
			
			logger.info("The URL"+URL);
			driver.get(URL);
			driver.manage().window().maximize();
			Remote_Browser_Opened=true;
			Driver.driverMapObj.put("WEBDRIVER", driver);
			
			logger.info("Browser opened and maximized the window sucessfully");
			}
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}
	}
	
	
	public boolean Store_Value(String Value,String CoulmnName)
	{
		//FileOutputStream fileOut=null;
		InputStream inp=null;
		org.apache.poi.ss.usermodel.Workbook dataWB=null;
		boolean result=true;
		
		try
		{   
			
			String SheetName=Driver.driverMapObj.get("Data_Sheet_Name").toString();
			String dataID=Driver.driverMapObj.get("Data_Sheet_ID").toString();
			String FilePath=Driver.driverMapObj.get("Data_Sheet_Path").toString();
			
			inp=new FileInputStream(FilePath);
			dataWB=WorkbookFactory.create(inp);
			
			org.apache.poi.ss.usermodel.Sheet Sheet=null;
			
			try
			{
				Sheet=dataWB.getSheet(SheetName);
			}
			catch(Exception e)
			{
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				result=false;
			}
			
			
			Row rowWB=null;
			int rowNo=Sheet.getLastRowNum()+1;
			int rowItrCount=0;
			
			int col=Driver.findCol(Sheet,"DSID");
			
			if(col==-1)
			{
				logger.info("Error:Column name not found in the excel sheet");
				result=false;
			}
			
			int flag=0;
			for(Row rowIter : Sheet)
			{
				rowItrCount++;
				if(rowIter.getCell(col)==null || rowIter.getCell(col).getStringCellValue().equals(""))
				{
					;
				}
				else
				{
					if(rowIter.getCell(col).getStringCellValue().trim().equals(dataID))
					{
						rowWB=rowIter;
						flag=1;
						break;
					}
				}
				
				if(rowItrCount>rowNo)
				{
					break;
				}
			}
			
			
			if(flag==0)
			{
				rowWB=Sheet.createRow(rowNo+1);
				rowWB.createCell(col).setCellValue(dataID);
			}
			
			col=Driver.findCol(Sheet,CoulmnName);
			
			if(col==-1)
			{
				logger.info("Error: Column Name not found");
				result=false;
			}
			
			rowWB.createCell(col).setCellValue(Value);
			
			if(FilePath.contains(".xlsx"))
			{
				XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook)dataWB);
			}
			else
			{
				HSSFFormulaEvaluator.evaluateAllFormulaCells((HSSFWorkbook)dataWB);
			}
			
			
			FileOutputStream fileOut=new FileOutputStream(FilePath);
			dataWB.write(fileOut);
			fileOut.flush();
			fileOut.close();

			logger.info("Data Updated sucessfully in the excel sheet"+SheetName+" Column "+CoulmnName);
			
			
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
		}
		finally
		{
			try {
				inp.close();
				dataWB.close();			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			}
			
		}
		
		return result;
	}
	
	public void closedriver()
	{
		try
		{  
			
		//	driver.quit();
			
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
		}
	}
	
	
	public Properties ApplicationProperties() throws Exception
	{
		 FileInputStream fis = null;
	     
		 fis = new FileInputStream(EnvironmentVariables.Application_Properties+AutomationMainClass.environment+".properties");
         prop = new Properties();
         prop.load(fis);
	      
	    /*  try {
	          fis = new FileInputStream(EnvironmentVariables.Application_Properties+AutomationMainClass.environment+".properties");
	          prop = new Properties();
	          prop.load(fis);
	       } catch(FileNotFoundException fnfe) {
	          fnfe.printStackTrace();
	       } catch(IOException ioe) {
	          ioe.printStackTrace();
	       } finally {
	          try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			}
	       }*/
	      
	   return prop;
	}
	
	public Properties ApplicationCommonProperties()
	{
		 FileInputStream fis = null;
	     
	      
	      try {
	          fis = new FileInputStream(EnvironmentVariables.Application_Common_Properties);
	          prop = new Properties();
	          prop.load(fis);
	       } catch(FileNotFoundException fnfe) {
	          fnfe.printStackTrace();
	       } catch(IOException ioe) {
	          ioe.printStackTrace();
	       } finally {
	          try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			}
	       }
	      
	   return prop;
	}
	
	public Properties ApplicationCommonProperties1() throws FileNotFoundException,IOException
	{
		 FileInputStream fis = null;
	     
	          fis = new FileInputStream(EnvironmentVariables.Application_Common_Properties);
	          prop = new Properties();
	          prop.load(fis);
	      
	   return prop;
	}
	
	public Properties Load_TestExecutioData_Properties() throws Exception
	  {
		Properties properties=new Properties();
		
			 FileInputStream fis = null;
             fis = new FileInputStream(EnvironmentVariables.TestExecutionData_Properties);
		     properties.load(fis);
		      
		   return properties;
	  }
	
	
	public Properties GetSQLQueryProperties()
	{
		 FileInputStream fis = null;
		 Properties Properties = null;
	      
	      try {
	    	  logger.info("Entering into the Method: GetSQLQueryProperties");
	    	  
	          fis = new FileInputStream(EnvironmentVariables.SQLQuery_Properties);
	          Properties = new Properties();
	          Properties.load(fis);
	          
	          logger.info("All the SQL Properties are loaded Successfully");
	       } catch(FileNotFoundException fnfe) {
	          fnfe.printStackTrace();
	       } catch(IOException ioe) {
	          ioe.printStackTrace();
	       } finally {
	          try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			}
	       }
	      
	   return Properties;
	}
	
	public int Count_Occurrences(String expression,char CountChar)
	{
		int count=0;
		try
		{
			for (int i = 0; i < expression.length(); i++) {
			    if (expression.charAt(i) == CountChar) {
			        count++;
			    }
			}
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
		}
		return count;
	}


	
	public String CurrentDate()
	{
		String CurrentDate=null;
		try
		{
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");  
			LocalDateTime now = LocalDateTime.now();  
			CurrentDate=dtf.format(now);
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
		}
		return CurrentDate;
	}
	
	public static void setKey(final String myKey) {
	    MessageDigest sha = null;
	    try {
	      key = myKey.getBytes("UTF-8");
	      sha = MessageDigest.getInstance("SHA-1");
	      key = sha.digest(key);
	      key = Arrays.copyOf(key, 16);
	      secretKey = new SecretKeySpec(key, "AES");
	    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
	      e.printStackTrace();
	    }
	  }

	  public static String encrypt(final String strToEncrypt, final String secret) {
	    try {
	      setKey(secret);
	      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	      return Base64.getEncoder()
	        .encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	    } catch (Exception e) {
	      System.out.println("Error while encrypting: " + e.toString());
	    }
	    return null;
	  }

	  public static String decrypt(final String strToDecrypt, final String secret) {
	    try {
	      setKey(secret);
	      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	      cipher.init(Cipher.DECRYPT_MODE, secretKey);
	      return new String(cipher.doFinal(Base64.getDecoder()
	        .decode(strToDecrypt)));
	    } catch (Exception e) {
	      System.out.println("Error while decrypting: " + e.toString());
	    }
	    return null;
	  }
	  
		public String getdescryptedDBpassword(String property) 
		{
			final String secretKey = "!@#$%^&*()AutomationDataScienceKeyGeneration!@#$%^&*()";
	 	    String decryptedString = CustomFunctions.decrypt(property, secretKey) ;
			return decryptedString;
		}

}
