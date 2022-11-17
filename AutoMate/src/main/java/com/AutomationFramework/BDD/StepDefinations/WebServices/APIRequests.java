package com.AutomationFramework.BDD.StepDefinations.WebServices;

import static org.mockito.ArgumentMatchers.nullable;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.AutomationFramework.BDD.Runner.DataClass;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.Pojos.DSDecisionsWrapper;
import com.AutomationFramework.com.Pojos.FeaturesConfig;
import com.Wrapper.ResponseWrapper;

public class APIRequests {

	public static Timestamp ICO_Request_TimeStamp1=null;
	public static String Request=null;

	public void CreategetICOByVINAPIRequest(JSONObject APIjsonObject, DSDecisionsWrapper dSDecisionsWrapper_OBJ, List<FeaturesConfig> features_configs) throws JSONException,Exception 
	{
		ICO_Request_TimeStamp1=new Timestamp(System.currentTimeMillis());
		Request= "Regression_Test_"+ICO_Request_TimeStamp1;
		
		String JSONRequest=null;
		String Color="Red";
		String Mileage="";
		String ZipCode="";
		String Accident="";
		String VehicleCondition="";
		String Car_Smoke="";
		String Warning_Lights="";
		String Keys="";
		String loan_amount="130";
		String customerOutOfMarket="false";
		String trim=null;
		
		//try
		{
			JSONObject vehicleInfo=APIjsonObject.getJSONObject("vehicleInfo");
			
			trim=vehicleInfo.getString("trim");
			
			if(trim.toString().compareTo("null")==0)
			{
				 
				  JSONObject dataProviderInfo_multiTrim=APIjsonObject.getJSONObject("dataProviderInfo");
				  JSONObject blackBook_multiTrim=dataProviderInfo_multiTrim.getJSONObject("blackBook");
				  JSONArray alternativesArray_multiTrim = blackBook_multiTrim.getJSONArray("alternatives");
				  
				  JSONObject alternatives_Json = alternativesArray_multiTrim.getJSONObject(0);
				  trim=alternatives_Json.get("trim").toString();
			}
			JSONObject dataProviderInfo=APIjsonObject.getJSONObject("dataProviderInfo");
			JSONObject blackBook=dataProviderInfo.getJSONObject("blackBook");
			
			JSONArray featuresArray = blackBook.getJSONArray("features");
			
		/*	if(!(DataClass.hmap.get("Color").isEmpty() && DataClass.hmap.get("Color")==null))
			{
				Color=DataClass.hmap.get("Color");
			}*/
			if(!(DataClass.hmap.get("Mileage").isEmpty() && DataClass.hmap.get("Mileage")==null))
			{
				Mileage=DataClass.hmap.get("Mileage").trim();
			}
			if(!(DataClass.hmap.get("ZipCode").isEmpty() && DataClass.hmap.get("ZipCode")==null))
			{
				ZipCode=DataClass.hmap.get("ZipCode").trim();
				
				if(ZipCode.length()<5)
				{
					ZipCode=StringUtils.leftPad(ZipCode, 5, '0');
				}
			}
			
			if(!(DataClass.hmap.get("Accident").isEmpty() && DataClass.hmap.get("Accident")==null))
			{
				if((DataClass.hmap.get("Accident").toString().trim().compareToIgnoreCase("YES")==0)||(DataClass.hmap.get("Accident").toString().trim().compareToIgnoreCase("Y")==0)
						||(DataClass.hmap.get("Accident").toString().trim().compareToIgnoreCase("1")==0)
						||(DataClass.hmap.get("Accident").toString().trim().compareToIgnoreCase("TRUE")==0))
				{
					Accident="true";
				}
				else
				{
					Accident="false";
				}

			}
			if(!(DataClass.hmap.get("VehicleCondition").isEmpty() && DataClass.hmap.get("VehicleCondition")==null))
			{
				if(DataClass.hmap.get("VehicleCondition").toString().trim().compareToIgnoreCase("GOOD")==0)
				{
					VehicleCondition="good";
				}
				else if(DataClass.hmap.get("VehicleCondition").toString().trim().compareToIgnoreCase("FAIR")==0)
				{
					VehicleCondition="fair";
				}
				else if(DataClass.hmap.get("VehicleCondition").toString().trim().compareToIgnoreCase("GREAT")==0)
				{
					VehicleCondition="great";
				}
				else if(DataClass.hmap.get("VehicleCondition").toString().trim().compareToIgnoreCase("POOR")==0)
				{
					VehicleCondition="poor";
				}
				else
				{
					VehicleCondition=DataClass.hmap.get("VehicleCondition").trim().toLowerCase();
				}
			}
			if(!(DataClass.hmap.get("Car_Smoke").isEmpty() && DataClass.hmap.get("Car_Smoke")==null))
			{
				if((DataClass.hmap.get("Car_Smoke").toString().trim().compareToIgnoreCase("YES")==0)||(DataClass.hmap.get("Car_Smoke").toString().trim().compareToIgnoreCase("Y")==0)
						||(DataClass.hmap.get("Car_Smoke").toString().trim().compareToIgnoreCase("1")==0)
						||(DataClass.hmap.get("Car_Smoke").toString().trim().compareToIgnoreCase("TRUE")==0))
				{
					Car_Smoke="true";
				}
				else
				{
					Car_Smoke="false";
				}
			}
			if(!(DataClass.hmap.get("Warning_Lights").isEmpty() && DataClass.hmap.get("Warning_Lights")==null))
			{
				if((DataClass.hmap.get("Warning_Lights").toString().trim().compareToIgnoreCase("YES")==0)||(DataClass.hmap.get("Warning_Lights").toString().trim().compareToIgnoreCase("Y")==0)
						||(DataClass.hmap.get("Warning_Lights").toString().trim().compareToIgnoreCase("1")==0)
						||(DataClass.hmap.get("Warning_Lights").toString().trim().compareToIgnoreCase("TRUE")==0))
				{
					Warning_Lights="true";
				}
				else
				{
					Warning_Lights="false";
				}
			}
			if(!(DataClass.hmap.get("Keys")==null))
			{
				if((DataClass.hmap.get("Keys").toString().trim().compareToIgnoreCase("ONE")==0)||(DataClass.hmap.get("Keys").toString().trim().compareToIgnoreCase("1")==0))
				{
					Keys="one";
				}
				else
				{
					Keys="multiple";
				}
			}
			if(!(DataClass.hmap.get("customerOutOfMarket")==null))
			{
				if((DataClass.hmap.get("customerOutOfMarket").toString().trim().compareToIgnoreCase("TRUE")==0)||(DataClass.hmap.get("customerOutOfMarket").toString().trim().compareToIgnoreCase("1")==0))
				{
					customerOutOfMarket="true";
				}
			}
			if(!(DataClass.hmap.get("features")==null))
			{
				JSONArray jArray = new JSONArray();
				
				
				String features_str = DataClass.hmap.get("features").toString().trim();
		        String[] arrOfStr = features_str.split(",", 10);
		        
		        if(arrOfStr.length==0)
		        {
		        	if(features_str.contains(":"))
			        {
					        	JSONObject Creating_Obj = new JSONObject();
					        	Creating_Obj.put("name", features_str.split(":")[0]);
								Creating_Obj.put("selected",features_str.split(":")[1] );
								jArray.put(Creating_Obj);
			        }
			        else
			        {
					        	JSONObject Creating_Obj = new JSONObject();
					        	Creating_Obj.put("name", features_str);
								Creating_Obj.put("selected",true );
								jArray.put(Creating_Obj);
			        }
		        }
		        else
		        {
				        if(features_str.contains(":"))
				        {
				        	 for(int i=0;i<arrOfStr.length;i++)
						        {
						        	JSONObject Creating_Obj = new JSONObject();
						        	Creating_Obj.put("name", arrOfStr[i].split(":")[0]);
									Creating_Obj.put("selected",arrOfStr[i].split(":")[1] );
									jArray.put(Creating_Obj);
						        }
				        }
				        else
				        {
				        	 for(int i=0;i<arrOfStr.length;i++)
						        {
						        	JSONObject Creating_Obj = new JSONObject();
						        	Creating_Obj.put("name", arrOfStr[i]);
									Creating_Obj.put("selected",true );
									jArray.put(Creating_Obj);
						        }
				        }
		        
		       
		        }
		        featuresArray=jArray;
			}
		/*	if(!(DataClass.hmap.get("loan_amount").isEmpty() && DataClass.hmap.get("loan_amount")==null))
			{
				loan_amount=DataClass.hmap.get("loan_amount");
			}
			if(!(DataClass.hmap.get("customerOutOfMarket").isEmpty() && DataClass.hmap.get("customerOutOfMarket")==null))
			{
				if(DataClass.hmap.get("customerOutOfMarket").toString().compareToIgnoreCase("TRUE")==0)
				{
					customerOutOfMarket="true";
				}
				else if(DataClass.hmap.get("customerOutOfMarket").toString().compareToIgnoreCase("FALSE")==0)
				{
					customerOutOfMarket="false";
				}
				else
				{
					customerOutOfMarket="false";
				}
				
			}*/

			
			if(dSDecisionsWrapper_OBJ.getModel_year()==null)
			{
				JSONRequest="\r\n"
						+ "{\r\n"
						+ "  \"icoRequestMessage\": {\r\n"
						+ "    \"requestId\": \""+APIRequests.Request+"\",\r\n"
						+ "    \"vehicleInfo\": {\r\n"
						+ "        \"vin\": \""+DataClass.hmap.get("VIN")+"\",\r\n"
						+ "        \"year\": "+vehicleInfo.getString("year")+",\r\n"
						+ "        \"make\": \""+vehicleInfo.getString("make")+"\",\r\n"
						+ "        \"model\": \""+vehicleInfo.getString("model")+"\",\r\n"
						+ "        \"trim\": \""+trim+"\",\r\n"
						+ "      \"exteriorColor\": \""+Color+"\",\r\n"
						+ "      \"mileage\": "+Mileage+", \r\n"
						+ "      \"zipCode\": \""+ZipCode+"\"\r\n"
						+ "    },\r\n"
						+ "    \"features\": "+featuresArray+",\r\n"
						+ "    \"conditions\": {\r\n"
						+ "      \"overallCondition\": \""+VehicleCondition+"\",\r\n"
						+ "      \"hadAccident\": "+Accident+",\r\n"
						+ "      \"smokeIn\": "+Car_Smoke+",\r\n"
						+ "      \"warningLights\": "+Warning_Lights+",\r\n"
						+ "      \"keys\": \""+Keys+"\",\r\n"
						+ "      \"remainingLoanLeaseAmount\": "+loan_amount+"\r\n"
						+ "    },\r\n"
						+ "        \"customerOutOfMarket\": "+customerOutOfMarket+"\r\n"
						+ "  }\r\n"
						+ "}\r\n"
						+ "";
			}
			else
			{
			
			JSONRequest="\r\n"
					+ "{\r\n"
					+ "  \"icoRequestMessage\": {\r\n"
					+ "    \"requestId\": \""+APIRequests.Request+"\",\r\n"
					+ "    \"vehicleInfo\": {\r\n"
					+ "        \"vin\": \""+DataClass.hmap.get("VIN")+"\",\r\n"
					+ "        \"year\": "+dSDecisionsWrapper_OBJ.getModel_year()+",\r\n"
					+ "        \"make\": \""+dSDecisionsWrapper_OBJ.getMake()+"\",\r\n"
					+ "        \"model\": \""+dSDecisionsWrapper_OBJ.getModel()+"\",\r\n"
					+ "        \"trim\": \""+dSDecisionsWrapper_OBJ.getTrim()+"\",\r\n"
					+ "      \"exteriorColor\": \""+Color+"\",\r\n"
					+ "      \"mileage\": "+Mileage+", \r\n"
					+ "      \"zipCode\": \""+ZipCode+"\"\r\n"
					+ "    },\r\n"
					+ "    \"features\": "+featuresArray+",\r\n"
					+ "    \"conditions\": {\r\n"
					+ "      \"overallCondition\": \""+VehicleCondition+"\",\r\n"
					+ "      \"hadAccident\": "+Accident+",\r\n"
					+ "      \"smokeIn\": "+Car_Smoke+",\r\n"
					+ "      \"warningLights\": "+Warning_Lights+",\r\n"
					+ "      \"keys\": \""+Keys+"\",\r\n"
					+ "      \"remainingLoanLeaseAmount\": "+loan_amount+"\r\n"
					+ "    },\r\n"
					+ "        \"customerOutOfMarket\": "+customerOutOfMarket+"\r\n"
					+ "  }\r\n"
					+ "}\r\n"
					+ "";
		}
			
			DataClass.APIGetCallhmap.put("ICO_BY_VIN_JSONRequest",JSONRequest);
		}
		/*catch(Exception e)
		{
			ResponseWrapper.setMessage(e.getMessage());
			e.printStackTrace();
		}*/
	}

