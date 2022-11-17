package com.AutomationFramework.com.Main;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.AutomationFramework.BDD.Runner.CustomClass;
import com.AutomationFramework.BDD.StepDefinations.UI.AutomateUIProcess;
import com.AutomationFramework.BDD.StepDefinations.Utility.Util;
import com.AutomationFramework.com.DriverEngine.Driver;
import com.AutomationFramework.com.custom.AutomationNotifications;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.logs.Logging;
import com.AutomationFramework.BDD.Runner.DataClass;
import com.Wrapper.APIControllerWrapper;
import com.Wrapper.UINewRequestWrapper;
import com.Wrapper.ResponseWrapper;

public class AutomationMainClass {
	public static final Logger logger = LoggerFactory.getLogger(AutomationMainClass.class);
	
	public static String TestCase_Name=null;
	public static String environment_Global=null;
	public static String environment=null;
	public static String UserName = null;
	public static String Email_Notification="N";
	public static String Email_Address=null;
	public static Timestamp TimeStamp=null;
	public static String email_mode="regular";
	
	
	
	public void main(APIControllerWrapper aPIControllerWrapper_OBJ) throws Exception,Throwable
	{
		Logging log=new Logging();
		
		log.logaction();
		
		
		AutomationMainClass obj_MainClass=new AutomationMainClass();
		AutomationNotifications OBJ_AutomationNotifications=new AutomationNotifications();
		String SheetName=null;
		String DataSheetPath=null;
		String dataRow=null;
		CustomFunctions customfunctions_obj=new CustomFunctions();
		Properties prop=null;
		boolean flag=true;
		
		try {
				

		/******************************* Logger Settings Method*************************************************************/
		
			TimeStamp=new Timestamp(System.currentTimeMillis());
			
			TestCase_Name=aPIControllerWrapper_OBJ.getTestCase_ID();
			environment_Global=aPIControllerWrapper_OBJ.getEnv();
			UserName=aPIControllerWrapper_OBJ.getUserID();
			SheetName=aPIControllerWrapper_OBJ.getSheetName();
			dataRow=aPIControllerWrapper_OBJ.getDataRows();
			DataSheetPath=aPIControllerWrapper_OBJ.getDataSheetPath();
			//DataSheetPath="WebServicesExecution";
			
			if(aPIControllerWrapper_OBJ.getNotification()!=null)
			{
				Email_Notification=aPIControllerWrapper_OBJ.getNotification().toUpperCase();
				
				if(aPIControllerWrapper_OBJ.getEmail_address()!=null)
				{
					Email_Address=aPIControllerWrapper_OBJ.getEmail_address();
				}
				else
				{
					//EXCEPTION
				}
			}

			/****************************************Loading Properties and Reports ******************************************/
			prop=customfunctions_obj.ApplicationCommonProperties1();
			
			DataClass.APIGetCallhmap.put("DataSource_Excel_True",prop.getProperty("Results_In_Excel"));
			DataClass.APIGetCallhmap.put("STORE_RESULTS_IN_DB",prop.getProperty("STORE_RESULTS_IN_DB"));
			
			/*******************************************************************************************************************/
			/******************************* Calling the Execute Test Method****************************************************/
			/*******************************************************************************************************************/
			
		    if(aPIControllerWrapper_OBJ.getInputintake().toString().compareToIgnoreCase("DB")==0)
		    {
		    	System.out.println("Enterning into the DB loop");
		    	flag=obj_MainClass.ExecuteDBDataProcessing(aPIControllerWrapper_OBJ,dataRow);
		    }
		    else
		    {
		    	flag=obj_MainClass.ExecuteTestDataProcessing(dataRow,SheetName,DataSheetPath);
		    }
			
			 if(flag==false)
		     {
				 logger.debug("Test case failed Please check your Request and test case name "+TestCase_Name);	
				 ResponseWrapper.setMessage("Test case failed and test case name"+TestCase_Name);
			    ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
		     }
			 else
		        {
				 	ResponseWrapper.setMessage("Automation Execution Completed successfully");
					ResponseWrapper.setStatuscode(HttpStatus.OK);
		        }
			
			
			logger.debug("*************** Execution Completed and existing Main method");
			
		}
		finally
		{
			/***************************************************************************************************************************/
			/************************************* Email Notification ******************************************************************/
			/***************************************************************************************************************************/
		try
		{
			logger.debug("*************** Execution Email Notification********************************");
			
			if((AutomationMainClass.Email_Notification.toString().compareToIgnoreCase("Y")==0)||(AutomationMainClass.Email_Notification.toString().compareToIgnoreCase("YES")==0))
			{
				String CSVFileName=null;
				String Data_Sheet_Path=null;
				if(Driver.driverMapObj.get("CSVFileName")!=null)
				{
					CSVFileName=Driver.driverMapObj.get("CSVFileName").toString();
				}
				if(Driver.driverMapObj.get("Data_Sheet_Path")!=null)
				{
					Data_Sheet_Path=Driver.driverMapObj.get("Data_Sheet_Path").toString();
				}
				OBJ_AutomationNotifications.email_notification(CSVFileName,Data_Sheet_Path);
			}
			else
			{
				logger.debug("Email notification is off");
			}
			
			
			
		} catch (Exception e) {
			
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
		}
		}
	
		
		
	}
	
	
	private boolean ExecuteDBDataProcessing(APIControllerWrapper aPIControllerWrapper_OBJ,String dataRow) throws IOException, Exception 
	{

		
		Util Obj_Util=new Util();
		AutomationMainClass obj_MainClass=new AutomationMainClass();
		boolean flag=false;
		
	
			if(aPIControllerWrapper_OBJ.getDataRows().contains(";"))
			{
				String startDSID=dataRow.split(";")[0].toString();
				String endDSID=dataRow.split(";")[1].toString();

				
				int start=Integer.parseInt(startDSID);
				int end=Integer.parseInt(endDSID);
				
				for(int DBUniqueid=start;DBUniqueid<=end;DBUniqueid++)
				{
					boolean flag_run=true;
					flag_run=Obj_Util.load_database_data_Properties(DBUniqueid, aPIControllerWrapper_OBJ);
					
					if(flag_run==true)
					{
					 flag=obj_MainClass.TestRun();
					}
				}
			}
			else if(aPIControllerWrapper_OBJ.getDataRows().contains(","))
			{
				String[] uniqueIds = aPIControllerWrapper_OBJ.getDataRows().split(",");
				
				for(int DBUniqueid=0;DBUniqueid<uniqueIds.length;DBUniqueid++)
				{
					int id=Integer.parseInt(uniqueIds[DBUniqueid]);
					boolean flag_run=true;
					flag_run=Obj_Util.load_database_data_Properties(id, aPIControllerWrapper_OBJ);
					
					if(flag_run==true)
					{
						flag=obj_MainClass.TestRun();
					}
				}
				
			}
			else
			{
				System.out.println("Before DB Call");
				boolean flag_run=true;
				int DBUniqueid=Integer.parseInt(dataRow);
				flag_run=Obj_Util.load_database_data_Properties(DBUniqueid, aPIControllerWrapper_OBJ);
				//flag=obj_MainClass.TestRun();
				
				if(flag_run==true)
				{
				flag=obj_MainClass.TestRun();
				}
			}
		
		
		return flag;
	
	}


