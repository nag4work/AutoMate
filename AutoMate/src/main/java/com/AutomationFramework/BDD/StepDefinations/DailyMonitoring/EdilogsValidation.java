package com.AutomationFramework.BDD.StepDefinations.DailyMonitoring;

import static org.mockito.ArgumentMatchers.nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import cucumber.api.java.en.Given;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.impl.DataBaseConnections;
import com.AutomationFramework.com.impl.SnowFlakeDataBaseConnection;
import com.Wrapper.ResponseWrapper;

public class EdilogsValidation {
	
	static int count=0;
	static boolean error_record_flag=false;
	static boolean ignore_error_record_flag=false;
	static int ignore_error_record_count=0;
	
	@Given("^Get error status and data from edi error logs$")
	public void Get_error_status_and_data_edi_errorlogs() throws Throwable 
	{
		String past_one_hour_UTCTime=null;
        String email_html="";
        System.out.println("Control at Get_error_status_and_data_edi_errorlogs Call");

		try {
			AutomationMainClass.email_mode="EDI Error Logs";
			past_one_hour_UTCTime=getUTCTimeZone();

			email_html=get_edi_error_logs_data(past_one_hour_UTCTime);
			
			
			  if(count==0)
		         {  	
		        	 APIMonitoringWrapper.Subject="info : ";
		        	 
		        	 APIMonitoringWrapper.Message_Body="<br><br><font size=3><font color='red'>No Records reported from past one hour on EDI error logs.</font> <br></br>"
		
								+ "<br></br>Thanks,"
								+ "<br>Automation Group.<br></br>";
		         }
			  else {
				
			
			  
			  
				  if(error_record_flag==true)
				  {
					  APIMonitoringWrapper.Subject="Urgent:";
			        	 
			        	 APIMonitoringWrapper.Message_Body="<font size=3> Please find the reported error data from past one hour on EDI error logs.<br></br>"
			        			 + "<br></br><b><u> Error Records:</u></b><br></br>"
								    + "\t\t"+email_html.toString()
			        				
									+ "<br></br>Thanks,"
									+ "<br>Automation Group.<br></br>";
				  }
		         else
		         {
		        	 APIMonitoringWrapper.Subject="All Clear:";
		        	 
		        	 if(ignore_error_record_flag==false)
		        	 {
		        		 APIMonitoringWrapper.Message_Body="<font size=3>All the Records reported from past one hour on EDI error logs are Success Records. Total Record count "+count+"<br></br>"
			        				
								+ "<br></br>Thanks,"
								+ "<br>Automation Group.<br></br>";
		        	 }
		        	 else {
		        		 APIMonitoringWrapper.Message_Body="<font size=3>All the Records reported from past one hour on EDI error logs are "+(count-ignore_error_record_count)+" Success records and "+ignore_error_record_count+" error records (Data at the root level is invalid. Line 1, position 1).<br></br>"
			        				
								+ "<br></br>Thanks,"
								+ "<br>Automation Group.<br></br>";
					}
		        	 
		        	 
		         }
			  }
			 
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	private String get_edi_error_logs_data(String past_one_hour_UTCTime) {

		CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		DataBaseConnections DataBaseConnections_OBJ=new DataBaseConnections();
		Statement stmt=null;
		String Query=null;
		ResultSet Res=null;
	    String email_html="";
        Properties Properties = null;
     
        
		try
		{
				Properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
				DataBaseConnections_OBJ.Logging_SQLServerCVPConnection("EDIERRORLOG");
				stmt = DataBaseConnections.SQL_CVP_db_connection.createStatement();
	        
			    Query=Properties.getProperty("get_data_edi_error_logs");
			    Query=Query.replaceFirst("\\?", past_one_hour_UTCTime);
		        Res=stmt.executeQuery(Query);
		        
		        StringBuilder buf = new StringBuilder();
				buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=2 cellspacing=0>" +"<tr>" +"<th>vin</th>" +"<th>processed_date</th>"+"<th>output_type</th>"+"<th>output_desc</th>"+"</tr>");
					
		        
				while(Res.next())
		 	        {
					count=count+1;
					String output_type=Res.getString(3);
					String output_desc=Res.getString(4);
					
                       if(output_type.toString().compareToIgnoreCase("Error")==0)
                       {
                    	   if(!(output_desc.toString().contains("The best overloaded method match for 'ReadVendorEmails.ErrorObject.LogError(string, string, string, string, string, string)' has some invalid arguments")))
                    	   {
                    		   error_record_flag=true;
                    		   buf.append("<tr><td>"+Res.getString(1)+"</td>"+ "<td>"+Res.getTimestamp(2)+"</td>"+ "<td>"+Res.getString(3)+"</td>"+ "<td>"+Res.getString(4)+"</td>");
                    	   }
                    	   else {
                    		   ignore_error_record_flag=true;
                    		   ignore_error_record_count=ignore_error_record_count+1;
                    		   
						}
                    	  
                       }
		 	        }
		        	 
		        	buf.append("</table>" +"</body>" + "</html>");
		 			email_html = buf.toString();
		

            		

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				Res.close();
				stmt.close();
				DataBaseConnections_OBJ.Logging_CloseCVPDBConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return email_html;
		
	}
	


	private String getUTCTimeZone() {
		
		String past_one_hour_UTCTime=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00.000");  
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
  		Calendar c = Calendar.getInstance();
  		System.out.println("Date : " +  c.getTime());
  		c.add(Calendar.HOUR_OF_DAY, -1);
  		past_one_hour_UTCTime=sdf.format(c.getTime());
  		System.out.println("After subtracting 1 hrs : " + past_one_hour_UTCTime);
		
		return past_one_hour_UTCTime;
		
	}

}
