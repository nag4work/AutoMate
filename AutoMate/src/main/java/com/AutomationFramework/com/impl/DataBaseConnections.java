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

public class DataBaseConnections {
	
	public static final Logger logger = LoggerFactory.getLogger(DataBaseConnections.class);
	public static Connection connection=null;
	public static Connection results_db_connection=null;
	public static Connection Automation_results_db_connection=null;
	public static Connection SQL_CVP_db_connection=null;
	
	public void SQLServerConnection() throws Exception  {
		
		Properties prop = null;
		CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		
		String SQL_Server_Driver_DLL=null;
		String User_Name=null;
    	String Password=null;
    	String Host=null;
    	String Port=null;
    	String DataBaseName=null;
		
	    try {
	    	
	    	logger.info("Entering into the Method: DataBaseConnections");
	    	/**************** Calling Application Properties***************************/
	    	
	    	prop=CustomFunctions_OBJ.ApplicationProperties();
	    	
	    	SQL_Server_Driver_DLL=prop.getProperty("SQL_Server_Driver_DLL");
	    	
	    	User_Name=prop.getProperty("SQL_Server_User_Name");
	    	Password=getdescryptedDBpassword(prop.getProperty("SQL_Server_Password"));
	    	Host=prop.getProperty("SQL_Server_Host");
	    	Port=prop.getProperty("SQL_Server_Port");
	    	DataBaseName=prop.getProperty("SQL_Server_DataBaseName");
	    	
	    	
	    	String path=System.getProperty("java.library.path");
	    	path=EnvironmentVariables.current_dir+SQL_Server_Driver_DLL+path;
	    	System.setProperty("java.library.path", path);
	    	
	    	
	    	try
	    	{
	    	final Field sysPathsField=ClassLoader.class.getDeclaredField("sys_paths");
	    	sysPathsField.setAccessible(true);
	    	sysPathsField.set(null, null);
	    	}catch (NoSuchFieldException e) {
	    		ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} 
	    	
	    	
	    	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    	logger.info("Info: Loaded the jdbc.SQLServerDriver DB Driver Successfully");
	    	
	    	String connection_URL = "jdbc:sqlserver://"+Host+";databaseName="+DataBaseName+";user="+User_Name+";password="+Password+";encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.sql.azuresynapse.net;";
	      
	    	// String connection_URL = "jdbc:sqlserver://azwu2cvpuat-saw.sql.azuresynapse.net;databaseName=cvp;user=user_api;password=!qazfsdf357@@@(7xsw23Edcvfr4;encrypt=true;hostNameInCertificate=*.sql.azuresynapse.net;";

	    	logger.info("Info: Connection String"+connection_URL);
	      
	    	connection = DriverManager.getConnection(connection_URL); 
	    	
	    	logger.info("Info: Connected to the DataBase Successfully");


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
	
	
	public void CloseDBConnection()
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
	
	
	
	public void Connect_Results_SQLServerDB() throws Exception  {
		
		Properties prop = null;
		CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		
		String SQL_Server_Driver_DLL=null;
		String User_Name=null;
    	String Password=null;
    	String Host=null;
    	String Port=null;
    	String DataBaseName=null;
		
	    try {
	    	
	    	logger.info("Entering into the Method: Connect_Results_SQLServerDB");
	    	/**************** Calling Application Properties***************************/
	    	
	    	prop=CustomFunctions_OBJ.ApplicationCommonProperties();
	    	//prop=CustomFunctions_OBJ.ApplicationProperties();
	    	
	    	SQL_Server_Driver_DLL=prop.getProperty("SQL_Server_Driver_DLL");
	    	
	    	User_Name=prop.getProperty("Results_DB_SQL_Server_User_Name");
	    	Password=getdescryptedDBpassword(prop.getProperty("Results_DB_SQL_Server_Password"));
	    	Host=prop.getProperty("Results_DB_SQL_Server_Host");
	    	Port=prop.getProperty("SQL_Server_Port");
	    	DataBaseName=prop.getProperty("Results_DB_SQL_Server_DataBaseName");
	    	
	    	
	    	String path=System.getProperty("java.library.path");
	    	//System.out.println("path"+path);
	    	path=EnvironmentVariables.current_dir+SQL_Server_Driver_DLL+path;
	    	System.setProperty("java.library.path", path);
	    	
	    	
	    	try
	    	{
	    	final Field sysPathsField=ClassLoader.class.getDeclaredField("sys_paths");
	    	sysPathsField.setAccessible(true);
	    	sysPathsField.set(null, null);
	    	}catch (NoSuchFieldException e) {
	    		ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} 
	    	
	    	
	    	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    	logger.info("Info: Loaded the jdbc.SQLServerDriver DB Driver Successfully");
	    	
	    	String connection_URL = "jdbc:sqlserver://"+Host+";databaseName="+DataBaseName+";user="+User_Name+";password="+Password+";encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.sql.azuresynapse.net;";
	      
	    	// String connection_URL = "jdbc:sqlserver://azwu2cvpuat-saw.sql.azuresynapse.net;databaseName=cvp;user=user_api;password=!qazfsdf357@@@(7xsw23Edcvfr4;encrypt=true;hostNameInCertificate=*.sql.azuresynapse.net;";

	    	logger.info("Info: Connection String"+connection_URL);
	      
	    	results_db_connection = DriverManager.getConnection(connection_URL); 
	    	
	    	logger.info("Info: Connected to the DataBase Successfully");


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
	
	
	public void CloseResultsDBConnection()
	{
		try
		{
			logger.info("Entering into the Loop to Close the DB Connection");
			
			results_db_connection.close();
			
			logger.info("Closeed the DB Connection Successfully");
			
		}  catch (SQLException e) {
			logger.error("Error: Exception while closing the DB Connection");
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	
public void Connect_Automation_Results_SQLServerDB() throws Exception  {
		
		Properties prop = null;
		CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		
		String SQL_Server_Driver_DLL=null;
		String User_Name=null;
    	String Password=null;
    	String Host=null;
    	String Port=null;
    	String DataBaseName=null;
		
	    try {
	    	
	    	logger.info("Entering into the Method: Connect_Results_SQLServerDB");
	    	/**************** Calling Application Properties***************************/
	    	
	    	prop=CustomFunctions_OBJ.ApplicationProperties();
	    	
	    	SQL_Server_Driver_DLL=prop.getProperty("SQL_Server_Driver_DLL");
	    	
	    	User_Name=prop.getProperty("Results_DB_SQL_Server_User_Name");
	    	Password=getdescryptedDBpassword(prop.getProperty("Results_DB_SQL_Server_Password"));
	    	Host=prop.getProperty("Results_DB_SQL_Server_Host");
	    	Port=prop.getProperty("SQL_Server_Port");
	    	DataBaseName=prop.getProperty("Results_DB_SQL_Server_DataBaseName");
	    	
	    	
	    	String path=System.getProperty("java.library.path");
	    	path=EnvironmentVariables.current_dir+SQL_Server_Driver_DLL+path;
	    	System.setProperty("java.library.path", path);
	    	
	    	
	    	try
	    	{
	    	final Field sysPathsField=ClassLoader.class.getDeclaredField("sys_paths");
	    	sysPathsField.setAccessible(true);
	    	sysPathsField.set(null, null);
	    	}catch (NoSuchFieldException e) {
	    		ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} 
	    	
	    	
	    	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    	logger.info("Info: Loaded the jdbc.SQLServerDriver DB Driver Successfully");
	    	
	    	String connection_URL = "jdbc:sqlserver://"+Host+";databaseName="+DataBaseName+";user="+User_Name+";password="+Password+";encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.sql.azuresynapse.net;";
	      
	    	logger.info("Info: Connection String"+connection_URL);
	      
	    	Automation_results_db_connection = DriverManager.getConnection(connection_URL); 
	    	
	    	logger.info("Info: Connected to the DataBase Successfully");


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
	
	
	public void CloseAutomationResultsDBConnection()
	{
		try
		{
			logger.info("Entering into the Loop to Close the DB Connection");
			
			Automation_results_db_connection.close();
			
			logger.info("Closeed the DB Connection Successfully");
			
		}  catch (SQLException e) {
			logger.error("Error: Exception while closing the DB Connection");
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
	}

	
public void Logging_SQLServerCVPConnection(String dbname) throws Exception  {
		
		Properties prop = null;
		CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		
		String SQL_Server_Driver_DLL=null;
		String User_Name=null;
    	String Password=null;
    	String Host=null;
    	String Port=null;
    	String DataBaseName=null;
		
	    try {
	    	
	    	logger.info("Entering into the Method: SQLServerCVPConnection");
	    	/**************** Calling Application Properties***************************/
	    	
	    	prop=CustomFunctions_OBJ.ApplicationProperties();
	    	
	    	SQL_Server_Driver_DLL=prop.getProperty("SQL_Server_Driver_DLL");
	    	
	    	User_Name=prop.getProperty("Logging_SQL_Server_User_Name");
	    	if(dbname.toString().compareToIgnoreCase("EDIERRORLOG")==0)
	    	{
	    		Password=getdescryptedDBpassword(prop.getProperty("EDI_Logging_SQL_Server_Password"));
	    	}
	    	else {
	    		Password=getdescryptedDBpassword(prop.getProperty("Logging_SQL_Server_Password"));
			}
	    	
	    	Host=prop.getProperty("Logging_SQL_Server_Host");
	    	Port=prop.getProperty("Logging_SQL_Server_Port");
	    	DataBaseName=prop.getProperty("Logging_SQL_Server_DB");
	    	
	    	
	    	String path=System.getProperty("java.library.path");
	    	path=EnvironmentVariables.current_dir+SQL_Server_Driver_DLL+path;
	    	System.setProperty("java.library.path", path);
	    	
	    	
	    	try
	    	{
	    	final Field sysPathsField=ClassLoader.class.getDeclaredField("sys_paths");
	    	sysPathsField.setAccessible(true);
	    	sysPathsField.set(null, null);
	    	}catch (NoSuchFieldException e) {
	    		ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} 
	    	
	    	
	    	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    	logger.info("Info: Loaded the jdbc.SQLServerDriver DB Driver Successfully");
	    	
	    	String connection_URL = "jdbc:sqlserver://"+Host+";databaseName="+DataBaseName+";user="+User_Name+";password="+Password+";encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.sql.azuresynapse.net;";
	      
	    	logger.info("Info: Connection String"+connection_URL);
	      
	    	SQL_CVP_db_connection = DriverManager.getConnection(connection_URL); 
	    	
	    	logger.info("Info: Connected to the DataBase Successfully");


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
	
	
	public void Logging_CloseCVPDBConnection()
	{
		try
		{
			logger.info("Entering into the Loop to Close the DB Connection");
			
			SQL_CVP_db_connection.close();
			
			logger.info("Closeed the DB Connection Successfully");
			
		}  catch (SQLException e) {
			logger.error("Error: Exception while closing the DB Connection");
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	String getdescryptedDBpassword(String property) 
	{
		final String secretKey = "!@#$%^&*()AutomationDataScienceKeyGeneration!@#$%^&*()";
 	    String decryptedString = CustomFunctions.decrypt(property, secretKey) ;
		return decryptedString;
	}
	
	
	
public void SQLServerConnection(String User_Name,String Password,String Host, String Port,String DataBaseName ) throws Exception  {
		
		String SQL_Server_Driver_DLL="src/main/resources/Config/Microsoft JDBC Driver SQL Server/sqljdbc_6.0/enu/auth/x64;";

		
	    try {
	    	
	    	logger.info("Entering into the Method: DataBaseConnections");
	    	/**************** Calling Application Properties***************************/
	    	
	    	
	    	String path=System.getProperty("java.library.path");
	    	path=EnvironmentVariables.current_dir+SQL_Server_Driver_DLL+path;
	    	System.setProperty("java.library.path", path);
	    	
	    	
	    	try
	    	{
	    	final Field sysPathsField=ClassLoader.class.getDeclaredField("sys_paths");
	    	sysPathsField.setAccessible(true);
	    	sysPathsField.set(null, null);
	    	}catch (NoSuchFieldException e) {
	    		ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			} 
	    	
	    	
	    	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    	logger.info("Info: Loaded the jdbc.SQLServerDriver DB Driver Successfully");
	    	
	    	String connection_URL = "jdbc:sqlserver://"+Host+";databaseName="+DataBaseName+";user="+User_Name+";password="+Password+";encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.sql.azuresynapse.net;";
	    	  
	    	logger.info("Info: Connection String"+connection_URL);
	      
	    	connection = DriverManager.getConnection(connection_URL); 
	    	
	    	logger.info("Info: Connected to the DataBase Successfully");


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
