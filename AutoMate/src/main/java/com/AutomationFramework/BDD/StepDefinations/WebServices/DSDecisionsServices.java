package com.AutomationFramework.BDD.StepDefinations.WebServices;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import com.AutomationFramework.BDD.Runner.DataClass;
import com.AutomationFramework.com.DriverEngine.Driver;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.Pojos.QueryParametersWrapper;
import com.AutomationFramework.com.Pojos.ResultsDBWrapper;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.custom.EnvironmentVariables;
import com.AutomationFramework.com.impl.DataBaseImplementation;
import com.AutomationFramework.com.impl.DataValidation;
import com.AutomationFramework.com.impl.ResultsDBImplementation;
import com.AutomationFramework.com.impl.WebServicesImpl;
import com.Wrapper.ResponseWrapper;

public class DSDecisionsServices {

public static final Logger logger = LoggerFactory.getLogger(DSDecisionsServices.class);
	
	WebServicesImpl WebServicesImpl_Obj=new WebServicesImpl();
	CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
	QueryParametersWrapper queryParametersWrapper_OBJ=new QueryParametersWrapper();

	public static List<String[]> Summary=new ArrayList<String[]>();
	
	@When("^Data loaded successfully invoke the get DS Decisions API using VIN and UVC$")
	public void Get_ICO_Value_Using_VIN_and_UVC() throws Throwable 
	{
		
		ICORestAPIs restAPI=new ICORestAPIs();
		JSONObject get_DetailsBy_VIN_API_Res =null;
		DataValidation datavalidation_Obj=new DataValidation();
		

		try {

			logger.info("Calling get details by VIN API method");
			
			
			if(DataClass.hmap.get("VIN")!=null)
			{
				  get_DetailsBy_VIN_API_Res =null;
				  DataClass.APIGetCallhmap.put("ICO","ICOBYVIN");
				  DataClass.APIGetCallhmap.put("UVC","");
				  
				  restAPI.Get_DetailsBy_VIN_API();
				  get_DetailsBy_VIN_API_Res=restAPI.jsonObject_Get_DetailsBy_VIN_API;
				  
				  
				  JSONObject vehicleInfo=get_DetailsBy_VIN_API_Res.getJSONObject("vehicleInfo");
				  JSONObject dataProviderInfo=get_DetailsBy_VIN_API_Res.getJSONObject("dataProviderInfo");
				  JSONObject blackBook=dataProviderInfo.getJSONObject("blackBook");
				  JSONArray alternativesArray = blackBook.getJSONArray("alternatives");
				  JSONArray featuresArray = blackBook.getJSONArray("features");
				  
				  int alternativesArray_size=alternativesArray.length();
				  
				  if(alternativesArray_size!=0)
				  {
					  DataClass.APIGetCallhmap.put("Comments","MultiTrimScenario");
					  
					  //if(DataClass.hmap.get("UVC").length()>0)
					if(DataClass.hmap.get("UVC")!=null)
					  {
						  JSONObject jsonObject_multiTrim =null;
						  JSONObject jsonObject_ICO=null;
						  String alternatives_UVC=null;
						  alternatives_UVC=DataClass.hmap.get("UVC").trim();
						  DataClass.APIGetCallhmap.put("UVC",alternatives_UVC);
						  
							  jsonObject_multiTrim=restAPI.Get_Vehicle_DetailsBy_VIN_and_UVC_API(alternatives_UVC);  
							  jsonObject_ICO=Get_ICO_by_VIN_and_UVC_API(jsonObject_multiTrim);
							  ICO_Response_Processing(jsonObject_ICO);
							  StoreICO_VIN_Details_ValidationResults();
					  }
					  else
					  {
						  for(int i=0;i<alternativesArray_size;i++)
						  {
							  String alternatives_UVC=null;
							  JSONObject jsonObject_multiTrim =null;
							  JSONObject jsonObject_ICO=null;
							  
							  JSONObject alternatives_Json = alternativesArray.getJSONObject(i);
							  alternatives_UVC=alternatives_Json.get("uvc").toString();
							  
							  
							  DataClass.APIGetCallhmap.put("UVC",alternatives_UVC);
							  
							 // Get_multiTrim_Details_Using_VIN_UVC_from_DataBase(alternatives_UVC);
							  jsonObject_multiTrim=restAPI.Get_Vehicle_DetailsBy_VIN_and_UVC_API(alternatives_UVC);
							  
							 // datavalidation_Obj.Compare_DSDecisions_Detail_By_VIN(jsonObject_multiTrim,DataBaseImplementation.DSDecisionsWrapper_OBJ,DataBaseImplementation.features_configs);
							  
							  jsonObject_ICO=Get_ICO_by_VIN_and_UVC_API(jsonObject_multiTrim);
							  ICO_Response_Processing(jsonObject_ICO);
							  StoreICO_VIN_Details_ValidationResults();
							  
						  }
					  }
				  }
				  else
				  {
					  JSONObject jsonObject_ICO=null;
					  DataClass.APIGetCallhmap.put("ICO","ICOBYVIN");
					  DataClass.APIGetCallhmap.put("Comments","SingleTrim");
					  DataClass.APIGetCallhmap.put("UVC",blackBook.getString("uvc"));
					 // restAPI.Get_Details_from_DataBase();
					  //restAPI.Validate_WebServices_With_DBObject();
					  restAPI.Get_ICO_by_VIN_API();
					  jsonObject_ICO=restAPI.jsonObject_Get_ICOBy_VIN_API;
					  ICO_Response_Processing(jsonObject_ICO);
					  StoreICO_VIN_Details_ValidationResults();
				  }
			}
			else
			{
				  JSONObject jsonObject_ICO=null;
				  JSONObject get_DetailsBy_UVC_API_Res =null;
				  JSONObject ICO_Estimate_Response =null;
				  
				  
				  DataClass.APIGetCallhmap.put("ICO","Estimate");
				  DataClass.APIGetCallhmap.put("Comments","SingleTrim With UVC");
				  DataClass.APIGetCallhmap.put("UVC",DataClass.hmap.get("UVC").trim());
				  get_DetailsBy_UVC_API_Res=restAPI.Get_DetailsBy_UVC_API();
				  
				 //restAPI.Get_Details_from_DataBase();
				 // restAPI.Validate_WebServices_With_DBObject();
				  ICO_Estimate_Response=restAPI.Get_ICO_Estimate_by_UVC_API(get_DetailsBy_UVC_API_Res);

				  ICO_Response_Processing(ICO_Estimate_Response);
				  StoreICO_VIN_Details_ValidationResults();
			}
			

		}
		catch(JSONException e)
		{
			logger.info("Exception for-->"+DataClass.hmap.get("VIN"));
			e.printStackTrace();
		}
		
		catch (Exception e) {
			//ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			logger.info("Exception for-->"+DataClass.hmap.get("VIN"));
			e.printStackTrace();
		}

	}
	
	
	public void Get_multiTrim_Details_Using_VIN_UVC_from_DataBase(String alternatives_UVC) throws Throwable 
	{
		queryParametersWrapper_OBJ=new QueryParametersWrapper();
		DataBaseImplementation DataBaseImplementation_OBJ=new DataBaseImplementation();
		
		try
		{
			logger.info("Entering into the method Get multiTrim Details_from_DataBase ");
			
			logger.info("Setting the data for queryParametersWrapper_OBJ");
			queryParametersWrapper_OBJ.setWebServiceName("DSDecisions_multiTrim");
			queryParametersWrapper_OBJ.setQueryparameter1(DataClass.hmap.get("VIN"));
			queryParametersWrapper_OBJ.setQueryparameter2(alternatives_UVC);
			
			logger.info("WebService name in the queryParametersWrapper_OBJ "+queryParametersWrapper_OBJ.getWebServiceName());
			logger.info("Queryparameter1 name in the queryParametersWrapper_OBJ "+queryParametersWrapper_OBJ.getQueryparameter1());
			
			queryParametersWrapper_OBJ.setQueryname("DSDecisions_multiTrim_vehicleInfo");	
			logger.info("Calling the execute SQL Query methods for DSDecisions_Detail_By_VIN_vehicleInfo with Query name as "+queryParametersWrapper_OBJ.getQueryname());
			
			DataBaseImplementation_OBJ.Execute_SQL_Query(queryParametersWrapper_OBJ);
			logger.info("DB Execution Call completed for DSDecisions_Detail_By_VIN_vehicleInfo with Query name as "+queryParametersWrapper_OBJ.getQueryname());
			
			
			queryParametersWrapper_OBJ.setQueryname("DSDecisions_multiTrim_Features");		
			logger.info("Calling the execute SQL Query methods for DSDecisions_Detail_By_VIN_Features with Query name as "+queryParametersWrapper_OBJ.getQueryname());
			DataBaseImplementation_OBJ.Execute_SQL_Query(queryParametersWrapper_OBJ);
			logger.info("DB Execution Call completed for DSDecisions_Detail_By_VIN_Features with Query name as "+queryParametersWrapper_OBJ.getQueryname());
			
			
		}catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	public JSONObject Get_ICO_by_VIN_and_UVC_API(JSONObject jsonObject_multiTrim) throws JSONException,Exception 
	{
		Properties prop = null;
		String getIOCByVin=null;
		String Final_Header=null;
		ICORestAPIs restAPI=new ICORestAPIs();
		JSONObject jsonObject_ICO=null;
		APIRequests apiRequests_OBJ=new APIRequests();
		

		//try {

			logger.info("Calling Get_ICO_by_VIN_API method");
			  
			prop=CustomFunctions_OBJ.ApplicationProperties();
			
			
			
			
			String Header1="Content-Type,application/json";
			String Header2="AUTH-KEY-ICO,"+prop.getProperty("ICO_BY_VIN_URL_Auth_Key");

			Final_Header=Header1.concat(";").concat(Header2).concat(";");
			
			DataClass.APIGetCallhmap.put("Header",Final_Header);			
			DataClass.APIGetCallhmap.put("GetURLParamVal","/ico");			
			DataClass.APIGetCallhmap.put("APIURL",prop.getProperty("ICO_BY_VIN_URL"));
			DataClass.APIGetCallhmap.put("GETorPOSTCall","POST");
			DataClass.APIGetCallhmap.put("ICO_Service_Response_Time","ICO_by_VIN_Service_Resp_Time");
			
			apiRequests_OBJ.CreategetICOByVINAPIRequest(jsonObject_multiTrim,DataBaseImplementation.DSDecisionsWrapper_OBJ,DataBaseImplementation.features_configs);
			
			getIOCByVin=WebServicesImpl_Obj.APICalls("ICO");
			
			jsonObject_ICO=restAPI.ConvertStringToJson(getIOCByVin); 
			
			logger.info("Get IOC by VIN API Service response for multi trim"+getIOCByVin);

			logger.info("Execution of get IOC for multi trim VIN and UVC API method completed");


      return jsonObject_ICO;

	}
	
	public void ICO_Response_Processing(JSONObject ICOResponse) throws Exception 
	{
		String StatusCode=null;
		
		//try {
			
			logger.info("Enterning into the method Store_Data_in_Source");
			
			StatusCode=DataClass.APIGetCallhmap.get("statusCode");
			
			if(StatusCode.toString().compareTo("404")==0)
			{
				JSONObject error=ICOResponse.getJSONObject("error");
				DataClass.APIGetCallhmap.put("ICO_Value",error.getString("message").toString());
				DataClass.APIGetCallhmap.put("noPriceTriggerValues","");
				DataClass.APIGetCallhmap.put("disposition","");
				//CustomFunctions_OBJ.Store_Value(error.getString("message").toString(),"ICO_Value");
			}
			else if(StatusCode.toString().compareTo("400")==0)
			{
				JSONObject error=ICOResponse.getJSONObject("error");
				DataClass.APIGetCallhmap.put("ICO_Value",error.getString("message").toString());
				DataClass.APIGetCallhmap.put("noPriceTriggerValues","");
				//DataClass.APIGetCallhmap.put("disposition","");
				DataClass.APIGetCallhmap.put("disposition","");
			}
			else if(StatusCode.toString().compareTo("500")==0)
			{
				JSONObject error=ICOResponse.getJSONObject("error");
				DataClass.APIGetCallhmap.put("ICO_Value",error.getString("message").toString());
				DataClass.APIGetCallhmap.put("noPriceTriggerValues","");
				DataClass.APIGetCallhmap.put("disposition","");
			}
			else if(StatusCode.toString().compareTo("200")==0)
			{
				if(DataClass.APIGetCallhmap.get("ICO").compareToIgnoreCase("Estimate")==0)
				{

					JSONObject estimateResponseMessage=ICOResponse.getJSONObject("estimateResponseMessage");
					DataClass.APIGetCallhmap.put("maxEstimatedPrice",String.valueOf(estimateResponseMessage.getDouble("maxEstimatedPrice")));
					DataClass.APIGetCallhmap.put("minEstimatedPrice",String.valueOf(estimateResponseMessage.getDouble("minEstimatedPrice")));
					DataClass.APIGetCallhmap.put("disposition",estimateResponseMessage.getString("disposition"));
					
					JSONArray noPriceTriggerValuesArray = estimateResponseMessage.getJSONArray("noPriceTriggerValues");
					
					StringBuffer noPriceTriggerValues=new StringBuffer();
					String noPriceTriggerValue=null;
					for(int i=0;i<noPriceTriggerValuesArray.length();i++)
					{
						
						if(i==0)
						{
							noPriceTriggerValue=noPriceTriggerValuesArray.get(i).toString();
							noPriceTriggerValues=noPriceTriggerValues.append(noPriceTriggerValue);
						}
						else
						{
							noPriceTriggerValues=noPriceTriggerValues.append(",").append(noPriceTriggerValuesArray.get(i));
						}
						
					}
					DataClass.APIGetCallhmap.put("noPriceTriggerValues",noPriceTriggerValues.toString());
				
				}
				else
				{
					JSONObject icoResponseMessage=ICOResponse.getJSONObject("icoResponseMessage");
					DataClass.APIGetCallhmap.put("ICO_Value",String.valueOf(icoResponseMessage.getDouble("icoValue")));
					DataClass.APIGetCallhmap.put("disposition",icoResponseMessage.getString("disposition"));
					
					JSONArray noPriceTriggerValuesArray = icoResponseMessage.getJSONArray("noPriceTriggerValues");
					
					StringBuffer noPriceTriggerValues=new StringBuffer();
					String noPriceTriggerValue=null;
					for(int i=0;i<noPriceTriggerValuesArray.length();i++)
					{
						
						if(i==0)
						{
							noPriceTriggerValue=noPriceTriggerValuesArray.get(i).toString();
							noPriceTriggerValues=noPriceTriggerValues.append(noPriceTriggerValue);
						}
						else
						{
							noPriceTriggerValues=noPriceTriggerValues.append(",").append(noPriceTriggerValuesArray.get(i));
						}
						
					}
					DataClass.APIGetCallhmap.put("noPriceTriggerValues",noPriceTriggerValues.toString());
				}
				
				

			}
			else
			{
				DataClass.APIGetCallhmap.put("ICO_Value",ICOResponse.toString());
			}
			

		
			logger.info("Execution of Store_Data_in_Source method completed");



	}
	
  public void StoreICO_VIN_Details_ValidationResults() {
		
		CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		ResultsDBWrapper ResultsDBWrapper_OBJ=new ResultsDBWrapper();
		ResultsDBImplementation obj_ResultsDBImplementation=new ResultsDBImplementation();
		final String NEW_LINE_SEPERATOR="\n";
		StringBuffer Comments=new StringBuffer();
		String CurrentDate=null;
		String FileN=null;
		
		try
		{
			CurrentDate=CustomFunctions_OBJ.CurrentDate();

			{
				logger.info("Enterning into the CSV creation loop");
				final Object[] FILE_HEADER= {"Request_ID",
											 "User_Name",
											 "Date",
											 "Environment",
											 "inputCondition",
											 "VINNumber",
											 "UVC",
											 "Mileage",
											 "ZipCode","Details_By_VIN_Service_Res_Time",
											 "ICO_Service_Res_Time",
											 "Details_By_VIN_UVC_Service_Res_Time",
											 "Status",
											 "Comments","disposition","icoValue",
											 "maxEstimatedPrice","minEstimatedPrice",
											 "noPriceTriggerValues"};
				
				
				Summary.add(new String[] {APIRequests.Request,AutomationMainClass.UserName,CurrentDate,AutomationMainClass.environment
						,DataClass.hmap.get("Input_data_Condition")
						,DataClass.hmap.get("VIN")
						,DataClass.APIGetCallhmap.get("UVC")
						,DataClass.hmap.get("Mileage")
						,DataClass.hmap.get("ZipCode")
						,DataClass.APIGetCallhmap.get("GetAPIResponseTime")
						,DataClass.APIGetCallhmap.get("POSTAPIResponseTime")
						,DataClass.APIGetCallhmap.get("MultiTrimServiceResponseTime")
						,DataValidation.FinalValidation.toString()
						,DataClass.APIGetCallhmap.get("Comments")
						,DataClass.APIGetCallhmap.get("disposition")
						,DataClass.APIGetCallhmap.get("ICO_Value")
						,DataClass.APIGetCallhmap.get("maxEstimatedPrice")
						,DataClass.APIGetCallhmap.get("minEstimatedPrice")
						,DataClass.APIGetCallhmap.get("noPriceTriggerValues")});
				
				
				String STORE_RESULTS_IN_DB=DataClass.APIGetCallhmap.get("STORE_RESULTS_IN_DB");
				
				if((STORE_RESULTS_IN_DB.toString().trim().compareToIgnoreCase("Y")==0)||(STORE_RESULTS_IN_DB.toString().trim().compareToIgnoreCase("Yes")==0))
				{
					ResultsDBWrapper_OBJ.setTest_Case_Name(AutomationMainClass.TestCase_Name);
					ResultsDBWrapper_OBJ.setUser_Name(APIRequests.Request);
					ResultsDBWrapper_OBJ.setDate(CurrentDate);
					ResultsDBWrapper_OBJ.setEnvironment(AutomationMainClass.environment);
					ResultsDBWrapper_OBJ.setInput_data_Condition(DataClass.hmap.get("Input_data_Condition"));
					ResultsDBWrapper_OBJ.setInput_Value(DataClass.hmap.get("VIN"));
					ResultsDBWrapper_OBJ.setUvc(DataClass.APIGetCallhmap.get("UVC"));
					ResultsDBWrapper_OBJ.setMileage(DataClass.hmap.get("Mileage"));
					ResultsDBWrapper_OBJ.setZipcode(DataClass.hmap.get("ZipCode"));
					ResultsDBWrapper_OBJ.setDetails_By_VIN_Service_Res_Time(DataClass.APIGetCallhmap.get("GetAPIResponseTime"));
					ResultsDBWrapper_OBJ.setICO_Service_Res_Time(DataClass.APIGetCallhmap.get("POSTAPIResponseTime"));
					ResultsDBWrapper_OBJ.setDetails_By_VIN_UVC_Service_Res_Time(DataClass.APIGetCallhmap.get("MultiTrimServiceResponseTime"));
					if(DataValidation.FinalValidation.toString().trim().length()>=3000)
					{
						ResultsDBWrapper_OBJ.setStatus(DataValidation.FinalValidation.toString().trim().substring(0,2900));
					}
					else
					{
						ResultsDBWrapper_OBJ.setStatus(DataValidation.FinalValidation.toString().trim());
					}
					ResultsDBWrapper_OBJ.setComments(DataClass.APIGetCallhmap.get("Comments"));
					ResultsDBWrapper_OBJ.setDisposition(DataClass.APIGetCallhmap.get("disposition"));
					ResultsDBWrapper_OBJ.setIcoValue(DataClass.APIGetCallhmap.get("ICO_Value"));
					ResultsDBWrapper_OBJ.setMaxEstimatedPrice(DataClass.APIGetCallhmap.get("maxEstimatedPrice"));
					ResultsDBWrapper_OBJ.setMinEstimatedPrice(DataClass.APIGetCallhmap.get("minEstimatedPrice"));
					ResultsDBWrapper_OBJ.setNoPriceTriggerValues(DataClass.APIGetCallhmap.get("noPriceTriggerValues"));
					
					obj_ResultsDBImplementation.insetResultsinDB(ResultsDBWrapper_OBJ,"ICOResults");
				}
				
				FileN=EnvironmentVariables.CSV_File_Loc+AutomationMainClass.UserName+"_ICO_Service_"+CurrentDate+".CSV";
				Driver.driverMapObj.put("CSVFileName",FileN);
				
				CSVFormat csvFileFormat=CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPERATOR);
				
				try
				{
					FileWriter fileWriter2=new FileWriter(FileN);
					CSVPrinter csvPrinter=new CSVPrinter(fileWriter2,csvFileFormat);
					csvPrinter.printRecord(FILE_HEADER);
					csvPrinter.printRecords(Summary);
					
					
					fileWriter2.close();
					csvPrinter.close();
				}
				catch(Exception e)
				{
					ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
					e.printStackTrace();
				}
				
			}
			
			DataValidation.FinalValidation=new StringBuffer();
			
			
			
			
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
	}
}
