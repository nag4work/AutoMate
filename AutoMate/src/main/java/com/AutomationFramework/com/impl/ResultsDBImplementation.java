package com.AutomationFramework.com.impl;


import com.AutomationFramework.BDD.Runner.DataClass;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.Pojos.ResultsDBWrapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class ResultsDBImplementation {
	public static final Logger logger = LoggerFactory.getLogger(ResultsDBImplementation.class);
	private JdbcTemplate jdbcTemplate;
	public static ArrayList<String> Results_Column = new ArrayList<String>();
	
	public void insetResultsinDB(ResultsDBWrapper ResultsDBWrapper_OBJ,String DBResultsDecision)
	
	{
		DataBaseConnections Obj_DataBaseConnections=new DataBaseConnections();
		try
		{
			Obj_DataBaseConnections.Connect_Results_SQLServerDB();
			
			/************************************************************************************************************/
			if(DBResultsDecision.toString().compareToIgnoreCase("ICOResults")==0)
			{
				String INSERT_QUERY = "INSERT INTO [dbo].[ICOResults]([Test_Case_Name],[User_Name],[Date],[Environment]\r\n"
						+ "           ,[Input_Data_Condition]\r\n"
						+ "           ,[Input_Value]\r\n"
						+ "           ,[UVC]\r\n"
						+ "           ,[Mileage]\r\n"
						+ "           ,[ZipCode]\r\n"
						+ "           ,[Details_By_VIN_Service_Res_Time]\r\n"
						+ "           ,[ICO_Service_Res_Time]\r\n"
						+ "           ,[VIN_UVC_Details_By_Service_Res_Time]\r\n"
						+ "           ,[Status]\r\n"
						+ "           ,[Comments]\r\n"
						+ "           ,[disposition]\r\n"
						+ "           ,[icoValue]\r\n"
						+ "           ,[maxEstimatedPrice]\r\n"
						+ "           ,[minEstimatedPrice]\r\n"
						+ "           ,[noPriceTriggerValues]\r\n"
						+ "           ,[DM_Created_DateTime])\r\n"
						+ "     VALUES\r\n"
						+ "           (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement preparedStmt = DataBaseConnections.results_db_connection.prepareStatement(INSERT_QUERY);
								  preparedStmt.setString (1, ResultsDBWrapper_OBJ.getTest_Case_Name());
							      preparedStmt.setString (2, ResultsDBWrapper_OBJ.getUser_Name());
							      preparedStmt.setString (3, ResultsDBWrapper_OBJ.getDate());
							      preparedStmt.setString (4, ResultsDBWrapper_OBJ.getEnvironment());
							      preparedStmt.setString (5, ResultsDBWrapper_OBJ.getInput_data_Condition());
							      preparedStmt.setString (6, ResultsDBWrapper_OBJ.getInput_Value());
							      preparedStmt.setString (7, ResultsDBWrapper_OBJ.getUvc());
							      preparedStmt.setString (8, ResultsDBWrapper_OBJ.getMileage());
							      preparedStmt.setString (9, ResultsDBWrapper_OBJ.getZipcode());
							      preparedStmt.setString (10, ResultsDBWrapper_OBJ.getDetails_By_VIN_Service_Res_Time());
							      preparedStmt.setString (11, ResultsDBWrapper_OBJ.getICO_Service_Res_Time());
							      preparedStmt.setString (12, ResultsDBWrapper_OBJ.getDetails_By_VIN_UVC_Service_Res_Time());
							      preparedStmt.setString (13, ResultsDBWrapper_OBJ.getStatus());
							      preparedStmt.setString (14, ResultsDBWrapper_OBJ.getComments());
							      preparedStmt.setString (15, ResultsDBWrapper_OBJ.getDisposition());
							      preparedStmt.setString (16, ResultsDBWrapper_OBJ.getIcoValue());
							      preparedStmt.setString (17, ResultsDBWrapper_OBJ.getMaxEstimatedPrice());
							      preparedStmt.setString (18, ResultsDBWrapper_OBJ.getMinEstimatedPrice());
							      preparedStmt.setString (19, ResultsDBWrapper_OBJ.getNoPriceTriggerValues());
							      preparedStmt.setTimestamp(20, AutomationMainClass.TimeStamp);


			      preparedStmt.execute();


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
	
	
public void insetDailyMonitoringResults(String SchemaName,String Dateofexecution,long NoOfRecords,String DataValidation)
	
	{
		DataBaseConnections Obj_DataBaseConnections=new DataBaseConnections();
		try
		{
			Obj_DataBaseConnections.Connect_Automation_Results_SQLServerDB();
			
				String INSERT_QUERY = "INSERT INTO [dbo].[DailyMonitoringResults] ([SchemaName],[Dateofexecution] ,[NoOfRecords],[DataValidation]) VALUES(?,?,?,?)";
				PreparedStatement preparedStmt = DataBaseConnections.Automation_results_db_connection.prepareStatement(INSERT_QUERY);
								  preparedStmt.setString (1, SchemaName);
							      preparedStmt.setString (2, Dateofexecution);
							      preparedStmt.setString (3, String.valueOf(NoOfRecords));
							      preparedStmt.setString (4, DataValidation);

							      preparedStmt.execute();

			
			
			
			
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


	public ArrayList<String> getlast7daysdata(String DataValidation,String SchemaName ) {
		
		PreparedStatement preparedstmt = null;
		ResultSet resultset = null;
		ArrayList<String> Results = new ArrayList<String>();
		Results_Column = new ArrayList<String>();
		
		try
		{
			String QUERY = "select top 7 DateofExecution,NoOfRecords from dbo.DailyMonitoringResults where DataValidation='"+DataValidation+"' and SchemaName='"+SchemaName+"' order by ID desc";
			  
			preparedstmt = DataBaseConnections.Automation_results_db_connection.prepareStatement(QUERY);  
			resultset = preparedstmt.executeQuery();
	          
	          while (resultset.next()) {
	        	  int i=0;
	        	  
	        	  Results_Column.add(i,resultset.getString(1));
	        	  Results.add(i, resultset.getString(2));
	        	  
	        	  i++;

	          }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	return Results;
	}

}
