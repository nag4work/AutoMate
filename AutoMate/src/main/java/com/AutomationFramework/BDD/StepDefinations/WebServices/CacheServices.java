package com.AutomationFramework.BDD.StepDefinations.WebServices;

import static org.mockito.ArgumentMatchers.longThat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.en.Given;
import com.AutomationFramework.BDD.Runner.DataClass;
import com.AutomationFramework.BDD.StepDefinations.DailyMonitoring.APIMonitoringWrapper;
import com.AutomationFramework.BDD.StepDefinations.DailyMonitoring.MonitoringValidations;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.impl.DataBaseConnections;
import com.AutomationFramework.com.impl.SnowFlakeDataBaseConnection;
import com.AutomationFramework.com.impl.WebServicesImpl;
import com.Wrapper.ResponseWrapper;

public class CacheServices {
	
public static final Logger logger = LoggerFactory.getLogger(MonitoringValidations.class);
	
CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
WebServicesImpl WebServicesImpl_Obj=new WebServicesImpl();
public static String Subject="";
public static String Message_Body;

	@Given("^Get the cache Service DB count from snow flake and from Cache Web Service$")
	public void Get_ICO_Value_Using_VIN_and_UVC() throws Throwable 
	{
		String Query=null;
    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
    	Properties properties = null;
    	Statement stmt=null;
    	long UVC_count=0;
    	long MILEAGE_ADJ_Count=0;
    	long ADD_DEDUCT_Count=0;
    	long ZIPCODE_Count=0;
    	long MAI_delete_Count=0;
    	long MAI_add_Count=0;
    	
    	JSONObject Cache_UVC_json =null;
    	JSONObject Cache_add_deduct_json =null;
    	JSONObject Cache_Mileage_adj_json =null;
    	JSONObject Cache_ZIPCODE_json =null;
    	JSONObject Cache_MAI_json =null;
    	
    	 String Cache_UVC_Key=null;
	     String Cache_UVC_Count=null;
	     String Cache_UVC_message=null;
	     
	     String Cache_add_deduct_Key=null;
	     String Cache_add_deduct_Count=null;
	     String Cache_add_deduct_message=null;
	     
	     String Cache_Mileage_adj_Key=null;
	     String Cache_Mileage_adj_Count=null;
	     String Cache_Mileage_adj_message=null;
	     
	     
	     String Cache_MAI_Key=null;
	     String Cache_MAI_Count=null;
	     String Cache_MAI_message=null;
	     
	     String Cache_ZipCode_Key=null;
	     String Cache_ZipCode_Count=null;
	     String Cache_ZipCode_message=null;
    	
		try {

			 AutomationMainClass.email_mode="Cache Services Monitoring";
			
	        /********************************************Get the SnowFlake Data***********************************************************/
			 
			 SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("applications");
			 properties=CustomFunctions_OBJ.GetSQLQueryProperties();
				
		     stmt = SnowFlakeDataBaseConnection.connection.createStatement();
		     
		     
		     Query=properties.getProperty("Get_UVC_Count");
		     UVC_count=ExecuteCountQuery(Query,stmt);
		     
		     Query=properties.getProperty("Get_ADD_DEDUCT_Count");
		     ADD_DEDUCT_Count=ExecuteCountQuery(Query,stmt);
		     
		     Query=properties.getProperty("Get_MILEAGE_ADJ_Count");
		     MILEAGE_ADJ_Count=ExecuteCountQuery(Query,stmt);
		     
		     Query=properties.getProperty("Get_ZIPCODE_Count");
		     ZIPCODE_Count=ExecuteCountQuery(Query,stmt);
		     
		     Query=properties.getProperty("Get_MAI_add_Count");
		     MAI_add_Count=ExecuteCountQuery(Query,stmt);
		     
		     Query=properties.getProperty("Get_MAI_delete_Count");
		     MAI_delete_Count=ExecuteCountQuery(Query,stmt,"");
	        

			 Cache_UVC_json=Get_CacheService_Details("UVC");
		     Cache_add_deduct_json=Get_CacheService_Details("ADD_DEDUCT");
		     Cache_Mileage_adj_json=Get_CacheService_Details("MILEAGE_ADJ");
		     Cache_MAI_json=Get_CacheService_Details("MAI");
		     Cache_ZIPCODE_json=Get_CacheService_Details("ZIPCODE");
		    
		     /*************************Process UVC JSON Object********************************************************************************/
			     try
			     {
				     Cache_UVC_Key=Cache_UVC_json.get("key").toString();
				     Cache_UVC_Count=Cache_UVC_json.get("count").toString();
				     Cache_UVC_message=Cache_UVC_json.get("message").toString();
			     }
			     catch(Exception E)
			     {
			    	 ;
			     }
			     
			 
			 /*************************Process ADD DEDUCT JSON Object********************************************************************************/
			     try
			     {
				     Cache_add_deduct_Key=Cache_add_deduct_json.get("key").toString();
				     Cache_add_deduct_Count=Cache_add_deduct_json.get("count").toString();
				     Cache_add_deduct_message=Cache_add_deduct_json.get("message").toString();
			     }
			     catch(Exception E)
			     {
			    	 ;
			     }
			     
			 /*************************Process MILEAGE ADJ JSON Object********************************************************************************/     
			     try
			     {
			    	 Cache_Mileage_adj_Key=Cache_Mileage_adj_json.get("key").toString();
			    	 Cache_Mileage_adj_Count=Cache_Mileage_adj_json.get("count").toString();
			    	 Cache_Mileage_adj_message=Cache_Mileage_adj_json.get("message").toString();
			     }
			     catch(Exception E)
			     {
			    	 ;
			     }
			     
			     
			     /*************************Process MAI JSON Object********************************************************************************/     
			     try
			     {
			    	 Cache_MAI_Key=Cache_MAI_json.get("key").toString();
			    	 Cache_MAI_Count=Cache_MAI_json.get("count").toString();
			    	 Cache_MAI_message=Cache_MAI_json.get("message").toString();
			     }
			     catch(Exception E)
			     {
			    	 ;
			     }
			     
			     
			     /*************************Process ZipCode JSON Object********************************************************************************/     
			     try
			     {
			    	 Cache_ZipCode_Key=Cache_ZIPCODE_json.get("key").toString();
			    	 Cache_ZipCode_Count=Cache_ZIPCODE_json.get("count").toString();
			    	 Cache_ZipCode_message=Cache_ZIPCODE_json.get("message").toString();
			     }
			     catch(Exception E)
			     {
			    	 ;
			     }
			     
			 
		     
		     
		     
	         
	         if((Cache_Mileage_adj_message.compareToIgnoreCase("FAILURE")==0)||(Cache_add_deduct_message.compareToIgnoreCase("FAILURE")==0)
	        		 ||(Cache_UVC_message.compareToIgnoreCase("FAILURE")==0)
	        		 ||(Cache_MAI_message.compareToIgnoreCase("FAILURE")==0)
	        		 ||(Cache_ZipCode_message.compareToIgnoreCase("FAILURE")==0))
	        	 
	         {  	
	        	 CacheServices.Subject="Critical:";
	         }
	         else
	         {
	        	 CacheServices.Subject="All Clear:";
	         }

          
	         
	            StringBuilder buf = new StringBuilder();
				buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=10 cellspacing=0>" +"<tr>" +"<th>Key</th>" +"<th>Service Count</th>"+"<th>Service Message</th>"+"<th>DB Count</th>"+"</tr>");
				buf.append("<tr><td>"+Cache_UVC_Key+"</td><td>"+Cache_UVC_Count+"</td><td>"+Cache_UVC_message+"</td><td>"+UVC_count+"</td>");
				buf.append("<tr><td>"+Cache_add_deduct_Key+"</td><td>"+Cache_add_deduct_Count+"</td><td>"+Cache_add_deduct_message+"</td><td>"+ADD_DEDUCT_Count+"</td>");
				buf.append("<tr><td>"+Cache_Mileage_adj_Key+"</td><td>"+Cache_Mileage_adj_Count+"</td><td>"+Cache_Mileage_adj_message+"</td><td>"+MILEAGE_ADJ_Count+"</td>");
				
				buf.append("<tr><td>"+Cache_ZipCode_Key+"</td><td>"+Cache_ZipCode_Count+"</td><td>"+Cache_ZipCode_message+"</td><td>"+ZIPCODE_Count+"</td>");
				buf.append("<tr><td>"+Cache_MAI_Key+"</td><td>"+Cache_MAI_Count+"</td><td>"+Cache_MAI_message+"</td><td>"+(MAI_add_Count-MAI_delete_Count)+"</td>");
				buf.append("</table>" +"</body>" + "</html>");
	        
			
				CacheServices.Message_Body="<br>Automation execution for the Cache Services Monitoring completed. Please find the results below.<br></br>"
						+ "<br></br><b><u> Cache Service Status:</u></b><br></br>"
					    + "\t\t"+buf.toString()
						
						
						+ "<br><br></br> This is auto generated email. Please don't reply to this email."
						+ "<br></br>Thanks,"
						+ "<br>EDS Automation Group.<br></br>";

		}

		
		catch (Exception e) 
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
	
	
	public JSONObject Get_CacheService_Details(String CacheKey) throws Throwable 
	{
		Properties prop = null;
		String getCacheServicesResponse=null;
		String Final_Header=null;
		JSONObject jsonObject_getCacheServicesResponse =null;

		try {

			logger.info("Calling get details by VIN API method");
			  
			prop=CustomFunctions_OBJ.ApplicationProperties();
			

			String Header1="Content-Type,application/json";
			String Header2=prop.getProperty("Cache_Services_Key")+","+prop.getProperty("Cache_Services_Key_Value");

			Final_Header=Header1.concat(";").concat(Header2).concat(";");
			
			DataClass.APIGetCallhmap.put("Header",Final_Header);			
			DataClass.APIGetCallhmap.put("GetURLParamVal","*_"+CacheKey+"?count="+prop.getProperty("Cache_count_Value"));			
			DataClass.APIGetCallhmap.put("APIURL",prop.getProperty("Cache_Services"));
			DataClass.APIGetCallhmap.put("GETorPOSTCall","GET");
			DataClass.APIGetCallhmap.put("Get_Details_By_VIN_Service_Response_Time","Detail_by_VIN_Service_Response_Time");
			
			getCacheServicesResponse=WebServicesImpl_Obj.APICalls("CacheServices");
			
			jsonObject_getCacheServicesResponse=ConvertStringToJson(getCacheServicesResponse); 
			
			//System.out.println(jsonObject);
			logger.info("Execution of Get_CacheService_Details method completed");

		} catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		return jsonObject_getCacheServicesResponse;

	}
	
	
	public JSONObject ConvertStringToJson(String APIResponseString)
	{
		JSONObject json =null;
		try
		{
			json = new JSONObject(APIResponseString);  
			logger.info(json.toString()); 
			
		}catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
	return json;
	}


	private String getCurrentDate() {
		

		 String CurrentDate="";
		try
		{
			 Date date = new Date();
			 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd");
			 TimeZone pstTime = TimeZone.getTimeZone("PST");
			 pstFormat.setTimeZone(pstTime);
			 CurrentDate=pstFormat.format(date);

				
		}
		catch(Exception E)
		{
			;
		}
		return CurrentDate;
	}
	

	
private long ExecuteCountQuery(String query,Statement stmt) {
		
        ResultSet Res=null;
	    long count=0;
	    
		try
		{
        	
        	Res=stmt.executeQuery(query);
        	
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

private long ExecuteCountQuery(String query,Statement stmt,String filed) {
	
    ResultSet Res=null;
    long count=0;
    
	try
	{
    	
    	Res=stmt.executeQuery(query);
    	
    	 while(Res.next())
	        {
    		 count=count+1;
    		 
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




}
