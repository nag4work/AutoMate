package com.AutomationFramework.BDD.StepDefinations.DailyMonitoring;

import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cucumber.api.java.en.Given;
import com.AutomationFramework.BDD.Runner.DataClass;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.Pojos.QueryParametersWrapper;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.impl.DataBaseConnections;
import com.AutomationFramework.com.impl.ResultsDBImplementation;
import com.AutomationFramework.com.impl.SnowFlakeDataBaseConnection;
import com.AutomationFramework.com.impl.WebServicesImpl;
import com.Wrapper.ResponseWrapper;

public class MonitoringValidations {

public static final Logger logger = LoggerFactory.getLogger(MonitoringValidations.class);
	
	WebServicesImpl WebServicesImpl_Obj=new WebServicesImpl();
	CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
	QueryParametersWrapper queryParametersWrapper_OBJ=new QueryParametersWrapper();

	public static List<String[]> Summary=new ArrayList<String[]>();
	
	@Given("^Scheduled invoked validate CPI data for daily monitoring task$")
	public void Get_ICO_Value_Using_VIN_and_UVC() throws Throwable 
	{
		SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
		CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		Statement stmt=null;
        Properties Properties = null;
        System.out.println("Control at Get_ICO_Value_Using_VIN_and_UVC Call");

		try {

			 String CurrentDate="";
			 /*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
			 LocalDateTime now = LocalDateTime.now();
			 CurrentDate=dtf.format(now);*/
			 
			 Date date = new Date();
			 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd");
			 TimeZone pstTime = TimeZone.getTimeZone("PST");
			 pstFormat.setTimeZone(pstTime);
			 CurrentDate=pstFormat.format(date);
			 
			
			SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
			Properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
	        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
	        
	        getCPIDatafromDataBase(stmt,Properties,CurrentDate);
	        
	        getStatesDatafromDataBase(stmt,Properties,CurrentDate);
	        
	        getDrillDownDatafromDataBase(stmt,Properties,CurrentDate);
	        
	        getRetailListingDatafromDataBase(stmt,Properties,CurrentDate);
	        
	        
	        if((MonitoringWrapper.BB_CPI_MODELLIST_Subject.contains("Urgent")) ||(MonitoringWrapper.BB_STATE_STATELIST_Subject.contains("Urgent"))
	        			||(MonitoringWrapper.BB_DRILLDOWN_STYLELIST_Subject.contains("Urgent")) ||(MonitoringWrapper.BB_RETAI_LISTINGS_Subject.contains("Urgent")))
	        {  	
	        	if(AutomationMainClass.environment.toString().compareToIgnoreCase("PROD")==0)
	        	{
	        		MonitoringWrapper.Subject="Critical:";
	        	}
	        	else {
	        		MonitoringWrapper.Subject="Urgent:";
				}
	        	
	        }
	        else
	        {
	        	MonitoringWrapper.Subject="All Clear :";
	        } 
			
	        
			
	        MonitoringWrapper.Message_Body="<br>Automation execution for the Daily Monitoring completed. Please find the results below.<br></br>"
						+ "<br></br><b><u> CPI Data Validation Status:</u></b><br></br>"
					    + "\t\t"+MonitoringWrapper.CPI_Data_Complete_Html
						+      "<br><b><u>Count: </u></b>"+MonitoringWrapper.BB_CPI_MODELLIST_Comments
						+ "<br> <b><u>TimeStamp: </u></b>"+MonitoringWrapper.BB_CPI_MODELLIST_TimeStampComments
						
						+ "<br></br><b><u> States Data Validation Status:</u></b><br></br>"
					    + "\t\t"+MonitoringWrapper.States_Data_Complete_Html
						+ "<b><u>Count: </u></b>"+MonitoringWrapper.BB_STATE_STATELIST_Comments
						
						+ "<br></br><b><u> Drill Down Data Validation Status:</u></b><br></br>"
					    + "\t\t"+MonitoringWrapper.BBDrillDownStyleList_Data_Complete_Html
						+ "<b><u>Count: </u></b>"+MonitoringWrapper.BB_DRILLDOWN_STYLELIST_Comments
						+ "<br> <b><u>TimeStamp: </u></b>"+MonitoringWrapper.BB_DRILLDOWN_STYLELIST_TSComments
						
						+ "<br></br><b><u> Retail Listing Data Validation Status:</u></b><br></br>"
					    + "\t\t"+MonitoringWrapper.BB_RETAI_LISTINGS_Data_Complete_Html
						+ "<b><u>Count: </u></b>"+MonitoringWrapper.BB_RETAI_LISTINGS_Comments
						+ "<br> <b><u>TimeStamp: </u></b>"+MonitoringWrapper.BB_RETAI_LISTINGS_TSComments
						
						
						+ "<br><br></br> This is auto generated email. Please don't reply to this email."
						+ "<br></br>Thanks,"
						+ "<br>EDS Automation Group.<br></br>";
	        
	        
	        
                       

		}

		
		catch (Exception e) {
			//ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			stmt.close();
			SnowFlakeDataBaseConnection_OBJ.CloseSnowFlakeDBConnection();
		}

	}
	
	/*****************************************************************************************************************************************************************/
	 /*************************************************Retail Listing Data Validation starts *****************************************************************************/
	
