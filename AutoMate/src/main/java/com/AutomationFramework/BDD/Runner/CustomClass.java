package com.AutomationFramework.BDD.Runner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cucumber.listener.ExtentProperties;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import cucumber.runtime.ClassFinder;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import com.AutomationFramework.com.DriverEngine.Driver;
import com.AutomationFramework.com.KeywordHelper.WebAppKeywordHelper;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.custom.EnvironmentVariables;
import com.Wrapper.ResponseWrapper;

public class CustomClass {
	
	public static String ExtentReportsLocationFileName=null;
	public static ExtentTest extentTest;
	public static String Screenshotpath=null;
	//public static final Logger logger = Logger.getLogger(CustomClass.class);
	public static final Logger logger = LoggerFactory.getLogger(CustomClass.class);
	//public static final ExtentReports extent=new ExtentReports(ExtentReportsLocationFileName,true);
	public static ExtentReports extent=null;
	public static String LogFolder=null;
	public static int passed=0;
    public static int failed=0;
    
	
	public boolean CucumberRun(String testCaseID) throws IOException,Exception
	{
		boolean flag=true;
		CustomClass CustomClass_obj=new CustomClass();
		CustomFunctions customfunctions_obj=new CustomFunctions();
		
		
		//try{
		
			/****************************************Loading Properties and Reports ******************************************/
			customfunctions_obj.ApplicationCommonProperties();
			CustomClass_obj.reports(testCaseID);
			
			
			
			ClassLoader classLoader=CustomClass.class.getClassLoader();
			ResourceLoader resourceLoader=new MultiLoader(classLoader);
			ClassFinder classFinder=new ResourceLoaderClassFinder(resourceLoader,classLoader);
			
			List<String> PluginList=new ArrayList<String>();

			PluginList.add("--plugin");
			PluginList.add("html:target/cucumber-html-report");
			PluginList.add("--plugin");
			PluginList.add("json:target/cucumber.json");
			//PluginList.add("--plugin");
			//PluginList.add("com.cucumber.listener.ExtentCucumberFormatter:");
			
		   
			
			RuntimeOptions ro=new RuntimeOptions(PluginList);
			
			ro.getFilters().add("@"+testCaseID);
			//ro.getGlue().add("com.BDD.StepDefinations");
			//ro.getFeaturePaths().add("classpath:lithia/BDD/Features");
			ro.getGlue().add("com.AutomationFramework.BDD.StepDefinations");
			ro.getFeaturePaths().add("classpath:Features");
			//ro.getFeaturePaths().add("lithia/AutomationFramework/BDD/Features");
			
			cucumber.runtime.Runtime runtime=new cucumber.runtime.Runtime(resourceLoader,classFinder,classLoader,ro);
			runtime.run();
			
			List<Throwable> errorList=runtime.getErrors();
			
			if(errorList.isEmpty())
			{
				passed+=1;
				
			}
			else
			{
				Driver Driver_OBJ=new Driver();
				WebAppKeywordHelper WebAppKeywordHelper_OBJ=new WebAppKeywordHelper();
				Driver_OBJ.getFailureScreenshot(WebAppKeywordHelper_OBJ.webdriver, AutomationMainClass.TestCase_Name, "");
				
				failed+=1;
				
			}
			
			/*	if(errorList.isEmpty())
			{
				logger.info("Test case passed and test case name"+testCaseID);
				extentTest.log(LogStatus.PASS, "Test Case : "+testCaseID+"Passed");
				passed+=1;
				return flag;
			}
			else
			{
				logger.info("Test case failed and test case name"+testCaseID);
				extentTest.log(LogStatus.FAIL, "Test Case : "+testCaseID+"Failed");
				failed+=1;
				ResponseWrapper.setMessage(errorList.toString());
				return false;
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			logger.debug(e.getLocalizedMessage());
		}*/
		
		return flag;
	}
	

	
	
	public void reports(String testCaseID)
	{
		
		try
		{
			
			
			ExtentReportsLocationFileName=CustomFunctions.prop.getProperty("Extent_Report_Path")+new SimpleDateFormat("YYY-MM-dd'T'HH:mm:ss").format(new Date()).toString().replaceAll(":","").replaceAll("-","").replaceAll("'","")+"_"+"ScreenshotReport.html";
			extent=new ExtentReports(ExtentReportsLocationFileName,true);
			
			CustomClass.extentTest=CustomClass.extent.startTest(testCaseID,"Logging");
			
			extent.loadConfig(new File(EnvironmentVariables.current_dir+CustomFunctions.prop.getProperty("Extent_Report_Xml_Path")));
			
		    LogFolder="Run_"+new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss").format(new Date()).replace(":","").replace("_","");
		   
		    ExtentProperties extentProperties= ExtentProperties.INSTANCE;
		    extentProperties.setReportPath(CustomFunctions.prop.getProperty("BDD_Report_Path")+LogFolder+CustomFunctions.prop.getProperty("BDD_Report_HTML_File_Name"));
		   
		    Screenshotpath=CustomFunctions.prop.getProperty("ScreenShot_Path");
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
	}

}
