package com.AutomationFramework.BDD.StepDefinations.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cucumber.api.java.en.Given;
import com.AutomationFramework.BDD.Runner.DataClass;
import com.AutomationFramework.BDD.Util.Utility;
import com.AutomationFramework.com.DriverEngine.Driver;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.custom.EnvironmentVariables;
import com.AutomationFramework.com.impl.DataBaseConnections;
import com.Wrapper.APIControllerWrapper;
import com.Wrapper.ResponseWrapper;

public class Util {
	
	public static final Logger logger = LoggerFactory.getLogger(Util.class);
	public Properties prop = null;


	@Given("^Load the data properies from the given DSID \"(.*)\" sheetname \"(.*)\" and excelpath \"(.*)\"$")
	public void load_data_Properties(String dsid,String sheetname, String filepath) throws Exception {

		Workbook tempWB=null;
		CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		try {

			
			String DataSheet=null;
			int rowNo=0;
			Utility util_obj=new Utility();
			
			/***************************** Load TestExecutio Data Properties************************/
			
			prop=CustomFunctions_OBJ.Load_TestExecutioData_Properties();
			
			DataSheet=prop.getProperty(filepath);
			
			Driver.driverMapObj.put("Data_Sheet_ID", dsid);
			Driver.driverMapObj.put("Data_Sheet_Name", sheetname);
			Driver.driverMapObj.put("Data_Sheet_Path", EnvironmentVariables.current_dir+File.separator+DataSheet);
			
			if(DataSheet.contains(".xlsx"))
			{
				tempWB=new XSSFWorkbook(DataSheet);
			}
			else
			{
				InputStream inp=new FileInputStream(DataSheet);
				tempWB=(Workbook)new HSSFWorkbook(new POIFSFileSystem(inp));
				inp.close();
			}
			
			Sheet ws=tempWB.getSheet(sheetname);
			
				for(int i=0;i<ws.getLastRowNum();i++)
				{
					String Value="";
						if(ws.getRow(i).getCell(0).getCellType()==CellType.NUMERIC)
						{
							Value=NumberToTextConverter.toText(ws.getRow(i).getCell(0).getNumericCellValue());
						}
						else
						{
							Value=ws.getRow(i).getCell(0).toString();
						}
					
						if(Value.toString().equals(dsid))
						{
							rowNo=ws.getRow(i).getRowNum();
							break;
						}
				}
				
		  if(DataClass.hmap==null)
		  {
			  DataClass.hmap=new HashMap<String,String>();
		  }
		  
		  
		  for(int i=0;i<ws.getRow(rowNo).getLastCellNum();i++)
		  {
			  String key="",Value="";
			  
			  if(ws.getRow(rowNo).getCell(i)!=null)
			  {
				  if(ws.getRow(rowNo).getCell(i).getCellType()==CellType.NUMERIC)
				  {
					  Value=NumberToTextConverter.toText(ws.getRow(rowNo).getCell(i).getNumericCellValue());
				  }
				  else
				  {
					  Value=ws.getRow(rowNo).getCell(i).toString(); 
				  }
				  
				  
				  if(ws.getRow(0).getCell(i).getCellType()==CellType.NUMERIC)
				  {
					  key=NumberToTextConverter.toText(ws.getRow(0).getCell(i).getNumericCellValue());
				  }
				  else
				  {
					  key=ws.getRow(0).getCell(i).toString(); 
				  }
				  
				  
				  DataClass.hmap.put(key, Value);
			  }
		  }

		} catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		
		finally
		{
			tempWB.close();
		}

	}
	
	
	/*************************************************************************************************************************************************************/
	/***************************************Method to load the data using Maven run time properties instead of the Scenario Outline******************************/
	/*************************************************************************************************************************************************************/
	
