@tag
Feature: Daily Monitoring Automation
  This is the Daily Monitoring Automation with DB

@CPIData_Val @DailyMonitoring
	 Scenario: Validate CPI data as Daily monitoring
		When Scheduled invoked validate CPI data for daily monitoring task


@APIMonitoring_Val @DailyMonitoring
	 Scenario: API Daily monitoring automation
		Given Run the API monitoring the task to get hourly Status
	      
@Get_No_Price_Disposition_Vehicle_Details_Val @DailyMonitoring
	 Scenario: get_No_Price_Disposition_Vehicle_Details Too Few Comparable Vehicles
		Given Run the given query to get No Price disposition vehicle details

@Get_Missing_VINs_From_DriveWay_Table @DailyMonitoring
	 Scenario: Source: APIINTEGRATION.DRIVEWAY_CUSTOMERS Target: APIINTEGRATION.VINS_DECODED_BB_USED_VEHICLES_LIST
		Given Get the Missing VINs from the driveway customer table from the given date 
		
		
@Get_edilogs_error_status @DailyMonitoring
	 Scenario: Automate edi error logs
		Given Get error status and data from edi error logs
