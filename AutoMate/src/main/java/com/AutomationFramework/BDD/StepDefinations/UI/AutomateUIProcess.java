package com.AutomationFramework.BDD.StepDefinations.UI;

import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.ArgumentMatchers.nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;

import com.AutomationFramework.BDD.StepDefinations.Mdigital.MdigitalWrapper;
import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.custom.AutomationNotifications;
import com.AutomationFramework.com.custom.EnvironmentVariables;
import com.AutomationFramework.com.impl.DataBaseConnections;
import com.Wrapper.UINewRequestWrapper;

public class AutomateUIProcess {
	
	DataBaseConnections obj_DataBaseConnections=new DataBaseConnections();
	AutomationNotifications obj_AutomationNotifications=new AutomationNotifications();
	Statement stmt=null;
	String Query=null;
	private BufferedWriter fileWriter;
	String FilePath=EnvironmentVariables.current_dir+"/Logs/Results/FileCompare/";
	
	public void processUIInputAutomation(UINewRequestWrapper ui_ControllerWrapper_OBJ) throws Exception
	{
		
		String email_html=null;
		String CurrentDateHour="";
		String Condition=null;
		boolean target_query=false;
		
		 
		 Date date = new Date();
		 DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00.000");
		 TimeZone pstTime = TimeZone.getTimeZone("PST");
		 pstFormat.setTimeZone(pstTime);
		 CurrentDateHour=pstFormat.format(date);
		 
		try {
			
		
			if((ui_ControllerWrapper_OBJ.getDb_type().contains("SQL Server"))|| (ui_ControllerWrapper_OBJ.getDb_type().contains("Oracle")))
			{
				obj_DataBaseConnections.SQLServerConnection(ui_ControllerWrapper_OBJ.getDb_UName(),
															ui_ControllerWrapper_OBJ.getDb_Password(),
															ui_ControllerWrapper_OBJ.getServer_name(),
															ui_ControllerWrapper_OBJ.getPort(),
															ui_ControllerWrapper_OBJ.getDatabase_name());
				
				stmt = DataBaseConnections.connection.createStatement();
			}
			
			if(ui_ControllerWrapper_OBJ.getSql_type().compareToIgnoreCase("COUNT")==0)
			{
				int Source_count=0;
				ResultSet Res=null;
				
				Res=stmt.executeQuery(ui_ControllerWrapper_OBJ.getSource_query());
				
				 if(Res.next())
		 	        {
					
					Source_count=Res.getInt(1);

		 	        }
		        	 
		        	
				 Res.close();
				if(ui_ControllerWrapper_OBJ.getCondition().compareToIgnoreCase("Match with target Query")==0)
				{
						target_query=true;
						ResultSet Res1=null;
						Res1=stmt.executeQuery(ui_ControllerWrapper_OBJ.getTarget_query());
						int target_count=0;
						while(Res1.next())
				 	        {
							
							target_count=Res1.getInt(1);
	
				 	        }
						
						Res1.close();
						StringBuilder buf = new StringBuilder();
						buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=2 cellspacing=0>" +"<tr>" +"<th>Condition</th>" +"<th>Source Query Count</th>"+"<th>Target Query Count</th>"+"<th>Status</th>"+"</tr>");
						Condition="Source Query Count Match with Target Query";
						if(Source_count==target_count)
						{
							buf.append("<tr><td>"+Condition+"</td><td>"+Source_count+"</td><td>"+target_count+"</td><td>Pass</td>");
						}
						else {
							buf.append("<tr style='color: red;'><td>"+Condition+"</td><td>"+Source_count+"</td><td>"+target_count+"</td><td>fail</td>");
						}
						
						buf.append("</table>" +"</body>" + "</html>");
			 			email_html = buf.toString();
				}
				else {
					int Assertion_Value=Integer.parseInt(ui_ControllerWrapper_OBJ.getAssertion());
					
					String status="Fail";
					boolean alert=true;
					
					if(ui_ControllerWrapper_OBJ.getCondition().compareToIgnoreCase("EQUALS")==0)
					{
						Condition="Source Query count Match with Equals Assesrtion Value";
						if(Assertion_Value==Source_count)
						{
							status="Pass";
							alert=false;
						}

					}
					else if(ui_ControllerWrapper_OBJ.getCondition().compareToIgnoreCase("NOT EQUALS")==0)
					{
						Condition="Source Query Count Not Match with Assesrtion Value";
						if(Assertion_Value!=Source_count)
						{
							status="Pass";
							alert=false;
						}
					}
					else if(ui_ControllerWrapper_OBJ.getCondition().compareToIgnoreCase("Greater Than")==0)
					{
						Condition="Source Query count should be greater than Assesrtion Value";
						if(Source_count>Assertion_Value)
						{
							status="Pass";
							alert=false;
						}
					}
					else if(ui_ControllerWrapper_OBJ.getCondition().compareToIgnoreCase("Less Than")==0)
					{
						Condition="Source Query count should be less than Assesrtion Value";
						if(Source_count<Assertion_Value)
						{
							status="Pass";
							alert=false;
						}
					}
					else if(ui_ControllerWrapper_OBJ.getCondition().compareToIgnoreCase("Greater Than or equal to")==0)
					{
						Condition="Source Query count should be greater than or equal to Assesrtion Value";
						if(Source_count>=Assertion_Value)
						{
							status="Pass";
							alert=false;
						}
					}
					else if(ui_ControllerWrapper_OBJ.getCondition().compareToIgnoreCase("Less Than or equal to")==0)
					{
						Condition="Source Query count should be less than or equal to Assesrtion Value";
						if(Source_count<=Assertion_Value)
						{
							status="Pass";
							alert=false;
						}
					}
					
					
					StringBuilder buf = new StringBuilder();
					buf.append("\t\t\t\t<html>" +"<body>" +"<table border=1 cellpadding=2 cellspacing=0>" +"<tr>" +"<th>Condition</th>" +"<th>Source Query Count</th>"+"<th>Assesrtion Value</th>"+"<th>Status</th>"+"</tr>");
					if(alert==false)
					{
						buf.append("<tr><td>"+Condition+"</td><td>"+Source_count+"</td><td>"+Assertion_Value+"</td><td>Pass</td>");
					}
					else {
						buf.append("<tr style='color: red;'><td>"+Condition+"</td><td>"+Source_count+"</td><td>"+Assertion_Value+"</td><td>Pass</td>");
					}
					
					buf.append("</table>" +"</body>" + "</html>");
					email_html = buf.toString();
					
				}
			}
			else if(ui_ControllerWrapper_OBJ.getSql_type().compareToIgnoreCase("COMPARE")==0)
			{
				target_query=true;
				String SourceFile = FilePath+getFileName("Source");
				
				exporttableDataFile(ui_ControllerWrapper_OBJ.getSource_query(),SourceFile);
				
				String TargetFile = FilePath+getFileName("Target");
				exporttableDataFile(ui_ControllerWrapper_OBJ.getTarget_query(),SourceFile);
				
				String CompareFile = FilePath+getFileName("Compare");
				
				CompareFileHashset(new File(SourceFile), new File(TargetFile), new File(CompareFile));
				
				
			}
			
			//Send an email...............................
			String MessageBody="";
			if(target_query==true)
			{
				 MessageBody="<br>Hi All, <br><br>"
				 		    + "Please find the automation results based on your UI Request.<br></br>"
							+ "<br></br><b><u> Condition: "+Condition+"</u></b><br></br>"
						    + "\t\t\t\t\t <b><u>SQL Type:</b></u> &nbsp; &nbsp; "+ui_ControllerWrapper_OBJ.getSql_type()+"</br>"
						    + "\t\t\t\t\t <b><u>SQL Condition:</b></u> &nbsp; &nbsp; "+ui_ControllerWrapper_OBJ.getCondition()+"</br>"
						    + "\t\t\t\t\t <b><u>Source Query:</b></u> &nbsp; &nbsp;"+ui_ControllerWrapper_OBJ.getSource_query()+"</br>"
						    + "\t\t\t\t\t <b><u>Target Query:</b></u> &nbsp; &nbsp;"+ui_ControllerWrapper_OBJ.getTarget_query()+"</br>"
						    
						    + "<br></br><b><u> Results:</u></b><br></br>"
						    + "\t\t\t\t\t"+email_html
							
						   
							+ "<br><br></br> This is auto generated email. Please don't reply to this email."
							+ "<br></br>Thanks,"
							+ "<br>Midas Automation Group.<br></br>";
			}
			else {
				 MessageBody="<br>Hi All, <br><br>"
				 		    + "Please find the automation results based on your UI Request.<br></br>"
				 		   + "<br></br><b><u> Condition: "+Condition+"</u></b><br></br>"
						    + "\t\t\t\t\t <b><u>SQL Type:</b></u> &nbsp; &nbsp;"+ui_ControllerWrapper_OBJ.getSql_type()+"</br>"
						    + "\t\t\t\t\t <b><u>SQL Condition:</b></u> &nbsp; &nbsp;"+ui_ControllerWrapper_OBJ.getCondition()+"</br>"
						    + "\t\t\t\t\t <b><u>Source Query:</b></u> &nbsp; &nbsp;"+ui_ControllerWrapper_OBJ.getSource_query()+"</br>"
						    + "\t\t\t\t\t <b><u>Assertion Value:</b></u> &nbsp; &nbsp;"+ui_ControllerWrapper_OBJ.getAssertion()+"</br>"
						    
						    + "<br></br><b><u> Results:</u></b><br></br>"
						    + "\t\t\t\t\t"+email_html
							
						   
							+ "<br><br></br> This is auto generated email. Please don't reply to this email."
							+ "<br></br>Thanks,"
							+ "<br>Midas Automation Group.<br></br>";
			}
			
			
			AutomationMainClass.Email_Address=ui_ControllerWrapper_OBJ.getEmail_id();
			 obj_AutomationNotifications.send_email_RestAPI("","","Automation Execution Results for the UI Request: "+CurrentDateHour,MessageBody);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			stmt.close();
		
			DataBaseConnections.connection.close();
		}
	}
	
	
	private int writeHeaderLine(ResultSet result) throws SQLException, IOException {
		
	ResultSetMetaData metaData = result.getMetaData();
	int numberOfColumns = metaData.getColumnCount();
	String headerLine = "";
	// exclude the first column which is the ID field
	for (int i = 2; i <= numberOfColumns; i++) {
	
		String columnName = metaData.getColumnName(i);
			headerLine = headerLine.concat(columnName).concat(",");
		}
	
		fileWriter.write(headerLine.substring(0, headerLine.length() - 1));
	
	return numberOfColumns;
}
	
