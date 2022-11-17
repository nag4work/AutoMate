package com.AutomationFramework.BDD.StepDefinations.WebServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.AutomationFramework.BDD.Runner.DataClass;
import com.AutomationFramework.BDD.StepDefinations.DailyMonitoring.APIMonitoringWrapper;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.Pojos.QueryParametersWrapper;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.custom.EnvironmentVariables;
import com.AutomationFramework.com.impl.WebServicesImpl;
import com.Wrapper.ResponseWrapper;

public class WebServiceExceptionStatus {
	
public static final Logger logger = LoggerFactory.getLogger(DSDecisionsServices.class);
	
	WebServicesImpl WebServicesImpl_Obj=new WebServicesImpl();
	CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
	QueryParametersWrapper queryParametersWrapper_OBJ=new QueryParametersWrapper();

	public static List<String[]> Summary=new ArrayList<String[]>();

	public static String Message_Body;
	
	@When("^Data loaded successfully invoke validate the webservice exception status$")
	public void Get_exception_record_status() throws Throwable 
	{
		String line = "";  
		String splitBy = ",";  
		BufferedReader br = null;

		try {

				logger.info("Calling Get_exception_record_status method");
				AutomationMainClass.email_mode="Val_WebServices_exception_Status";
				
				//getAzurelogAnalytics();
				
				StringBuilder buf = new StringBuilder();
				buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=10 cellspacing=0>" +"<tr>" +"<th>Host</th>" +"<th>Path</th>"+"<th>File StatusCode</th>"+"<th>Current execution Status Code</th>"+"</tr>");
				
				File myDirectory = new File(EnvironmentVariables.current_dir+"/Logs/ExceptionInputFiles/");
				File[] listOfFiles = myDirectory.listFiles();
				
				for (int i = 0; i < listOfFiles.length; i++)
				{
					
					   if (listOfFiles[i].getName().endsWith(".csv"))
						{
						    br = new BufferedReader(new FileReader(listOfFiles[i])); 
						    
						   while ((line = br.readLine()) != null)   
						   {  
							   String rec_StatusCode=null;
							   int StatusCode=0;
							   String Path=null;
							   String[] record = line.split(splitBy);    
							   
							   rec_StatusCode=record[2];
							   Path=record[1];
							   
							   if(((rec_StatusCode.toString().compareTo("503")==0)||(rec_StatusCode.toString().compareTo("500")==0))&&(Path.contains("vehicledetails")))
							   {
								   StatusCode=invokeVehicleDetailsService(Path);
								   buf.append("<tr><td>"+record[0]+"</td><td>"+Path+"</td><td>"+rec_StatusCode+"</td><td>"+StatusCode+"</td>");
									
							   }
							   
							     
						   }
						   
						   br.close();
						   
						   movefiletoArchivelocation(myDirectory,listOfFiles[i].getName());

					    }
				}
				
				 
				  
				buf.append("</table>" +"</body>" + "</html>");
				
				
				WebServiceExceptionStatus.Message_Body="<br>Automation execution for the 503 records validation completed. Please find the results below.<br></br>"
						+ "<br></br><b><u> Validation Status:</u></b><br></br>"
					    + "\t\t"+buf.toString()
						
						+ "<br><br></br> This is auto generated email. Please don't reply to this email."
						+ "<br></br>Thanks,"
						+ "<br>EDS Automation Group.<br></br>";
				  

			
			

		}
		
		catch (Exception e) {

			e.printStackTrace();
		}
		finally
		{
			br.close();
		}

	}
	

	
	
	private void getAzurelogAnalytics() {
		
		/*String Query="// Request errors by host and path\r\n"
				+ "// Count number of requests with error responses by host and path.\r\n"
				+ "// Summarize number of requests by host, path, and status codes >= 400\r\n"
				+ "// To create an alert for this query, click '+ New alert rule'\r\n"
				+ "AzureDiagnostics\r\n"
				+ "| where ResourceProvider == \"MICROSOFT.NETWORK\" and Category == \"FrontdoorAccessLog\"\r\n"
				+ "| where isReceivedFromClient_b == true\r\n"
				+ "| where toint(httpStatusCode_s) >= 400\r\n"
				+ "| extend ParsedUrl = parseurl(requestUri_s)\r\n"
				+ "| summarize RequestCount = count() by Host = tostring(ParsedUrl.Host), Path = tostring(ParsedUrl.Path), StatusCode = httpStatusCode_s, ResourceId\r\n"
				+ "| order by RequestCount desc";
		
		String workspace_id="00d142c5-cf11-4c82-a66d-c16c583ff5e7";
		
		LogsQueryClient logsQueryClient = new LogsQueryClientBuilder().credential(new DefaultAzureCredentialBuilder().build()).buildClient();

		LogsBatchQuery logsBatchQuery = new LogsBatchQuery();
		String query1 = logsBatchQuery.addWorkspaceQuery(workspace_id, Query, new QueryTimeInterval(Duration.ofDays(7)));
		
		LogsBatchQueryResultCollection batchResults = logsQueryClient.queryBatchWithResponse(logsBatchQuery, Context.NONE).getValue();

		LogsBatchQueryResult query1Result = batchResults.getResult(query1);
		
		for (LogsTableRow row : query1Result.getTable().getRows()) {
		    System.out.println(row.getColumnValue("Path") + " " + row.getColumnValue("StatusCode"));
		}
		
		*/

	}




	private void movefiletoArchivelocation(File myDirectory, String fileName) {
		
		Path temp;
		try {
			
			temp = Files.move(Paths.get(myDirectory+File.separator+fileName),Paths.get(EnvironmentVariables.current_dir+"/Logs/ExceptionInputFiles/Archive/"+fileName));
		 
		        if(temp != null)
		        {
		            System.out.println("File renamed and moved successfully");
		        }
		        else
		        {
		            System.out.println("Failed to move the file");
		        }
		        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}




	private int invokeVehicleDetailsService(String path) {

		Properties prop = null;
		String Final_Header=null;
		int statusCode=0;

		try {

			logger.info("Calling get details by VIN API method");
			  
			prop=CustomFunctions_OBJ.ApplicationProperties();
			

			String Header1="Content-Type,application/json";
			String Header2="Auth-key,"+prop.getProperty("Detail_By_VIN_Auth_Key");

			Final_Header=Header1.concat(";").concat(Header2).concat(";");
			
			DataClass.APIGetCallhmap.put("Header",Final_Header);			
			DataClass.APIGetCallhmap.put("GetURLParamVal",path);			
			DataClass.APIGetCallhmap.put("APIURL",prop.getProperty("get_record_exception_url"));
			DataClass.APIGetCallhmap.put("GETorPOSTCall","GET");
			
			
			statusCode=ExceptionAPIGetCall();


		} catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}

	
		
		
		
		return statusCode;
		
	}
	
	public int ExceptionAPIGetCall()
	{
		int statusCode=0;
		String URLParameter=null;
		Response response=null;
		WebServicesImpl WebServicesImpl_obj=new WebServicesImpl();
		
		try
		{
			    RestAssured.baseURI = DataClass.APIGetCallhmap.get("APIURL");			 
		        RequestSpecification httpRequest = RestAssured.given();	
		        
		        httpRequest=WebServicesImpl_obj.APIHeadersProcessing(httpRequest);
		        URLParameter=DataClass.APIGetCallhmap.get("GetURLParamVal");
		        response = httpRequest.request(Method.GET, URLParameter);  
		        
		        statusCode = response.getStatusCode();
		        

		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		
		return statusCode;
	}




}
