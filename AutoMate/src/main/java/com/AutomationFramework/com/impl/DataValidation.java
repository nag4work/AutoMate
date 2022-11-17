package com.AutomationFramework.com.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.AutomationFramework.BDD.Runner.DataClass;
import com.AutomationFramework.BDD.StepDefinations.WebServices.ICORestAPIs;
import com.AutomationFramework.com.Pojos.DSDecisionsWrapper;
import com.AutomationFramework.com.Pojos.FeaturesConfig;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.Wrapper.ResponseWrapper;

public class DataValidation {
	//public static final Logger logger = Logger.getLogger(DataValidation.class);
	public static final Logger logger = LoggerFactory.getLogger(DataValidation.class);
	public static StringBuffer FinalValidation=new StringBuffer();

	public void Compare_DSDecisions_Detail_By_VIN(JSONObject APIjsonObject,DSDecisionsWrapper dSDecisionsWrapper_OBJ, List<FeaturesConfig> features_configs) {
		
		FinalValidation=new StringBuffer();
		int featuresArray_Size=0;
		int DB_Object_Size=0;
		JSONObject features_Json_Create = new JSONObject();
		
		try
		{
			logger.info("Calling the Method : Compare_DSDecisions_Detail_By_VIN");	
			
			JSONObject vehicleInfo=APIjsonObject.getJSONObject("vehicleInfo");
			JSONObject dataProviderInfo=APIjsonObject.getJSONObject("dataProviderInfo");
			JSONObject blackBook=dataProviderInfo.getJSONObject("blackBook");
			
			JSONArray featuresArray = blackBook.getJSONArray("features");

			
			ValidatedData(vehicleInfo.get("vin").toString(),DataClass.hmap.get("VIN"),"vehicleInfo_vin");
			ValidatedData(vehicleInfo.get("year").toString(),dSDecisionsWrapper_OBJ.getModel_year(),"vehicleInfo_year");
			ValidatedData(vehicleInfo.get("make").toString(),dSDecisionsWrapper_OBJ.getMake(),"vehicleInfo_make");
			ValidatedData(vehicleInfo.get("model").toString(),dSDecisionsWrapper_OBJ.getModel(),"vehicleInfo_model");
			ValidatedData(vehicleInfo.get("trim").toString(),dSDecisionsWrapper_OBJ.getTrim(),"vehicleInfo_trim");
			ValidatedData(vehicleInfo.get("fuel").toString(),dSDecisionsWrapper_OBJ.getFuel(),"vehicleInfo_fuel");
			
			ValidatedData(blackBook.get("uvc").toString(),dSDecisionsWrapper_OBJ.getUvc(),"blackBook_uvc");
			ValidatedData(blackBook.get("make").toString(),dSDecisionsWrapper_OBJ.getMake(),"blackBook_make");
			ValidatedData(blackBook.get("model").toString(),dSDecisionsWrapper_OBJ.getModel(),"blackBook_model");
			ValidatedData(blackBook.get("trim").toString(),dSDecisionsWrapper_OBJ.getTrim(),"blackBook_trim");
			
			
			
			featuresArray_Size=featuresArray.length();
			DB_Object_Size=DataBaseImplementation.features_configs.size();
			
			if(featuresArray_Size==0 && DB_Object_Size==0)
			{
				;
			}
			else if(featuresArray_Size!=DB_Object_Size)
			{
				FinalValidation.append("The Size of API features array and  DB feature array are different--> API Features array size : "+ featuresArray_Size +" and DB Features array size :  "+ DB_Object_Size).append("\n");
			}
			else if(featuresArray_Size==0)
			{
				FinalValidation.append("API Returned no features but DB Returned the data for the given VIN Number").append("\n");
			}
			else if(DB_Object_Size==0)
			{
				FinalValidation.append("There is no features data in DB for the provided VIN Number but API Returned the data").append("\n");
			}
			else if(featuresArray_Size==DB_Object_Size)
			{
				
				JSONArray jArray = new JSONArray();
				features_Json_Create = new JSONObject();
				
					for(int i=0;i<DB_Object_Size;i++)
					{
						JSONObject Creating_Obj = new JSONObject();
						String features_DB_Name=null;
						String features_DB_Selected=null;
						boolean features_DB_Selected_Boolean=false;
						String features_API_Name=null;
						String features_API_Selected=null;
						
						JSONObject features_Json = featuresArray.getJSONObject(i);
						
						features_DB_Name=DataBaseImplementation.features_configs.get(i).getName();
						features_DB_Selected=DataBaseImplementation.features_configs.get(i).getSelected();
						
						features_API_Name=features_Json.get("name").toString();
						features_API_Selected=features_Json.get("selected").toString();
						
						if(features_DB_Selected.toString().compareToIgnoreCase("Y")==0)
						{
							features_DB_Selected="true";
							features_DB_Selected_Boolean=true;
						}
						else
						{
							features_DB_Selected="false";
							features_DB_Selected_Boolean=false;
						}
						
						ValidatedData(features_API_Name,features_DB_Name,"Features_name");
						ValidatedData(features_API_Selected,features_DB_Selected,"Feature \""+features_API_Name+"\" Selected");
						
						
						Creating_Obj.put("name", features_DB_Name);
						Creating_Obj.put("selected",features_DB_Selected_Boolean );
						jArray.put(Creating_Obj);
					}
					
					
					features_Json_Create.put("features", jArray);
					
					if(jArray.toString().compareTo(featuresArray.toString())==0)
					{
						;
					}
					else
					{
						FinalValidation.append("After creating DB Object as features object, data not matched with API features Array").append("\n");
						FinalValidation.append("DB Data Array"+jArray).append("\n");
						FinalValidation.append("API Data Array"+featuresArray).append("\n");
					}
					
					
			}
			
			logger.info("Execution Completed for the Method : Compare_DSDecisions_Detail_By_VIN");		
		}
		catch(Exception e)
		{
			logger.info("Exception Occured in the Method : Compare_DSDecisions_Detail_By_VIN");		
			FinalValidation.append("Execption Occured on the validation").append("\n");
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			FinalValidation.append("Validation Completed").append("\n");
			//CustomFunctions_OBJ.Store_Value(FinalValidation.toString(),"Status");
			//FinalValidation=new StringBuffer();
			logger.info("DB Validation with API Response completed for the Service : DSDecisions_Detail_By_VIN");		
		}
	}

	private void ValidatedData(String APIData, String DBData,String FieldName) 
	{
		if(APIData==null)
		{
			FinalValidation.append("API Response Null for the field "+FieldName).append("\n");
		}
		else if(DBData==null)
		{
			FinalValidation.append("DB data Null for the field "+FieldName).append("\n");
		}
		else
		{
			if(APIData.compareTo(DBData)==0)
			{
				;
			}
			else
			{
				FinalValidation.append(FieldName +" field value not matching,  API Response data: "+ APIData +" DB Data: "+ DBData).append("\n");
			}
		}
		
	}

}