	private String getFileName(String baseName) 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String dateTimeInfo = dateFormat.format(new Date());
	
		return baseName.concat(String.format("_%s.csv", dateTimeInfo));
		
}	
	
	private String escapeDoubleQuotes(String value) {
	
		return value.replaceAll("\"", "\"\"");
	}
	
	
	
	public void exporttableDataFile(String Query,String FileName)
	{
		ResultSet Res=null;
		try {
			Res=stmt.executeQuery(Query);
		
		
		fileWriter = new BufferedWriter(new FileWriter(FileName));
		int columnCount = writeHeaderLine(Res);
		 
		 while (Res.next()) {
			String line = "";

						for (int i = 2; i <= columnCount; i++) {
						
							Object valueObject = Res.getObject(i);
						 
							String valueString = "";
						 		if (valueObject != null) valueString = valueObject.toString();
						 
								 if (valueObject instanceof String) 
								 {
									 valueString = "\"" + escapeDoubleQuotes(valueString) + "\"";
								 }
			
								 line = line.concat(valueString);
			
								if (i != columnCount) {
									line = line.concat(",");
								}
						}

			fileWriter.newLine();
			fileWriter.write(line);
			}
		 
		 fileWriter.close();
		 Res.close();
		 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void CompareFileHashset(File SourceFile,File destFile,File fileout)
	{
		
		try {
			String str="";
			String strd="";
			
			FileReader sfr=new FileReader(SourceFile);
			BufferedReader sbr=new BufferedReader(sfr);
			HashSet<String> file1=new HashSet<String>();
			
			FileReader dfr=new FileReader(destFile);
			BufferedReader dbr=new BufferedReader(dfr);
			
			FileOutputStream fos=new FileOutputStream(fileout);
			BufferedWriter bWriter=new BufferedWriter(new OutputStreamWriter(fos));
			
			
			
			while((strd=dbr.readLine())!=null)
			{
				file1.add(strd.trim());
			}
			
			while((str=sbr.readLine())!=null)
			{
				if(file1.contains(str.trim()))
				{
					;
				}
				else {
					bWriter.write(str);
					bWriter.newLine();
				}
			
			}
			
			file1.clear();
			sbr.close();
			dfr.close();
			bWriter.close();
			
		} catch (IOException e) {
			;
		}
		
		
	}

}
