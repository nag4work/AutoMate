package com.AutomationFramework.BDD.StepDefinations.WebServices;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import com.AutomationFramework.BDD.Runner.DataClass;
import com.AutomationFramework.com.DriverEngine.Driver;
import com.AutomationFramework.com.KeywordHelper.WebAppKeywordHelper;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.Pojos.QueryParametersWrapper;
import com.AutomationFramework.com.Pojos.ResultsDBWrapper;
import com.AutomationFramework.com.custom.AutomationNotifications;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.custom.EnvironmentVariables;
import com.AutomationFramework.com.impl.DataBaseImplementation;
import com.AutomationFramework.com.impl.DataValidation;
import com.AutomationFramework.com.impl.ResultsDBImplementation;
import com.AutomationFramework.com.impl.WebServicesImpl;
import com.Wrapper.ResponseWrapper;

public class ICORestAPIs {
	
	//public static final Logger logger = Logger.getLogger(RestAPI.class);
	public static final Logger logger = LoggerFactory.getLogger(ICORestAPIs.class);
	
	WebServicesImpl WebServicesImpl_Obj=new WebServicesImpl();
	CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
	QueryParametersWrapper queryParametersWrapper_OBJ=new QueryParametersWrapper();
	public JSONObject jsonObject_Get_DetailsBy_VIN_API =null;
	public JSONObject jsonObject_Get_ICOBy_VIN_API=null;
	public static List<String[]> Summary=new ArrayList<String[]>();

