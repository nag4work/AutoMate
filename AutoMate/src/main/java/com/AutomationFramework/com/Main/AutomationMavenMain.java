package com.AutomationFramework.com.Main;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Wrapper.APIControllerWrapper;


public class AutomationMavenMain {
	public static final Logger logger = LoggerFactory.getLogger(AutomationMavenMain.class);



	
	public static void main(String[] args)
	{
		AutomationMainClass obj_AutomationMainClass=new AutomationMainClass();
		APIControllerWrapper aPIControllerWrapper_OBJ=new APIControllerWrapper();
		
		
		try {
				

			
			if(System.getProperty("testCase_ID")!=null)
			{
				aPIControllerWrapper_OBJ.setTestCase_ID(System.getProperty("testCase_ID"));
			}
			else
			{
				logger.error("TestCase_Name field value missing in the arguments");
			}
			
			if(System.getProperty("env")!=null)
			{
				aPIControllerWrapper_OBJ.setEnv(System.getProperty("env"));
			}
			else
			{
				logger.error("Environment field value missing in the arguments");
			}
			
			if(System.getProperty("tablename")!=null)
			{
				aPIControllerWrapper_OBJ.setTablename(System.getProperty("tablename"));
			}
			else
			{
				logger.info("tablename field value missing in the arguments and looking for SheetName");
				aPIControllerWrapper_OBJ.setDataSheetPath("WebServicesExecution");
			}
			
			if(System.getProperty("dataRows")!=null)
			{
				aPIControllerWrapper_OBJ.setDataRows(System.getProperty("dataRows"));
			}
			else
			{
				logger.error("dataRows field value missing in the arguments");
			}
			
			if(System.getProperty("notification")!=null)
			{
				aPIControllerWrapper_OBJ.setNotification(System.getProperty("notification"));
				aPIControllerWrapper_OBJ.setEmail_address(System.getProperty("email_address"));
			}
			else
			{
				aPIControllerWrapper_OBJ.setNotification("N");
			}
			

			
			aPIControllerWrapper_OBJ.setUserID(System.getProperty("userID"));
			aPIControllerWrapper_OBJ.setInputintake("DB");
			
			obj_AutomationMainClass.main(aPIControllerWrapper_OBJ);
			
			System.out.println("Execution Completed and existing Main method.");
			logger.debug("*************** Execution Completed and existing Main method");
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} catch (Throwable e) {
			
			e.printStackTrace();
		}

		
	}
	



}