	public boolean ExecuteTestDataProcessing(String dataRow,String SheetName, String DataSheetPath) throws Exception
	{
		
		Util Obj_Util=new Util();
		AutomationMainClass obj_MainClass=new AutomationMainClass();
		boolean flag=false;
		
	
			if(dataRow.contains(";"))
			{
				String startDSID=dataRow.split(";")[0].toString();
				String endDSID=dataRow.split(";")[1].toString();
				
				int NumberLength=startDSID.split("DS")[1].length();
				
				int start=Integer.parseInt(startDSID.split("DS")[1]);
				int end=Integer.parseInt(endDSID.split("DS")[1]);
				
				for(int i=start;i<=end;i++)
				{
					String numVal=String.valueOf(i);
					
					if(NumberLength>0)
					{
						while(numVal.length()<NumberLength)
						{
							numVal="0"+numVal;
						}
					}
					
					String DSID="DS"+numVal;
					
					Obj_Util.load_data_Properties(DSID, SheetName, DataSheetPath);
					flag=obj_MainClass.TestRun();
				}
			}
			else
			{
				Obj_Util.load_data_Properties(dataRow, SheetName, DataSheetPath);
				flag=obj_MainClass.TestRun();
			}
		
		
		return flag;
	}
	
	
	
	public boolean TestRun() throws IOException,Exception
	{
		CustomClass Obj_CustomClass=new CustomClass();
		boolean flag=true;
		//try{
		
			if(environment_Global.contains(";"))
			{
				String[] arrOfenvironment = environment_Global.split(";", 5);
				int arrOfenvironment_Size=0;
				arrOfenvironment_Size=arrOfenvironment.length;
				
				for(int i=0;i<arrOfenvironment_Size;i++)
				{
					
					environment=arrOfenvironment[i].trim().toUpperCase();
			        logger.debug("***************************************** Calling the Cucumber run method from For Loop********************************/");
				
			        flag=Obj_CustomClass.CucumberRun(TestCase_Name);
			        
			        if(flag==false)
			        {
			        	logger.debug("Test case failed and test case name"+TestCase_Name);	
			        }
			        else
			        {
			        	logger.debug("Test case Passed and test case name"+TestCase_Name);
			        }
				
				}
			}
			else
			{
				environment=environment_Global.trim().toUpperCase();
				logger.debug("***************************************** Calling the Cucumber run method directly********************************/");
				flag=Obj_CustomClass.CucumberRun(TestCase_Name);
				
			
			}
		/*}
		catch (Exception e) 
		{
			
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
		}*/
		
		return flag;
	}
	
	
	public void UI_main(UINewRequestWrapper ui_ControllerWrapper_OBJ) throws Exception,Throwable
	{
		Logging log=new Logging();
		
		log.logaction();
		
		
			
		
		
		AutomateUIProcess obj_AutomateUIProcess=new AutomateUIProcess();
		obj_AutomateUIProcess.processUIInputAutomation(ui_ControllerWrapper_OBJ);
		ResponseWrapper.setMessage("Automation Execution Completed successfully");
		ResponseWrapper.setStatuscode(HttpStatus.OK);
		
	
	
		
		
	}

}
