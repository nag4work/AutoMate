package com.AutomationFramework.com.Sftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import com.AutomationFramework.com.Main.AutomationMainClass;
import com.AutomationFramework.com.custom.CustomFunctions;
import com.AutomationFramework.com.custom.EnvironmentVariables;


public class SftpConnection {
	
	public static final Logger logger = LoggerFactory.getLogger(SftpConnection.class);
	Properties prop = null;
	CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
	
	String sftp_host=null;
	int sftp_port = 22;
	String sftp_user = null;
	String sftp_pass = null;
	String sftp_workingDr = null;
	static Session session = null;
	static Channel channel = null;
	static ChannelSftp channelSftp = null;
	
	
	public static void main(String [] args)
	{
		AutomationMainClass.environment="DEV";
		String fileName="AutomationTesting.txt";
		String upload_path=EnvironmentVariables.current_dir+"/Logs/sftp/Upload/";
		String download_path=EnvironmentVariables.current_dir+"/Logs/sftp/Download/";
		sftpconnection(fileName,upload_path,download_path);
		
	}

	
	private static void sftpconnection(String fileName,String upload_path,String download_path) 
	{
		SftpConnection SftpConnection_obj=new SftpConnection();
		
		
		try
		{
			SftpConnection_obj.connectionproperties();
			SftpConnection_obj.Upload_File(fileName,upload_path);
			SftpConnection_obj.Download_File(fileName,download_path);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			channel.disconnect();
			logger.info("Channel disconnected.");
			session.disconnect();
			logger.info("Host Session disconnected.");
			
			channelSftp.exit();
			logger.info("sftp Channel exited.");
		}
	}


	public void  connectionproperties()
	{
		try
		{
			JSch jsch = new JSch();
			
			prop=CustomFunctions_OBJ.ApplicationProperties();
			
			sftp_host = prop.getProperty("sftp_host_name");
			sftp_port = 22;
			sftp_user = prop.getProperty("sftp_username");
			sftp_pass = CustomFunctions_OBJ.getdescryptedDBpassword(prop.getProperty("sftp_password"));
			sftp_workingDr = prop.getProperty("sftp_workingDr");
			
			session = jsch.getSession(sftp_user, sftp_host, sftp_port);
			session.setPassword(sftp_pass);

			java.util.Properties config = new java.util.Properties();

			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			logger.info("Host connected.");
			channel = session.openChannel("sftp");
			channel.connect();
			logger.info("sftp channel opened and connected.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void Upload_File (String fileName,String upload_path) {


		try {
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(sftp_workingDr);
			File f = new File(upload_path+fileName);
			channelSftp.put(new FileInputStream(f), f.getName());
	
			logger.info("File transfered successfully to host.");

		} catch (Exception ex)
		{

		logger.info("Exception found while tranfer the response.");

		} 
		}
	
	
	public void Download_File (String fileName,String download_path) {

		byte[] buffer = new byte[1024];
		int readCount;
	
		try {
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(sftp_workingDr);
			
	
			BufferedInputStream bis = new BufferedInputStream(channelSftp.get(fileName));
			File newFile = new File(download_path+fileName);
			OutputStream os = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);
	
			while ((readCount = bis.read(buffer)) > 0) 
			{
				bos.write(buffer, 0, readCount);
			} 
			bis.close();
			bos.close();
		}
		
		catch (Exception ex)
		{
         ex.printStackTrace();
		logger.info("Exception found while tranfer the response.");

		} 
		}

}