	public void load_data_Properties1(String dsid,String sheetname, String filepath) throws Throwable {

		Workbook tempWB=null;
		CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		try {

			
			String DataSheet=null;
			int rowNo=0;
			Utility util_obj=new Utility();
			
			/***************************** Load TestExecutio Data Properties************************/
			
			prop=CustomFunctions_OBJ.Load_TestExecutioData_Properties();
			
			DataSheet=prop.getProperty(filepath);
			
			Driver.driverMapObj.put("Data_Sheet_ID", dsid);
			Driver.driverMapObj.put("Data_Sheet_Name", sheetname);
			Driver.driverMapObj.put("Data_Sheet_Path", EnvironmentVariables.current_dir+File.separator+DataSheet);
			
			if(DataSheet.contains(".xlsx"))
			{
				tempWB=new XSSFWorkbook(DataSheet);
			}
			else
			{
				InputStream inp=new FileInputStream(DataSheet);
				tempWB=(Workbook)new HSSFWorkbook(new POIFSFileSystem(inp));
				inp.close();
			}
			
			Sheet ws=tempWB.getSheet(sheetname);
			
				for(int i=0;i<ws.getLastRowNum();i++)
				{
					String Value="";
						if(ws.getRow(i).getCell(0).getCellType()==CellType.NUMERIC)
						{
							Value=NumberToTextConverter.toText(ws.getRow(i).getCell(0).getNumericCellValue());
						}
						else
						{
							Value=ws.getRow(i).getCell(0).toString();
						}
					
						if(Value.toString().equals(dsid))
						{
							rowNo=ws.getRow(i).getRowNum();
							break;
						}
				}
				
		  if(DataClass.hmap==null)
		  {
			  DataClass.hmap=new HashMap<String,String>();
		  }
		  
		  
		  for(int i=0;i<ws.getRow(rowNo).getLastCellNum();i++)
		  {
			  String key="",Value="";
			  
			  if(ws.getRow(rowNo).getCell(i)!=null)
			  {
				  if(ws.getRow(rowNo).getCell(i).getCellType()==CellType.NUMERIC)
				  {
					  Value=NumberToTextConverter.toText(ws.getRow(rowNo).getCell(i).getNumericCellValue());
				  }
				  else
				  {
					  Value=ws.getRow(rowNo).getCell(i).toString(); 
				  }
				  
				  
				  if(ws.getRow(0).getCell(i).getCellType()==CellType.NUMERIC)
				  {
					  key=NumberToTextConverter.toText(ws.getRow(0).getCell(i).getNumericCellValue());
				  }
				  else
				  {
					  key=ws.getRow(0).getCell(i).toString(); 
				  }
				  
				  
				  DataClass.hmap.put(key, Value);
			  }
		  }

		} catch (Exception e) {
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		
		
		finally
		{
			tempWB.close();
		}

	}


	public boolean load_database_data_Properties(int dBUniqueid, APIControllerWrapper aPIControllerWrapper_OBJ) throws SQLException,Exception 
	{

		DataBaseConnections Obj_DataBaseConnections=new DataBaseConnections();
		PreparedStatement preparedstmt = null;
		ResultSet rs = null;
		boolean flag=false;
		try {


		  if(DataClass.hmap==null)
		  {
			  DataClass.hmap=new HashMap<String,String>();
			  
			  
		  }
		  System.out.println("Before DB Call");
		  Obj_DataBaseConnections.Connect_Results_SQLServerDB();
		  System.out.println("After DB Connection");
		  
		  String QUERY = "select * from dbo."+aPIControllerWrapper_OBJ.getTablename()+" where ID="+dBUniqueid;
		  preparedstmt = DataBaseConnections.results_db_connection.prepareStatement(QUERY);
		  
          rs = preparedstmt.executeQuery();
          
          while (rs.next()) {
        	  

        	  ResultSetMetaData rsmd = rs.getMetaData();
        	  
        	  int column_count = rsmd.getColumnCount();
        	  
        	  for(int i=1;i<=column_count;i++)
        	  {
        		  DataClass.hmap.put(rsmd.getColumnName(i), rs.getString(i));  
        	  }
        	  flag=true;

          }
		}
		
		finally
		{
			Obj_DataBaseConnections.CloseResultsDBConnection();
		}

	return flag;
		
	}
	
	

  
}
