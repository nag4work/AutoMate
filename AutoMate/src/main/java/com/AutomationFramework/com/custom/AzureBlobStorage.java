package com.AutomationFramework.com.custom;


import java.io.File;
import java.util.Properties;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;

import com.AutomationFramework.com.Main.AutomationMainClass;



public class AzureBlobStorage {
	
	Properties prop = null;
	CustomFunctions CustomFunctions_OBJ=new CustomFunctions();
	String Download_path=null;
	String Upload_path=null;
	
	public static void main(String[] args) {
		try
		{
			BlobContainerClient containerClient=null;
			AzureBlobStorage OBJ_AzureBlobStorage=new AzureBlobStorage();
			AutomationMainClass.environment="DEV";
			containerClient=OBJ_AzureBlobStorage.AzureContainerBlob_Connection();
			OBJ_AzureBlobStorage.AzureContainerBlob_Download(containerClient);
			System.out.println("All the files downloaded.");
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
	        

	}
	
	public BlobContainerClient AzureContainerBlob_Connection()
	{
		BlobContainerClient containerClient=null;
		String    accountName=null;
		String    accountkey=null;
		String    destinationContainerName=null;
		try
		{
			
			prop=CustomFunctions_OBJ.ApplicationProperties();
			
			accountName= prop.getProperty("Azure_Blob_Account_Name");
			accountkey = CustomFunctions_OBJ.getdescryptedDBpassword(prop.getProperty("Azure_Blob_Key"));
			destinationContainerName = prop.getProperty("Azure_Blob_ContainerName"); 
			Download_path=EnvironmentVariables.current_dir+prop.getProperty("Azure_File_Download_Path"); 
			Upload_path=EnvironmentVariables.current_dir+prop.getProperty("Azure_File_Upload_Path"); 
			
			BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().endpoint("https://" +accountName +".blob.core.windows.net").sasToken(accountkey).buildClient();
			containerClient = blobServiceClient.getBlobContainerClient(destinationContainerName);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return containerClient;
	} 
	
	public void AzureContainerBlob_Download(BlobContainerClient containerClient)
	{
		try
		{
			PagedIterable<BlobItem> blobs = containerClient.listBlobs();
	         BlobClient blobClient = null;
	         for (BlobItem blobItem : blobs) {
	             blobClient = containerClient.getBlobClient(blobItem.getName());
	    
	             if (blobClient != null) {
	                 String filePath=Download_path+ blobItem.getName();
	                 FileUtils.forceMkdirParent(new File(filePath));
	                 blobClient.downloadToFile(filePath);
	             }

	         }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void AzureContainerBlob_Upload(BlobContainerClient containerClient,String sourceFilename,String destinationBlobName)
	{
		try
		{
			//String    sourceFilename = Upload_path+"test1.txt";
			//String    destinationBlobName = "file.txt";
			BlobClient blobClient = containerClient.getBlobClient(destinationBlobName);
			blobClient.uploadFromFile(sourceFilename); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	




}