	public void CreategetICOEstimateByVINAPIRequest(JSONObject APIjsonObject) throws JSONException,Exception 
	{
		ICO_Request_TimeStamp1=new Timestamp(System.currentTimeMillis());
		Request="Regression_Test_"+ICO_Request_TimeStamp1;
		
		
		String JSONRequest=null;
		String Color="Red";
		String Mileage="";
		String ZipCode="";
		String Accident="";
		String VehicleCondition="";
		String Car_Smoke="";
		String Warning_Lights="";
		String Keys="";
		String loan_amount="0";
		String customerOutOfMarket="false";
		

		{
			JSONObject vehicleInfo=APIjsonObject.getJSONObject("vehicleInfo");
			JSONObject dataProviderInfo=APIjsonObject.getJSONObject("dataProviderInfo");
			JSONObject blackBook=dataProviderInfo.getJSONObject("blackBook");
			
			JSONArray featuresArray = blackBook.getJSONArray("features");
			

			if(!(DataClass.hmap.get("Mileage").isEmpty() && DataClass.hmap.get("Mileage")==null))
			{
				Mileage=DataClass.hmap.get("Mileage").trim();
			}
			if(!(DataClass.hmap.get("ZipCode").isEmpty() && DataClass.hmap.get("ZipCode")==null))
			{
				ZipCode=DataClass.hmap.get("ZipCode").trim();
				if(ZipCode.length()<5)
				{
					ZipCode=StringUtils.leftPad(ZipCode, 5, '0');
				}
			}
			
			if(!(DataClass.hmap.get("Accident").isEmpty() && DataClass.hmap.get("Accident")==null))
			{
				if((DataClass.hmap.get("Accident").toString().trim().compareToIgnoreCase("YES")==0)||(DataClass.hmap.get("Accident").toString().trim().compareToIgnoreCase("Y")==0)
						||(DataClass.hmap.get("Accident").toString().trim().compareToIgnoreCase("1")==0)
						||(DataClass.hmap.get("Accident").toString().trim().compareToIgnoreCase("TRUE")==0))
				{
					Accident="true";
				}
				else
				{
					Accident="false";
				}

			}
			if(!(DataClass.hmap.get("VehicleCondition").isEmpty() && DataClass.hmap.get("VehicleCondition")==null))
			{
				if(DataClass.hmap.get("VehicleCondition").toString().trim().compareToIgnoreCase("GOOD")==0)
				{
					VehicleCondition="good";
				}
				else if(DataClass.hmap.get("VehicleCondition").toString().trim().compareToIgnoreCase("FAIR")==0)
				{
					VehicleCondition="fair";
				}
				else if(DataClass.hmap.get("VehicleCondition").toString().trim().compareToIgnoreCase("GREAT")==0)
				{
					VehicleCondition="great";
				}
				else if(DataClass.hmap.get("VehicleCondition").toString().trim().compareToIgnoreCase("POOR")==0)
				{
					VehicleCondition="poor";
				}
				else
				{
					VehicleCondition=DataClass.hmap.get("VehicleCondition").trim().toLowerCase();
				}
			}
			if(!(DataClass.hmap.get("Car_Smoke").isEmpty() && DataClass.hmap.get("Car_Smoke")==null))
			{
				if((DataClass.hmap.get("Car_Smoke").toString().trim().compareToIgnoreCase("YES")==0)||(DataClass.hmap.get("Car_Smoke").toString().trim().compareToIgnoreCase("Y")==0)
						||(DataClass.hmap.get("Car_Smoke").toString().trim().compareToIgnoreCase("1")==0)
						||(DataClass.hmap.get("Car_Smoke").toString().trim().compareToIgnoreCase("TRUE")==0))
				{
					Car_Smoke="true";
				}
				else
				{
					Car_Smoke="false";
				}
			}
			if(!(DataClass.hmap.get("Warning_Lights").isEmpty() && DataClass.hmap.get("Warning_Lights")==null))
			{
				if((DataClass.hmap.get("Warning_Lights").toString().trim().compareToIgnoreCase("YES")==0)||(DataClass.hmap.get("Warning_Lights").toString().trim().compareToIgnoreCase("Y")==0)
						||(DataClass.hmap.get("Warning_Lights").toString().trim().compareToIgnoreCase("1")==0)
						||(DataClass.hmap.get("Warning_Lights").toString().trim().compareToIgnoreCase("TRUE")==0))
				{
					Warning_Lights="true";
				}
				else
				{
					Warning_Lights="false";
				}
			}
			if(!(DataClass.hmap.get("Keys").isEmpty() && DataClass.hmap.get("Keys")==null))
			{
				if((DataClass.hmap.get("Keys").toString().trim().compareToIgnoreCase("ONE")==0)||(DataClass.hmap.get("Keys").toString().trim().compareToIgnoreCase("1")==0))
				{
					Keys="one";
				}
				else
				{
					Keys="multiple";
				}
			}

			if(!(DataClass.hmap.get("customerOutOfMarket")==null))
			{
				if((DataClass.hmap.get("customerOutOfMarket").toString().trim().compareToIgnoreCase("TRUE")==0)||(DataClass.hmap.get("customerOutOfMarket").toString().trim().compareToIgnoreCase("1")==0))
				{
					customerOutOfMarket="true";
				}
			}
			if(!(DataClass.hmap.get("features")==null))
			{
				JSONArray jArray = new JSONArray();
				
				
				String features_str = DataClass.hmap.get("features").toString().trim();
		        String[] arrOfStr = features_str.split(",", 10);
		        
		        if(arrOfStr.length==0)
		        {
		        	if(features_str.contains(":"))
			        {
					        	JSONObject Creating_Obj = new JSONObject();
					        	Creating_Obj.put("name", features_str.split(":")[0]);
								Creating_Obj.put("selected",features_str.split(":")[1] );
								jArray.put(Creating_Obj);
			        }
			        else
			        {
					        	JSONObject Creating_Obj = new JSONObject();
					        	Creating_Obj.put("name", features_str);
								Creating_Obj.put("selected",true );
								jArray.put(Creating_Obj);
			        }
		        }
		        else
		        {
				        if(features_str.contains(":"))
				        {
				        	 for(int i=0;i<arrOfStr.length;i++)
						        {
						        	JSONObject Creating_Obj = new JSONObject();
						        	Creating_Obj.put("name", arrOfStr[i].split(":")[0]);
									Creating_Obj.put("selected",arrOfStr[i].split(":")[1] );
									jArray.put(Creating_Obj);
						        }
				        }
				        else
				        {
				        	 for(int i=0;i<arrOfStr.length;i++)
						        {
						        	JSONObject Creating_Obj = new JSONObject();
						        	Creating_Obj.put("name", arrOfStr[i]);
									Creating_Obj.put("selected",true );
									jArray.put(Creating_Obj);
						        }
				        }
		        
		       
		        }
		        featuresArray=jArray;
			}

			
			{
				JSONRequest="\r\n"
						+ "{\r\n"
						+ "  \"icoRequestMessage\": {\r\n"
						+ "    \"requestId\": \""+APIRequests.Request+"\",\r\n"
						+ "    \"vehicleInfo\": {\r\n"
						+ "        \"uvc\": \""+DataClass.hmap.get("UVC")+"\",\r\n"
						+ "        \"year\": "+vehicleInfo.getString("year")+",\r\n"
						+ "        \"make\": \""+vehicleInfo.getString("make")+"\",\r\n"
						+ "        \"model\": \""+vehicleInfo.getString("model")+"\",\r\n"
						+ "        \"trim\": \""+vehicleInfo.getString("trim")+"\",\r\n"
						+ "      \"exteriorColor\": \""+Color+"\",\r\n"
						+ "      \"mileage\": "+Mileage+", \r\n"
						+ "      \"zipCode\": \""+ZipCode+"\"\r\n"
						+ "    },\r\n"
						+ "    \"features\": "+featuresArray+",\r\n"
						+ "    \"conditions\": {\r\n"
						+ "      \"overallCondition\": \""+VehicleCondition+"\",\r\n"
						+ "      \"hadAccident\": "+Accident+",\r\n"
						+ "      \"smokeIn\": "+Car_Smoke+",\r\n"
						+ "      \"warningLights\": "+Warning_Lights+",\r\n"
						+ "      \"keys\": \""+Keys+"\",\r\n"
						+ "      \"remainingLoanLeaseAmount\": "+loan_amount+"\r\n"
						+ "    },\r\n"
						+ "        \"customerOutOfMarket\": "+customerOutOfMarket+"\r\n"
						+ "  }\r\n"
						+ "}\r\n"
						+ "";
			}

			
			DataClass.APIGetCallhmap.put("ICO_BY_VIN_JSONRequest",JSONRequest);
		}
		/*catch(Exception e)
		{
			ResponseWrapper.setMessage(e.getMessage());
			e.printStackTrace();
		}*/
	}

}
