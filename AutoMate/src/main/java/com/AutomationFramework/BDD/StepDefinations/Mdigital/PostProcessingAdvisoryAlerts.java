package com.AutomationFramework.BDD.StepDefinations.Mdigital;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.en.Given;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.impl.SnowFlakeDataBaseConnection;

public class PostProcessingAdvisoryAlerts {
	
	public static final Logger logger = LoggerFactory.getLogger(PostProcessingAdvisoryAlerts.class);
	
	
	CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
	
	@Given("^Execute post processing queires and get results$")
	public void Execute_post_processing_Alerts() throws Throwable 
	{
		long count=0;

		try {

			 AutomationMainClass.email_mode="Post Processing Advisory Alerts";

			
	        /********************************************Get the SnowFlake Data***********************************************************/
			/*Post-Process Advisory Alerts:	Alert Odyssey if any of the following are violated:
			AGE: Field should not be greater than 999 ASKINGPRICE:Field should not be greater than 999,999 DRIVEWAY_PRICE: Field should not be greater than 999,999 PHOTOCOUNT: Field should not be greater than 60*/
			 MdigitalWrapper.Post_Processing_FileName=getFileName();
			 count=get_PostProcessing_Data_status();

	         
	         System.out.println("Enterting into the Execute_given_hourly_max_digital_testcases");
        	 MdigitalWrapper.Subject="Weekly Driveway Advisory Alerts - " ;

	         

	         
	        
	         MdigitalWrapper.Post_Processing_Alerts_Message_Body=
	        		 "<br><font size=3>This is the weekly Advisory Alert report on Driveway vehicle inventory. These units are on Driveway.com today but have triggered specific business rules that indicate there may be problems with how the data reaches Driveway or how the data was entered.</br>"+
	        		 "<br>If you have any feedback or recommendations about the rules / criteria for these Advisory Alerts, please email drivewayfeed@com.com.</br>"+
	        		 
	        		 "<br><b><u>Current Advisory Alerts:</b></u>"+
	        		 "<br>A unit on Driveway will have an Advisory Alert if at least one of the following criteria are met:"+
	        		 "<br>&ensp;&ensp;1.	AGE:"+
	        		 "<br>&ensp;&ensp;&ensp; a. Field should not be greater than 999"+
	        		 "<br>&ensp;&ensp;&ensp; b. Vehicles with an inventory age of 1,000 days or more are not expected"+
	        		 
					"<br>&ensp;&ensp;2.	ASKINGPRICE:"+
					"<br>&ensp;&ensp;&ensp; a. Field should not be greater than 999,999"+
					"<br>&ensp;&ensp;&ensp; b. Vehicles priced by the store as $1M or more are often mistaken entries"+
					
					"<br>&ensp;&ensp;3.	DRIVEWAY_PRICE:"+
					"<br>&ensp;&ensp;&ensp; a. Field should not be greater than 999,999"+
					"<br>&ensp;&ensp;&ensp; b. Vehicles priced by the store, specifically for Driveway as $1M or more, are often mistaken entries"+
					
					
					"<br>&ensp;&ensp;4.	PHOTOCOUNT:"+
					"<br>&ensp;&ensp;&ensp; a. Field should not be greater than 60"+
					"<br>&ensp;&ensp;&ensp; b. 60 or more photos are uncommon, so it often indicates an issue with duplicate photos"+

					"<br><br><br>The value that triggered the alert(s) for the unit is indicated in <font color='red'>red </font>in the table at the bottom of this email.</br>"+
					
					"<br><b><u>Data source:</b></u></br>"+
	        		"<br>The data for this week's Advisory Alert may be found on the below file with the following filename.<br>File name, <b><u>"+MdigitalWrapper.Post_Processing_FileName+"</b></u>.<br></br>"
						+ "<br></br><b><u> Units with Advisory Alerts:</u></b><br></br>"
					    + "\t\t\t\t\t"+MdigitalWrapper.Post_Processing_Advisory_data
			
						+ "<br><br></br> This is auto-generated email. Please reach out to drivewayfeed@com.com if you have questions about this email."
						+ "<br></br>Thanks,"
						+ "<br>Automation Group.<br></br>";
	         
	         System.out.println("Message Body creation completed:Execute_given_hourly_max_digital_testcases");
	         

	        
	        
		}

		
		catch (Exception e) {
			//ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}


	}

	private String getFileName() {



    	String Query=null;
    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
    	Properties properties = null;
    	Statement stmt=null;
    	ResultSet Res=null;
    	String File_Name=null;
		try
		{
			SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
			properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
	        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
	        
			Query=properties.getProperty("get_Post_Processing_Alert_FileName");
			Res=stmt.executeQuery(Query);
        	

        	 while(Res.next())
 	        {
        		 File_Name=Res.getString(1);
        		 
 	        }


		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				stmt.close();
				SnowFlakeDataBaseConnection_OBJ.CloseSnowFlakeDBConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
return File_Name;

	}

	private long get_PostProcessing_Data_status() {


    	String Query=null;
    	SnowFlakeDataBaseConnection SnowFlakeDataBaseConnection_OBJ=new SnowFlakeDataBaseConnection();
    	Properties properties = null;
    	Statement stmt=null;
    	ResultSet Res=null;
    	long count=0;
		try
		{
			SnowFlakeDataBaseConnection_OBJ.Connect_SnowFlake_DataBase("STAGE");
			properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
	        stmt = SnowFlakeDataBaseConnection.connection.createStatement();
	        
			Query=properties.getProperty("get_data_Post_Processing_advisory");
			Res=stmt.executeQuery(Query);
        	
        	StringBuilder buf = new StringBuilder();
			buf.append("\t\t<html>" +"<body>" +"<table border=1 cellpadding=2 cellspacing=0>" +"<tr>" +"<th>VIN</th>" +"<th>DEALER_STOCK</th>"+"<th>Lithia_233</th>"+"<th style=width:15%;>AGE</th>"+"<th>ASKINGPRICE</th>"+"<th>DRIVEWAY_PRICE</th>"+"<th>PHOTOCOUNT</th>"+"</tr>");
			
        	 while(Res.next())
 	        {
        		 count=count+1; 
           		 String age=Res.getString(4);
        		 String ASKINGPRICE=Res.getString(5);
        		 String DRIVEWAY_PRICE=Res.getString(6);
        		 String PHOTOCOUNT=Res.getString(7);
        		 
   
        		 
        		 if(age.length()>0)
    			 {
    					 if(Integer.parseInt(age)>999)
    		    		 {
    						 age="<td style='color: red;'>"+age+"</td>";
    		    		 }
    					 else {
    						 age="<td>"+age+"</td>";
    					}
    			 }
    			 else {
    				 age="<td>"+age+"</td>";
    			}
        		 
        		 
        		 ASKINGPRICE=getemailformatvalue(ASKINGPRICE);
        		 DRIVEWAY_PRICE=getemailformatvalue(DRIVEWAY_PRICE);
        		
        		 if(PHOTOCOUNT.length()>0)
    			 {
    					 if(Integer.parseInt(PHOTOCOUNT)>60)
    		    		 {
    						 PHOTOCOUNT="<td style='color: red;'>"+PHOTOCOUNT+"</td>";
    		    		 }
    					 else {
    						 PHOTOCOUNT="<td>"+PHOTOCOUNT+"</td>";
    					}
    			 }
    			 else {
    				 PHOTOCOUNT="<td>"+PHOTOCOUNT+"</td>";
    			}
        		 

        		 
        		 
        		
        		 buf.append("<tr><td>"+Res.getString(1)+"</td>"
        		 		+ "<td>"+Res.getString(2)+"</td>"
        		 		+ "<td>"+Res.getString(3)+"</td>"
        		 		+ age+ASKINGPRICE+DRIVEWAY_PRICE+PHOTOCOUNT);
     			
        		 
 	        }
        	 
        	buf.append("</table>" +"</body>" + "</html>");
 			MdigitalWrapper.Post_Processing_Advisory_data=buf.toString();
            		

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				stmt.close();
				SnowFlakeDataBaseConnection_OBJ.CloseSnowFlakeDBConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
return count;
		
	
		
	}


	private String getemailformatvalue(String db_Value) {
	
		String final_value="";
		try {
			 if(db_Value.length()>0)
			 {
				 if (Double.parseDouble(db_Value)>999999)
				 {
					 final_value="<td style='color: red;'>"+db_Value+"</td>";
				 }
				 else {
					 final_value="<td>"+db_Value+"</td>";
				}
   			 
			 }
			 else {
				 final_value="<td>"+db_Value+"</td>";
			}
		} catch (Exception e) {
			;
		}
		return final_value;
	}
	

}
