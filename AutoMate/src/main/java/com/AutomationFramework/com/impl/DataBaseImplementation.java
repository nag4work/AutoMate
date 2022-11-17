package com.AutomationFramework.com.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.AutomationFramework.com.Pojos.DSDecisionsWrapper;
import com.AutomationFramework.com.Pojos.FeaturesConfig;
import com.AutomationFramework.com.Pojos.QueryParametersWrapper;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.Wrapper.ResponseWrapper;

public class DataBaseImplementation {
	
	public static final Logger logger = LoggerFactory.getLogger(DataBaseImplementation.class);
	public static DSDecisionsWrapper DSDecisionsWrapper_OBJ=new DSDecisionsWrapper();
	//public static FeaturesConfig FeaturesConfig_OBJ=new FeaturesConfig();
	public static List<FeaturesConfig> features_configs=new ArrayList<FeaturesConfig>();
	
	public void Execute_SQL_Query(QueryParametersWrapper queryParametersWrapper_OBJ)
	{
		DataBaseConnections DataBaseConnections_OBJ=new DataBaseConnections();
		CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
		Statement stmt=null;
		String Query=null;
        Properties Properties = null;
        
		try
		{
			logger.debug("Entering into the Method: Execute_SQL_Query");
			
			/****************** Read the query Peroperties**************************************/

			Properties=CustomFunctions_OBJ.GetSQLQueryProperties();
			
			DataBaseConnections_OBJ.SQLServerConnection();
	        stmt = DataBaseConnections.connection.createStatement();
	        
	        
	        Query=Properties.getProperty(queryParametersWrapper_OBJ.getQueryname());
	        
	        logger.debug("Validating the Query Name "+queryParametersWrapper_OBJ.getQueryname());
	        
	        if(queryParametersWrapper_OBJ.getQueryname().toString().compareTo("DSDecisions_Detail_By_VIN_vehicleInfo")==0)
	        {
	        	 ExecuteQueryDSDecisions_Detail_By_VIN_vehicleInfo(Query,queryParametersWrapper_OBJ,stmt);
	        	 
	        }
	        if(queryParametersWrapper_OBJ.getQueryname().toString().compareTo("DSDecisions_multiTrim_vehicleInfo")==0)
	        {
	        	ExecuteQuery_DSDecisions_multiTrim_vehicleInfo(Query,queryParametersWrapper_OBJ,stmt);
	        }
	        if(queryParametersWrapper_OBJ.getQueryname().toString().compareTo("DSDecisions_Detail_By_VIN_Features")==0)
	        {
	        	 ExecuteQueryDSDecisions_Detail_By_VIN_Features(Query,queryParametersWrapper_OBJ,stmt);
	        	 
	        }
	        if(queryParametersWrapper_OBJ.getQueryname().toString().compareTo("DSDecisions_multiTrim_Features")==0)
	        {
	        	ExecuteQuery_DSDecisions_multiTrim_Features(Query,queryParametersWrapper_OBJ,stmt);
	        }
	        

		}
		catch(Exception e)
		{
			
			logger.debug("Exception Occured at "+e.getLocalizedMessage());
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		finally {
			try {
				stmt.close();
				DataBaseConnections.connection.close();
				logger.info("All the DB Connections are closed Successfully");
			} catch (SQLException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			}
			
		}
	}

	private void ExecuteQueryDSDecisions_Detail_By_VIN_Features(String query,QueryParametersWrapper queryParametersWrapper_OBJ, Statement stmt) {

		
		String Query=null;
        ResultSet Res=null;
       
        features_configs=new ArrayList<FeaturesConfig>();
	
		try
		{
			logger.debug("Executing the Query "+query+"With Input "+queryParametersWrapper_OBJ.getQueryparameter1());
			//Query=query.replaceFirst("\\?", queryParametersWrapper_OBJ.getQueryparameter1());
			Query=query.replaceAll("\\?", queryParametersWrapper_OBJ.getQueryparameter1());
        	
        	Res=stmt.executeQuery(Query);
        	 while(Res.next())
 	        {
        		 FeaturesConfig FeaturesConfig_OBJ=new FeaturesConfig();
        		 FeaturesConfig_OBJ.setName(Res.getString(1));
        		 FeaturesConfig_OBJ.setSelected(Res.getString(2));
        		 
        		 features_configs.add(FeaturesConfig_OBJ);
 	        }
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try {
				Res.close();
			} catch (SQLException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			}
		}
		
	
		
	}
	
	private void ExecuteQuery_DSDecisions_multiTrim_Features(String query,QueryParametersWrapper queryParametersWrapper_OBJ, Statement stmt) {

		
		String Query=null;
        ResultSet Res=null;
       
        features_configs=new ArrayList<FeaturesConfig>();
	
		try
		{
			logger.debug("Executing the Query "+query+"With Input "+queryParametersWrapper_OBJ.getQueryparameter1() +" and "+queryParametersWrapper_OBJ.getQueryparameter2());
			//Query=query.replaceFirst("\\?", queryParametersWrapper_OBJ.getQueryparameter1());
			Query=query.replaceAll("\\?vin", queryParametersWrapper_OBJ.getQueryparameter1());
			Query=Query.replaceAll("\\?uvc", queryParametersWrapper_OBJ.getQueryparameter2());
        	
        	Res=stmt.executeQuery(Query);
        	 while(Res.next())
 	        {
        		 FeaturesConfig FeaturesConfig_OBJ=new FeaturesConfig();
        		 FeaturesConfig_OBJ.setName(Res.getString(1));
        		 FeaturesConfig_OBJ.setSelected(Res.getString(2));
        		 
        		 features_configs.add(FeaturesConfig_OBJ);
 	        }
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try {
				Res.close();
			} catch (SQLException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			}
		}
		
	
		
	}

	private void ExecuteQueryDSDecisions_Detail_By_VIN_vehicleInfo(String query,QueryParametersWrapper queryParametersWrapper_OBJ,Statement stmt) {
		
		String Query=null;
        ResultSet Res=null;
        DSDecisionsWrapper_OBJ=new DSDecisionsWrapper();
	
		try
		{
			logger.debug("Executing the Query "+query+"With Input "+queryParametersWrapper_OBJ.getQueryparameter1());
			Query=query.replaceFirst("\\?", queryParametersWrapper_OBJ.getQueryparameter1());
        	
        	Res=stmt.executeQuery(Query);
        	
        	 while(Res.next())
 	        {
        		 DSDecisionsWrapper_OBJ.setModel_year(Res.getString(1));
        		 DSDecisionsWrapper_OBJ.setMake(Res.getString(2));
        		 DSDecisionsWrapper_OBJ.setModel(Res.getString(3));        		 
        		 DSDecisionsWrapper_OBJ.setTrim(Res.getString(4));
        		 DSDecisionsWrapper_OBJ.setFuel(Res.getString(5));
        		 DSDecisionsWrapper_OBJ.setUvc(Res.getString(6));
 	        }
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try {
				Res.close();
			} catch (SQLException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
	
	private void ExecuteQuery_DSDecisions_multiTrim_vehicleInfo(String query,QueryParametersWrapper queryParametersWrapper_OBJ,Statement stmt) {
		
		String Query=null;
        ResultSet Res=null;
        DSDecisionsWrapper_OBJ=new DSDecisionsWrapper();
	
		try
		{
			logger.debug("Executing the Query "+query+"With Input "+queryParametersWrapper_OBJ.getQueryparameter1()+" and "+queryParametersWrapper_OBJ.getQueryparameter2());
			Query=query.replaceFirst("\\?", queryParametersWrapper_OBJ.getQueryparameter1());
			Query=Query.replaceFirst("\\?", queryParametersWrapper_OBJ.getQueryparameter2());
        	
        	Res=stmt.executeQuery(Query);
        	
        	 while(Res.next())
 	        {
        		 DSDecisionsWrapper_OBJ.setModel_year(Res.getString(1));
        		 DSDecisionsWrapper_OBJ.setMake(Res.getString(2));
        		 DSDecisionsWrapper_OBJ.setModel(Res.getString(3));        		 
        		 DSDecisionsWrapper_OBJ.setTrim(Res.getString(4));
        		 DSDecisionsWrapper_OBJ.setFuel(Res.getString(5));
        		 DSDecisionsWrapper_OBJ.setUvc(Res.getString(6));
 	        }
		}
		catch(Exception e)
		{
			ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try {
				Res.close();
			} catch (SQLException e) {
				ResponseWrapper.setMessage(e.toString() +":"+e.getMessage());
				e.printStackTrace();
			}
		}
		
	}

}