	private void getRetailListingDatafromDataBase(Statement stmt, Properties properties,String CurrentDate) {
		


		String Query=null;
		String load_id="";
		MonitoringWrapper MonitoringWrapper_OBJ=new MonitoringWrapper();
    	String Stage_Query=null;
    	String application_Query=null;
    	long Stage_count=0;
    	long app_count=0;
    	String Stage_TimeStamp="";
    	String applications_TimeStamp="";
		
		try
		{
			Query=properties.getProperty("BBRETAILLISTING_LOAD");
            load_id= getLoadID(Query,stmt);
            
            if(load_id.toString().compareToIgnoreCase("")==0)
            {
            	MonitoringWrapper.BB_RETAI_LISTINGS_Data_Html = "BBRETAILLISTING_LOAD is blank. Please check the status from DB";
            	MonitoringWrapper.BB_RETAI_LISTINGS_Comments="BBRETAILLISTING_LOAD is blank.".toString();
            	MonitoringWrapper.BB_RETAI_LISTINGS_TSComments="BBRETAILLISTING_LOAD is blank.".toString();
            	MonitoringWrapper.BB_RETAI_LISTINGS_Subject="Urgent:";
            	logger.error("Load is blank please check the DB data manually");
            }
            else
            {
            	
            	
            	
            	/**************************************************************************************************************/
            	/******************** Getting Count from BB_CPI_MODELLIST table from STAGE and APPLICATIONS schema*************/
            	Stage_Query=properties.getProperty("stage_BB_RETAILISTING_LISTINGS_Count");
            	Stage_count=ExecuteCountQuery(Stage_Query,stmt,load_id);
            	MonitoringWrapper_OBJ.setStage_BB_RETAI_LISTINGS_Count(Stage_count);
            	
            	application_Query=properties.getProperty("app_BB_RETAILISTING_LISTINGS_Count");
            	app_count=ExecuteCountQuery(application_Query,stmt,load_id);
            	MonitoringWrapper_OBJ.setApp_BB_RETAI_LISTINGS_count(app_count);
            	
            	/**************************************************************************************************************/
            	/******************** Getting Time Stamp from BB_CPI_MODELLIST table from STAGE and APPLICATIONS schema*************/
            	
            	Stage_Query=properties.getProperty("stage_BB_RETAILISTING_LISTINGS_TS");
            	Stage_TimeStamp=ExecuteTimeStampQuery(Stage_Query,stmt,load_id);
            	MonitoringWrapper_OBJ.setStage_BB_RETAI_LISTINGS_TimeStamp(Stage_TimeStamp);
            	
            	
            	application_Query=properties.getProperty("app_BB_RETAILISTING_LISTINGS_TS");
            	applications_TimeStamp=ExecuteTimeStampQuery(application_Query,stmt,load_id);
            	MonitoringWrapper_OBJ.setAppl_BB_RETAI_LISTINGS_TimeStamp(applications_TimeStamp);
            	
            	
            	ValidateRetailListingtData_emailBodyCreation(MonitoringWrapper_OBJ,CurrentDate);
    			
            }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	
		
	
	}
	
	private void ValidateRetailListingtData_emailBodyCreation(MonitoringWrapper monitoringWrapper_OBJ,String CurrentDate) {

		StringBuffer Comments=new StringBuffer();
		StringBuffer TimeStamp_Comments=new StringBuffer();
		String Subject = "All Clear:";
		try
		{
			if((monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_Count()==0)&&(monitoringWrapper_OBJ.getApp_BB_RETAI_LISTINGS_count()==0))
			{
				Comments=Comments.append("<font color='red'>Retail Listing Stage Schema and Applications Schema data Count showing 0 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_Count()==0)
			{
				Comments=Comments.append("<font color='red'>Retail Listing Stage Schema records Count showing 0 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getApp_BB_RETAI_LISTINGS_count()==0)
			{
				Comments=Comments.append("<font color='red'>Retail Listing Applications Schema records Count showing 0 records</font>");
				Subject="Urgent:";
			}
			else if((monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_Count()<3000000)&&(monitoringWrapper_OBJ.getApp_BB_RETAI_LISTINGS_count()<3000000))
			{
				Comments=Comments.append("<font color='red'>Retail Listing Stage Schema and Applications Schema data Count showing less than 3000000 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_Count()<3000000)
			{
				Comments=Comments.append("<font color='red'>Retail Listing Stage Schema records Count showing less than 3000000 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getApp_BB_RETAI_LISTINGS_count()<3000000)
			{
				Comments=Comments.append("<font color='red'>Retail Listing Applications Schema records Count showing less than 3000000 records</font>");
				Subject="Urgent:";
			}
			else
			{
				if(monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_Count()==monitoringWrapper_OBJ.getApp_BB_RETAI_LISTINGS_count())
				{
					Comments=Comments.append("Retail Listing Count is matching");
					//Subject="All Clear:";
				}
				else
				{
					Comments=Comments.append("Retail Listing Count is not matching: But Stage and applications schema couts are more than 3152598");
					//Subject="All Clear:";
				}
			}
			
			
			/***************************************************************************************************************************************************/
			/*************************************Validating TimeStamp******************************************************************************************/
			
			if((monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_TimeStamp().contains(";")) &&(monitoringWrapper_OBJ.getAppl_BB_RETAI_LISTINGS_TimeStamp().contains(";")))
			{
				TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>Retail Listing Stage Schema and Applications Schema Time Stamp returns multiple date time stamp</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_TimeStamp().contains(";"))
			{
				TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>Retail Listing Stage Schema Time Stamp returns multiple date time stamp</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getAppl_BB_RETAI_LISTINGS_TimeStamp().contains(";"))
			{
				TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>Retail Listing Applications Schema Time Stamp returns multiple date time stamp</font>");
				Subject="Urgent:";
			}
			else
			{
				/* String CurrentDate="";
				 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
				 LocalDateTime now = LocalDateTime.now();
				 CurrentDate=dtf.format(now);*/
				 
				 if((monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_TimeStamp().toString().compareTo(CurrentDate)==0) &&(monitoringWrapper_OBJ.getAppl_BB_RETAI_LISTINGS_TimeStamp().toString().compareTo(CurrentDate)==0))
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("Retail Listing Time Stamp Validation completed.");
					 //Subject="All Clear:";
				 }
				 else if((monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_TimeStamp().toString().compareTo(CurrentDate)!=0) &&(monitoringWrapper_OBJ.getAppl_BB_RETAI_LISTINGS_TimeStamp().toString().compareTo(CurrentDate)!=0))
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>Retail Listing Stage and applications Schema Time Stamp not matching with Current date time</font>");
					 Subject="Urgent:";
				 }
				 else if(monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_TimeStamp().toString().compareTo(CurrentDate)!=0)
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>Retail Listing Stage Schema Time Stamp not matching with Current date time Stamp</font>");
					 Subject="Urgent:";
				 }
				 else if(monitoringWrapper_OBJ.getAppl_BB_RETAI_LISTINGS_TimeStamp().toString().compareTo(CurrentDate)!=0)
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>Retail Listing Applications Schema Time Stamp not matching with Current date time Stamp</font>"); 
					 Subject="Urgent:";
				 }
				 else if((monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_TimeStamp().toString().compareTo(CurrentDate)!=0) &&(monitoringWrapper_OBJ.getAppl_BB_RETAI_LISTINGS_TimeStamp().toString().compareTo(CurrentDate)!=0))
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>Retail Listing Stage and Applications Schema Time Stamp not matching with Current date time Stamp</font>"); 
					 Subject="Urgent:";
				 }
				 else
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>Retail Listing Issue with TimeStamp. Please validate</font>"); 
					 Subject="Urgent:";
				 }
			}
			
			
			MonitoringWrapper.BB_RETAI_LISTINGS_Comments=Comments.toString();
			MonitoringWrapper.BB_RETAI_LISTINGS_TSComments=TimeStamp_Comments.toString();
			MonitoringWrapper.BB_RETAI_LISTINGS_Subject=Subject;
			MonitoringWrapper.stage_BB_RETAI_LISTINGS_Count=monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_Count();
			MonitoringWrapper.app_BB_RETAI_LISTINGS_count=monitoringWrapper_OBJ.getApp_BB_RETAI_LISTINGS_count();
			MonitoringWrapper.stage_BB_RETAI_LISTINGS_TimeStamp=monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_TimeStamp();
			MonitoringWrapper.appl_BB_RETAI_LISTINGS_TimeStamp=monitoringWrapper_OBJ.getAppl_BB_RETAI_LISTINGS_TimeStamp();
			AutomationMainClass.email_mode="Daily Monitoring";
			AutomationMainClass.UserName="Daily Monitoring Team";
			
			
			StringBuilder buf = new StringBuilder();
			buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=10 cellspacing=0>" +"<tr>" +"<th>Schema</th>" +"<th>Count</th>" +"<th>TimeStamp</th>"+"</tr>");
			buf.append("<tr><td>Stage</td><td>"+MonitoringWrapper.stage_BB_RETAI_LISTINGS_Count+"</td><td>"+MonitoringWrapper.stage_BB_RETAI_LISTINGS_TimeStamp+"</td>");
			buf.append("<tr><td>Applications</td><td>"+MonitoringWrapper.app_BB_RETAI_LISTINGS_count+"</td><td>"+MonitoringWrapper.appl_BB_RETAI_LISTINGS_TimeStamp+"</td>");
			buf.append("</table>" +"</body>" + "</html>");
			MonitoringWrapper.BB_RETAI_LISTINGS_Data_Html = buf.toString();
			
			/*************************************************************************************************************************************************/
			/****************************************Insert Results into Automation Database******************************************************************/
			
			DataBaseConnections Obj_DataBaseConnections=new DataBaseConnections();
			ResultsDBImplementation obj_ResultsDBImplementation=new ResultsDBImplementation();
			ArrayList<String> Stage_Results = new ArrayList<String>();
			ArrayList<String> Application_Results = new ArrayList<String>();
			try
			{
				Obj_DataBaseConnections.Connect_Automation_Results_SQLServerDB();
				
				Stage_Results=obj_ResultsDBImplementation.getlast7daysdata("RetailListing","Stage");
				Application_Results=obj_ResultsDBImplementation.getlast7daysdata("RetailListing","Applications");
				
				MonitoringWrapper.BB_RETAI_LISTINGS_Data_Html_last_7_Days=Create7daysReportTable(Stage_Results,Application_Results);
				
				obj_ResultsDBImplementation.insetDailyMonitoringResults("Stage", CurrentDate, monitoringWrapper_OBJ.getStage_BB_RETAI_LISTINGS_Count(), "RetailListing");
				obj_ResultsDBImplementation.insetDailyMonitoringResults("Applications", CurrentDate, monitoringWrapper_OBJ.getApp_BB_RETAI_LISTINGS_count(), "RetailListing");
				
				
				MonitoringWrapper.BB_RETAI_LISTINGS_Data_Complete_Html=Combinetwotablesets(MonitoringWrapper.BB_RETAI_LISTINGS_Data_Html,MonitoringWrapper.BB_RETAI_LISTINGS_Data_Html_last_7_Days);
			
			}
			catch(Exception E)
			{
				E.printStackTrace();
			}
			finally
			{
				Obj_DataBaseConnections.CloseAutomationResultsDBConnection();
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	
		
	
		
	}
	
	/*************************************************End of Retail Listing Data  Data Validation *************************************************************************/
	
	
	
	
	
	

	

	/*****************************************************************************************************************************************************************/
	 /*************************************************Drilldown Data Validation starts *****************************************************************************/
	
	
	private void getDrillDownDatafromDataBase(Statement stmt, Properties properties,String CurrentDate) {

		String Query=null;
		String load_id="";
		MonitoringWrapper MonitoringWrapper_OBJ=new MonitoringWrapper();
    	String Stage_Query=null;
    	String application_Query=null;
    	long Stage_count=0;
    	long app_count=0;
    	String Stage_TimeStamp="";
    	String applications_TimeStamp="";
		
		try
		{
			Query=properties.getProperty("BBDRILLDOWN_LOAD");
            load_id= getLoadID(Query,stmt);
            
            if(load_id.toString().compareToIgnoreCase("")==0)
            {
            	MonitoringWrapper.BBDrillDownStyleList_Data_Html = "BBDRILLDOWN_LOAD is blank. Please check the status from DB";
            	MonitoringWrapper.BB_DRILLDOWN_STYLELIST_Comments="BBDRILLDOWN_LOAD is blank.".toString();
            	MonitoringWrapper.BB_DRILLDOWN_STYLELIST_TSComments="BBDRILLDOWN_LOAD is blank.".toString();
            	MonitoringWrapper.BB_DRILLDOWN_STYLELIST_Subject="Urgent:";
            	logger.error("Load is blank please check the DB data manually");
            }
            else
            {
            	
            	
            	
            	/**************************************************************************************************************/
            	/******************** Getting Count from BB_CPI_MODELLIST table from STAGE and APPLICATIONS schema*************/
            	Stage_Query=properties.getProperty("stage_BB_DRILLDOWN_STYLELIST_Count");
            	Stage_count=ExecuteCountQuery(Stage_Query,stmt,load_id);
            	MonitoringWrapper_OBJ.setStage_BB_DRILLDOWN_STYLELIST_Count(Stage_count);
            	
            	application_Query=properties.getProperty("app_BB_DRILLDOWN_STYLELIST_Count");
            	app_count=ExecuteCountQuery(application_Query,stmt,load_id);
            	MonitoringWrapper_OBJ.setApp_BB_DRILLDOWN_STYLELIST_count(app_count);
            	
            	/**************************************************************************************************************/
            	/******************** Getting Time Stamp from BB_CPI_MODELLIST table from STAGE and APPLICATIONS schema*************/
            	
            	Stage_Query=properties.getProperty("stage_BB_DRILLDOWN_STYLELIST_TS");
            	Stage_TimeStamp=ExecuteTimeStampQuery(Stage_Query,stmt,load_id);
            	MonitoringWrapper_OBJ.setStage_BB_DRILLDOWN_STYLELIST_TimeStamp(Stage_TimeStamp);
            	
            	
            	application_Query=properties.getProperty("app_BB_DRILLDOWN_STYLELIST_TS");
            	applications_TimeStamp=ExecuteTimeStampQuery(application_Query,stmt,load_id);
            	MonitoringWrapper_OBJ.setAppl_BB_DRILLDOWN_STYLELIST_TimeStamp(applications_TimeStamp);
            	
            	
            	ValidateDrillDownStyleListData_emailBodyCreation(MonitoringWrapper_OBJ,CurrentDate);
    			
            }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	
		
	}
	
	
	private void ValidateDrillDownStyleListData_emailBodyCreation(MonitoringWrapper monitoringWrapper_OBJ,String CurrentDate) {

		
		StringBuffer Comments=new StringBuffer();
		StringBuffer TimeStamp_Comments=new StringBuffer();
		String Subject = "All Clear:";
		try
		{
			if((monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_Count()==0)&&(monitoringWrapper_OBJ.getApp_BB_DRILLDOWN_STYLELIST_count()==0))
			{
				Comments=Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Stage Schema and Applications Schema data Count showing 0 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_Count()==0)
			{
				Comments=Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Stage Schema records Count showing 0 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getApp_BB_DRILLDOWN_STYLELIST_count()==0)
			{
				Comments=Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Applications Schema records Count showing 0 records</font>");
				Subject="Urgent:";
			}
			else if((monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_Count()<61769)&&(monitoringWrapper_OBJ.getApp_BB_DRILLDOWN_STYLELIST_count()<61769))
			{
				Comments=Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Stage Schema and Applications Schema data Count showing less than 61769 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_Count()<61769)
			{
				Comments=Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Stage Schema records Count showing less than 61769 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getApp_BB_DRILLDOWN_STYLELIST_count()<61769)
			{
				Comments=Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Applications Schema records Count showing less than 61769 records</font>");
				Subject="Urgent:";
			}
			else
			{
				if(monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_Count()==monitoringWrapper_OBJ.getApp_BB_DRILLDOWN_STYLELIST_count())
				{
					Comments=Comments.append("BB_DRILLDOWN_STYLELIST Count is matching");
					//Subject="All Clear:";
				}
				else
				{
					Comments=Comments.append("BB_DRILLDOWN_STYLELIST Count is not matching: But Stage and applications schema couts are more than 61769");
					//Subject="All Clear:";
				}
			}
			
			
			/***************************************************************************************************************************************************/
			/*************************************Validating TimeStamp******************************************************************************************/
			
			if((monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_TimeStamp().contains(";")) &&(monitoringWrapper_OBJ.getAppl_BB_DRILLDOWN_STYLELIST_TimeStamp().contains(";")))
			{
				TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Stage Schema and Applications Schema Time Stamp returns multiple date time stamp</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_TimeStamp().contains(";"))
			{
				TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Stage Schema Time Stamp returns multiple date time stamp</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getAppl_BB_DRILLDOWN_STYLELIST_TimeStamp().contains(";"))
			{
				TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Applications Schema Time Stamp returns multiple date time stamp</font>");
				Subject="Urgent:";
			}
			else
			{
				/* String CurrentDate="";
				 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
				 LocalDateTime now = LocalDateTime.now();
				 CurrentDate=dtf.format(now);*/
				 
				 if((monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_TimeStamp().toString().compareTo(CurrentDate)==0) &&(monitoringWrapper_OBJ.getAppl_BB_DRILLDOWN_STYLELIST_TimeStamp().toString().compareTo(CurrentDate)==0))
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("BB_DRILLDOWN_STYLELIST Time Stamp Validation completed.");
					 //Subject="All Clear:";
				 }
				 else if((monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_TimeStamp().toString().compareTo(CurrentDate)!=0) &&(monitoringWrapper_OBJ.getAppl_BB_DRILLDOWN_STYLELIST_TimeStamp().toString().compareTo(CurrentDate)!=0))
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Stage and applications Schema Time Stamp not matching with Current date time</font>");
					 Subject="Urgent:";
				 }
				 else if(monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_TimeStamp().toString().compareTo(CurrentDate)!=0)
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Stage Schema Time Stamp not matching with Current date time Stamp</font>");
					 Subject="Urgent:";
				 }
				 else if(monitoringWrapper_OBJ.getAppl_BB_DRILLDOWN_STYLELIST_TimeStamp().toString().compareTo(CurrentDate)!=0)
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Applications Schema Time Stamp not matching with Current date time Stamp</font>"); 
					 Subject="Urgent:";
				 }
				 else if((monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_TimeStamp().toString().compareTo(CurrentDate)!=0) &&(monitoringWrapper_OBJ.getAppl_BB_DRILLDOWN_STYLELIST_TimeStamp().toString().compareTo(CurrentDate)!=0))
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Stage and Applications Schema Time Stamp not matching with Current date time Stamp</font>"); 
					 Subject="Urgent:";
				 }
				 else
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>BB_DRILLDOWN_STYLELIST Issue with TimeStamp. Please validate</font>"); 
					 Subject="Urgent:";
				 }
			}
			
			
			MonitoringWrapper.BB_DRILLDOWN_STYLELIST_Comments=Comments.toString();
			MonitoringWrapper.BB_DRILLDOWN_STYLELIST_TSComments=TimeStamp_Comments.toString();
			MonitoringWrapper.BB_DRILLDOWN_STYLELIST_Subject=Subject;
			MonitoringWrapper.stage_BB_DRILLDOWN_STYLELIST_Count=monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_Count();
			MonitoringWrapper.app_BB_DRILLDOWN_STYLELIST_count=monitoringWrapper_OBJ.getApp_BB_DRILLDOWN_STYLELIST_count();
			MonitoringWrapper.stage_BB_DRILLDOWN_STYLELIST_TimeStamp=monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_TimeStamp();
			MonitoringWrapper.appl_BB_DRILLDOWN_STYLELIST_TimeStamp=monitoringWrapper_OBJ.getAppl_BB_DRILLDOWN_STYLELIST_TimeStamp();
			AutomationMainClass.email_mode="Daily Monitoring";
			AutomationMainClass.UserName="Daily Monitoring Team";
			
			
			StringBuilder buf = new StringBuilder();
			buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=10 cellspacing=0>" +"<tr>" +"<th>Schema</th>" +"<th>Count</th>" +"<th>TimeStamp</th>"+"</tr>");
			buf.append("<tr><td>Stage</td><td>"+MonitoringWrapper.stage_BB_DRILLDOWN_STYLELIST_Count+"</td><td>"+MonitoringWrapper.stage_BB_DRILLDOWN_STYLELIST_TimeStamp+"</td>");
			buf.append("<tr><td>Applications</td><td>"+MonitoringWrapper.app_BB_DRILLDOWN_STYLELIST_count+"</td><td>"+MonitoringWrapper.appl_BB_DRILLDOWN_STYLELIST_TimeStamp+"</td>");
			buf.append("</table>" +"</body>" + "</html>");
			MonitoringWrapper.BBDrillDownStyleList_Data_Html = buf.toString();
			
			
			/*************************************************************************************************************************************************/
			/****************************************Insert Results into Automation Database******************************************************************/
			
			DataBaseConnections Obj_DataBaseConnections=new DataBaseConnections();
			ResultsDBImplementation obj_ResultsDBImplementation=new ResultsDBImplementation();
			ArrayList<String> Stage_Results = new ArrayList<String>();
			ArrayList<String> Application_Results = new ArrayList<String>();
			try
			{
				Obj_DataBaseConnections.Connect_Automation_Results_SQLServerDB();
				
				Stage_Results=obj_ResultsDBImplementation.getlast7daysdata("DrillDown","Stage");
				Application_Results=obj_ResultsDBImplementation.getlast7daysdata("DrillDown","Applications");
				
				MonitoringWrapper.BBDD_Data_Html_last_7_Days=Create7daysReportTable(Stage_Results,Application_Results);
				
				
				obj_ResultsDBImplementation.insetDailyMonitoringResults("Stage", CurrentDate, monitoringWrapper_OBJ.getStage_BB_DRILLDOWN_STYLELIST_Count(), "DrillDown");
				obj_ResultsDBImplementation.insetDailyMonitoringResults("Applications", CurrentDate, monitoringWrapper_OBJ.getApp_BB_DRILLDOWN_STYLELIST_count(), "DrillDown");
				
				MonitoringWrapper.BBDrillDownStyleList_Data_Complete_Html=Combinetwotablesets(MonitoringWrapper.BBDrillDownStyleList_Data_Html,MonitoringWrapper.BBDD_Data_Html_last_7_Days);
			
			}
			catch(Exception E)
			{
				E.printStackTrace();
			}
			finally
			{
				Obj_DataBaseConnections.CloseAutomationResultsDBConnection();
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	
		
	}
	
	
	 /*************************************************End of Drill Down Data Validation *********************************************************************************************/
	
	
	
	
	
	
	
	
	
	
	

	

	/*****************************************************************************************************************************************************************/
	 /*************************************************States Data Validation *********************************************************************************************/
	
	
     private void getStatesDatafromDataBase(Statement stmt, Properties properties,String CurrentDate) {
		
 		String Query=null;
 		String load_id="";
 		MonitoringWrapper MonitoringWrapper_OBJ=new MonitoringWrapper();
     	String Stage_Query=null;
     	String application_Query=null;
     	long Stage_count=0;
     	long app_count=0;
 		
 		try
 		{
 			Query=properties.getProperty("BBSTATE_load");
             load_id= getLoadID(Query,stmt);
             
             if(load_id.toString().compareToIgnoreCase("")==0)
             {
            	 MonitoringWrapper.States_Data_Html = "BBSTATE_load is blank. Please check the status from DB";
             	MonitoringWrapper.BB_STATE_STATELIST_Comments="BBSTATE_load is blank.".toString();
             	MonitoringWrapper.BB_STATE_STATELIST_Subject="Urgent:";
             	logger.error("Load ID is blank for states data validation:please check the DB data manually");
             }
             else
             {
             	/**************************************************************************************************************/
             	/******************** Getting Count from BB_CPI_MODELLIST table from STAGE and APPLICATIONS schema*************/
             	Stage_Query=properties.getProperty("stage_BB_STATE_STATELIST_Count");
             	Stage_count=ExecuteCountQuery(Stage_Query,stmt,load_id);
             	MonitoringWrapper_OBJ.setStage_BB_STATE_STATELIST_Count(Stage_count);
             	
             	application_Query=properties.getProperty("applications_BB_STATE_STATELIST_count");
             	app_count=ExecuteCountQuery(application_Query,stmt,load_id);
             	MonitoringWrapper_OBJ.setApplications_BB_STATE_STATELIST_count(app_count);
             	
             	ValidateStatesData_emailBodyCreation(MonitoringWrapper_OBJ,CurrentDate);
     			
             }
 		}
 		catch(Exception e)
 		{
 			e.printStackTrace();
 		}
 		
 	
		
	}

	private void ValidateStatesData_emailBodyCreation(MonitoringWrapper monitoringWrapper_OBJ,String CurrentDate) {
		

		
		StringBuffer Comments=new StringBuffer();
		String Subject = "All Clear:";
		try
		{
			if((monitoringWrapper_OBJ.getStage_BB_STATE_STATELIST_Count()==0)&&(monitoringWrapper_OBJ.getApplications_BB_STATE_STATELIST_count()==0))
			{
				Comments=Comments.append("<font color='red'>BB_STATE_STATELIST Stage Schema and Applications Schema data Count showing 0 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getStage_BB_STATE_STATELIST_Count()==0)
			{
				Comments=Comments.append("<font color='red'>BB_STATE_STATELIST Stage Schema records Count showing 0 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getApplications_BB_STATE_STATELIST_count()==0)
			{
				Comments=Comments.append("<font color='red'>BB_STATE_STATELIST Applications Schema records Count showing 0 records</font>");
				Subject="Urgent:";
			}
			else if((monitoringWrapper_OBJ.getStage_BB_STATE_STATELIST_Count()<54)&&(monitoringWrapper_OBJ.getApplications_BB_STATE_STATELIST_count()<54))
			{
				Comments=Comments.append("<font color='red'>BB_STATE_STATELIST Stage Schema and Applications Schema data Count showing less than 54 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getStage_BB_STATE_STATELIST_Count()<54)
			{
				Comments=Comments.append("<font color='red'>BB_STATE_STATELIST Stage Schema records Count showing less than 54 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getApplications_BB_STATE_STATELIST_count()<54)
			{
				Comments=Comments.append("<font color='red'>BB_STATE_STATELIST Applications Schema records Count showing less than 54 records</font>");
				Subject="Urgent:";
			}
			else
			{
					if((monitoringWrapper_OBJ.getStage_BB_STATE_STATELIST_Count()>54)&&(monitoringWrapper_OBJ.getApplications_BB_STATE_STATELIST_count()>54))
					{
						Comments=Comments.append("BB_STATE_STATELIST Stage Schema and Applications Schema data Count more than 54 records");
						//Subject="All Clear:";
					}
					else if((monitoringWrapper_OBJ.getStage_BB_STATE_STATELIST_Count()==54)&&(monitoringWrapper_OBJ.getApplications_BB_STATE_STATELIST_count()==54))
					{
						Comments=Comments.append("BB_STATE_STATELIST Count matching");
						//Subject="All Clear:";
					}
					else if(monitoringWrapper_OBJ.getStage_BB_STATE_STATELIST_Count() == monitoringWrapper_OBJ.getApplications_BB_STATE_STATELIST_count())
					{
						Comments=Comments.append("BB_STATE_STATELIST Count matching");
						//Subject="All Clear:";
					}
					else
					{
						Comments=Comments.append("BB_STATE_STATELIST Validation Completed. Please check count status from above table.");
						//Subject="All Clear:";
					}

			}
			
			
			
			
			
			MonitoringWrapper.BB_STATE_STATELIST_Comments=Comments.toString();
			MonitoringWrapper.BB_STATE_STATELIST_Subject=Subject;
			MonitoringWrapper.stage_BB_STATE_STATELIST_Count=monitoringWrapper_OBJ.getStage_BB_STATE_STATELIST_Count();
			MonitoringWrapper.applications_BB_STATE_STATELIST_count=monitoringWrapper_OBJ.getApplications_BB_STATE_STATELIST_count();

			
			
			StringBuilder buf = new StringBuilder();
			buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=10 cellspacing=0>" +"<tr>" +"<th>Schema</th>" +"<th>Count</th>");
			buf.append("<tr><td>Stage</td><td>"+MonitoringWrapper.stage_BB_STATE_STATELIST_Count+"</td>");
			buf.append("<tr><td>Applications</td><td>"+MonitoringWrapper.applications_BB_STATE_STATELIST_count+"</td>");
			buf.append("</table>" +"</body>" + "</html>");
			MonitoringWrapper.States_Data_Html = buf.toString();
			
			
			/*************************************************************************************************************************************************/
			/****************************************Insert Results into Automation Database******************************************************************/
			
			DataBaseConnections Obj_DataBaseConnections=new DataBaseConnections();
			ResultsDBImplementation obj_ResultsDBImplementation=new ResultsDBImplementation();
			ArrayList<String> Stage_Results = new ArrayList<String>();
			ArrayList<String> Application_Results = new ArrayList<String>();
			try
			{
				Obj_DataBaseConnections.Connect_Automation_Results_SQLServerDB();
				
				Stage_Results=obj_ResultsDBImplementation.getlast7daysdata("States","Stage");
				Application_Results=obj_ResultsDBImplementation.getlast7daysdata("States","Applications");
				
				MonitoringWrapper.States_Data_Html_last_7_Days=Create7daysReportTable(Stage_Results,Application_Results);
				
				obj_ResultsDBImplementation.insetDailyMonitoringResults("Stage", CurrentDate, monitoringWrapper_OBJ.getStage_BB_STATE_STATELIST_Count(), "States");
				obj_ResultsDBImplementation.insetDailyMonitoringResults("Applications", CurrentDate, monitoringWrapper_OBJ.getApplications_BB_STATE_STATELIST_count(), "States");
				
				MonitoringWrapper.States_Data_Complete_Html=Combinetwotablesets(MonitoringWrapper.States_Data_Html,MonitoringWrapper.States_Data_Html_last_7_Days);
			
			}
			catch(Exception E)
			{
				E.printStackTrace();
			}
			finally
			{
				Obj_DataBaseConnections.CloseAutomationResultsDBConnection();
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	
	}
	
	
	
	 /*************************************************End of States Data Validation *********************************************************************************************/
	
	
	
	
	

	/*****************************************************************************************************************************************************************/
	 /*************************************************CPI Data Validation *********************************************************************************************/
	
	private void getCPIDatafromDataBase(Statement stmt, Properties properties,String CurrentDate) {
		
		String Query=null;
		String load_id="";
		
		try
		{
			Query=properties.getProperty("BBCPI_LOAD");
            load_id= getLoadID(Query,stmt);
            
            if(load_id.toString().compareToIgnoreCase("")==0)
            {
            	MonitoringWrapper.CPI_Data_Html = "BBCPI_LOAD is blank. Please check the status from DB";
            	MonitoringWrapper.BB_CPI_MODELLIST_Comments="BBCPI_LOAD is blank.".toString();
            	MonitoringWrapper.BB_CPI_MODELLIST_TimeStampComments="BBCPI_LOAD is blank.".toString();
            	MonitoringWrapper.BB_CPI_MODELLIST_Subject="Urgent:";
            				
            	logger.error("Load is blank please check the DB data manually");
            }
            else
            {
            	MonitoringWrapper MonitoringWrapper_OBJ=new MonitoringWrapper();
            	String Stage_Query=null;
            	String application_Query=null;
            	long Stage_count=0;
            	long app_count=0;
            	
            	
            	/**************************************************************************************************************/
            	/******************** Getting Count from BB_CPI_MODELLIST table from STAGE and APPLICATIONS schema*************/
            	Stage_Query=properties.getProperty("stage_BB_CPI_MODELLIST_Count");
            	Stage_count=ExecuteCountQuery(Stage_Query,stmt,load_id);
            	MonitoringWrapper_OBJ.setStage_BB_CPI_MODELLIST_Count(Stage_count);
            	
            	application_Query=properties.getProperty("applications_BB_CPI_MODELLIST");
            	app_count=ExecuteCountQuery(application_Query,stmt,load_id);
            	MonitoringWrapper_OBJ.setApplications_BB_CPI_MODELLIST_count(app_count);
            	
            	
            	/**************************************************************************************************************/
            	/******************** Getting Time Stamp from BB_CPI_MODELLIST table from STAGE and APPLICATIONS schema*************/
            	
            	String Stage_TimeStamp="";
            	String applications_TimeStamp="";
            	Stage_Query=properties.getProperty("stage_BB_CPI_MODELLIST_timestamp");
            	Stage_TimeStamp=ExecuteTimeStampQuery(Stage_Query,stmt,load_id);
            	MonitoringWrapper_OBJ.setStage_BB_CPI_MODELLIST_TimeStamp(Stage_TimeStamp);
            	
            	
            	application_Query=properties.getProperty("applications_BB_CPI_MODELLIST_timestamp");
            	applications_TimeStamp=ExecuteTimeStampQuery(application_Query,stmt,load_id);
            	MonitoringWrapper_OBJ.setApplications_BB_CPI_MODELLIST_TimeStamp(applications_TimeStamp);
            	

            	
            	ValidateCPIData_emailBodyCreation(MonitoringWrapper_OBJ,CurrentDate);
    			
            }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}


	private void ValidateCPIData_emailBodyCreation(MonitoringWrapper monitoringWrapper_OBJ,String CurrentDate) {
		
		StringBuffer Comments=new StringBuffer();
		StringBuffer TimeStamp_Comments=new StringBuffer();
		String Subject = "All Clear:";
		try
		{
			if((monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_Count()==0)&&(monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_count()==0))
			{
				Comments=Comments.append("<font color='red'>CPI Stage Schema and Applications Schema data Count showing 0 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_Count()==0)
			{
				Comments=Comments.append("<font color='red'>CPI Stage Schema records Count showing 0 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_count()==0)
			{
				Comments=Comments.append("<font color='red'>CPI Applications Schema records Count showing 0 records</font>");
				Subject="Urgent:";
			}
			else if((monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_Count()<28571)&&(monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_count()<28571))
			{
				Comments=Comments.append("<font color='red'>CPI Stage Schema and Applications Schema data Count showing less than 28571 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_Count()<28571)
			{
				Comments=Comments.append("<font color='red'>CPI Stage Schema records Count showing less than 28571 records</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_count()<28571)
			{
				Comments=Comments.append("<font color='red'>CPI Applications Schema records Count showing less than 28571 records</font>");
				Subject="Urgent:";
			}
			else
			{
				if(monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_Count()==monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_count())
				{
					Comments=Comments.append("Count is matching");
					//Subject="All Clear:";
				}
				else
				{
					Comments=Comments.append("Count is not matching: But Stage and applications schema couts are more than 28571");
					//Subject="Medium:";
				}
			}
			
			
			/***************************************************************************************************************************************************/
			/*************************************Validating TimeStamp******************************************************************************************/
			
			if((monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_TimeStamp().contains(";")) &&(monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_TimeStamp().contains(";")))
			{
				TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>CPI Stage Schema and Applications Schema Time Stamp returns multiple date time stamp</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_TimeStamp().contains(";"))
			{
				TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>CPI Stage Schema Time Stamp returns multiple date time stamp</font>");
				Subject="Urgent:";
			}
			else if(monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_TimeStamp().contains(";"))
			{
				TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>CPI Applications Schema Time Stamp returns multiple date time stamp</font>");
				Subject="Urgent:";
			}
			else
			{
				/* String CurrentDate="";
				 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
				 LocalDateTime now = LocalDateTime.now();
				 CurrentDate=dtf.format(now);*/
				 
				 if((monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_TimeStamp().toString().compareTo(CurrentDate)==0) &&(monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_TimeStamp().toString().compareTo(CurrentDate)==0))
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("Time Stamp Validation completed.");
					 //Subject="All Clear:";
				 }
				 else if((monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_TimeStamp().toString().compareTo(CurrentDate)!=0) &&(monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_TimeStamp().toString().compareTo(CurrentDate)!=0))
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>CPI Stage and applications Schema Time Stamp not matching with Current date time</font>");
					 Subject="Urgent:";
				 }
				 else if(monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_TimeStamp().toString().compareTo(CurrentDate)!=0)
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>CPI Stage Schema Time Stamp not matching with Current date time Stamp</font>");
					 Subject="Urgent:";
				 }
				 else if(monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_TimeStamp().toString().compareTo(CurrentDate)!=0)
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>CPI Applications Schema Time Stamp not matching with Current date time Stamp</font>"); 
					 Subject="Urgent:";
				 }
				 else if((monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_TimeStamp().toString().compareTo(CurrentDate)!=0) &&(monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_TimeStamp().toString().compareTo(CurrentDate)!=0))
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>CPI Stage and Applications Schema Time Stamp not matching with Current date time Stamp</font>"); 
					 Subject="Urgent:";
				 }
				 else
				 {
					 TimeStamp_Comments=TimeStamp_Comments.append("<font color='red'>Issue with TimeStamp. Please validate</font>"); 
					 Subject="Urgent:";
				 }
			}
			
			
			MonitoringWrapper.BB_CPI_MODELLIST_Comments=Comments.toString();
			MonitoringWrapper.BB_CPI_MODELLIST_TimeStampComments=TimeStamp_Comments.toString();
			MonitoringWrapper.BB_CPI_MODELLIST_Subject=Subject;
			MonitoringWrapper.stage_BB_CPI_MODELLIST_Count=monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_Count();
			MonitoringWrapper.applications_BB_CPI_MODELLIST_count=monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_count();
			MonitoringWrapper.stage_BB_CPI_MODELLIST_TimeStamp=monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_TimeStamp();
			MonitoringWrapper.applications_BB_CPI_MODELLIST_TimeStamp=monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_TimeStamp();
			AutomationMainClass.email_mode="Daily Monitoring";
			AutomationMainClass.UserName="Daily Monitoring Team";
			
			
			StringBuilder buf = new StringBuilder();
			
			buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=10 cellspacing=0>" +"<tr>" +"<th>Schema</th>" +"<th>Count</th>" +"<th>TimeStamp</th>"+"</tr>");
			buf.append("<tr><td>Stage</td><td>"+MonitoringWrapper.stage_BB_CPI_MODELLIST_Count+"</td><td>"+MonitoringWrapper.stage_BB_CPI_MODELLIST_TimeStamp+"</td>");
			buf.append("<tr><td>Applications</td><td>"+MonitoringWrapper.applications_BB_CPI_MODELLIST_count+"</td><td>"+MonitoringWrapper.applications_BB_CPI_MODELLIST_TimeStamp+"</td>");
			buf.append("</table>" +"</body>" + "</html>");
			MonitoringWrapper.CPI_Data_Html = buf.toString();
			
			
			/*************************************************************************************************************************************************/
			/****************************************Insert Results into Automation Database******************************************************************/
			
			DataBaseConnections Obj_DataBaseConnections=new DataBaseConnections();
			ResultsDBImplementation obj_ResultsDBImplementation=new ResultsDBImplementation();
			ArrayList<String> Stage_Results = new ArrayList<String>();
			ArrayList<String> Application_Results = new ArrayList<String>();
			try
			{
				Obj_DataBaseConnections.Connect_Automation_Results_SQLServerDB();
				
				Stage_Results=obj_ResultsDBImplementation.getlast7daysdata("CPI","Stage");
				Application_Results=obj_ResultsDBImplementation.getlast7daysdata("CPI","Applications");
				
				MonitoringWrapper.CPI_Data_Html_last_7_Days=Create7daysReportTable(Stage_Results,Application_Results);
				
				
				obj_ResultsDBImplementation.insetDailyMonitoringResults("Stage", CurrentDate, monitoringWrapper_OBJ.getStage_BB_CPI_MODELLIST_Count(), "CPI");
				obj_ResultsDBImplementation.insetDailyMonitoringResults("Applications", CurrentDate, monitoringWrapper_OBJ.getApplications_BB_CPI_MODELLIST_count(), "CPI");
			
				MonitoringWrapper.CPI_Data_Complete_Html=Combinetwotablesets(MonitoringWrapper.CPI_Data_Html,MonitoringWrapper.CPI_Data_Html_last_7_Days);
				
			
			}
			catch(Exception E)
			{
				E.printStackTrace();
			}
			finally
			{
				Obj_DataBaseConnections.CloseAutomationResultsDBConnection();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}


	private String Combinetwotablesets(String cPI_Data_Html, String cPI_Data_Html_last_7_Days) {
		String Second_Table_format=null;
		try
		{
			StringBuilder buf = new StringBuilder();
	        buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=10 cellspacing=0>" +"<tr>" +"<th>Current Date</th>" +"<th>Last 7 Days Report</th>" +"</tr>");
			buf.append("<tr><td>"+cPI_Data_Html+"</td>");
			buf.append("<td>"+cPI_Data_Html_last_7_Days+"</td>");
			buf.append("</table>" +"</body>" + "</html>");
			Second_Table_format=buf.toString();
		}
		catch(Exception E)
		{
			;
		}
		return Second_Table_format;
	}

	private String Create7daysReportTable(ArrayList<String> stage_Results, ArrayList<String> application_Results) {
		
		String Table_format=null;
		
		try
		{
		   String Stage_Day1="0";
		   String Stage_Day2="0";
		   String Stage_Day3="0";
		   String Stage_Day4="0";
		   String Stage_Day5="0";
		   String Stage_Day6="0";
		   String Stage_Day7="0";
		   
		   String app_Day1="0";
		   String app_Day2="0";
		   String app_Day3="0";
		   String app_Day4="0";
		   String app_Day5="0";
		   String app_Day6="0";
		   String app_Day7="0";
		   
		   String Column_Day1="Day1";
		   String Column_Day2="Day2";
		   String Column_Day3="Day3";
		   String Column_Day4="Day4";
		   String Column_Day5="Day5";
		   String Column_Day6="Day6";
		   String Column_Day7="Day7";
		   
		   try
		   {
			   Stage_Day1=stage_Results.get(0);
			   Stage_Day2=stage_Results.get(1);
			   Stage_Day3=stage_Results.get(2);
			   Stage_Day4=stage_Results.get(3);
			   Stage_Day5=stage_Results.get(4);
			   Stage_Day6=stage_Results.get(5);
			   Stage_Day7=stage_Results.get(6);
		   }
		   catch(Exception e)
		   {
			   ;
		   }
		   
		   try
		   {
			   
			   app_Day1=application_Results.get(0);
			   app_Day2=application_Results.get(1);
			   app_Day3=application_Results.get(2);
			   app_Day4=application_Results.get(3);
			   app_Day5=application_Results.get(4);
			   app_Day6=application_Results.get(5);
			   app_Day7=application_Results.get(6);
		   }
		   catch(Exception e)
		   {
			   ;
		   }
		   
		   try
		   {
			   
			   Column_Day1=ResultsDBImplementation.Results_Column.get(0);
			   Column_Day2=ResultsDBImplementation.Results_Column.get(1);
			   Column_Day3=ResultsDBImplementation.Results_Column.get(2);
			   Column_Day4=ResultsDBImplementation.Results_Column.get(3);
			   Column_Day5=ResultsDBImplementation.Results_Column.get(4);
			   Column_Day6=ResultsDBImplementation.Results_Column.get(5);
			   Column_Day7=ResultsDBImplementation.Results_Column.get(6);
		   }
		   catch(Exception e)
		   {
			   ;
		   }
		   
		   
		   
		    StringBuilder buf = new StringBuilder();
			buf.append("<html>" +"<body>" +"<table border=1 cellpadding=10 cellspacing=0 style=float: right>" +"<tr>" +"<th>Schema</th>" +"<th>"+Column_Day7+"</th>" +"<th>"+Column_Day6+"</th>"+"<th>"+Column_Day5+"</th>"+"<th>"+Column_Day4+"</th>"+"<th>"+Column_Day3+"</th>"+"<th>"+Column_Day2+"</th>"+"<th>"+Column_Day1+"</th>"+"</tr>");
			buf.append("<tr><td>Stage</td><td>"+Stage_Day7+"</td><td>"+Stage_Day6+"</td><td>"+Stage_Day5+"</td><td>"+Stage_Day4+"</td><td>"+Stage_Day3+"</td><td>"+Stage_Day2+"</td><td>"+Stage_Day1+"</td>");
			buf.append("<tr><td>Applications</td><td>"+app_Day7+"</td><td>"+app_Day6+"</td><td>"+app_Day5+"</td><td>"+app_Day4+"</td><td>"+app_Day3+"</td><td>"+app_Day2+"</td><td>"+app_Day1+"</td>");
			buf.append("</table>" +"</body>" + "</html>");
			Table_format = buf.toString();
			
			
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
		
		return Table_format;
		
	}

	private String getLoadID(String query,Statement stmt) {
		

        ResultSet Res=null;
	    String load_id="0";
	    String load_id_json="";
		try
		{

        	Res=stmt.executeQuery(query);
        	
        	 if(Res.next())
 	        {
        		 load_id_json=Res.getString(1);
 	        }
        	 
        	 JSONArray array = new JSONArray(load_id_json);  
        	 JSONObject object = array.getJSONObject(0);
        	 load_id=object.getString("load_id");
        	 System.out.println(object.getString("load_id"));  
  
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try {
				Res.close();
			} catch (SQLException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			}
		}
		
		return load_id;
		
	}
	
private long ExecuteCountQuery(String query,Statement stmt,String load_id) {
		
		String Query=null;
        ResultSet Res=null;
	    long count=0;
	    
		try
		{
			Query=query.replaceFirst("\\?", load_id);
        	
        	Res=stmt.executeQuery(Query);
        	
        	 if(Res.next())
 	        {
        		 count=Res.getLong(1);
        		 
 	        }
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try {
				Res.close();
			} catch (SQLException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			}
		}
		return count;
	}


private String ExecuteTimeStampQuery(String query,Statement stmt,String load_id) {
	
	String Query=null;
    ResultSet Res=null;
    String final_date="";
    int i=0;
	try
	{
		Query=query.replaceFirst("\\?", load_id);
    	
    	Res=stmt.executeQuery(Query);
    	
    	 	while(Res.next())
	        {
    	 		String Date=null;
    	 		
    	 		
    	 		Date=Res.getString(1);
    	 		if(i==0)
    	 		{
    	 			final_date=Date;	
    	 		}
    	 		else
    	 		{
    	 			final_date=final_date.concat(";").concat(Date);
    	 		}
    	 		i++;
    	 		
	        }
    	 	
	}
	catch(Exception e)
	{
		ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
		e.printStackTrace();
	}
	finally
	{
		try {
			Res.close();
		} catch (SQLException e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
	}
	return final_date;
}

	

}
