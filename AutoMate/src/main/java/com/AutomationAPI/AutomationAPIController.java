package com.AutomationAPI;

import java.io.File;
import java.util.Properties;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.AutomationFramework.BDD.StepDefinations.DailyMonitoring.APIMonitoringWrapper;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.custom.AutomationNotifications;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.custom.EnvironmentVariables;
import com.Wrapper.APIControllerWrapper;
import com.Wrapper.UIexistingScriptsRequestWrapper;
import com.Wrapper.UINewRequestWrapper;
import com.Wrapper.ResponseWrapper;
import com.Wrapper.UIContactWrapper;

@RestController
@ControllerAdvice
public class AutomationAPIController extends ResponseEntityExceptionHandler {
   
	   @PostMapping("/Automation")
	   @ResponseBody
	   public ResponseEntity<Object> AutomationExecution(@RequestParam("file") MultipartFile file, @RequestParam("request") String JsonRequest)
	  // public ResponseEntity<Object> AutomationExecution(@RequestBody APIControllerWrapper Obj_APIControllerWrapper) 
	   {
		   AutomationMainClass OBJ_AutomationMainClass=new AutomationMainClass();
		   APIControllerWrapper Obj_APIControllerWrapper=new APIControllerWrapper();
		   try {
			   
		   Obj_APIControllerWrapper=ProcessinputRequests(file,JsonRequest);
		  
			   
			   if(Obj_APIControllerWrapper.getTestCase_ID()==null || Obj_APIControllerWrapper.getTestCase_ID().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("testCase_ID Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }
			   else if(Obj_APIControllerWrapper.getSheetName()==null || Obj_APIControllerWrapper.getSheetName().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("SheetName value Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }
			   else if(Obj_APIControllerWrapper.getEnv()==null || Obj_APIControllerWrapper.getEnv().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("Environment value Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }
			 /*  else if(Obj_APIControllerWrapper.getDataSheetPath()==null || Obj_APIControllerWrapper.getDataSheetPath().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("DataSheetPath value Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }*/
			   else if(Obj_APIControllerWrapper.getDataRows()==null || Obj_APIControllerWrapper.getDataRows().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("dataRows value Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }
			   else
			   {
				   try
				   {
					   OBJ_AutomationMainClass.main(Obj_APIControllerWrapper);
					   ResponseWrapper.setMessage(ResponseWrapper.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.OK);
				   }
				   catch(Exception  e)
				   {
					   ResponseWrapper.setMessage(e.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.EXPECTATION_FAILED);
				   }
				   catch(Throwable  e)
				   {
					   ResponseWrapper.setMessage(e.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.EXPECTATION_FAILED);
				   }
				   
			   }
			
		} catch (Exception e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
		   
	    return new ResponseEntity<>(ResponseWrapper.getMessage(), ResponseWrapper.getStatuscode());
	   }
	
	   
	   
	   /*****************************************************************************************************************************/
	   /***********************************Checking All the request parameters******************************************************
	    * Exception will be thrown if any parameter missing in the list As message with missing parameter and bad request
	    * V 1.1/
	    */
	   /*****************************************************************************************************************************/
	   
	   
	   @Override
	   protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) 
	   {
	       String name = ex.getParameterName();
	       logger.error(name + " parameter is missing");

	       return new ResponseEntity<>(name + " parameter is missing", HttpStatus.BAD_REQUEST);
	   }
	   
	   
	 
	   @PostMapping("/Automation/invoke")
	   @ResponseBody
	   public ResponseEntity<Object> AutomationRequestParamAPIExecution(@RequestParam("file") MultipartFile file,
			   															@RequestParam("testCase_ID") String testCase_ID,
			   															@RequestParam("env") String env,
			   															@RequestParam("userID") String userID,
			   															@RequestParam("notification") String notification,
			   															@RequestParam("email_address") String email_address,
			   															@RequestParam("sheetName") String sheetName,
			   															@RequestParam("dataRows") String dataRows)
	     {
		   AutomationMainClass OBJ_AutomationMainClass=new AutomationMainClass();
		   APIControllerWrapper Obj_APIControllerWrapper=new APIControllerWrapper();
		   try {
			   
		   Obj_APIControllerWrapper=ProcessRequestParaminputRequests(file,testCase_ID,env,userID,notification,email_address,sheetName,dataRows);
		  
			   
			   if(Obj_APIControllerWrapper.getTestCase_ID()==null || Obj_APIControllerWrapper.getTestCase_ID().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("testCase_ID Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }
			   else if(Obj_APIControllerWrapper.getSheetName()==null || Obj_APIControllerWrapper.getSheetName().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("SheetName value Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }
			   else if(Obj_APIControllerWrapper.getEnv()==null || Obj_APIControllerWrapper.getEnv().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("Environment value Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }
			 /*  else if(Obj_APIControllerWrapper.getDataSheetPath()==null || Obj_APIControllerWrapper.getDataSheetPath().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("DataSheetPath value Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }*/
			   else if(Obj_APIControllerWrapper.getDataRows()==null || Obj_APIControllerWrapper.getDataRows().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("dataRows value Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }
			   else
			   {
				   try
				   {
					   OBJ_AutomationMainClass.main(Obj_APIControllerWrapper);
					   ResponseWrapper.setMessage(ResponseWrapper.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.OK);
				   }
				   catch(Exception  e)
				   {
					   ResponseWrapper.setMessage(e.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.EXPECTATION_FAILED);
				   }
				   catch(Throwable  e)
				   {
					   ResponseWrapper.setMessage(e.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.EXPECTATION_FAILED);
				   }
				   
			   }
			
		} catch (Exception e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
		   
	    return new ResponseEntity<>(ResponseWrapper.getMessage(), ResponseWrapper.getStatuscode());
	   }
	   
	   
  private APIControllerWrapper ProcessRequestParaminputRequests(MultipartFile file, String testCase_ID, String env,
			String userID, String notification, String email_address, String sheetName, String dataRows) throws Exception {
		
	     APIControllerWrapper Obj_APIControllerWrapper=new APIControllerWrapper();
	     CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		 Properties prop = new Properties();
	  
		  prop=CustomFunctions_OBJ.Load_TestExecutioData_Properties();
		  
		  String destination = EnvironmentVariables.current_dir+File.separator+prop.getProperty("WebServicesExecution");
		  File StoreFile = new File(destination);
		  file.transferTo(StoreFile);
		  
		  Obj_APIControllerWrapper.setDataSheetPath("WebServicesExecution");
		  
	     Obj_APIControllerWrapper.setTestCase_ID(testCase_ID);
	     Obj_APIControllerWrapper.setEnv(env);
	     Obj_APIControllerWrapper.setUserID(userID);
	     Obj_APIControllerWrapper.setNotification(notification);
	     Obj_APIControllerWrapper.setEmail_address(email_address);
	     Obj_APIControllerWrapper.setSheetName(sheetName);
	     Obj_APIControllerWrapper.setDataRows(dataRows);
	     Obj_APIControllerWrapper.setInputintake("Excel");
	     
	     
		return Obj_APIControllerWrapper;
	}


private APIControllerWrapper ProcessinputRequests(MultipartFile file, String jsonRequest) throws Exception
	{
		  APIControllerWrapper Obj_APIControllerWrapper=new APIControllerWrapper();
		  CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		  Properties prop = new Properties();
		  
		  
		  Obj_APIControllerWrapper = new ObjectMapper().readValue(jsonRequest, APIControllerWrapper.class);
	      
		  
		  prop=CustomFunctions_OBJ.Load_TestExecutioData_Properties();
		  
		  String destination = EnvironmentVariables.current_dir+File.separator+prop.getProperty("WebServicesExecution");
		  File StoreFile = new File(destination);
		  file.transferTo(StoreFile);
		  
		  Obj_APIControllerWrapper.setDataSheetPath("WebServicesExecution");
		
		return Obj_APIControllerWrapper;
	}


	@GetMapping("/Greeting")
       public String getName()
       {
		   System.out.println("greeting");
           return "Welcome to Automation Page";
       }
	
	
	/*************************************************************************************************************************************************************************/
	/********************************************************** Calling the Automation Framework with DB Request**************************************************************/
	/*************************************************************************************************************************************************************************/
	
	
	  @PostMapping("/Automation/execution/databaseinput")
	   @ResponseBody
	   public ResponseEntity<Object> AutomationExecutionUsing_DB(@RequestBody APIControllerWrapper Obj_APIControllerWrapper)
	   {
		   AutomationMainClass OBJ_AutomationMainClass=new AutomationMainClass();
		   try {
			   
		    
			   if(Obj_APIControllerWrapper.getTestCase_ID()==null || Obj_APIControllerWrapper.getTestCase_ID().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("testCase_ID Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }
			   else if(Obj_APIControllerWrapper.getTablename()==null || Obj_APIControllerWrapper.getTablename().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("SheetName value Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }
			   else if(Obj_APIControllerWrapper.getEnv()==null || Obj_APIControllerWrapper.getEnv().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("Environment value Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }
			   else if(Obj_APIControllerWrapper.getDataRows()==null || Obj_APIControllerWrapper.getDataRows().toString().compareTo("")==0)
			   {
				   ResponseWrapper.setMessage("dataRows value Missing in the Request");
				   ResponseWrapper.setStatuscode(HttpStatus.BAD_REQUEST);
			   }
			   else
			   {
				   try
				   {
					   Obj_APIControllerWrapper.setInputintake("DB");
					   OBJ_AutomationMainClass.main(Obj_APIControllerWrapper);
					   ResponseWrapper.setMessage(ResponseWrapper.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.OK);
					   logger.info("***************************************Execution Completed*************************************");
				   }
				   catch(Exception  e)
				   {
					   e.printStackTrace();
					   ResponseWrapper.setMessage(e.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.EXPECTATION_FAILED);
				   }
				   catch(Throwable  e)
				   {
					   ResponseWrapper.setMessage(e.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.EXPECTATION_FAILED);
				   }
				   
			   }
			
		} catch (Exception e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
		   
	    return new ResponseEntity<>(ResponseWrapper.getMessage(), ResponseWrapper.getStatuscode());
	   }
	  
	  
	  
	  //@PostMapping("/Automation/UI")
	  @RequestMapping(value = "/Automation/UI" ,method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
	  @CrossOrigin(origins = "http://localhost:3000")
	  @ResponseBody
	   public ResponseEntity<Object> AutomationExecution_UI(@RequestBody String jsonRequest )
	   {
		   AutomationMainClass OBJ_AutomationMainClass=new AutomationMainClass();
		   UINewRequestWrapper Obj_UIControllerWrapper=new UINewRequestWrapper();
		   		try {
			   
		   			 
		   			Obj_UIControllerWrapper = new ObjectMapper().readValue(jsonRequest, UINewRequestWrapper.class);
					   OBJ_AutomationMainClass.UI_main(Obj_UIControllerWrapper);
					   ResponseWrapper.setMessage(ResponseWrapper.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.OK);
					   logger.info("***************************************Execution Completed*************************************");
				   }
				   catch(Exception  e)
				   {
					   e.printStackTrace();
					   ResponseWrapper.setMessage(e.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.EXPECTATION_FAILED);
				   } catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		   
	    return new ResponseEntity<>(ResponseWrapper.getMessage(), ResponseWrapper.getStatuscode());
	   }
	  

	//@PostMapping("/Automation/UI")
	  @RequestMapping(value = "/Automation/UI/runscripts" ,method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
	  @CrossOrigin(origins = "http://localhost:3000")
	  @ResponseBody
	   public ResponseEntity<Object> AutomationExecution_UI_runscripts(@RequestBody String jsonRequest )
	   {
		   AutomationMainClass OBJ_AutomationMainClass=new AutomationMainClass();
		   UIexistingScriptsRequestWrapper Obj_APIUIControllerWrapper=new UIexistingScriptsRequestWrapper();
		   APIControllerWrapper Obj_APIControllerWrapper=new APIControllerWrapper();
		   		try {
			   
		   			 
		   			Obj_APIUIControllerWrapper = new ObjectMapper().readValue(jsonRequest, UIexistingScriptsRequestWrapper.class);
		   			
		   			Obj_APIControllerWrapper.setTestCase_ID(Obj_APIUIControllerWrapper.getTestCase_ID());
		   			Obj_APIControllerWrapper.setEnv(Obj_APIUIControllerWrapper.getEnv());
		   			Obj_APIControllerWrapper.setUserID("UI Request");
		   			Obj_APIControllerWrapper.setNotification("Y");
		   			Obj_APIControllerWrapper.setEmail_address(Obj_APIUIControllerWrapper.getEmail_id());
		   			Obj_APIControllerWrapper.setTablename("DSDecisionsServicesInput");
		   		    
		   			if(Obj_APIUIControllerWrapper.getDbrowsEnd()!=null&&Obj_APIUIControllerWrapper.getDbrowsEnd()!="")
		   			{
		   				Obj_APIControllerWrapper.setDataRows(Obj_APIUIControllerWrapper.getDbrowsStart()+";"+Obj_APIUIControllerWrapper.getDbrowsStart());
		   			}
		   			else {
		   				Obj_APIControllerWrapper.setDataRows(Obj_APIUIControllerWrapper.getDbrowsStart());
					}
		   			
		   			
		   			
						   try
						   {
							   Obj_APIControllerWrapper.setInputintake("DB");
							   OBJ_AutomationMainClass.main(Obj_APIControllerWrapper);
							   ResponseWrapper.setMessage(ResponseWrapper.getMessage());
							   ResponseWrapper.setStatuscode(HttpStatus.OK);
							   logger.info("***************************************Execution Completed*************************************");
						   }
						   catch(Exception  e)
						   {
							   e.printStackTrace();
							   ResponseWrapper.setMessage(e.getMessage());
							   ResponseWrapper.setStatuscode(HttpStatus.EXPECTATION_FAILED);
						   }
						   catch(Throwable  e)
						   {
							   ResponseWrapper.setMessage(e.getMessage());
							   ResponseWrapper.setStatuscode(HttpStatus.EXPECTATION_FAILED);
						   }
						   
					   
				   }
				   catch(Exception  e)
				   {
					   e.printStackTrace();
					   ResponseWrapper.setMessage(e.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.EXPECTATION_FAILED);
				   } catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		   
	    return new ResponseEntity<>(ResponseWrapper.getMessage(), ResponseWrapper.getStatuscode());
	   }
	  
	
	  @RequestMapping(value = "/Automation/UI/Contact" ,method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
	  @CrossOrigin(origins = "http://localhost:3000")
	  @ResponseBody
	   public ResponseEntity<Object> AutomationExecution_UI_Contact(@RequestBody String jsonRequest )
	   {
		   
		   UIContactWrapper Obj_UIContactWrapper=new UIContactWrapper();
		   AutomationNotifications OBJ_AutomationNotifications=new AutomationNotifications();
		   
		   		try {
			   
		   			 
		   			Obj_UIContactWrapper = new ObjectMapper().readValue(jsonRequest, UIContactWrapper.class);
		   			AutomationMainClass.Email_Address="test";
		   			String Message_Body="<br>Hi All, <br> The below Automation request received from protal. Please find the details.<br></br>"
							+ "<br></br><b><u> Sender Name: </u></b>"+Obj_UIContactWrapper.getName()
							+ "<br><b><u> Sender Phone: </u></b>"+Obj_UIContactWrapper.getPhoneNumber()
							+ "<br><b><u> Sender E-mail: </u></b>"+Obj_UIContactWrapper.getEmail_id()
							
							+ "<br></br><b><u> Message:</u></b><br></br>"
						    + "\t\t"+Obj_UIContactWrapper.getMessage()
							
							+ "<br><br></br> This is auto generated email. Please don't reply to this email."
							+ "<br></br>Thanks,"
							+ "<br>Midas Automation Group.<br></br>";
		   			
		   			
		   			OBJ_AutomationNotifications.send_email_RestAPI("","","Automation Request",Message_Body);	
		   			
		   			AutomationMainClass.Email_Address=Obj_UIContactWrapper.getEmail_id();
		   			
		   			String Message_Body_sender="<br>Hi "+Obj_UIContactWrapper.getName()+", <br> we have recevied your request and our team will contact with the details shortly..<br></br>"
							
							
							+ "<br><br></br> This is auto generated email. Please don't reply to this email."
							+ "<br></br>Thanks,"
							+ "<br>Midas Automation Group.<br></br>";
		   			
		   			
		   			OBJ_AutomationNotifications.send_email_RestAPI("","","Automation Request",Message_Body_sender);	
						  
							   
							   ResponseWrapper.setMessage(ResponseWrapper.getMessage());
							   ResponseWrapper.setStatuscode(HttpStatus.OK);
							   logger.info("***************************************Execution Completed*************************************");
						  
						   
					   
				   }
				   catch(Exception  e)
				   {
					   e.printStackTrace();
					   ResponseWrapper.setMessage(e.getMessage());
					   ResponseWrapper.setStatuscode(HttpStatus.EXPECTATION_FAILED);
				   } catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		   
	    return new ResponseEntity<>(ResponseWrapper.getMessage(), ResponseWrapper.getStatuscode());
	   }
	  
	
	
	
}

