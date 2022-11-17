package com.AutomationFramework.com.impl;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.AutomationFramework.BDD.Runner.DataClass;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.Wrapper.ResponseWrapper;
import net.snowflake.client.jdbc.internal.apache.http.conn.ssl.NoopHostnameVerifier;
import net.snowflake.client.jdbc.internal.apache.http.conn.ssl.SSLConnectionSocketFactory;
import net.snowflake.client.jdbc.internal.apache.http.impl.client.CloseableHttpClient;
import net.snowflake.client.jdbc.internal.apache.http.impl.client.HttpClients;

public class WebServicesImpl {
	

	
	
	public static final Logger logger = LoggerFactory.getLogger(WebServicesImpl.class);
	CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
	
	

	public String APICalls(String WebServicename)
	{
		int statusCode=0;
		String jsonString=null;
		String URLParameter=null;
		Response response=null;
		
		try
		{
			
			
		    
			    RestAssured.baseURI = DataClass.APIGetCallhmap.get("APIURL");			 
		        RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
		        
		        httpRequest=APIHeadersProcessing(httpRequest);
		        httpRequest=APIBasicAuthProcessing(httpRequest);
		        httpRequest=APIParametersProcessing(httpRequest);
		        
		        if(DataClass.APIGetCallhmap.get("GETorPOSTCall").toString().equalsIgnoreCase("GET"))
		        {
		        	 URLParameter=DataClass.APIGetCallhmap.get("GetURLParamVal");
				        if(URLParameter=="" || URLParameter==null)
				        {
				        	URLParameter="";
				        }
				        else
				        {
				           
				        }
				        
				        response=CallGetAPICall(httpRequest,URLParameter,WebServicename);
		        }
		        else
		        {
		        	httpRequest.body(DataClass.APIGetCallhmap.get("ICO_BY_VIN_JSONRequest"));
		        	URLParameter=DataClass.APIGetCallhmap.get("GetURLParamVal");
		        	 
		        	response=CallPOSTAPICall(httpRequest);
		        }
		        
		        statusCode = response.getStatusCode();
		        
		        DataClass.APIGetCallhmap.put("statusCode",String.valueOf(statusCode));	
		        jsonString = response.asString();

		        
		 

		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		
		return jsonString;
	}
	
	private Response CallGetAPICall(RequestSpecification httpRequest, String uRLParameter,String WebServicename) 
	{
		Response response=null;
		String APIResponseTime=null;
		
		try
		{
			long startTime=System.currentTimeMillis();
			
			response = httpRequest.request(Method.GET, uRLParameter);
			
			long duration=System.currentTimeMillis()-startTime;
			double Seconds=((double)duration)/1000;
			if(duration<1000)
			{
				//APIResponseTime=duration+"ms";
				APIResponseTime=Seconds+"Seconds";
			}
			else
			{
				APIResponseTime=String.format("%.3f", Seconds)+ "Seconds";
			}
			
			if(WebServicename.toString().compareToIgnoreCase("GetVehicleDetailsByVIN")==0)
			{
				DataClass.APIGetCallhmap.put("GetAPIResponseTime",APIResponseTime);	
			}
			else if(WebServicename.toString().compareToIgnoreCase("GetVehicleDetailsByUVC")==0)
			{
				DataClass.APIGetCallhmap.put("GetAPIResponseTime",APIResponseTime);	
			}
			else if(WebServicename.toString().compareToIgnoreCase("MultiTrim")==0)
			{
				DataClass.APIGetCallhmap.put("MultiTrimServiceResponseTime",APIResponseTime);	
			}

			
			//CustomFunctions_OBJ.Store_Value(APIResponseTime,DataClass.APIGetCallhmap.get("Service_Response_Time"));
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		return response;
		
	}
	
	private Response CallPOSTAPICall(RequestSpecification httpRequest) 
	{
		Response response=null;
		String APIResponseTime=null;
		
		try
		{
			long startTime=System.currentTimeMillis();
			
			response = httpRequest.post();
			
			long duration=System.currentTimeMillis()-startTime;
			double Seconds=((double)duration)/1000;
			if(duration<1000)
			{
				//APIResponseTime=duration+"ms";
				APIResponseTime=Seconds+"Seconds";
			}
			else
			{
				APIResponseTime=String.format("%.3f", Seconds)+ "Seconds";
			}
			
			DataClass.APIGetCallhmap.put("POSTAPIResponseTime",APIResponseTime);	
			//CustomFunctions_OBJ.Store_Value(APIResponseTime,DataClass.APIGetCallhmap.get("Service_Response_Time"));
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		return response;
		
	}

	public RequestSpecification APIParametersProcessing(RequestSpecification httpRequest)
	{
		String Parameter=null;
		try
		{

			Parameter= DataClass.APIGetCallhmap.get("QueryParams");
	        
	        if(Parameter==null || Parameter=="")
	        {
	        	;
	        }
	        else
	        {
	        	if(Parameter.contains(";"))
		        {
		        	String[] ParametersData=Parameter.split(";");
		        	
		        	for(int i=0;i<ParametersData.length;i++)
		        	{
		        		String ParamKey=ParametersData[i].split(",")[0];
			        	String ParamVal=ParametersData[i].split(",")[1];
			        	
			        	httpRequest.queryParam(ParamKey, ParamVal);
			        	
		        	}
		        }
		        else
		        {
		        	String ParamKey=Parameter.split(",")[0];
		        	String ParamVal=Parameter.split(",")[1];
		        	
		        	httpRequest.queryParam(ParamKey, ParamVal);
		        }
	        	
	        }

		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		return httpRequest;
	}
	
	
	public RequestSpecification APIBasicAuthProcessing(RequestSpecification httpRequest)
	{
		String AuthData=null;
		try
		{

			AuthData= DataClass.APIGetCallhmap.get("Auth");
	        
	        if(AuthData==null || AuthData=="")
	        {
	        	;
	        }
	        else
	        {
	        
	        	if(AuthData.contains(";"))
		        {
		        	String[] Data=AuthData.split(";");
		        	
		        	for(int i=0;i<Data.length;i++)
		        	{
		        		String AuthKey=Data[i].split(",")[0];
			        	String AuthVal=Data[i].split(",")[1];

			        	httpRequest.auth().preemptive().basic(AuthKey, AuthVal);
			        	
		        	}
		        }
	        	else
	        	{
	        		String AuthKey=AuthData.split(",")[0];
		        	String AuthVal=AuthData.split(",")[1];
		        	
		        	httpRequest.param(AuthKey, AuthVal);
	        	}
	        }
	       

		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		return httpRequest;
	}

	public RequestSpecification APIHeadersProcessing(RequestSpecification httpRequest)
	{
		String HeaderData=null;
		try
		{

	        HeaderData= DataClass.APIGetCallhmap.get("Header");
	        
	        if(HeaderData==null || HeaderData=="")
	        {
	        	HeaderData="Content-Type:application/json";
	        }
	       
	        
	        	if(HeaderData.contains(";"))
		        {
		        	String[] HeadersData=HeaderData.split(";");
		        	
		        	for(int i=0;i<HeadersData.length;i++)
		        	{
		        		String HeaderKey=HeadersData[i].split(",")[0];
			        	String HeaderVal=HeadersData[i].split(",")[1];
			        	
			        	httpRequest.header(HeaderKey, HeaderVal);
			        	
		        	}
		        }
		        else
		        {
		        	String HeaderKey=HeaderData.split(",")[0];
		        	String HeaderVal=HeaderData.split(",")[1];
		        	
		        	httpRequest.header(HeaderKey, HeaderVal);
		        }

		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		return httpRequest;
	}
	
	
	
	
	public void RestAPI()
	{
		String getDetailsByVin=null;
		String Final_Header=null;
		String Header1="Content-Type,application/json";
		String Header2="Auth-key,037c114b3a5c4838b9ab689e0ad2c59f";

		Final_Header=Header1.concat(";").concat(Header2).concat(";");
		
		DataClass.APIGetCallhmap.put("Header",Final_Header);
		
		DataClass.APIGetCallhmap.put("GetURLParamVal","2HGFC2F73JH520208");
		
		DataClass.APIGetCallhmap.put("GetAPIURL","https://azwu2cvpuat-apim.azure-api.net/vehicledetails/vin/");
		
		getDetailsByVin=this.APICalls("Test");
		
		logger.info(getDetailsByVin);
		
		/*****************************Service2************************************/
		
		
        DataClass.APIGetCallhmap.put("GetURLParamVal","");
        
        DataClass.APIGetCallhmap.put("PathParams","uvc,2010380110");
		
		DataClass.APIGetCallhmap.put("GetAPIURL","https://azwu2cvpuat-apim.azure-api.net/vehicledetails/vin/KMHDU4AD4AU955646?");
		
		getDetailsByVin=this.APICalls("Test");
		
		logger.info(getDetailsByVin);
		
		
		
		
	}

}
