package com.AutomationFramework.BDD.StepDefinations.Mdigital;

import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.ArgumentMatchers.nullable;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azure.storage.blob.BlobContainerClient;

import cucumber.api.java.en.Given;
import com.AutomationFramework.BDD.Runner.DataClass;
import com.AutomationFramework.BDD.StepDefinations.DailyMonitoring.APIMonitoringWrapper;
import com.AutomationFramework.BDD.StepDefinations.WebServices.APIRequests;
import com.AutomationFramework.BDD.Util.Utility;
import com.AutomationFramework.com.DriverEngine.Driver;
import com.AutomationFramework.com.KeywordHelper.WebAppKeywordHelper;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.Pojos.QueryParametersWrapper;
import com.AutomationFramework.com.custom.AzureBlobStorage;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.custom.EnvironmentVariables;
import com.AutomationFramework.com.impl.DataBaseConnections;
import com.AutomationFramework.com.impl.DataValidation;
import com.AutomationFramework.com.impl.ResultsDBImplementation;
import com.AutomationFramework.com.impl.SnowFlakeDataBaseConnection;
import com.AutomationFramework.com.impl.WebServicesImpl;
import com.Wrapper.ResponseWrapper;

public class MdigitalMonitoring {



public static final Logger logger = LoggerFactory.getLogger(MdigitalMonitoring.class);
	
	
CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
public static List<String[]> Max_digitaLfailure_Summary=new ArrayList<String[]>();
	
