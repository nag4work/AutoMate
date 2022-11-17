#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@tag
Feature: API Automation
  This is the POC for API Automation with DB and flat file

@Get_Detail_By_VIN @Smoke
	 Scenario Outline: Validating the VIN
	  Given Load the data properies from the given DSID "<DSID>" sheetname "<SheetName>" and excelpath "<DataSheetPath>"
		When Data loaded successfully invoke the get details by VIN API
		And Get the details from the DataBase
		Then Validate the Web Service Response with DataBase data
		When get details by VIN API Success invoke the get ICO by VIN API
		Then Store the ICO Value in Data Source
		
		Examples:
		| DSID   | SheetName | DataSheetPath |
		| DS1   | Detail_By_VIN | WebServicesExecution |
		| DS2   | Detail_By_VIN | WebServicesExecution |
		| DS3   | Detail_By_VIN | WebServicesExecution |
		| DS4   | Detail_By_VIN | WebServicesExecution |
		| DS5   | Detail_By_VIN | WebServicesExecution |
		| DS6   | Detail_By_VIN | WebServicesExecution |
		
@Get_ICO_Value_By_VIN @Smoke
	 Scenario: Validating the VIN
		When Data loaded successfully invoke the get details by VIN API
		And Get the details from the DataBase
		Then Validate the Web Service Response with DataBase data
		When get details by VIN API Success invoke the get ICO by VIN API
		Then Store the ICO Value in Data Source
		

@DSDecisionsAPI_Val @Smoke
	 Scenario: Get the ICO value using VIN and UVC with Single Trim and Multi Trim scenario
		When Data loaded successfully invoke the get DS Decisions API using VIN and UVC

@Val_WebServices_exception_Status @Smoke
	 Scenario: Get the records status from Prod and validate all the vehicle details exceptions are valid or not
		When Data loaded successfully invoke validate the webservice exception status
		
@Get_Detail_By_VIN_And_UVC_MultiTrim @Smoke
	 Scenario Outline: Validating the VIN
	  Given Load the data properies from the given DSID "<DSID>" sheetname "<SheetName>" and excelpath "<DataSheetPath>"
		When Data loaded successfully invoke the get details by VIN and UVC on multitrim API
		
		Examples:
		| DSID   | SheetName | DataSheetPath |
		| DS1   | Get_Detail_By_VIN_UVC_MultiTrim | WebServicesExecution |
		| DS2   | Get_Detail_By_VIN_UVC_MultiTrim | WebServicesExecution |
		| DS3   | Get_Detail_By_VIN_UVC_MultiTrim | WebServicesExecution |
		| DS4   | Get_Detail_By_VIN_UVC_MultiTrim | WebServicesExecution |
		| DS5   | Get_Detail_By_VIN_UVC_MultiTrim | WebServicesExecution |
		| DS6   | Get_Detail_By_VIN_UVC_MultiTrim | WebServicesExecution |
		
		
@Val_CacheServices_count @Smoke
	 Scenario: Get the cache Service count from snow flake and Cache Web Service
		Given Get the cache Service DB count from snow flake and from Cache Web Service

 
	      
  