	@When("^Data loaded successfully invoke the get details by VIN API$")
	public void Get_DetailsBy_VIN_API() throws Throwable 
	{
		Properties prop = null;
		String getDetailsByVin=null;
		String Final_Header=null;
		

		try {

			logger.info("Calling get details by VIN API method");
			  
			prop=GetProperties();
			

			String Header1="Content-Type,application/json";
			String Header2="Auth-key,"+prop.getProperty("Detail_By_VIN_Auth_Key");

			Final_Header=Header1.concat(";").concat(Header2).concat(";");
			
			DataClass.APIGetCallhmap.put("Header",Final_Header);			
			DataClass.APIGetCallhmap.put("GetURLParamVal",DataClass.hmap.get("VIN").trim());			
			DataClass.APIGetCallhmap.put("APIURL",prop.getProperty("Detail_By_VIN_URL"));
			DataClass.APIGetCallhmap.put("GETorPOSTCall","GET");
			DataClass.APIGetCallhmap.put("Get_Details_By_VIN_Service_Response_Time","Detail_by_VIN_Service_Response_Time");
			
			
			getDetailsByVin=WebServicesImpl_Obj.APICalls("GetVehicleDetailsByVIN");
			
			jsonObject_Get_DetailsBy_VIN_API=ConvertStringToJson(getDetailsByVin); 
			
			//System.out.println(jsonObject);
			logger.info("Get Details by VIN API Service response "+getDetailsByVin);
			
			//DataClass.APIGetCallhmap.put("getDetailsByVin",getDetailsByVin);
			
			//CustomFunctions_OBJ.Store_Value(getDetailsByVin,"Get_Details_By_VIN_Response");
			logger.info("Execution of get details by VIN API method completed");

		} catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}

	}
	
	
	@When("^get details by VIN API Success invoke the get ICO by VIN API$")
	public void Get_ICO_by_VIN_API() throws Exception 
	{
		Properties prop = null;
		String getIOCByVin=null;
		String Final_Header=null;
		APIRequests apiRequests_OBJ=new APIRequests();
		

		//try {

			logger.info("Calling Get_ICO_by_VIN_API method");
			  
			prop=GetProperties();
			
			
			
			
			String Header1="Content-Type,application/json";
			//String Header2="AUTH-KEY-ICO-V1,"+prop.getProperty("ICO_BY_VIN_URL_Auth_Key");
			String Header2="AUTH-KEY-ICO,"+prop.getProperty("ICO_BY_VIN_URL_Auth_Key");

			Final_Header=Header1.concat(";").concat(Header2).concat(";");
			
			DataClass.APIGetCallhmap.put("Header",Final_Header);			
			DataClass.APIGetCallhmap.put("GetURLParamVal","/ico");			
			DataClass.APIGetCallhmap.put("APIURL",prop.getProperty("ICO_BY_VIN_URL"));
			DataClass.APIGetCallhmap.put("GETorPOSTCall","POST");
			DataClass.APIGetCallhmap.put("ICO_Service_Response_Time","ICO_by_VIN_Service_Resp_Time");
			
			apiRequests_OBJ.CreategetICOByVINAPIRequest(this.jsonObject_Get_DetailsBy_VIN_API,DataBaseImplementation.DSDecisionsWrapper_OBJ,DataBaseImplementation.features_configs);
			
	
			
			getIOCByVin=WebServicesImpl_Obj.APICalls("ICO");
			
			jsonObject_Get_ICOBy_VIN_API=ConvertStringToJson(getIOCByVin); 
			
			logger.info("Get IOC by VIN API Service response "+getIOCByVin);
			
			
			//DataClass.APIGetCallhmap.put("getIOCByVin",getIOCByVin);
			
			//CustomFunctions_OBJ.Store_Value(getIOCByVin,"Get_ICO_By_VIN_Response");
			
			logger.info("Execution of get IOC by VIN API method completed");

		/*} catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}*/

	}
	
	@Then("^Store the ICO Value in Data Source$")
	public void Store_Data_in_Source() throws Exception 
	{
		String StatusCode=null;
		
		//try {
			
			logger.info("Enterning into the method Store_Data_in_Source");
			
			StatusCode=DataClass.APIGetCallhmap.get("statusCode");
			
			if(StatusCode.toString().compareTo("404")==0)
			{
				JSONObject error=jsonObject_Get_ICOBy_VIN_API.getJSONObject("error");
				DataClass.APIGetCallhmap.put("ICO_Value",error.getString("message").toString());
				DataClass.APIGetCallhmap.put("noPriceTriggerValues","");
				DataClass.APIGetCallhmap.put("disposition","");
				//CustomFunctions_OBJ.Store_Value(error.getString("message").toString(),"ICO_Value");
			}
			else if(StatusCode.toString().compareTo("400")==0)
			{
				JSONObject error=jsonObject_Get_ICOBy_VIN_API.getJSONObject("error");
				DataClass.APIGetCallhmap.put("ICO_Value",error.getString("message").toString());
				DataClass.APIGetCallhmap.put("noPriceTriggerValues","");
				//DataClass.APIGetCallhmap.put("disposition","");
				DataClass.APIGetCallhmap.put("disposition","");
			}
			else if(StatusCode.toString().compareTo("500")==0)
			{
				JSONObject error=jsonObject_Get_ICOBy_VIN_API.getJSONObject("error");
				DataClass.APIGetCallhmap.put("ICO_Value",error.getString("message").toString());
				DataClass.APIGetCallhmap.put("noPriceTriggerValues","");
				DataClass.APIGetCallhmap.put("disposition","");
			}
			else if(StatusCode.toString().compareTo("200")==0)
			{
				JSONObject icoResponseMessage=jsonObject_Get_ICOBy_VIN_API.getJSONObject("icoResponseMessage");
				DataClass.APIGetCallhmap.put("ICO_Value",String.valueOf(icoResponseMessage.getDouble("icoValue")));
				DataClass.APIGetCallhmap.put("disposition",icoResponseMessage.getString("disposition"));
				
				JSONArray noPriceTriggerValuesArray = icoResponseMessage.getJSONArray("noPriceTriggerValues");
				
				StringBuffer noPriceTriggerValues=new StringBuffer();
				String noPriceTriggerValue=null;
				for(int i=0;i<noPriceTriggerValuesArray.length();i++)
				{
					//JSONObject noPriceTriggerValues_Json = noPriceTriggerValuesArray.get(i);
					if(i==0)
					{
						noPriceTriggerValue=noPriceTriggerValuesArray.get(i).toString();
						noPriceTriggerValues=noPriceTriggerValues.append(noPriceTriggerValue);
					}
					else
					{
					//noPriceTriggerValues=noPriceTriggerValues.append(",").append(noPriceTriggerValuesArray.get(i)).append("\n");
					noPriceTriggerValues=noPriceTriggerValues.append(",").append(noPriceTriggerValuesArray.get(i));
					}
					
				}
				DataClass.APIGetCallhmap.put("noPriceTriggerValues",noPriceTriggerValues.toString());
				
				//Double Value=0.00;
				//Value=icoResponseMessage.getDouble("icoValue");				
    			//CustomFunctions_OBJ.Store_Value(String.valueOf(icoResponseMessage.getDouble("icoValue")),"ICO_Value");
			}
			else
			{
				DataClass.APIGetCallhmap.put("ICO_Value",jsonObject_Get_ICOBy_VIN_API.toString());
			}
			

			StoreICO_VIN_Details_ValidationResults(DataClass.APIGetCallhmap.get("DataSource_Excel_True"));
		
			logger.info("Execution of Store_Data_in_Source method completed");

		/*} catch (Exception e) {
			
			e.printStackTrace();
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());

		}*/

	}
	
	
	public void StoreICO_VIN_Details_ValidationResults(String DataSource_Excel_True) {
		
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
			
			if((DataSource_Excel_True.toString().trim().compareToIgnoreCase("Y")==0)||(DataSource_Excel_True.toString().trim().compareToIgnoreCase("Yes")==0))
			{
				CustomFunctions_OBJ.Store_Value(DataClass.APIGetCallhmap.get("GetAPIResponseTime"),DataClass.APIGetCallhmap.get("Get_Details_By_VIN_Service_Response_Time"));
				CustomFunctions_OBJ.Store_Value(DataClass.APIGetCallhmap.get("POSTAPIResponseTime"),DataClass.APIGetCallhmap.get("ICO_Service_Response_Time"));
				//CustomFunctions_OBJ.Store_Value(DataClass.APIGetCallhmap.get("getDetailsByVin"),"Get_Details_By_VIN_Response");
				//CustomFunctions_OBJ.Store_Value(DataClass.APIGetCallhmap.get("getIOCByVin"),"Get_ICO_By_VIN_Response");
				
				CustomFunctions_OBJ.Store_Value(DataClass.APIGetCallhmap.get("ICO_Value"),"ICO_Value");
				CustomFunctions_OBJ.Store_Value(DataValidation.FinalValidation.toString(),"Status");
			
			}
			else
			{
				logger.info("Enterning into the CSV creation loop");
				final Object[] FILE_HEADER= {"Test Case Name","User_Name","Date","Environment","VIN","Status","disposition","icoValue","noPriceTriggerValues"};
				
				Comments=Comments.append("Get_Details_By_VIN_Service_Response_Time: ").append(DataClass.APIGetCallhmap.get("GetAPIResponseTime")).append(",\n");
				Comments=Comments.append("ICO_Service_Response_Time: ").append(DataClass.APIGetCallhmap.get("POSTAPIResponseTime")).append(",\n");
				Comments=Comments.append("Get_Details_By_VIN_Service_Validation: ").append(DataValidation.FinalValidation.toString()).append(",\n");
				Comments=Comments.append("ICO_Value: ").append(DataClass.APIGetCallhmap.get("ICO_Value")).append("\n");
				
				
				Summary.add(new String[] {AutomationMainClass.TestCase_Name,AutomationMainClass.UserName,CurrentDate,AutomationMainClass.environment,DataClass.hmap.get("VIN"),Comments.toString()
						,DataClass.APIGetCallhmap.get("disposition")
						,DataClass.APIGetCallhmap.get("ICO_Value")
						,DataClass.APIGetCallhmap.get("noPriceTriggerValues")});
				
				
				ResultsDBWrapper_OBJ.setTest_Case_Name(AutomationMainClass.TestCase_Name);
				ResultsDBWrapper_OBJ.setUser_Name(AutomationMainClass.UserName);
				ResultsDBWrapper_OBJ.setDate(CurrentDate);
				ResultsDBWrapper_OBJ.setEnvironment(AutomationMainClass.environment);
				ResultsDBWrapper_OBJ.setInput_Value(DataClass.hmap.get("VIN"));
				ResultsDBWrapper_OBJ.setStatus(Comments.toString());
				ResultsDBWrapper_OBJ.setDisposition(DataClass.APIGetCallhmap.get("disposition"));
				ResultsDBWrapper_OBJ.setIcoValue(DataClass.APIGetCallhmap.get("ICO_Value"));
				ResultsDBWrapper_OBJ.setNoPriceTriggerValues(DataClass.APIGetCallhmap.get("noPriceTriggerValues"));
				
				obj_ResultsDBImplementation.insetResultsinDB(ResultsDBWrapper_OBJ,"ICOResults");
				
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


	@When("^Data loaded successfully invoke the get details by VIN and UVC on multitrim API$")
	public void Get_Detail_by_VIN_and_UVC_multitrim_API() throws Throwable 
	{
		Properties prop = null;
		String getDetailsByVinUVC=null;
		JSONObject jsonObject =null;
		String Final_Header=null;

		try {

			logger.info("Calling get details by VIN API method");
			  
			prop=GetProperties();
			

			
			String Header1="Content-Type,application/json";
			String Header2="Auth-key,037c114b3a5c4838b9ab689e0ad2c59f";

			Final_Header=Header1.concat(";").concat(Header2).concat(";");
			
			DataClass.APIGetCallhmap.put("Header",Final_Header);			
			DataClass.APIGetCallhmap.put("GetURLParamVal","");
		    DataClass.APIGetCallhmap.put("QueryParams","uvc,"+DataClass.hmap.get("UVC"));
			DataClass.APIGetCallhmap.put("GetAPIURL",prop.getProperty("Detail_by_VIN_and_UVC_MultiTrim"));
				
			
			getDetailsByVinUVC=WebServicesImpl_Obj.APICalls("MultiTrim");
			
			jsonObject=ConvertStringToJson(getDetailsByVinUVC); 
			
			//System.out.println(jsonObject);
			logger.info("Get Details by VIN API Service response "+getDetailsByVinUVC);
			
			//CustomFunctions_OBJ.Store_Value(getDetailsByVinUVC,"Response");
			logger.info("Execution of get details by VIN API method completed");

		} catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}

	}
	
	@And("^Get the details from the DataBase$")
	public void Get_Details_from_DataBase() throws Throwable 
	{
		queryParametersWrapper_OBJ=new QueryParametersWrapper();
		DataBaseImplementation DataBaseImplementation_OBJ=new DataBaseImplementation();
		
		try
		{
			logger.info("Entering into the method Get_Details_from_DataBase ");
			
			logger.info("Setting the data for queryParametersWrapper_OBJ");
			queryParametersWrapper_OBJ.setWebServiceName("DSDecisions_Detail_By_VIN");
			queryParametersWrapper_OBJ.setQueryparameter1(DataClass.hmap.get("VIN"));
			
			logger.info("WebService name in the queryParametersWrapper_OBJ "+queryParametersWrapper_OBJ.getWebServiceName());
			logger.info("Queryparameter1 name in the queryParametersWrapper_OBJ "+queryParametersWrapper_OBJ.getQueryparameter1());
			
			queryParametersWrapper_OBJ.setQueryname("DSDecisions_Detail_By_VIN_vehicleInfo");	
			logger.info("Calling the execute SQL Query methods for DSDecisions_Detail_By_VIN_vehicleInfo with Query name as "+queryParametersWrapper_OBJ.getQueryname());
			
			DataBaseImplementation_OBJ.Execute_SQL_Query(queryParametersWrapper_OBJ);
			logger.info("DB Execution Call completed for DSDecisions_Detail_By_VIN_vehicleInfo with Query name as "+queryParametersWrapper_OBJ.getQueryname());
			
			
			queryParametersWrapper_OBJ.setQueryname("DSDecisions_Detail_By_VIN_Features");		
			logger.info("Calling the execute SQL Query methods for DSDecisions_Detail_By_VIN_Features with Query name as "+queryParametersWrapper_OBJ.getQueryname());
			DataBaseImplementation_OBJ.Execute_SQL_Query(queryParametersWrapper_OBJ);
			logger.info("DB Execution Call completed for DSDecisions_Detail_By_VIN_Features with Query name as "+queryParametersWrapper_OBJ.getQueryname());
			
			
		}catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	public JSONObject Get_Vehicle_DetailsBy_VIN_and_UVC_API(String alternatives_UVC) throws Exception 
	{
		Properties prop = null;
		String getDetailsByVin=null;
		String Final_Header=null;
		JSONObject jsonObject_multiTrim =null;
		

		try {

			logger.info("Calling get vehicle details by VIN and UVC API method");
			  
			prop=GetProperties();
			

			String Header1="Content-Type,application/json";
			String Header2="Auth-key,"+prop.getProperty("Detail_By_VIN_AND_UVC_Auth_Key");

			Final_Header=Header1.concat(";").concat(Header2).concat(";");
			
			DataClass.APIGetCallhmap.put("Header",Final_Header);			
			DataClass.APIGetCallhmap.put("GetURLParamVal",DataClass.hmap.get("VIN")+"?uvc="+alternatives_UVC);			
			DataClass.APIGetCallhmap.put("APIURL",prop.getProperty("Detail_By_VIN_AND_UVC_URL"));
			DataClass.APIGetCallhmap.put("GETorPOSTCall","GET");
			DataClass.APIGetCallhmap.put("Get_Vehicles_Details_By_VIN_and_UVC_Service_Response_Time","Multi_Trim_Service_Response_Time");
			
			
			getDetailsByVin=WebServicesImpl_Obj.APICalls("MultiTrim");
			
			jsonObject_multiTrim=ConvertStringToJson(getDetailsByVin); 
			
			//System.out.println(jsonObject);
			logger.info("Get Details by VIN  and UVC  API Service response "+getDetailsByVin);
			
			//DataClass.APIGetCallhmap.put("getDetailsByVin",getDetailsByVin);
			
			//CustomFunctions_OBJ.Store_Value(getDetailsByVin,"Get_Details_By_VIN_Response");
			logger.info("Execution of by VIN  and UVC API method completed");

		} catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		return jsonObject_multiTrim;

	}
	
	public JSONObject Get_DetailsBy_UVC_API() throws Throwable 
	{
		Properties prop = null;
		String getDetailsByVin=null;
		String Final_Header=null;
		JSONObject jsonObject_Get_DetailsBy_UVC_API =null;
		
		
		try {

			logger.info("Calling get details by VIN API method");
			  
			prop=GetProperties();
			

			String Header1="Content-Type,application/json";
			String Header2="Auth-key,"+prop.getProperty("Detail_By_UVC_Auth_Key");

			Final_Header=Header1.concat(";").concat(Header2).concat(";");
			
			DataClass.APIGetCallhmap.put("Header",Final_Header);			
			DataClass.APIGetCallhmap.put("GetURLParamVal",DataClass.hmap.get("UVC"));			
			DataClass.APIGetCallhmap.put("APIURL",prop.getProperty("Detail_By_UVC_URL"));
			DataClass.APIGetCallhmap.put("GETorPOSTCall","GET");
			DataClass.APIGetCallhmap.put("Get_Details_By_VIN_Service_Response_Time","Detail_by_VIN_Service_Response_Time");
			
			
			getDetailsByVin=WebServicesImpl_Obj.APICalls("GetVehicleDetailsByUVC");
			
			jsonObject_Get_DetailsBy_UVC_API=ConvertStringToJson(getDetailsByVin); 
			

			logger.info("Get Details by UVC API Service response "+getDetailsByVin);

			logger.info("Execution of get details by UVC API method completed");

		} catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		return jsonObject_Get_DetailsBy_UVC_API;

	}
	
	public JSONObject Get_ICO_Estimate_by_UVC_API(JSONObject get_DetailsBy_UVC_API_Res) throws Exception 
	{
		Properties prop = null;
		String getICOEstimateByUVC=null;
		String Final_Header=null;
		APIRequests apiRequests_OBJ=new APIRequests();
		JSONObject ICO_Estimate_Response=null;

		//try {

			logger.info("Calling Get_ICO_by_VIN_API method");
			  
			prop=GetProperties();
			
			String Header1="Content-Type,application/json";
			String Header2="AUTH-KEY-ICO,"+prop.getProperty("ICO_ESTIMATE_BY_UVC_URL_Auth_Key");

			Final_Header=Header1.concat(";").concat(Header2).concat(";");
			
			DataClass.APIGetCallhmap.put("Header",Final_Header);			
			DataClass.APIGetCallhmap.put("GetURLParamVal","/estimate");			
			DataClass.APIGetCallhmap.put("APIURL",prop.getProperty("ICO_ESTIMATE_BY_UVC_URL"));
			DataClass.APIGetCallhmap.put("GETorPOSTCall","POST");
			DataClass.APIGetCallhmap.put("ICO_Service_Response_Time","ICO_Estimate_by_UVC_Service_Resp_Time");
			
			apiRequests_OBJ.CreategetICOEstimateByVINAPIRequest(get_DetailsBy_UVC_API_Res);
			
			getICOEstimateByUVC=WebServicesImpl_Obj.APICalls("ICOEstimate");
			
			ICO_Estimate_Response=ConvertStringToJson(getICOEstimateByUVC); 
			
			logger.info("Get IOC Estimate by UVC API Service response "+ICO_Estimate_Response);

			logger.info("Execution of get IOC Estimate by UVC API method completed");

			return ICO_Estimate_Response;

	}
	
	@Then("^Validate the Web Service Response with DataBase data$")
	public void Validate_WebServices_With_DBObject() throws Throwable 
	{
		DataValidation datavalidation_Obj=new DataValidation();
		
		try
		{
			logger.info("Calling the Method : Validate_WebServices_With_DBObject with WebService Name as "+queryParametersWrapper_OBJ.getWebServiceName());	
			
         if(queryParametersWrapper_OBJ.getWebServiceName().toString().equalsIgnoreCase("DSDecisions_Detail_By_VIN"))
         {
        	 datavalidation_Obj.Compare_DSDecisions_Detail_By_VIN(this.jsonObject_Get_DetailsBy_VIN_API,DataBaseImplementation.DSDecisionsWrapper_OBJ,DataBaseImplementation.features_configs);
         }

         logger.info("Execution completed for : Validate_WebServices_With_DBObject with WebService Name as "+queryParametersWrapper_OBJ.getWebServiceName());	
			
			
		}catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public JSONObject ConvertStringToJson(String APIResponseString)
	{
		JSONObject json =null;
		try
		{
			json = new JSONObject(APIResponseString);  
			logger.info(json.toString()); 
			
		}catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
	return json;
	}
	
	public Properties GetProperties()
	{
		
		Properties prop = null;
		try
		{
			prop=CustomFunctions_OBJ.ApplicationProperties();
			
		}catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		return prop;
	}
	
}
