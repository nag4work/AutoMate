package com.AutomationFramework.com.DriverEngine;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.AutomationFramework.BDD.Runner.CustomClass;
import com.AutomationFramework.com.KeywordHelper.WebAppKeywordHelper;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.custom.EnvironmentVariables;

public class Driver {
	public static final Logger logger = LoggerFactory.getLogger(Driver.class);
	public static HashMap driverMapObj=new HashMap();
	

	public boolean invokeWebAppKeywordMethod(String webDriveraction, String object_Field_Name,String object_Field_Type, String value) throws Exception 
	{
		
		String methodName=webDriveraction;
		boolean result=false;
		
		logger.info("info: Keyword Name:"+webDriveraction);
		Class C1=Class.forName("com.AutomationFramework.com.KeywordHelper.WebAppKeywordHelper");

		 //Class C1=Class.forName("com.com.KeywordHelper.WebAppKeywordHelper");

		
		
		Class PartTypes[]=new Class[] {String.class,String.class,String.class,String.class};
		
		Object params[]=new Object[4];
		params[0]=webDriveraction;
		params[1]=object_Field_Name;
		params[2]=object_Field_Type;
		params[3]=value;
		
		Method mthd=C1.getMethod(methodName, PartTypes);
		result=(Boolean)mthd.invoke(new WebAppKeywordHelper() , (Object[]) params);
		
		logger.info("info: Invoke Method returns"+result);
		
		return result;
	}


	public static int findCol(Sheet sheet, String ColName) {
		Row row=null;
		
		int colCount=0;		
		row=sheet.getRow(0);
		
			if(!(row==null))
			{
				colCount=row.getLastCellNum();
			}
			else
			{
				colCount=0;	
			}
			
		if(ColName.equalsIgnoreCase("MATCH_ADDRSS"))
			logger.info("info: Total Column Count"+colCount);
		
	    for(int j=0;j<colCount;j++)
	    {
	    	if(!(row.getCell(j)==null))
	    	{
	    		if(ColName.equalsIgnoreCase("MATCH_ADDRSS"))
	    			logger.info("info: Excel Col:"+row.getCell(j).toString().trim()+"--");
	    		if(row.getCell(j).toString().trim().equalsIgnoreCase(ColName) || row.getCell(j).toString().trim().equalsIgnoreCase((ColName + "[][String]")) )
	    		{
	    			return j;
	    		}
	    			
	    	}
	    }
		return -1;
	}
	
	
	public void getFailureScreenshot(WebDriver driver,String TestCaseName,String DSID)
	{
		String timeStamp_File=null;
		String timeStamp_image=null;
		timeStamp_File = new SimpleDateFormat("yyyy.MM.dd'T'HH.mm.ss").format(AutomationMainClass.TimeStamp);
		timeStamp_image = new SimpleDateFormat("yyyy.MM.dd'T'HH.mm.ss").format(new Date());
		
		 File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

	        try {
	            FileUtils.copyFile(screenshot, new File(EnvironmentVariables.current_dir+CustomClass.Screenshotpath
	            		+File.separator+TestCaseName.concat("_").concat(timeStamp_File)
	            		+File.separator+timeStamp_image+".png"));
	        } catch (IOException e) {
	        	logger.info(e.getMessage());
	        }
	}

}
