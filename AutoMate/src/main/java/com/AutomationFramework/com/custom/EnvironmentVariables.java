package com.AutomationFramework.com.custom;

public class EnvironmentVariables {

	public static String current_dir = System.getProperty("user.dir");	
	public static String Application_Properties=current_dir+"/src/main/resources/Application_";
	public static String Application_Common_Properties=current_dir+"/src/main/resources/Application_Common.properties";
	public static String SQLQuery_Properties=current_dir+"/src/main/resources/SQL_Query.Properties";
	public static String TestExecutionData_Properties=current_dir+"/src/main/resources/TestExecutionData.properties";
	public static String CSV_File_Loc=current_dir+"/Logs/Results/CSVFiles/";
}
