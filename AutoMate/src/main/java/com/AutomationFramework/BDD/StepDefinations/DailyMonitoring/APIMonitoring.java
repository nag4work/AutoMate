package com.AutomationFramework.BDD.StepDefinations.DailyMonitoring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.en.Given;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.Pojos.QueryParametersWrapper;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.impl.DataBaseConnections;
import com.AutomationFramework.com.impl.ResultsDBImplementation;
import com.AutomationFramework.com.impl.SnowFlakeDataBaseConnection;
import com.AutomationFramework.com.impl.WebServicesImpl;
import com.Wrapper.ResponseWrapper;

public class APIMonitoring {



public static final Logger logger = LoggerFactory.getLogger(MonitoringValidations.class);
	
	
CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
public static int warning_cnt=0;
public static int Urgent_cnt=0;
	public static List<String[]> Summary=new ArrayList<String[]>();
	
	@Given("^Run the API monitoring the task to get hourly Status$")
	public void Get_ICO_Value_Using_VIN_and_UVC() throws Throwable 
	{
		
		try {

			 AutomationMainClass.email_mode="API Monitoring";
			 String CurrentDateHour="";
			 CurrentDateHour=getCurrentDateTimeWithUTCformat();

			 System.out.println("CurrentDateHour------>"+CurrentDateHour);
			
			
	        /********************************************Get the SnowFlake Data***********************************************************/
	         getCVPDatafromSnowFlake(CurrentDateHour);
	        
	       
	        /********************************************Get the data from SQL Server*****************************************************/
	         
	         getExceptionCountData_CurrentDate(CurrentDateHour);
	         getExceptionCountData_PreviousDate(CurrentDateHour);
	         
	         if(APIMonitoringWrapper.Exceptions_Count>0)
	         {  	
	        	 if(warning_cnt>0)
	        	 {
	        		 APIMonitoringWrapper.Subject="Warning :"; 
	        	 }
	        	 else if(Urgent_cnt>0)
	        	 {
	        		 APIMonitoringWrapper.Subject="Urgent :"; 
	        	 }
	        	 else {
	        		 APIMonitoringWrapper.Subject="Critical:";
				}
	        	 
	         }
	         else
	         {
	        	 APIMonitoringWrapper.Subject="All Clear:";
	         }

	            StringBuilder buf = new StringBuilder();
				buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=10 cellspacing=0>" +"<tr>" +"<th>Query Name</th>" +"<th>Current Count</th>"+"<th>Previous Day Count At Present Time</th>"+"<th>Previous Day Complete Count</th>"+"</tr>");
				buf.append("<tr><td>Exceptions</td><td>"+APIMonitoringWrapper.Exceptions_Count+"</td><td>"+APIMonitoringWrapper.Exceptions_Count_Previous_Day_Current_Hour+"</td><td>"+APIMonitoringWrapper.Exceptions_Count_Previous_Day+"</td>");
				buf.append("<tr><td>Non Exceptions</td><td>"+APIMonitoringWrapper.Non_Exceptions_Count+"</td><td>"+APIMonitoringWrapper.Non_Exceptions_Count_Previous_Day_Current_Hour+"</td><td>"+APIMonitoringWrapper.Non_Exceptions_Count_Previous_Day+"</td>");
				buf.append("<tr><td>Total Request</td><td>"+APIMonitoringWrapper.Total_Request+"</td><td>"+APIMonitoringWrapper.Total_Request_Previous_Day_Current_Hour+"</td><td>"+APIMonitoringWrapper.Total_Request_Previous_Day+"</td>");
				buf.append("<tr><td>Total ICO & Estimate Count</td><td>"+APIMonitoringWrapper.Snow_Flake_CVP_count+"</td><td>"+APIMonitoringWrapper.Snow_Flake_CVP_count_Previous_Day_Current_Hour+"</td><td>"+APIMonitoringWrapper.Snow_Flake_CVP_count_Previous_Day+"</td>");
				buf.append("</table>" +"</body>" + "</html>");
	        
			
				APIMonitoringWrapper.Message_Body="<br>Automation execution for the API Monitoring completed. Please find the results below.<br></br>"
						+ "<br></br><b><u> API Monitoring Status:</u></b><br></br>"
					    + "\t\t"+buf.toString()
						
						+ "<br></br><b><u> Exceptions Details Status:</u></b><br></br>"
					    + "\t\t"+APIMonitoringWrapper.HTML_Exception_data
						
						+ "<br><br></br> This is auto generated email. Please don't reply to this email."
						+ "<br></br>Thanks,"
						+ "<br>EDS Automation Group.<br></br>";
	        
	        
	        
                       

		}

		
		catch (Exception e) {
			//ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}


	}
	
	





	private void getExceptionCountData_PreviousDate(String currentDateHour) {


		DataBaseConnections DataBaseConnections_OBJ=new DataBaseConnections();
		Statement stmt=null;
		String Query=null;
        Properties Properties = null;
        String PreviousDateHour=null;
        String PreviousDateCurrentTime=null;
        
        PreviousDateHour=getPreviousDateTimeWithUTCformat();
       

        
		try
		{
				Properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
				DataBaseConnections_OBJ.Logging_SQLServerCVPConnection("logDB");
				stmt = DataBaseConnections.SQL_CVP_db_connection.createStatement();
	        
			    Query=Properties.getProperty("Previous_day_Exceptions_Count");
			    APIMonitoringWrapper.Exceptions_Count_Previous_Day=ExecuteCountQuery_PreviousDay(Query,stmt,currentDateHour,PreviousDateHour);
			    
			    Query=Properties.getProperty("Previous_day_Non_Exceptions_Count");
			    APIMonitoringWrapper.Non_Exceptions_Count_Previous_Day=ExecuteCountQuery_PreviousDay(Query,stmt,currentDateHour,PreviousDateHour);
			    
			    Query=Properties.getProperty("Previous_day_Total_Request");
			    APIMonitoringWrapper.Total_Request_Previous_Day=ExecuteCountQuery_PreviousDay(Query,stmt,currentDateHour,PreviousDateHour);
		
			    
			    APIMonitoringWrapper.Snow_Flake_CVP_count_Previous_Day=getCVPDatafromSnowFlake(currentDateHour,PreviousDateHour);
			    
			    

				
				
				
			   PreviousDateCurrentTime=getPreviousDateCurrentTime();
			   System.out.println("Testing------>"+PreviousDateHour);
			   System.out.println("Testing------>"+PreviousDateCurrentTime);
			   Query=Properties.getProperty("Previous_day_Exceptions_Count");
			   APIMonitoringWrapper.Exceptions_Count_Previous_Day_Current_Hour=ExecuteCountQuery_PreviousDay(Query,stmt,PreviousDateCurrentTime,PreviousDateHour);
				    
			   Query=Properties.getProperty("Previous_day_Non_Exceptions_Count");
			   APIMonitoringWrapper.Non_Exceptions_Count_Previous_Day_Current_Hour=ExecuteCountQuery_PreviousDay(Query,stmt,PreviousDateCurrentTime,PreviousDateHour);
				    
			   Query=Properties.getProperty("Previous_day_Total_Request");
			   APIMonitoringWrapper.Total_Request_Previous_Day_Current_Hour=ExecuteCountQuery_PreviousDay(Query,stmt,PreviousDateCurrentTime,PreviousDateHour);
			
				    
			   APIMonitoringWrapper.Snow_Flake_CVP_count_Previous_Day_Current_Hour=getCVPDatafromSnowFlake(PreviousDateCurrentTime,PreviousDateHour);
				    

			    

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				stmt.close();
				DataBaseConnections_OBJ.Logging_CloseCVPDBConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

		
	
		
	
		
	}



	private String getCurrentDateTimeWithUTCformat() {
		
		 String CurrentDateHour="";
		 String CurrentDate="";
		 String CurrentHour="";
		 String PSTCurrentHour=null;
		 String Time = "00:00:00.000 am";
		try
		{
			Calendar day = Calendar.getInstance();
			day.set(Calendar.MILLISECOND, 0);
			day.set(Calendar.SECOND, 0);
			day.set(Calendar.MINUTE, 0);
			day.set(Calendar.HOUR_OF_DAY, 0);
			System.out.println(day.getTime());
	    
			 Date date = new Date();
			 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd");
			 TimeZone pstTime = TimeZone.getTimeZone("PST");
			 pstFormat.setTimeZone(pstTime);
			 CurrentDate=pstFormat.format(date);
			 

				final SimpleDateFormat formatter = new SimpleDateFormat("07:mm:ss.000", Locale.US);
				formatter.setTimeZone(TimeZone.getTimeZone("PST"));
				//Date UTCTime = formatter.parse(Time);
				PSTCurrentHour = formatter.format(day.getTime());
				
				formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
				//CurrentHour=formatter.format(UTCTime);
				
				CurrentHour=formatter.format(day.getTime());
			
				CurrentDateHour=CurrentDate+" "+CurrentHour;
			
			 
			 
			 System.out.println(CurrentDateHour);
				
		}
		catch(Exception E)
		{
			;
		}
		return CurrentDateHour;
	}
	
	
	private String getPreviousDateTimeWithUTCformat() {
		
		 String PreviousDateHour="";
		 String CurrentDate="";
		 String previousDate_formated="";
		 String PreviousHour="";
		 String Time = "00:00:00.000 am";
		 String CurrentDateHour="";
		try
		{
			
			Calendar day = Calendar.getInstance();
			day.set(Calendar.MILLISECOND, 0);
			day.set(Calendar.SECOND, 0);
			day.set(Calendar.MINUTE, 0);
			day.set(Calendar.HOUR_OF_DAY, 0);
			System.out.println(day.getTime());
			
			
			final SimpleDateFormat formatter = new SimpleDateFormat("07:mm:ss.000", Locale.US);
			formatter.setTimeZone(TimeZone.getTimeZone("PST"));
			//Date UTCTime = formatter.parse(Time);
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			//PreviousHour=formatter.format(UTCTime);
			PreviousHour=formatter.format(day.getTime());
			
			 Date date = new Date();
			 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd");
			 TimeZone pstTime = TimeZone.getTimeZone("PST");
			 pstFormat.setTimeZone(pstTime);
			 CurrentDate=pstFormat.format(date);
			 
			 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	         Date myDate = dateFormat.parse(CurrentDate);
	         Calendar calendar = Calendar.getInstance();
	         calendar.setTime(myDate);
	         calendar.add(Calendar.DAY_OF_YEAR, -1);

	        Date previousDate = calendar.getTime();
	        previousDate_formated = dateFormat.format(previousDate);
				
			
	        PreviousDateHour=previousDate_formated+" "+PreviousHour;
			 System.out.println(CurrentDateHour);
			 System.out.println(PreviousDateHour);
				
		}
		catch(Exception E)
		{
			;
		}
		return PreviousDateHour;
	}
	
	private String getPreviousDateCurrentTime() {
		
		 String CurrentDateTime_P="";
		 String previousDate_P="";
		try
		{
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.000");  
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000");
			  
			Instant instant = Instant.now();
			OffsetDateTime now = OffsetDateTime.now( ZoneOffset.UTC );
			System.out.println( "now.toString(): " + dtf.format(now) );
			CurrentDateTime_P=dtf.format(now) ;
			
            Date myDate = dateFormat.parse(CurrentDateTime_P);
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(myDate);
	        calendar.add(Calendar.DAY_OF_YEAR, -1);

	        Date previousDate = calendar.getTime();
	        previousDate_P = dateFormat.format(previousDate);
					
            System.out.println(previousDate_P);
				
		}
		catch(Exception E)
		{
			;
		}
	return previousDate_P;
	}







	private void getExceptionCountData_CurrentDate(String currentDateHour) {

		DataBaseConnections DataBaseConnections_OBJ=new DataBaseConnections();
		Statement stmt=null;
		String Query=null;
        Properties Properties = null;
        
		try
		{
				Properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
				DataBaseConnections_OBJ.Logging_SQLServerCVPConnection("logDB");
				stmt = DataBaseConnections.SQL_CVP_db_connection.createStatement();
	        
			    Query=Properties.getProperty("To_Get_Exceptions_Count");
			    System.out.println("CurrentDateHour------>"+Query);
			    APIMonitoringWrapper.Exceptions_Count=ExecuteCountQuery(Query,stmt,currentDateHour);
			    System.out.println("Exceptions_Count------>"+APIMonitoringWrapper.Exceptions_Count);
			    
			    Query=Properties.getProperty("To_Get_Non_Exceptions_Count");
			    APIMonitoringWrapper.Non_Exceptions_Count=ExecuteCountQuery(Query,stmt,currentDateHour);
			    
			    Query=Properties.getProperty("Total_Request");
			    APIMonitoringWrapper.Total_Request=ExecuteCountQuery(Query,stmt,currentDateHour);
			    
			    
			    Query=Properties.getProperty("To_Get_Exception_Detail");
			    APIMonitoringWrapper.HTML_Exception_data=ExecuteExceptionDetailQuery(Query,stmt,currentDateHour);
            		

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				stmt.close();
				DataBaseConnections_OBJ.Logging_CloseCVPDBConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

		
	
		
	}


	private String ExecuteExceptionDetailQuery(String query, Statement stmt, String currentDateHour) 
	{
		String Query=null;
        ResultSet Res=null;
        String email_html="";
	    
	    try
		{
			Query=query.replaceFirst("\\?", AutomationMainClass.environment);
			Query=Query.replaceFirst("\\?", currentDateHour);
        	Res=stmt.executeQuery(Query);
        	
        	StringBuilder buf = new StringBuilder();
			buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=10 cellspacing=0>" +"<tr>" +"<th>time_stamp</th>" +"<th>message</th>" 
        	+"<th>exception</th>"+"<th>log_event</th>"+"<th>event_type</th>"+"<th>event_code</th>"+"<th>source_context</th>"+"<th>environment_username</th>"+"<th>machine_name</th>"+"</tr>");
			
        	 while(Res.next())
 	        {
        		 buf.append("<tr><td>"+Res.getTimestamp(1)+"</td>"
        		 		+ "<td>"+Res.getString(2)+"</td>"
        		 		+ "<td>"+Res.getString(3)+"</td>"
        		 		+ "<td>"+Res.getString(4)+"</td>"
        		 		+ "<td>"+Res.getString(5)+"</td>"
        		 		+ "<td>"+Res.getString(6)+"</td>"
        		 		+ "<td>"+Res.getString(7)+"</td>"
        		 		+ "<td>"+Res.getString(8)+"</td>"
        		 		+ "<td>"+Res.getString(9)+"</td>");
        		 
        		 if(Res.getString(3).contains("Code:404"))
        		 {
        			 warning_cnt=warning_cnt+1;
        		 }
        		 if(Res.getString(3).contains("Code:500"))
        		 {
        			 Urgent_cnt=Urgent_cnt+1;
        		 }
     			
        		 
 	        }
        	 
        	buf.append("</table>" +"</body>" + "</html>");
 			email_html = buf.toString();
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
		return email_html;
	
	}







	private void getCVPDatafromSnowFlake(String currentDateHour) {

    	String Query=null;
    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
    	Properties properties = null;
    	Statement stmt=null;
		try
		{
			SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
			properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
	        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
	        
			    Query=properties.getProperty("To_Get_DriveWay_Count");
			    APIMonitoringWrapper.Snow_Flake_CVP_count=ExecuteCountQuery(Query,stmt,currentDateHour);
            		

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				stmt.close();
				SnowFlakeDataBaseConnection_OBJ.CloseSnowFlakeDBConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

		
	}
	
	private long getCVPDatafromSnowFlake(String currentDateHour,String PreviousDateHour) {

    	String Query=null;
    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
    	Properties properties = null;
    	Statement stmt=null;
    	long count=0;
		try
		{
			SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
			properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
	        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
	        
			    Query=properties.getProperty("Previous_day_DriveWay_Count");
			    count=ExecuteCountQuery_PreviousDay(Query,stmt,currentDateHour,PreviousDateHour);
            		

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				stmt.close();
				SnowFlakeDataBaseConnection_OBJ.CloseSnowFlakeDBConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
  return count;
		
	}


private long ExecuteCountQuery(String query,Statement stmt,String currentDateHour) {
		
		String Query=null;
        ResultSet Res=null;
	    long count=0;
	    
		try
		{
			Query=query.replaceFirst("\\?", currentDateHour);
			
			System.out.println("Query-->"+Query);
        	
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


private long ExecuteCountQuery_PreviousDay(String query, Statement stmt, String currentDateHour, String previousDateHour) {

	
	String Query=null;
    ResultSet Res=null;
    long count=0;
    
	try
	{
		Query=query.replaceFirst("\\?", previousDateHour);
		Query=Query.replaceFirst("\\?", currentDateHour);
    	
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

/*****************************************************************************************************************************************************/
/********************************Get the VINs information with NO Price disposition******************************************************************/
	
	@Given("^Run the given query to get No Price disposition vehicle details$")
	public void get_No_Price_Disposition_Vehicle_Details() throws Throwable 
	{
		String PreviousDateHour=null;
		String CurrentDateHour="";
		String Query=null;
    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
    	Properties properties = null;
    	Statement stmt=null;
    	ResultSet Res=null;
		 
		try {
	
			 AutomationMainClass.email_mode="No Price Vehicles Monitoring";
			
			 CurrentDateHour=getCurrentDateTimeWithUTCformat();
	

			 PreviousDateHour=getPreviousDateTimeWithUTCformat();
	       
	         
			 SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
			 properties=CustomFunctions_OBJ.GetSQLQueryProperties();
				
		     stmt = SnowFlakeDataBaseConnection.connection.createStatement();
		        
			 Query=properties.getProperty("get_No_Price_Disposition_Vehicle_Details");
			 Query=Query.replaceFirst("\\?", PreviousDateHour);
			 Query=Query.replaceFirst("\\?", CurrentDateHour);
	        	
	          Res=stmt.executeQuery(Query);
	          StringBuilder buf = new StringBuilder();
			  buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=3 cellspacing=0>" +"<tr>" +"<th>REQUEST_ID</th>" +"<th>VIN</th>"+"<th>UVC</th>"+"<th>MILEAGE</th>"+"<th>ZIP_CODE</th>"+"<th>INSERTED_DATE</th>"+"<th>ICO Retail Listing Count</th>"+"</tr>");
					
	        	
	        	 while(Res.next())
	 	        {
	        		 String UVC=null;
	        		 int count=0;
	        		 String VIN=null;
	        		 String MILEAGE=null;
	        		 VIN=Res.getString(2);
	        		 UVC=Res.getString(3);
	        		 MILEAGE=Res.getString(4);
	        		 
	        		 count=getICORetaiListingCount(stmt,VIN,UVC,MILEAGE,properties);
	        		 
	        		 if(count>=5)
	        		 {
	        			 APIMonitoringWrapper.NoPrice_Subject="Urgent:";
	        			 //APIMonitoringWrapper.NoPrice_Subject="Critical:";
		        		 buf.append("<tr><td>"+Res.getString(1)+"</td><td>"+Res.getString(2)+"</td><td>"+Res.getString(3)+"</td><td>"+Res.getString(4)+"</td>"
		        		 		+ "<td>"+Res.getString(5)+"</td>"
		        		 		+ "<td>"+Res.getString(6)+"</td>"
		        		 		+ "<td>"+count+"</td>"); 
	        		 }
	        		 
	       		 
	 	        }
	
	           buf.append("</table>" +"</body>" + "</html>");
	        
			
				APIMonitoringWrapper.NoPrice_Message_Body="<br>Automation execution for the noPrice vehicles Monitoring completed. Please find the results below.<br></br>"
						+ "<br></br><b><u> NoPrice Vehicles Monitoring Status:</u></b><br></br>"
					    + "\t\t"+buf.toString()

						
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
			SnowFlakeDataBaseConnection_OBJ.CloseSnowFlakeDBConnection();
		}
	
	
	}
	
	
	
	
	private int getICORetaiListingCount(Statement stmt, String VIN, String uVC, String mILEAGE, Properties properties) 
	{
		String Query=null;
    	ResultSet Res=null;
    	int count=0;
    	int mileage_band=1;
    	int start_mileage_band=0;
    	int end_mileage_band=0;
    	String fuel_type=null;
		try {
		        
			mileage_band=getmileageband(mILEAGE);
			
			start_mileage_band=mileage_band-2;
			end_mileage_band=mileage_band+1;
			
			fuel_type=get_fuel_type(stmt,VIN,properties);
			 
			 Query=properties.getProperty("get_RPT_ICO_RETAILISTING_LOOKUP_Count");
			 Query=Query.replaceFirst("\\?", uVC);
			 Query=Query.replaceFirst("\\?", String.valueOf(start_mileage_band));
			 Query=Query.replaceFirst("\\?", String.valueOf(end_mileage_band));
			 Query=Query.replaceFirst("\\?", fuel_type);
	        	
	          Res=stmt.executeQuery(Query);
	        	 if(Res.next())
	 	        {
	        		 count=Res.getInt(1);
	       		 
	 	        }
	
		}
	
		
		catch (Exception e) {
			e.printStackTrace();
		}

	return count;
	}
	
	private String get_fuel_type(Statement stmt, String VIN,Properties properties) 
	{
		String Query=null;
    	ResultSet Res=null;
    	String fuel_type=null;

		try {
		        

			
			Query=properties.getProperty("get_fuel_type");
			Query=Query.replaceFirst("\\?", VIN);
            Res=stmt.executeQuery(Query);
	        	 if(Res.next())
	 	        {
	        		 fuel_type=Res.getString(1);
	       		 
	 	        }
	
		}
	
		
		catch (Exception e) {
			e.printStackTrace();
		}

	return fuel_type;
	}


	private int getmileageband(String mILEAGE) 
	{
		int mileage_band=1;
		
		try
		{
				int mILEAGE_int=Integer.parseInt(mILEAGE);
				
				if(mILEAGE_int<5000)
				 {
					 mileage_band=1; 
				 }
				 else if((mILEAGE_int>=5000) &&(mILEAGE_int<10000))
				 {
					 mileage_band=2;
				 }
				 else if((mILEAGE_int>=10000) &&(mILEAGE_int<15000))
				 {
					 mileage_band=3;
				 }
				 else if((mILEAGE_int>=15000) &&(mILEAGE_int<20000))
				 {
					 mileage_band=4;
				 }
				 else if((mILEAGE_int>=20000) &&(mILEAGE_int<25000))
				 {
					 mileage_band=5;
				 }
				 else if((mILEAGE_int>=25000) &&(mILEAGE_int<30000))
				 {
					 mileage_band=6;
				 }
				 else if((mILEAGE_int>=30000) &&(mILEAGE_int<35000))
				 {
					 mileage_band=7;
				 }
				 else if((mILEAGE_int>=35000) &&(mILEAGE_int<40000))
				 {
					 mileage_band=8;
				 }
				 else if((mILEAGE_int>=40000) &&(mILEAGE_int<45000))
				 {
					 mileage_band=9;
				 }
				 else if((mILEAGE_int>=45000) &&(mILEAGE_int<50000))
				 {
					 mileage_band=10;
				 }
				 else if((mILEAGE_int>=50000) &&(mILEAGE_int<55000))
				 {
					 mileage_band=11;
				 }
				 else if((mILEAGE_int>=55000) &&(mILEAGE_int<60000))
				 {
					 mileage_band=12;
				 }
				 else if((mILEAGE_int>=60000) &&(mILEAGE_int<70000))
				 {
					 mileage_band=13;
				 }
				 else if((mILEAGE_int>=70000) &&(mILEAGE_int<80000))
				 {
					 mileage_band=14;
				 }
				 else if((mILEAGE_int>=80000) &&(mILEAGE_int<90000))
				 {
					 mileage_band=15;
				 }
				 else if((mILEAGE_int>=90000) &&(mILEAGE_int<100000))
				 {
					 mileage_band=16;
				 }
				 else if((mILEAGE_int>=100000) &&(mILEAGE_int<120000))
				 {
					 mileage_band=17;
				 }
				 else if((mILEAGE_int>=120000) &&(mILEAGE_int<140000))
				 {
					 mileage_band=18;
				 }
				 else if((mILEAGE_int>=140000) &&(mILEAGE_int<160000))
				 {
					 mileage_band=19;
				 }
				 else if((mILEAGE_int>=160000) &&(mILEAGE_int<180000))
				 {
					 mileage_band=20;
				 }
				 else if((mILEAGE_int>=180000) &&(mILEAGE_int<200000))
				 {
					 mileage_band=21;
				 }
				 else if(mILEAGE_int>=200000)
				 {
					 mileage_band=22;
				 }
	}
	catch(Exception e)
	{
		;
	}
		return mileage_band;
	}







	/*****************************************************************************************************************************************************/
	/*****************************************************************************************************************************************************/
	/********************************Get the VINs information with NO Price disposition******************************************************************/
	/*****************************************************************************************************************************************************/
	/*****************************************************************************************************************************************************/
		
		@Given("^Get the Missing VINs from the driveway customer table from the given date$")
		public void get_DriveWay_Missing_VINs() throws Throwable 
		{
			String end_datetime=null;
			String Stardatetime="";
			StringBuilder buf = new StringBuilder();
			String VINS_DECODED_BB_USED_VEHICLES_LIST=null;
			try {
		
				
				 AutomationMainClass.email_mode="Missing VINS Monitoring";
				
				 //CurrentDateHour=getCurrentDateTimeWithUTCformat();
				// PreviousDateHour=getPreviousDateTimeWithUTCformat();
				 
				   Instant Inst_Stardatetime=Instant.now().minus( 13 , ChronoUnit.HOURS);
				   Instant Inst_end_datetime=Instant.now().minus( 1 , ChronoUnit.HOURS);
				   System.out.println("Stardatetime-->"+Inst_Stardatetime); 
				   System.out.println("end_datetime-->"+Inst_end_datetime); 
				   
				   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00.000").withZone( ZoneId.of("UTC"));;
				   Stardatetime = formatter.format(Inst_Stardatetime);
				   end_datetime = formatter.format(Inst_end_datetime);
			       System.out.println("Instant in String format: "+Stardatetime);
			       System.out.println("Instant in String format: "+end_datetime);
			       
			       DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:00:00.000");
			       long time = df.parse(Stardatetime).getTime();
			       time=time-(15 * 60*1000);
			       System.out.println("Final End Time: "+time);
				 
			       Date result = new Date(time);
			       DateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:00.000");
			       Stardatetime=formatter1.format(result);
		       
				// VINS_DECODED_BB_USED_VEHICLES_LIST=getVINS_DECODED_BB_USED_VEHICLES_LISTData(end_datetime,Stardatetime);
				 buf=getDriveWayCustomerData(end_datetime,Stardatetime,VINS_DECODED_BB_USED_VEHICLES_LIST);
				 

				
					APIMonitoringWrapper.Missing_VINs_Message_Body="<br>Please find the VINs present in  Drive Way customer table but not present in VINS_DECODED_BB_USED_VEHICLES_LIST table. Please find the results below.<br></br>"
							+ "<br></br><b><u> Missing VINs from "+ Stardatetime+" to "+end_datetime +"(Note: Time is in UTC format):</u></b><br></br>"
						    + "\t\t"+buf.toString()

							
							+ "<br><br></br> This is auto generated email. Please don't reply to this email."
							+ "<br></br>Thanks,"
							+ "<br>EDS Automation Group.<br></br>";
		        
		        
		        
		                   
		
			}
		
			
			catch (Exception e) {
				//ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			}

		
		}
		
		
		private StringBuilder getDriveWayCustomerData(String currentDate, String PreviousDate,String Vins_Append_str) {

			String Query=null;
		    ResultSet Res=null;
		    StringBuilder buf = new StringBuilder();
	    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
	    	Properties properties = null;
	    	Statement stmt=null;
			try
			{
				SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
				properties=CustomFunctions_OBJ.GetSQLQueryProperties();
				
		        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
		        
				Query=properties.getProperty("get_DriveWay_Customer_VINs");
				Query=Query.replaceFirst("\\?", PreviousDate);
				Query=Query.replaceFirst("\\?", currentDate);

				Res=stmt.executeQuery(Query);
				
				
				  buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=3 cellspacing=0>" +"<tr>" +"<th>VINs</th>"+"<th>INSERTED_DATE</th>"+"</tr>");
					
			        	
	        	 while(Res.next())
			 	   {
			        	String DriveWay_VIN=null;
			        	String INSERTED_DATE=null;
			        	int count=0;
			        	DriveWay_VIN=Res.getString(1);
			        	INSERTED_DATE=Res.getString(2);
			        	
			        	count=getVINS_DECODED_BB_USED_VEHICLES_LISTCount(DriveWay_VIN,INSERTED_DATE,stmt,properties);
			        	
			        	if(count==0)
			        	{
			        		 APIMonitoringWrapper.Missing_VINs_Subject="Urgent:";
			        		 //APIMonitoringWrapper.Missing_VINs_Subject="Critical:";
			        		 buf.append("<tr><td>"+DriveWay_VIN+"</td><td>"+INSERTED_DATE+"</td>");
			        	}
			        		 
			 	    }
	        	 
	        	 buf.append("</table>" +"</body>" + "</html>");


			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try {
					stmt.close();
					SnowFlakeDataBaseConnection_OBJ.CloseSnowFlakeDBConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			return buf;
			
		}
		
		
		private int getVINS_DECODED_BB_USED_VEHICLES_LISTCount(String driveWay_VIN, String iNSERTED_DATE,Statement stmt,Properties properties) {


			String Query=null;
		    ResultSet Res=null;
		    int count=0;

			try
			{

				//SimpleDateFormat input_formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.US);
				SimpleDateFormat output_formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000", Locale.US);

				
				Date date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(iNSERTED_DATE);  
				Calendar cal = Calendar.getInstance();
				cal.setTime(date1);
				cal.add(Calendar.HOUR, -24);
				Date HourBack_Time = cal.getTime();
				
				String HoursBack_24 = output_formatter.format(HourBack_Time);

				 
				Query=properties.getProperty("get_VINS_DECODED_BB_USED_VEHICLES_LIST_VINs");
				Query=Query.replaceFirst("\\?", driveWay_VIN);
				Query=Query.replaceFirst("\\?", HoursBack_24);
				Query=Query.replaceFirst("\\?", iNSERTED_DATE);
				
				Res=stmt.executeQuery(Query);
				
	        	 if(Res.next())
			 	   {
	        		 count=Res.getInt(1);
			 	   }
	        	 
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			return count;
		}







		private String getVINS_DECODED_BB_USED_VEHICLES_LISTData(String currentDate, String PreviousDate) {

			String Query=null;
		    ResultSet Res=null;
			StringBuffer SB=new StringBuffer();
	    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
	    	Properties properties = null;
	    	Statement stmt=null;
			try
			{
				SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
				properties=CustomFunctions_OBJ.GetSQLQueryProperties();
				
		        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
		        
				Query=properties.getProperty("get_VINS_DECODED_BB_USED_VEHICLES_LIST_VINs");
				Query=Query.replaceFirst("\\?", PreviousDate);
				Query=Query.replaceFirst("\\?", currentDate);

				Res=stmt.executeQuery(Query);
			        	
	        	 while(Res.next())
			 	   {
	        		 SB.append(";").append(Res.getString(1));
			 	    }


			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try {
					Res.close();
					stmt.close();
					SnowFlakeDataBaseConnection_OBJ.CloseSnowFlakeDBConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
          return SB.toString();
			
		}
		

}
