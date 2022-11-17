package com.AutomationFramework.com.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.custom.EnvironmentVariables;
import com.Wrapper.ResponseWrapper;

public class SnowFlakeDataBaseConnection {

	public static final Logger logger = LoggerFactory.getLogger(DataBaseConnections.class);
	public static Connection connection=null;
	public static Connection results_db_connection=null;
	
	public void Connect_SnowFlake_DataBase(String schema) throws Exception  {
		
		Properties prop = null;
		CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		DataBaseConnections objDataBaseConnections=new DataBaseConnections();
		String sf_User_Name=null;
    	String sf_Password=null;
    	String sf_DataBaseName=null;
    	String sf_DB_warehouse=null;
    	String sf_Account=null;
    	String sf_Role=null;
    	String sf_DBHost=null;
    	String connectStr = null;
		
	    try {
	    	
	    	logger.info("Entering into the Method: DataBaseConnections");
	    	/**************** Calling Application Properties***************************/
	    	prop=CustomFunctions_OBJ.ApplicationProperties();
	    
	    	sf_User_Name=prop.getProperty("SNOWFlake_DB_User_Name");
	    	sf_Password=objDataBaseConnections.getdescryptedDBpassword(prop.getProperty("SNOWFlake_DB_Password"));
	    	sf_DBHost=prop.getProperty("SNOWFlake_DB_Host");
	    	sf_DB_warehouse=prop.getProperty("SNOWFlake_DB_warehouse");
	    	sf_DataBaseName=prop.getProperty("SNOWFlake_DB_DataBaseName");
	    	sf_Account=prop.getProperty("SnowFlake_Account");
	    	sf_Role=prop.getProperty("SnowFlake_Role");
	
	    	Class.forName("net.snowflake.client.jdbc.SnowflakeDriver");
	    	logger.info("Info: Loaded the SnowflakeDriver DB Driver Successfully");
	    	
	    	// build connection properties
	        Properties properties = new Properties();
	        properties.put("user", sf_User_Name);
	        properties.put("password", sf_Password);
	        properties.put("account", sf_Account);
	        properties.put("db", sf_DataBaseName);
	        //properties.put("schema", schema);
	        properties.put("warehouse", sf_DB_warehouse);
	        properties.put("role", sf_Role);
	        

	        
	        if(connectStr == null)
	        {
	         connectStr = "jdbc:snowflake://"+sf_DBHost+".snowflakecomputing.com";
	        }
	        
	        connection=DriverManager.getConnection(connectStr, properties);
	        
	    	logger.info("Info: Connected to the Snow Flake DataBase Successfully");


	    }  catch (SQLException e) {
			// TODO Auto-generated catch block
	    	ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void CloseSnowFlakeDBConnection()
	{
		try
		{
			logger.info("Entering into the Loop to Close the DB Connection");
			
			connection.close();
			
			logger.info("Closeed the DB Connection Successfully");
			
		}  catch (SQLException e) {
			logger.error("Error: Exception while closing the DB Connection");
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
	}
	
public void Connect_SnowFlake_DataBase(String sf_User_Name,String sf_Password,String sf_DataBaseName,String sf_DB_warehouse,String sf_Account,String sf_Role,String sf_DBHost,String connectStr) throws Exception  {
		


		
	    try {
	    	
	    	logger.info("Entering into the Method: DataBaseConnections");
	    	/**************** Calling Application Properties***************************/

	
	    	Class.forName("net.snowflake.client.jdbc.SnowflakeDriver");
	    	logger.info("Info: Loaded the SnowflakeDriver DB Driver Successfully");
	    	
	    	// build connection properties
	        Properties properties = new Properties();
	        properties.put("user", sf_User_Name);
	        properties.put("password", sf_Password);
	        properties.put("account", sf_Account);
	        properties.put("db", sf_DataBaseName);
	        //properties.put("schema", schema);
	        properties.put("warehouse", sf_DB_warehouse);
	        properties.put("role", sf_Role);
	        

	        
	        if(connectStr == null)
	        {
	         connectStr = "jdbc:snowflake://"+sf_DBHost+".snowflakecomputing.com";
	        }
	        
	        connection=DriverManager.getConnection(connectStr, properties);
	        
	    	logger.info("Info: Connected to the Snow Flake DataBase Successfully");


	    }  catch (SQLException e) {
			// TODO Auto-generated catch block
	    	ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
	}
	
}
