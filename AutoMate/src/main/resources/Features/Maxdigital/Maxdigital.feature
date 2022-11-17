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
  This is the Maxdigital Automation with DB and flat file

@Val_maxDigital_hourly_testcases @Smoke
	 Scenario: Get the records status from Prod and validate all the vehicle details exceptions are valid or not
		Given Execute the given hourly max digital test cases
		
@Val_maxDigital_Daily_testcases @Smoke
	 Scenario: Get the records status from Prod and validate all the vehicle details exceptions are valid or not
		Given Execute the given Daily max digital test cases
		
@Val_maxDigital_post_Processing_Advisory_Alerts @Smoke
	 Scenario: Story Post-processing advisory alerts to driveway team
		Given Execute post processing queires and get results
	
@Val_Failure_warning_Summaries_Report @Smoke
	 Scenario: Reporting: Monthly MAXDIGITAL_MASTER pipe load failure warning summaries
		Given get failure warning summaries from Max digital master load