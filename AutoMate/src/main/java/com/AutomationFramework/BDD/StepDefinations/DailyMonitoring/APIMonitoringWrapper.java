package com.AutomationFramework.BDD.StepDefinations.DailyMonitoring;

public class APIMonitoringWrapper {

	public static long Snow_Flake_CVP_count=0;
	public static long Exceptions_Count;
	public static long Non_Exceptions_Count;
	public static long Total_Request;
	public static String HTML_Exception_data;
	public static String Subject="";
	public static String Message_Body;
	
	
	public static long Exceptions_Count_Previous_Day;
	public static long Non_Exceptions_Count_Previous_Day;
	public static long Total_Request_Previous_Day;
	public static long Snow_Flake_CVP_count_Previous_Day=0;
	
	public static long Exceptions_Count_Previous_Day_Current_Hour;
	public static long Non_Exceptions_Count_Previous_Day_Current_Hour;
	public static long Total_Request_Previous_Day_Current_Hour;
	public static long Snow_Flake_CVP_count_Previous_Day_Current_Hour=0;
	
	
	
	
	public static String NoPrice_Message_Body="";
	public static String NoPrice_Subject="All Clear:";
	
	public static String Missing_VINs_Message_Body="";
	public static String Missing_VINs_Subject="All Clear:";
}
