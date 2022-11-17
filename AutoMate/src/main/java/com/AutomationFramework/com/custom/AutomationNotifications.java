package com.AutomationFramework.com.custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctc.wstx.shaded.msv_core.util.StringPair;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.AutomationFramework.BDD.Runner.DataClass;
import com.AutomationFramework.BDD.StepDefinations.DailyMonitoring.APIMonitoringWrapper;
import com.AutomationFramework.BDD.StepDefinations.DailyMonitoring.MonitoringWrapper;
import com.AutomationFramework.BDD.StepDefinations.Mdigital.MdigitalWrapper;
import com.AutomationFramework.BDD.StepDefinations.WebServices.CacheServices;
import com.AutomationFramework.BDD.StepDefinations.WebServices.ICORestAPIs;
import com.AutomationFramework.BDD.StepDefinations.WebServices.WebServiceExceptionStatus;
import com.AutomationFramework.com.Main.AutomationMainClass;

public class AutomationNotifications {
	public static final Logger logger = LoggerFactory.getLogger(AutomationNotifications.class);
	public static boolean Logic_Azure_App_new=false;
	public static boolean email_flag=true;
	
	public void email_notification(String CSVFileName,String DataInputSheet)
	{
		if(email_flag==true)
		{
			
		
		try {
			String Subject=null;
			String MessageBody=null;
			
			if(AutomationMainClass.UserName!=null)
			{
				MessageBody="Hi "+AutomationMainClass.UserName+",";
			}
			else
			{
				MessageBody="Hi All,";
			}
			
			if(AutomationMainClass.email_mode.toString().compareTo("Daily Monitoring")==0)
			{
				 String CurrentDate="";
				 /*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
				 LocalDateTime now = LocalDateTime.now();
				 CurrentDate=dtf.format(now);*/
				 
				 Date date = new Date();
				 DateFormat pstFormat = new SimpleDateFormat("MM-dd-yyyy");
				 TimeZone pstTime = TimeZone.getTimeZone("PST");
				 pstFormat.setTimeZone(pstTime);
				 CurrentDate=pstFormat.format(date);
				 
				Subject=MonitoringWrapper.Subject+AutomationMainClass.environment+" Daily Monitoring status "+CurrentDate;
				
				
				
				 MessageBody=MessageBody.concat(MonitoringWrapper.Message_Body);
			}
			else if(AutomationMainClass.email_mode.toString().compareTo("API Monitoring")==0)
			{
				String CurrentDateHour="";
				 
				 Date date = new Date();
				 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00.000");
				 TimeZone pstTime = TimeZone.getTimeZone("PST");
				 pstFormat.setTimeZone(pstTime);
				 CurrentDateHour=pstFormat.format(date);
				 
				 Subject=APIMonitoringWrapper.Subject+AutomationMainClass.environment+" API Monitoring status "+CurrentDateHour;
				 MessageBody=MessageBody.concat(APIMonitoringWrapper.Message_Body);
			}
			else if(AutomationMainClass.email_mode.toString().compareTo("Val_WebServices_exception_Status")==0)
			{
				 String CurrentDate="";
				 
				 Date date = new Date();
				 DateFormat pstFormat = new SimpleDateFormat("MM-dd-yyyy");
				 TimeZone pstTime = TimeZone.getTimeZone("PST");
				 pstFormat.setTimeZone(pstTime);
				 CurrentDate=pstFormat.format(date);
				 
				Subject=AutomationMainClass.environment+" Automation execution for the 503 records status "+CurrentDate;
				
				
				
				 MessageBody=MessageBody.concat(WebServiceExceptionStatus.Message_Body);
			}
			else if(AutomationMainClass.email_mode.toString().compareTo("Cache Services Monitoring")==0)
			{
				 String CurrentDateHour="";
				 Date date = new Date();
				 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:00:00.000");
				 TimeZone pstTime = TimeZone.getTimeZone("PST");
				 pstFormat.setTimeZone(pstTime);
				 CurrentDateHour=pstFormat.format(date);
				 
				 Subject=CacheServices.Subject+AutomationMainClass.environment+" Cache Services Monitoring status "+CurrentDateHour;
				
				
				
				 MessageBody=MessageBody.concat(CacheServices.Message_Body);
			}
			else if(AutomationMainClass.email_mode.toString().compareTo("Max Digital Hourly Monitoring")==0)
			{
				System.out.println("Email Notification for Max Digital Hourly Monitoring");
				String CurrentDateHour="";
				 
				 Date date = new Date();
				 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00.000");
				 TimeZone pstTime = TimeZone.getTimeZone("PST");
				 pstFormat.setTimeZone(pstTime);
				 CurrentDateHour=pstFormat.format(date);
				 
				 Subject=MdigitalWrapper.Subject+AutomationMainClass.environment+" MaxDigital Monitoring status "+CurrentDateHour;
				 MessageBody=MessageBody.concat(MdigitalWrapper.Hourly_monitoring_Message_Body);
			}
			else if(AutomationMainClass.email_mode.toString().compareTo("Post Processing Advisory Alerts")==0)
			{
				System.out.println("Email Notification for post processing Alerts");
				String CurrentDateHour="";
				 
				 Date date = new Date();
				 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd");
				 TimeZone pstTime = TimeZone.getTimeZone("PST");
				 pstFormat.setTimeZone(pstTime);
				 CurrentDateHour=pstFormat.format(date);
				 
				 Subject=MdigitalWrapper.Subject+CurrentDateHour;
				 //MessageBody=MessageBody.concat(MaxdigitalWrapper.Post_Processing_Alerts_Message_Body);
				 
				 MessageBody=MdigitalWrapper.Post_Processing_Alerts_Message_Body;
			}
			else if(AutomationMainClass.email_mode.toString().compareTo("No Price Vehicles Monitoring")==0)
			{
				String CurrentDateHour="";
				 
				 Date date = new Date();
				 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00.000");
				 TimeZone pstTime = TimeZone.getTimeZone("PST");
				 pstFormat.setTimeZone(pstTime);
				 CurrentDateHour=pstFormat.format(date);
				 
				 Subject=APIMonitoringWrapper.NoPrice_Subject+AutomationMainClass.environment+" No Price Vehicles validation status "+CurrentDateHour;
				 MessageBody=MessageBody.concat(APIMonitoringWrapper.NoPrice_Message_Body);
			}
			else if(AutomationMainClass.email_mode.toString().compareTo("Missing VINS Monitoring")==0)
			{
				String CurrentDateHour="";
				 
				 Date date = new Date();
				 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00.000");
				 TimeZone pstTime = TimeZone.getTimeZone("PST");
				 pstFormat.setTimeZone(pstTime);
				 CurrentDateHour=pstFormat.format(date);
				 
				 Subject=APIMonitoringWrapper.Missing_VINs_Subject+AutomationMainClass.environment+" Missing VINS validation status "+CurrentDateHour;
				 MessageBody=MessageBody.concat(APIMonitoringWrapper.Missing_VINs_Message_Body);
			}
			else if(AutomationMainClass.email_mode.toString().compareTo("EDI Error Logs")==0)
			{
				String CurrentDateHour="";
				 
				 Date date = new Date();
				 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00.000");
				 TimeZone pstTime = TimeZone.getTimeZone("PST");
				 pstFormat.setTimeZone(pstTime);
				 CurrentDateHour=pstFormat.format(date);
				 
				 Subject=APIMonitoringWrapper.Subject+AutomationMainClass.environment+" EDI error log monitoring status "+CurrentDateHour;
				 MessageBody=MessageBody.concat(APIMonitoringWrapper.Message_Body);
			}
			else if(AutomationMainClass.email_mode.toString().compareTo("Max Digital failure Warning Summaries")==0)
			{
				String CurrentDateHour="";
				 
				 Date date = new Date();
				 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00.000");
				 TimeZone pstTime = TimeZone.getTimeZone("PST");
				 pstFormat.setTimeZone(pstTime);
				 CurrentDateHour=pstFormat.format(date);
				 
				 Subject="Monthly MAXDIGITAL_MASTER pipe load failure warning summaries "+MdigitalWrapper.CurrentMonth_Year;
				 MessageBody=MdigitalWrapper.Max_Digital_failure_Warning_Summaries;
				 
				 Logic_Azure_App_new=true;
			}
			else
			{
			 Subject="EDS_Automation_Execution";
			 
			 MessageBody=MessageBody.concat("<br>Automation execution for the given request completed. Please find the updated results in the DB and run the below query to get the current request results.<br></br>"
						+ "<br></br> select * from ICOResults where DM_Created_DateTime='"+AutomationMainClass.TimeStamp+"';"
						+ "<br><br></br> This is auto generated email. Please don't reply to this email."
						+ "<br></br>Thanks,"
						+ "<br>EDS Automation Group.<br></br>");
			}
			
			

			
			
			
			
			
			if(Logic_Azure_App_new==true)
			{
				send_email_RestAPI_new_Logic_App(MdigitalWrapper.fileName,DataInputSheet,Subject,MessageBody);
			}
			else {
				send_email_RestAPI(CSVFileName,DataInputSheet,Subject,MessageBody);	
			}

			
			//send_email(CSVFileName,DataInputSheet,Smtp_Server,FromAddress,Subject,MessageBody);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		}
	}
	
	private void send_email_RestAPI_new_Logic_App(String attachment_name, String att, String subject,String messageBody) {

		int statusCode=0;
		Response response=null;
		Properties prop = null;
		CustomFunctions obj_CustomFunctions=new CustomFunctions();
		//Logging log=new Logging();
		try
		{

			
			 //  prop=log.readPropertiesFile(EnvironmentVariables.Application_Common_Properties);
			  prop=obj_CustomFunctions.ApplicationCommonProperties();
			    
			   RestAssured.baseURI = prop.getProperty("SMTP_attach_email_Address");			 
		       RequestSpecification httpRequest = RestAssured.given();	
		        
		     
		       httpRequest.header("Content-Type", "application/json");
		       httpRequest.header("Accept", "*/*");
		       
		       httpRequest.queryParam("api-version", prop.getProperty("SMTP_attach_email_API_Version"));
		       httpRequest.queryParam("sp", prop.getProperty("SMTP_attach_email_API_sp"));
		       httpRequest.queryParam("sv", prop.getProperty("SMTP_attach_email_API_sv"));
		       httpRequest.queryParam("sig", prop.getProperty("SMTP_attach_email_API_sig"));
		      
		       
		        String JSONRequest=null;
		        JSONRequest="{\"EmailTo\":\""+AutomationMainClass.Email_Address+"\",\r\n"
		        			+ "\"DataFactoryName\":\"\",\r\n"
			        		+ "\"PipelineName\":\"\",\r\n"
			        		+ "\"AttachmentName\":\""+attachment_name+"\",\r\n"
			        		+ "\"Subject\":\""+subject+"\",\r\n"
			        		+ "\"Body\":\""+messageBody+"\",\r\n"
			        		+ "\"ErrorMessage\":\"\"\r\n"
			        		+ "}";
		       // httpRequest.body(JSONRequest);
		        
		        httpRequest.body(JSONRequest);
		        response = httpRequest.post();
		        
		        statusCode = response.getStatusCode();
		        
		        if(statusCode==202)
		        {
		        	logger.info("Email Sent Successfully");
		        }
		        else
		        {
		        	logger.info("statusCode--->"+response.then().log().all().extract().response());
		        }

		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}


	
	
		
	}

	public void send_email_RestAPI(String CSVFileName,String DataInputSheet,String Subject,String MessageBody)
	{
		int statusCode=0;
		Response response=null;
		Properties prop = null;
		CustomFunctions obj_CustomFunctions=new CustomFunctions();
		//Logging log=new Logging();
		try
		{

			
			 //  prop=log.readPropertiesFile(EnvironmentVariables.Application_Common_Properties);
			  prop=obj_CustomFunctions.ApplicationCommonProperties();
			    
			   RestAssured.baseURI = prop.getProperty("SMTP_email_Address");			 
		       RequestSpecification httpRequest = RestAssured.given();	
		        
		     
		       httpRequest.header("Content-Type", "application/json");
		       httpRequest.header("Accept", "*/*");
		       
		       httpRequest.queryParam("api-version", prop.getProperty("SMTP_email_API_Version"));
		       httpRequest.queryParam("sp", prop.getProperty("SMTP_email_API_sp"));
		       httpRequest.queryParam("sv", prop.getProperty("SMTP_email_API_sv"));
		       httpRequest.queryParam("sig", prop.getProperty("SMTP_email_API_sig"));
		      
		       
		        String JSONRequest=null;
		        JSONRequest="{\"emailid\":\""+AutomationMainClass.Email_Address+"\",\r\n"
		        			+ "\"DataFactoryName\":\"\",\r\n"
			        		+ "\"PipelineName\":\"\",\r\n"
			        		+ "\"MSGTyp\":\"Normal\",\r\n"
			        		+ "\"EmailSub\":\""+Subject+"\",\r\n"
			        		+ "\"EmailBdy\":\""+MessageBody+"\",\r\n"
			        		+ "\"HasAttachment\":\"\"\r\n"
			        		+ "}";
		       // httpRequest.body(JSONRequest);
		        
		        httpRequest.body(JSONRequest);
		        response = httpRequest.post();
		        
		        statusCode = response.getStatusCode();
		        
		        if(statusCode==202)
		        {
		        	logger.info("Email Sent Successfully");
		        }
		        else
		        {
		        	logger.info("statusCode--->"+response.then().log().all().extract().response());
		        }

		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}


	
	}
	
	
	public static void main(String[] args)
	{
		send_email("","","","Automation","Test","Hi");
	}
	
	public static void send_email(String CSVFileName,String DataInputSheet,String Smtp_Server,String FromAddress,String Subject,String MessageBody)
	{
		try {
			boolean debug=false;
			int noOfRecipients=0;
		    String[] arrOfemailLst = "".split(";", 10);
		    noOfRecipients=arrOfemailLst.length;
		    
		    for(int i=0;i<noOfRecipients;i++)
		    {
		    	try
		    	{

		    		/*****************************Set the Host SMTP Address*************************************************************/
		    		Properties props=new Properties();
		    		props.put("mail.smtp.host", "smtp-alerts.com");//68.105.28.11
		    		//props.put("mail.smtp.host", "68.105.28.11");
		    		props.put("mail.smtp.port","21");
		    		props.put("mail.smtp.starttls.enable", "true");
		    		props.put("mail.debug", "true");
		    		props.put("mail.smtp.ssl.enable", "false");
		    		//props.put("mail.smtp.auth", "true");
		    		

		    		/*****************************Create properties and get the default Session***************************************/
		    		Session session=Session.getDefaultInstance(props,null);
		    		session.setDebug(debug);
		    		

		    		
		    		/*****************************Create Message*********************************************************************/    		
		    		Message msg=new MimeMessage(session);
		    		
		    		
		    		/*****************************Set the FROM and TO ADDRESS********************************************************/
		    		InternetAddress addressFrom=new InternetAddress(FromAddress);
		    		msg.setFrom(addressFrom);
		    		
		    		InternetAddress[] addressTo=new InternetAddress[noOfRecipients];
		    		for(int email_count=0;email_count<noOfRecipients;email_count++)
		    		{
		    			addressTo[email_count]=new InternetAddress(arrOfemailLst[email_count]);
		    		}
		    		msg.setRecipients(Message.RecipientType.TO, addressTo);
		    		
		    		
		    		
		    		msg.setSubject(Subject);
		    		
		    		MailcapCommandMap mc=(MailcapCommandMap)CommandMap.getDefaultCommandMap();
		    		
		    		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		    		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		    		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		    		mc.addMailcap("multippart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		    		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		    		
		    		CommandMap.setDefaultCommandMap(mc);
		    		
		    		MimeBodyPart mbp1=new MimeBodyPart();
		    		mbp1.setContent(MessageBody,"text/html");
		    		mbp1.setHeader("MIME-Version", "1.0");
		    		
		    		FileDataSource fds1=null;
		    		MimeBodyPart mbp2=new MimeBodyPart();
		    		fds1=new FileDataSource(DataInputSheet);
		    		mbp2.setDataHandler(new DataHandler(fds1));
		    		mbp2.setFileName(fds1.getName());
		    		
		    		FileDataSource fds2=null;
		    		MimeBodyPart mbp3=new MimeBodyPart();
		    		fds2=new FileDataSource(CSVFileName);
		    		mbp3.setDataHandler(new DataHandler(fds2));
		    		mbp3.setFileName(fds2.getName());
		    		
		    		Multipart mp=new MimeMultipart();
		    		mp.addBodyPart(mbp1);
		    		mp.addBodyPart(mbp2);
		    		mp.addBodyPart(mbp3);
		    		
		    		
		    		msg.setContent(mp);
		    		
		    		Transport.send(msg);
		    		
		    		System.out.println("Email Sent Successfully");
		    		
		    		
		    	}
		    	catch(Exception e)
				{
					e.printStackTrace();
				}
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