	@Given("^Execute the given hourly max digital test cases$")
	public void Execute_given_hourly_max_digital_testcases() throws Throwable 
	{
		int count=0;
		try {

			 AutomationMainClass.email_mode="Max Digital Hourly Monitoring";
			 //String CurrentDateHour="";
			 //CurrentDateHour=getCurrentDateTimeWithUTCformat();

			
	        /********************************************Get the SnowFlake Data***********************************************************/
	         get_Duplicate_Data();
	         getMaxdigital_HourlyRunning_Active_Inventory_count();
	         
	         //getMaxdigital_DailyRunning_VINs_decoded_from_BB_count();
	        
	        // count=get_Blackbook_Used_Vehicle_VIN_data_quality();
	         
	         get_Max_vs_BB_Year_or_Make_Mismatch();
	         
	         get_Maxdigital_UI_Count();
	         
	         System.out.println("Enterting into the Execute_given_hourly_max_digital_testcases");
	         
	         if((MdigitalWrapper.Maxdigital_Active_Inventory_dup_count>0) ||(MdigitalWrapper.Merchandise_Feed_dup_count>0))
	         {  	
	        	 MdigitalWrapper.Subject="Urgent:";
	         }
	         else if((MdigitalWrapper.Maxdigital_Sold_inventory_VINs_decoded_BB_count>0)||(MdigitalWrapper.Maxdigital_Active_inventory_VINs_exist_Merchandise_view_Pre_Suppression_count>0)
	        		 ||(MdigitalWrapper.Max_Active_Inventory_VINs_W_Dealerid_Stock_exist_Max_Sold_Inventory_cnt>0)
	        		 ||(MdigitalWrapper.Booked_Suppr_rule_count_9A>0) ||(MdigitalWrapper.Booked_Sales_Status_9B.compareToIgnoreCase("N")==0)
	        		 ||(MdigitalWrapper.Pending_Suppr_rule_count_9C>0) ||(MdigitalWrapper.Pending_Sales_Status_9D.compareToIgnoreCase("N")==0)
	        		 ||(MdigitalWrapper.In_Transit_Supp_rule_Count_10A>0) ||(MdigitalWrapper.In_Transit_inventory_status_10B.compareToIgnoreCase("N")==0)
	        		 ||(MdigitalWrapper.dealerinvoiceprice_Suppression_rule_count>0)
	        		 ||(MdigitalWrapper.Maxdigital_Active_inventory_VINs_decoded_from_BB>0))
	         {
	        	 MdigitalWrapper.Subject="Urgent:";
	         }
	         else if(MdigitalWrapper.Active_inventory_count<50000)
	         {
	        	 MdigitalWrapper.Subject="Urgent:";
	         }
	         else
	         {
	        	MdigitalWrapper.Subject="All Clear:";
	         }
	         
	         StringBuilder buf = new StringBuilder();
	         buf=CreateHourlyMonitoringMessageBody();
	         
	        
	         MdigitalWrapper.Hourly_monitoring_Message_Body="<br>Automation for the Max digital Hourly Monitoring queries execution completed. Please find the results below.<br></br>"
						+ "<br></br><b><u> Max Digital Monitoring Status:</u></b><br></br>"
					    + "\t\t\t\t\t"+buf.toString()
						
					    + "<br></br><b><u>Used Cars and New Cars UI status Vs DB:</u></b><br></br>"
					    + "\t\t\t\t\t"+MdigitalWrapper.New_Used_Cars_Data
					    
					    + "<br></br><b><u>Max vs BB (Year or Make Mismatch) Monitoring Status:</u></b><br></br>"
					    + "\t\t\t\t\t"+MdigitalWrapper.Max_vs_BB_Year_or_Make_Mismatch
					    
					    
					    + "<br></br><b><u>Status: Max Active Inventory VINs with same Dealerid, Stock also exist in Max Sold Inventory:</u></b><br></br>"
					    + "\t\t\t\t\t"+MdigitalWrapper.Max_Active_Inventory_VINs_W_Dealerid_Stock_exist_Max_Sold_Inventory_VINs
					    
					    
						+ "<br><br></br> This is auto generated email. Please don't reply to this email."
						+ "<br></br>Thanks,"
						+ "<br>EDS Automation Group.<br></br>";
	         
	         System.out.println("Message Body creation completed:Execute_given_hourly_max_digital_testcases");
	        
	        
		}

		
		catch (Exception e) {
			//ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}


	}
	
	
	private void get_Maxdigital_UI_Count() {
		
		CustomFunctions CustomFunctions_obj=new CustomFunctions();
		Utility Util_Obj=new Utility();
		ResultSet Res=null;
		String Query=null;
    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
    	Properties properties = null;
    	Statement stmt=null;
    	String Used_Cars_UI="0";
		String New_Cars_UI="0";
		String Used_cars_ui_full_text="0";
		String New_cars_ui_full_text="0";
     	
		try
		{
		
			CustomFunctions_obj.getURL("https://www.driveway.com/shop");
			try
			{
				if(CustomFunctions.Remote_Browser_Opened==true)
				{
				
				
					/*Util_Obj.Execute("Hover", "btnShop_Menu", "Execute");
					Util_Obj.Execute("Click_Button", "btnShop_Menu", "Execute");
					
					Util_Obj.Execute("Hover", "btnShopUsedcars", "Execute");
					Util_Obj.Execute("Click_Button", "btnShopUsedcars", "Execute");*/
					Util_Obj.Execute("Pause", "", "5");
					Util_Obj.Execute("Hover", "txtgetUsedCarsNumbers", "Execute");
					Util_Obj.Execute("Get_Text", "txtgetUsedCarsNumbers", "Execute");
					
					Used_cars_ui_full_text=WebAppKeywordHelper.PageText;
					Used_Cars_UI=Used_cars_ui_full_text.replace("Used Cars For Sale", "").replace(",", "");
					System.out.println(Used_Cars_UI);
					
					//Util_Obj.Execute("Hover", "btnShop_Menu", "Execute");
					//Util_Obj.Execute("Click_Button", "btnShop_Menu", "Execute");
					Util_Obj.Execute("Pause", "", "15");
					Util_Obj.Execute("Hover", "btnShopNewcars", "Execute");
					Util_Obj.Execute("Click_Button", "btnShopNewcars", "Execute");
					Util_Obj.Execute("Pause", "", "15");
					Util_Obj.Execute("Hover", "txtgetNewCarsNumbers", "Execute");
					Util_Obj.Execute("Get_Text", "txtgetNewCarsNumbers", "Execute");
					
					New_cars_ui_full_text=WebAppKeywordHelper.PageText;
					New_Cars_UI=New_cars_ui_full_text.replace("New Cars For Sale", "").replace(",", "");;
					System.out.println(New_Cars_UI);
				}
				else
				{
					Used_Cars_UI="Unable to Open the Browser";
					New_Cars_UI="Unable to Open the Browser";
				}
			}
				catch(Exception E)
				{
					;
				}

				SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
				properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			    stmt = SnowFlakeDataBaseConnection.connection.createStatement();

			        Query=properties.getProperty("get_Used_new_Cars_Count");
			        Res=stmt.executeQuery(Query);
			        
			        StringBuilder buf = new StringBuilder();
					buf.append("\t\t<html>" +"<body>" +"<table border=2 cellpadding=3 cellspacing=0>" +"<tr>" +"<th>VEHICLE_CONDITION</th>"+"<th>DB_Count</th>"+"<th>UI_Count</th>"+"</tr>");
					
		        	 while(Res.next())
		 	        {
		        		 String Vehile_Condition=null;
		        		 Vehile_Condition=Res.getString(1);
		        		 if(Vehile_Condition.compareToIgnoreCase("Used")==0)
		        		 {
		        			 buf.append("<tr><td>"+Vehile_Condition+"</td><td>"+Res.getString(2)+"</td><td>"+Used_Cars_UI+"</td>");
		        		 }
		        		 else
		        		 {
		        			 buf.append("<tr><td>"+Vehile_Condition+"</td><td>"+Res.getString(2)+"</td><td>"+New_Cars_UI+"</td>");
		        		 }
		        		 
		 	        }
		        	 
		        	buf.append("</table>" +"</body>" + "</html>");
		 			MdigitalWrapper.New_Used_Cars_Data=buf.toString();

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


	private StringBuilder CreateHourlyMonitoringMessageBody() 
	{
		StringBuilder buf = new StringBuilder();
		
		
		buf.append("\t\t\t\t<html>" +"<body>" +"<table border=5 cellpadding=1 cellspacing=0>" +"<tr>"+"<th style=width:5%;>No   </th>" +"<th>Category</th>" +"<th>Scenario</th>"+"<th>Expected Result</th>"+"<th style=width:10%;>Count/Validation     </th>"+"<th style=width:10%;>Status    </th>"+"</tr>");
		
		if(MdigitalWrapper.Maxdigital_Active_Inventory_dup_count>0)
		{
			buf.append("<tr style='color: red;'><td>1</td><td>Maxdigital Active Inventory</td><td>Duplicates check in Active Inventory</td><td>Should be no duplicates</td><td>"+MdigitalWrapper.Maxdigital_Active_Inventory_dup_count+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>1</td><td>Maxdigital Active Inventory</td><td>Duplicates check in Active Inventory</td><td>Should be no duplicates</td><td>"+MdigitalWrapper.Maxdigital_Active_Inventory_dup_count+"</td><td>Pass</td>");
		}
		
		if(MdigitalWrapper.Merchandise_Feed_dup_count>0)
		{
			buf.append("<tr style='color: red;'><td>2</td><td>Post Suppression - Merchandise Inventory</td><td>Duplicates check in Merchandise feed Inventory</td><td>Should be no duplicates</td><td>"+MdigitalWrapper.Merchandise_Feed_dup_count+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>2</td><td>Post Suppression - Merchandise Inventory</td><td>Duplicates check in Merchandise feed Inventory</td><td>Should be no duplicates</td><td>"+MdigitalWrapper.Merchandise_Feed_dup_count+"</td><td>Pass</td>");
		}
		
		if(MdigitalWrapper.Maxdigital_Sold_inventory_VINs_decoded_BB_count>0)
		{
			buf.append("<tr style='color: red;'><td>3</td><td>Maxdigital Sold Inventory vs BB VIN Decoding Process</td><td>Maxdigital Sold inventory VINs got decoded from BB</td><td>All VINs should be decoded</td><td>"+MdigitalWrapper.Maxdigital_Sold_inventory_VINs_decoded_BB_count+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>3</td><td>Maxdigital Sold Inventory vs BB VIN Decoding Process</td><td>Maxdigital Sold inventory VINs got decoded from BB</td><td>All VINs should be decoded</td><td>"+MdigitalWrapper.Maxdigital_Sold_inventory_VINs_decoded_BB_count+"</td><td>Pass</td>");
		}
		
		if(MdigitalWrapper.Maxdigital_Active_inventory_VINs_decoded_from_BB>0)
		{
			buf.append("<tr style='color: red;'><td>4</td><td>Maxdigital Active Inventory vs BB VIN Decoding Process</td><td>Maxdigital Active inventory VINs got decoded from BB</td><td>All VINs should be decoded</td><td>"+MdigitalWrapper.Maxdigital_Active_inventory_VINs_decoded_from_BB+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>4</td><td>Maxdigital Active Inventory vs BB VIN Decoding Process</td><td>Maxdigital Active inventory VINs got decoded from BB</td><td>All VINs should be decoded</td><td>"+MdigitalWrapper.Maxdigital_Active_inventory_VINs_decoded_from_BB+"</td><td>Pass</td>");
		}
		
		if(MdigitalWrapper.Active_inventory_count<50000)
		{
			buf.append("<tr style='color: red;'><td>5</td><td>Pre Suppression - Merchandise Inventory</td><td>Pre Suppression Merchandise Inventory count falls below threshold value</td><td>Count should be > 50,000</td><td>"+MdigitalWrapper.Active_inventory_count+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>5</td><td>Pre Suppression - Merchandise Inventory</td><td>Pre Suppression Merchandise Inventory count falls below threshold value</td><td>Count should be > 50,000</td><td>"+MdigitalWrapper.Active_inventory_count+"</td><td>Pass</td>");
		}
		
		if(MdigitalWrapper.Maxdigital_Active_inventory_VINs_exist_Merchandise_view_Pre_Suppression_count>0)
		{
			buf.append("<tr style='color: red;'><td>6</td><td>Merchandise Pre Suppression vs Maxdigital Active Inventory</td><td>Maxdigital Active inventory VINs exist in the Merchandise view (Pre Suppression)</td><td>All VINs in Maxdigital should exist in Merchandise view</td><td>"+MdigitalWrapper.Maxdigital_Active_inventory_VINs_exist_Merchandise_view_Pre_Suppression_count+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>6</td><td>Merchandise Pre Suppression vs Maxdigital Active Inventory</td><td>Maxdigital Active inventory VINs exist in the Merchandise view (Pre Suppression)</td><td>All VINs in Maxdigital should exist in Merchandise view</td><td>"+MdigitalWrapper.Maxdigital_Active_inventory_VINs_exist_Merchandise_view_Pre_Suppression_count+"</td><td>Pass</td>");
		}
		
		if(MdigitalWrapper.Max_Active_Inventory_VINs_W_Dealerid_Stock_exist_Max_Sold_Inventory_cnt>0)
		{
			buf.append("<tr style='color: red;'><td>7</td><td>VINs exist in both Max Active & Sold Inventory files </td><td>Max Active Inventory VINs with same Dealerid, Stock also exist in Max Sold Inventory</td><td>Should not yield any VINs</td><td>"+MdigitalWrapper.Max_Active_Inventory_VINs_W_Dealerid_Stock_exist_Max_Sold_Inventory_cnt+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>7</td><td>VINs exist in both Max Active & Sold Inventory files</td><td>Max Active Inventory VINs with same Dealerid, Stock also exist in Max Sold Inventory</td><td>Should not yield any VINs</td><td>"+MdigitalWrapper.Max_Active_Inventory_VINs_W_Dealerid_Stock_exist_Max_Sold_Inventory_cnt+"</td><td>Pass</td>");
		}
		
		if(MdigitalWrapper.Booked_Suppr_rule_count_9A>0)
		{
			buf.append("<tr style='color: red;'><td>8A</td><td>Suppression Rule - Booked Count</td><td>Check if Booked logic is working fine or not</td><td>Count Should be 0</td><td>"+MdigitalWrapper.Booked_Suppr_rule_count_9A+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>8A</td><td>Suppression Rule - Booked Count</td><td>Check if Booked logic is working fine or not</td><td>Count Should be 0</td><td>"+MdigitalWrapper.Booked_Suppr_rule_count_9A+"</td><td>Pass</td>");
		}
		
		if(MdigitalWrapper.Booked_Sales_Status_9B.compareToIgnoreCase("N")==0)
		{
			buf.append("<tr style='color: red;'><td>8B</td><td>Suppression Rule - Booked sales status Flag</td><td>Check if Booked sales status flag is working fine or not</td><td>Booked sales status Flag Should be Y</td><td>"+MdigitalWrapper.Booked_Sales_Status_9B+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>8B</td><td>Suppression Rule - Booked sales status Flag </td><td>Check if Booked sales status flag is working fine or not</td><td>Booked sales status Flag Should be Y</td><td>"+MdigitalWrapper.Booked_Sales_Status_9B+"</td><td>Pass</td>");
		}
		
		/**************************************************************************************/
		
		if(MdigitalWrapper.Pending_Suppr_rule_count_9C>0)
		{
			buf.append("<tr style='color: red;'><td>8C</td><td>Suppression Rule - Pending Count</td><td>Check if Pending logic is working fine or not</td><td>Count Should be 0</td><td>"+MdigitalWrapper.Pending_Suppr_rule_count_9C+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>8C</td><td>Suppression Rule - Pending Count</td><td>Check if Pending logic is working fine or not</td><td>Count Should be 0</td><td>"+MdigitalWrapper.Pending_Suppr_rule_count_9C+"</td><td>Pass</td>");
		}
		
		if(MdigitalWrapper.Pending_Sales_Status_9D.compareToIgnoreCase("N")==0)
		{
			buf.append("<tr style='color: red;'><td>8D</td><td>Suppression Rule - Pending sales status Flag</td><td>Check if Pending sales status flag is working fine or not</td><td>Pending sales status Flag Should be Y</td><td>"+MdigitalWrapper.Pending_Sales_Status_9D+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>8D</td><td>Suppression Rule - Pending sales status Flag</td><td>Check if Pending sales status flag is working fine or not</td><td>Pending sales status Flag Should be Y</td><td>"+MdigitalWrapper.Pending_Sales_Status_9D+"</td><td>Pass</td>");
		}
		
		
		if(MdigitalWrapper.In_Transit_Supp_rule_Count_10A>0)
		{
			buf.append("<tr style='color: red;'><td>9A</td><td>In-Transit Suppresssion rule:Count</td><td>Check if Intransit logic count is working fine or not</td><td>Count Should be 0</td><td>"+MdigitalWrapper.In_Transit_Supp_rule_Count_10A+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>9A</td><td>In-Transit Suppresssion rule:Count</td><td>Check if Intransit logic count is working fine or not</td><td>Count Should be 0</td><td>"+MdigitalWrapper.In_Transit_Supp_rule_Count_10A+"</td><td>Pass</td>");
		}
		
		if(MdigitalWrapper.In_Transit_inventory_status_10B.compareToIgnoreCase("N")==0)
		{
			buf.append("<tr style='color: red;'><td>9B</td><td>In-Transit Suppresssion rule - inventory status Flag</td><td>Check if Intransit flag  inventory status is working fine or not</td><td>inventory status Flag Should be Y</td><td>"+MdigitalWrapper.In_Transit_inventory_status_10B+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>9B</td><td>In-Transit Suppresssion rule - inventory status Flag</td><td>Check if Intransit flag  inventory status is working fine or not</td><td>inventory status Flag Should be Y</td><td>"+MdigitalWrapper.In_Transit_inventory_status_10B+"</td><td>Pass</td>");
		}
		
		if(MdigitalWrapper.dealerinvoiceprice_Suppression_rule_count>0)
		{
			buf.append("<tr style='color: red;'><td>10</td><td>Suppression Rule - Dealer Invoice Price</td><td>Dealer invoice price count</td><td>Count Should be 0</td><td>"+MdigitalWrapper.dealerinvoiceprice_Suppression_rule_count+"</td><td>Fail</td>");
		}
		else
		{
			buf.append("<tr><td>10</td><td>Suppression Rule - Dealer Invoice Price</td><td>Dealer invoice price count</td><td>Count Should be 0</td><td>"+MdigitalWrapper.dealerinvoiceprice_Suppression_rule_count+"</td><td>Pass</td>");
		}
		
		buf.append("</table>" +"</body>" + "</html>");
		
		return buf;
		
	}
	
	
private int get_Max_vs_BB_Year_or_Make_Mismatch() {
		
    	String Query=null;
    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
    	Properties properties = null;
    	Statement stmt=null;
    	ResultSet Res=null;
    	int count=0;
		try
		{
			SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
			properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
	        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
	        
			    Query=properties.getProperty("get_Max_vs_BB_Year_or_Make_Mismatch");
			    
			    Res=stmt.executeQuery(Query);
	        	
	        	StringBuilder buf = new StringBuilder();
				buf.append("\t\t<html>" +"<body>" +"<table border=2 cellpadding=3 cellspacing=0>" +"<tr>" +"<th>MAX_VIN</th>"+"<th>MAX_YEAR</th>"+"<th>MAX_MAKE</th>"+"<th>BB_YEAR</th>"+"<th>BB_MAKE</th>"+"</tr>");
				
	        	 while(Res.next())
	 	        {
	        		 count=count++;
	        		 buf.append("<tr><td>"+Res.getString(1)+"</td>"
	        		 		+ "<td>"+Res.getString(2)+"</td>"
	        		 		+ "<td>"+Res.getString(3)+"</td>"
	        		 		+ "<td>"+Res.getString(4)+"</td>"
	        		 		+ "<td>"+Res.getString(5)+"</td>");
	 	        }
	        	 
	        	buf.append("</table>" +"</body>" + "</html>");
	 			MdigitalWrapper.Max_vs_BB_Year_or_Make_Mismatch=buf.toString();

            		

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


	private int get_Blackbook_Used_Vehicle_VIN_data_quality() {
		
    	String Query=null;
    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
    	Properties properties = null;
    	Statement stmt=null;
    	ResultSet Res=null;
    	int count=0;
		try
		{
			SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
			properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
	        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
	        
			    Query=properties.getProperty("get_Blackbook_Used_Vehicle_VIN_data_quality");
			    
			    Res=stmt.executeQuery(Query);
	        	
	        	StringBuilder buf = new StringBuilder();
				buf.append("\t\t<html>" +"<body>" +"<table border=2 cellpadding=3 cellspacing=1>" +"<tr>" +"<th>TOTAL_RECORD_COUNT</th>"+"<th>DRIVETRAIN</th>"+"<th>MODEL_YEAR</th>"+"<th>MAKE</th>"+"<th>MODEL</th>"+"<th>SERIES</th>"+"<th>STYLE</th>"+"<th>CYLINDERS</th>"
				+"<th>ENGINE_DISPLACEMENT</th>"+"<th>TRANSMISSION</th>"+"<th>EXT_DOORS</th>"+"<th>ENGINE_DESCRIPTION</th>"+"<th>FUEL_TYPE</th>"+"</tr>");
				
	        	 while(Res.next())
	 	        {
	        		 count=count+1;
	        		 buf.append("<tr><td>"+Res.getInt(1)+"</td>"
	        		 		+ "<td>"+Res.getBigDecimal(2)+"%</td>"
	        		 		+ "<td>"+Res.getBigDecimal(3)+"%</td>"
	        		 		+ "<td>"+Res.getBigDecimal(4)+"%</td>"
	        		 		+ "<td>"+Res.getBigDecimal(5)+"%</td>"
	        		 		+ "<td>"+Res.getBigDecimal(6)+"%</td>"
	        		 		+ "<td>"+Res.getBigDecimal(7)+"%</td>"
	        		 		+ "<td>"+Res.getBigDecimal(8)+"%</td>"
	        		 		+ "<td>"+Res.getBigDecimal(9)+"%</td>"
	        		 		+ "<td>"+Res.getBigDecimal(10)+"%</td>"
	        		 		+ "<td>"+Res.getBigDecimal(11)+"%</td>"
	        		 		+ "<td>"+Res.getBigDecimal(12)+"%</td>"
	        		 		+ "<td>"+Res.getBigDecimal(13)+"%</td>");
	     			
	        		 
	 	        }
	        	 
	        	buf.append("</table>" +"</body>" + "</html>");
	 			MdigitalWrapper.Blackbook_Used_Vehicle_VIN_data=buf.toString();

            		

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


	private void getMaxdigital_DailyRunning_VINs_decoded_from_BB_count() {

    	String Query=null;
    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
    	Properties properties = null;
    	Statement stmt=null;
		try
		{
			SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
			properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
	        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
	        
			    Query=properties.getProperty("get_Maxdigital_Active_inventory_VINs_decoded_from_BB");
			    MdigitalWrapper.Maxdigital_Active_inventory_VINs_decoded_from_BB=ExecuteCountQuery(Query,stmt);
			    


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
	
	private void getMaxdigital_HourlyRunning_Active_Inventory_count() {

    	String Query=null;
    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
    	Properties properties = null;
    	Statement stmt=null;
		try
		{
			SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
			properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
	        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
	        
			    Query=properties.getProperty("get_Maxdigital_Sold_inventory_VINs_decoded_BB");
			    MdigitalWrapper.Maxdigital_Sold_inventory_VINs_decoded_BB_count=ExecuteCountQuery(Query,stmt);
			    
			    Query=properties.getProperty("get_Maxdigital_Active_inventory_VINs_decoded_from_BB");
			    MdigitalWrapper.Maxdigital_Active_inventory_VINs_decoded_from_BB=ExecuteCountQuery(Query,stmt);
			    
			    Query=properties.getProperty("get_Active_inventory_count");
			    MdigitalWrapper.Active_inventory_count=ExecuteCountQuery(Query,stmt);
			    
			    Query=properties.getProperty("get_Maxdigital_Active_inventory_VINs_exist_Merchandise_view_Pre_Suppression_count");
			    MdigitalWrapper.Maxdigital_Active_inventory_VINs_exist_Merchandise_view_Pre_Suppression_count=ExecuteCountQuery(Query,stmt);
			    
			    Query=properties.getProperty("get_Max_Active_Inventory_VINs_W_Dealerid_Stock_exist_Max_Sold_Inventory_cnt");
			    MdigitalWrapper.Max_Active_Inventory_VINs_W_Dealerid_Stock_exist_Max_Sold_Inventory_cnt=ExecuteCountQuery(Query,stmt);
			    
			    if(MdigitalWrapper.Max_Active_Inventory_VINs_W_Dealerid_Stock_exist_Max_Sold_Inventory_cnt>0)
			    {
			        ResultSet Res=null;
			        StringBuffer SB=new StringBuffer();
			    	Query=properties.getProperty("get_Max_Active_Inventory_VINs_W_Dealerid_Stock_exist_Max_Sold_Inventory_VINs");
			    	Res=stmt.executeQuery(Query);
			    	
			    	StringBuilder buf = new StringBuilder();
					buf.append("\t\t<html>" +"<body>" +"<table border=1 cellpadding=3 cellspacing=1>" +"<tr>" +"<th>VIN</th>"+"</tr>");
		        	
			    	while(Res.next())
		 	        {
			    		 buf.append("<tr><td>"+Res.getString(1)+"</td>");
		     			
		        		 
		 	        }
			    	buf.append("</table>" +"</body>" + "</html>");
		 			MdigitalWrapper.Max_Active_Inventory_VINs_W_Dealerid_Stock_exist_Max_Sold_Inventory_VINs=buf.toString();
			    }

			    
			    String Audit_Summary=null;
			    Query=properties.getProperty("get_Audit_Summary");
			    Audit_Summary=ExecuteQuery(Query,stmt);
			    
			    Query=properties.getProperty("get_Booked_Suppr_rule_count_9A");
			    Query=Query.replaceFirst("\\?", Audit_Summary);
			    MdigitalWrapper.Booked_Suppr_rule_count_9A=ExecuteCountQuery(Query,stmt);
			    
			    Query=properties.getProperty("get_Booked_Sales_Status_9B");
			    MdigitalWrapper.Booked_Sales_Status_9B=ExecuteQuery(Query,stmt);
			    
			    Query=properties.getProperty("get_Pending_Suppr_rule_count_9C");
			    Query=Query.replaceFirst("\\?", Audit_Summary);
			    MdigitalWrapper.Pending_Suppr_rule_count_9C=ExecuteCountQuery(Query,stmt);
			    
			    Query=properties.getProperty("get_Pending_Sales_Status_9D");
			    MdigitalWrapper.Pending_Sales_Status_9D=ExecuteQuery(Query,stmt);
			    
			   // Query=properties.getProperty("get_In_Transit_Suppresssion_rule");
			   // MaxdigitalWrapper.In_Transit_Suppresssion_rule_count=ExecuteCountQuery(Query,stmt);
			    
			    
			    Query=properties.getProperty("get_In_Transit_Supp_rule_Count_10A");
			    Query=Query.replaceFirst("\\?", Audit_Summary);
			    MdigitalWrapper.In_Transit_Supp_rule_Count_10A=ExecuteCountQuery(Query,stmt);
			    
			    Query=properties.getProperty("get_In_Transit_inventory_status_10B");
			    MdigitalWrapper.In_Transit_inventory_status_10B=ExecuteQuery(Query,stmt);
			    
			    
			    
			    
			    
			    Query=properties.getProperty("get_dealerinvoiceprice_Suppression_rule");
			    MdigitalWrapper.dealerinvoiceprice_Suppression_rule_count=ExecuteCountQuery(Query,stmt);
            		

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
	
	private void get_Duplicate_Data() {

    	String Query=null;
    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
    	Properties properties = null;
    	Statement stmt=null;
		try
		{
			SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
			properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
	        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
	        
			    Query=properties.getProperty("get_dup_maxdigital_Active_Inventory");
			    MdigitalWrapper.Maxdigital_Active_Inventory_dup_count=Execute_maxdigital_duplicateDetailQuery(Query,stmt);
			    
			    
			    Query=properties.getProperty("get_dup_Merchandise_Feed_Pre_Suppression");
			    MdigitalWrapper.Merchandise_Feed_dup_count=Execute_Merchandise_duplicateDetailQuery(Query,stmt);
            		

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
	
	private long Execute_Merchandise_duplicateDetailQuery(String query, Statement stmt) 
	{
        ResultSet Res=null;
        long count=0;
	    
	    try
		{

        	Res=stmt.executeQuery(query);
        	
        	StringBuilder buf = new StringBuilder();
			buf.append("\t\t<html>" +"<body>" +"<table border=1 cellpadding=10 cellspacing=0>" +"<tr>" +"<th>VIN</th>" +"<th>DEALER_STOCK</th>"+"<th>DEALERID</th>"+"<th>MAXDIGITAL_SYNDICATION_ID</th>"+"</tr>");
			
        	 while(Res.next())
 	        {
        		 count=count++;
        		 buf.append("<tr><td>"+Res.getString(1)+"</td>"
        		 		+ "<td>"+Res.getString(2)+"</td>"
        		 		+ "<td>"+Res.getString(3)+"</td>"
        		 		+ "<td>"+Res.getString(4)+"</td>");
     			
        		 
 	        }
        	 
        	buf.append("</table>" +"</body>" + "</html>");
 			MdigitalWrapper.Merchandise_Feed_dup_data=buf.toString();
 			
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
	
	
	private long Execute_maxdigital_duplicateDetailQuery(String query, Statement stmt) 
	{
        ResultSet Res=null;
        long count=0;
	    
	    try
		{

        	Res=stmt.executeQuery(query);
        	
        	StringBuilder buf = new StringBuilder();
			buf.append("\t\t<html>" +"<body>" +"<table border=1 cellpadding=10 cellspacing=0>" +"<tr>" +"<th>VIN</th>" +"<th>STOCK</th>"+"<th>DEALERID</th>"+"<th>MAXDIGITALSYNDICATIONID</th>"+"</tr>");
			
        	 while(Res.next())
 	        {
        		 count=count++;
        		 buf.append("<tr><td>"+Res.getString(1)+"</td>"
        		 		+ "<td>"+Res.getString(2)+"</td>"
        		 		+ "<td>"+Res.getString(3)+"</td>"
        		 		+ "<td>"+Res.getString(4)+"</td>");
     			
        		 
 	        }
        	 
        	buf.append("</table>" +"</body>" + "</html>");
 			MdigitalWrapper.Maxdigital_Active_Inventory_dup_data=buf.toString();
 			
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

private String ExecuteQuery(String query,Statement stmt) {
	
    ResultSet Res=null;
    String value=null;
    
	try
	{
		
		Res=stmt.executeQuery(query);
    	
    	 if(Res.next())
	        {
    		 value=Res.getString(1);
    		 
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
	return value;
}



private String getCurrentDateTimeWithUTCformat() {
	
	 String CurrentDateHour="";
	 String CurrentDate="";
	 String CurrentHour="";
	 String Time = "00:00:00.000 am";
	try
	{
		 Date date = new Date();
		 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd");
		 TimeZone pstTime = TimeZone.getTimeZone("PST");
		 pstFormat.setTimeZone(pstTime);
		 CurrentDate=pstFormat.format(date);
		 
		 
			final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.000", Locale.US);
			formatter.setTimeZone(TimeZone.getTimeZone("PST"));
			Date UTCTime = formatter.parse(Time);
			
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			CurrentHour=formatter.format(UTCTime);
		
			CurrentDateHour=CurrentDate+" "+CurrentHour;
			
	}
	catch(Exception E)
	{
		;
	}
	return CurrentDateHour;
}


/******************************************************************************************************************************************************************************************/

@Given("^Execute the given Daily max digital test cases$")
public void Execute_given_Daily_max_digital_testcases() throws Throwable 
{
	int count=0;
	int count1=0;
	try {

		 AutomationMainClass.email_mode="Max Digital Hourly Monitoring";
		
		 System.out.println("Enterting into the Execute_given_Daily_max_digital_testcases");
        /********************************************Get the SnowFlake Data***********************************************************/
		 count=get_Blackbook_Used_Vehicle_VIN_data_quality();     
		 count1=get_driveway_merchandise_active_inventory();
         
         
         if(count==0 ||count1==0 )
         {  	
        	 MdigitalWrapper.Subject="Urgent:";
         }
         else {
        	 MdigitalWrapper.Subject="All Clear:";
		}
        

         MdigitalWrapper.Hourly_monitoring_Message_Body="<br>Automation for the Max digital Hourly Monitoring queries execution completed. Please find the results below.<br></br>"
				    + "<br></br><b><u>Blackbook data quality - Percentage(%) of Nulls:</u></b><br></br>"
				    + "\t\t\t\t\t"+MdigitalWrapper.Blackbook_Used_Vehicle_VIN_data
				   /* + "<br></br><b><u>Boolean value checks for Intransit, Booked & pending, Recon order open flag fields:</u></b><br></br>"
				    + "\t\t\t\t\t"+MaxdigitalWrapper.driveway_merchandise_active_inventory_Data*/
				    
					+ "<br><br></br> This is auto generated email. Please don't reply to this email."
					+ "<br></br>Thanks,"
					+ "<br>EDS Automation Group.<br></br>";
         
         System.out.println("Message Body creation completed:Execute_given_hourly_max_digital_testcases");
        
        
	}

	
	catch (Exception e) {
		//ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
		e.printStackTrace();
	}


}


private int get_driveway_merchandise_active_inventory() {
	
	String Query=null;
	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
	Properties properties = null;
	Statement stmt=null;
	ResultSet Res=null;
	int count=0;
	try
	{
		SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
		properties=CustomFunctions_OBJ.GetSQLQueryProperties();
		
        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
        
		    Query=properties.getProperty("get_driveway_merchandise_active_inventory");
		    
		    Res=stmt.executeQuery(Query);
        	
        	StringBuilder buf = new StringBuilder();
			buf.append("\t\t<html>" +"<body>" +"<table border=2 cellpadding=3 cellspacing=1>" +"<tr>" +"<th>RECONDITIONING_ORDER_OPEN</th>"+"<th>SALES_STATUS_BOOKED</th>"+"<th>SALES_STATUS_PENDING</th>"+"<th>INVENTORY_STATUS_INTRANSIT</th>"+"</tr>");
			
        	 while(Res.next())
 	        {
        		 count=count+1;
        		 buf.append("<tr><td>"+Res.getString(1)+"</td>"+ "<td>"+Res.getString(2)+"</td>"+ "<td>"+Res.getString(3)+"</td>"+ "<td>"+Res.getString(4)+"</td>");
     			
        		 
 	        }
        	 
        	buf.append("</table>" +"</body>" + "</html>");
 			MdigitalWrapper.driveway_merchandise_active_inventory_Data=buf.toString();

        		

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


/**************************************************************************************************************************************************************************************************/
/******************************************************************Reporting: Monthly MAXDIGITAL_MASTER pipe load failure warning summaries********************************************************/
/**************************************************************************************************************************************************************************************************/





@Given("^get failure warning summaries from Max digital master load$")
public void get_failure_warning_summaries_from_Max_digital_master_load() throws Throwable 
{


	try {

		 AutomationMainClass.email_mode="Max Digital failure Warning Summaries";

		 String Actual_env=AutomationMainClass.environment;
		 
		 load_and_Truncate_data();
		 
		 
		 ArrayList<String> uAT_list=new ArrayList<String>();
		 ArrayList<String> prod_list=new ArrayList<String>();
		 
		 prod_list=getLogdata("PROD","get_failure_warnings_PROD");
		 uAT_list=getLogdata("UAT","get_failure_warnings_UAT"); 
		
		 
		
		 
		 insertlogdBResults(prod_list,Actual_env);
		 insertlogdBResults(uAT_list,Actual_env);
		 
		 
		 get_maxdigital_pipe_load_data();
		 
		 MdigitalWrapper.CurrentMonth_Year=getlastmonth_year();
		
   
		 MdigitalWrapper.Max_Digital_failure_Warning_Summaries="Hi All,<br><br>Please find below Monthly Summary Report report of the hour-blocks in which EDS did not receive an inventory file from MAX Digital for the month of "+MdigitalWrapper.CurrentMonth_Year
			     +".<br><br>This report will help measure the reliability of MAX Digital's syndication services and measure the vendor's compliance with stated SLAs."
				
			    
				+ "<br><br></br> This is auto generated email. Please don't reply to this email."
				+ "<br></br>Thanks,"
				+ "<br>Midas Automation Group.<br></br>";
    
    System.out.println("Message Body creation get_failure_warning_summaries_from_Max_digital_master_load");
   
       
        
	}

	
	catch (Exception e) {
		//ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
		e.printStackTrace();
	}


}








private void load_and_Truncate_data() {

	
	
	Statement stmt = null;
	ResultSet Res = null;
	DataBaseConnections Obj_DataBaseConnections=new DataBaseConnections();
	Properties properties = null;
	String Query=null;
	try
	{
		
		Obj_DataBaseConnections.Connect_Automation_Results_SQLServerDB();
		properties=CustomFunctions_OBJ.GetSQLQueryProperties();
     

		stmt = DataBaseConnections.Automation_results_db_connection.createStatement();
		Query=properties.getProperty("To_Store_Historical_data");
		
		stmt.executeUpdate(Query);
		
		Query=properties.getProperty("To_Truncate_Table");
		
		stmt.executeQuery(Query);
		

		
		

    	   
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	finally
	{
		
			Obj_DataBaseConnections.CloseResultsDBConnection();

		
	}
	


	
}


private ArrayList<String> getLogdata(String db_env,String Query_Name) {

	Properties Properties = null;
	DataBaseConnections DataBaseConnections_OBJ=new DataBaseConnections();
	Statement stmt=null;
	String Query=null;
	ResultSet Res=null;
	
	ArrayList<String> list=new ArrayList<String>();
	
	try {
		AutomationMainClass.environment=db_env;
		
		
		Properties=CustomFunctions_OBJ.GetSQLQueryProperties();
		DataBaseConnections_OBJ.Logging_SQLServerCVPConnection("logDB");
		stmt = DataBaseConnections.SQL_CVP_db_connection.createStatement();
		
		
		Query=Properties.getProperty(Query_Name);
		
		Res=stmt.executeQuery(Query);
		
		 while(Res.next())
	        {

			 String data=null;
			 
			 data=Res.getString(1).concat(";").concat(Res.getDate(2).toString()).concat(";").concat(Res.getString(3)).concat(";").concat(Res.getString(4));
       	  
			 list.add(data);
     		
     		 
	        }
		
		
	} catch (Exception e) {
		try {
			stmt.close();
			Res.close();
			DataBaseConnections.SQL_CVP_db_connection.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		catch (Exception e2) {
			;
		}
	}
	
	
	return list;
	
}






private void insertlogdBResults(ArrayList<String> list, String actual_env) {
	

	DataBaseConnections Obj_DataBaseConnections=new DataBaseConnections();
	PreparedStatement preparedStmt=null;
	try
	{
		AutomationMainClass.environment=actual_env;
		
		
		Obj_DataBaseConnections.Connect_Automation_Results_SQLServerDB();
		
		for(int i=0;i<list.size();i++)
		{
			String data=list.get(i);
			String[] split_data=data.split(";");
			
			String env=split_data[0];
			String date=split_data[1];
			
			String Select_QUERY = "Select ID from [dbo].[maxdigital_pipe_load] where [Prod_DATE(UTC)]=?";
			preparedStmt= DataBaseConnections.Automation_results_db_connection.prepareStatement(Select_QUERY);
			preparedStmt.setString(1, date);
			ResultSet rs=preparedStmt.executeQuery();
			
			int id=0;
			while(rs.next()) {
			    id = rs.getInt(1);
			}
			
			if(id==0)
			{
				
				String INSERT_QUERY=null;
				
				if(env.compareToIgnoreCase("PROD")==0)
				{
					INSERT_QUERY = "INSERT INTO [dbo].[maxdigital_pipe_load] ([Prod_Environment],[Prod_DATE(UTC)] ,[Prod_Downtime],[Prod_Outage]) VALUES(?,?,?,?)";
					
					preparedStmt = DataBaseConnections.Automation_results_db_connection.prepareStatement(INSERT_QUERY);
					  preparedStmt.setString (1, split_data[0]);
				      preparedStmt.setString (2, split_data[1]);
				      preparedStmt.setString (3, split_data[2]);
				      preparedStmt.setString (4, split_data[3].trim().replaceAll("   ","").replaceAll("  ","").replaceAll(", ","\r\n").replaceAll(",","\r\n"));
				      
				      preparedStmt.execute();
				}
				else {
					INSERT_QUERY = "INSERT INTO [dbo].[maxdigital_pipe_load] ([Uat_Environment],[Uat_DATE(UTC)] ,[Uat_Downtime],[Uat_Outage],[Prod_DATE(UTC)]) VALUES(?,?,?,?,?)";
					
					preparedStmt = DataBaseConnections.Automation_results_db_connection.prepareStatement(INSERT_QUERY);
					  preparedStmt.setString (1, split_data[0]);
				      preparedStmt.setString (2, split_data[1]);
				      preparedStmt.setString (3, split_data[2]);
				      preparedStmt.setString (4, split_data[3].trim().replaceAll("   ","").replaceAll("  ","").replaceAll(", ","\r\n").replaceAll(",","\r\n"));
				      preparedStmt.setString (5, split_data[1]);
				      
				      preparedStmt.execute();
				}
				
				
				
							
			}
			else {
				
				String Update_QUERY=null;
				
				if(env.compareToIgnoreCase("PROD")==0)
				{
					Update_QUERY = "update [dbo].[maxdigital_pipe_load] set [Prod_Environment]=? ,[Prod_DATE(UTC)]=? ,[Prod_Downtime]=?,[Prod_Outage]=? where ID=?";
				}
				else {
					Update_QUERY = "update [dbo].[maxdigital_pipe_load] set [UAT_Environment]=? ,[Uat_DATE(UTC)]=? ,[Uat_Downtime]=?,[Uat_Outage]=? where ID=?";
				}
				
				preparedStmt = DataBaseConnections.Automation_results_db_connection.prepareStatement(Update_QUERY);
								  preparedStmt.setString (1, split_data[0]);
							      preparedStmt.setString (2, split_data[1]);
							      preparedStmt.setString (3, split_data[2]);
							      preparedStmt.setString (4, split_data[3].trim().replaceAll("   ","").replaceAll("  ","").replaceAll(", ","\r\n").replaceAll(",","\r\n"));
							      preparedStmt.setInt(5, id);
							      
							      preparedStmt.executeUpdate();
							      
			}

			
			
			
		}
		
						   
		
		
		
		
		
	}
	catch(SQLException e)
	{
		
		e.printStackTrace();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	finally
	{
		
			Obj_DataBaseConnections.CloseResultsDBConnection();

		
	}

	
}




private void get_maxdigital_pipe_load_data() {
	
	
	PreparedStatement preparedstmt = null;
	ResultSet resultset = null;
	DataBaseConnections Obj_DataBaseConnections=new DataBaseConnections();

	
	try
	{
		
		Obj_DataBaseConnections.Connect_Automation_Results_SQLServerDB();
		MdigitalWrapper.CurrentMonthDate=getlastmonthDate();
		
		MdigitalWrapper.fileName=MdigitalWrapper.CurrentMonthDate+"_outage_report_maxdigital_master_pipe_load.csv";
		
		String QUERY = "select * from dbo.maxdigital_pipe_load where [Prod_DATE(UTC)] like '"+MdigitalWrapper.CurrentMonthDate+"%' order by  [Prod_DATE(UTC)] asc";
		  
		preparedstmt = DataBaseConnections.Automation_results_db_connection.prepareStatement(QUERY);  
		resultset = preparedstmt.executeQuery();
          

    	 while(resultset.next())
	        {
    		 
    		 	String Prod_Date=resultset.getString(3);
    			String Downtime_CVP_Prod=resultset.getString(4);
    			String Outage_CVP_Prod=resultset.getString(5);    		 
    			String Downtime_CVP_UAT=resultset.getString(8);
    			String Outage_CVP_UAT=resultset.getString(9);

    			Max_digitaLfailure_Summary.add(new String[] {Prod_Date,Downtime_CVP_Prod,Outage_CVP_Prod,Downtime_CVP_UAT,Outage_CVP_UAT});
 			

	        }

    	   
    	   //CreateCSV_File(Max_digitaLfailure_Summary,CurrentMonthDate);
    	   
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	finally
	{
		
			Obj_DataBaseConnections.CloseResultsDBConnection();

		
	}
	

}


private void CreateCSV_File(List<String[]> max_digitaLfailure_Summary2,String fileName) {
	
	final String NEW_LINE_SEPERATOR="\n";
	String FilefullPath=null;
	
	final Object[] FILE_HEADER= {"Date (UTC)","Downtime (CVP Production)","Outage (CVP Production)","Downtime (CVP UAT)","Outage (CVP UAT)"};
	
		
		FilefullPath=EnvironmentVariables.CSV_File_Loc+fileName;
		
		CSVFormat csvFileFormat=CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPERATOR);
		
		try
		{
			FileWriter fileWriter2=new FileWriter(FilefullPath);
			CSVPrinter csvPrinter=new CSVPrinter(fileWriter2,csvFileFormat);
			csvPrinter.printRecord(FILE_HEADER);
			csvPrinter.printRecords(Max_digitaLfailure_Summary);
			
			
			fileWriter2.close();
			csvPrinter.close();
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		/**************************************************************************************************************************************/
		/******************************* Move the file to Archive Azure Blob storage***********************************************************/
		
		
		Movefilesto_Azure_Blob(FilefullPath,fileName);
	
}


private void Movefilesto_Azure_Blob(String fileN,String fileName) {
	
	BlobContainerClient containerClient=null;
	AzureBlobStorage OBJ_AzureBlobStorage=new AzureBlobStorage();
	containerClient=OBJ_AzureBlobStorage.AzureContainerBlob_Connection();
	OBJ_AzureBlobStorage.AzureContainerBlob_Upload(containerClient,fileN,fileName);
	System.out.println("All the files downloaded.");
	
	
}


private String getlastmonthDate() 
{
	YearMonth CurrentMonth = YearMonth.now();
	YearMonth lastMonth    = CurrentMonth.minusMonths(1);
	
	
	DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

	String CurrentMonthDate=CurrentMonth.format(monthYearFormatter);
	String lastMonthDate=lastMonth.format(monthYearFormatter);
	System.out.printf(CurrentMonthDate);
	
	return CurrentMonthDate;
	
}


private String getlastmonth_year() 
{
	YearMonth CurrentMonth = YearMonth.now();
	YearMonth lastMonth    = CurrentMonth.minusMonths(1);
	
	
	DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM yyyy");

	String CurrentMonth_year=CurrentMonth.format(monthYearFormatter);
	System.out.printf(CurrentMonth_year);
	
	return CurrentMonth_year;
	
}








}
