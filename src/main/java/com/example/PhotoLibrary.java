package com.example;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;

@Service
public class PhotoLibrary {
	
	private final String azureConnectionString;
	private final String azureAccountName;
	private final String azureAccessKey;
	private final String s3AzureEndpoint;
	private final String s3AwsEndpoint;
	private final String containerName;
	private final String awsAccessKey;
	private final String awsAccessSecret;

	@Autowired
	public PhotoLibrary(
			@Value("${azure.storage.account.name}") String azureAccountName, 
			@Value("${azure.storage.account.key}") String azureAccessKey,
			@Value("${containername}") String containerName,
			@Value("${AWS_ACCESS_KEY}") String AWS_ACCESS_KEY,
			@Value("${AWS_SECRET_ACCESS_KEY}") String AWS_SECRET_ACCESS_KEY,
			@Value("${s3proxy.azureendpoint}") String s3proxyAzureEndpoint,
			@Value("${s3proxy.awsendpoint}") String s3proxyAwsEndpoint)
	{
		
		this.azureConnectionString = "DefaultEndpointsProtocol=http;" +
		                        "AccountName="+ azureAccountName + 
		                        	";AccountKey=" + azureAccessKey; 	
		this.azureAccountName = azureAccountName;
		this.azureAccessKey = azureAccessKey;
		this.s3AzureEndpoint = s3proxyAzureEndpoint;
		this.s3AwsEndpoint = s3proxyAwsEndpoint;
		this.containerName = containerName;
		this.awsAccessKey = AWS_ACCESS_KEY;
		this.awsAccessSecret = AWS_SECRET_ACCESS_KEY;
	}
	// azure sdk
	public byte[] getFromAzureBlobStorage(String name) throws Exception {
		CloudStorageAccount storageAccount = CloudStorageAccount.parse(azureConnectionString);
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		CloudBlobContainer container = blobClient.getContainerReference(containerName);
		CloudBlockBlob blob = container.getBlockBlobReference(name);

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		blob.download(outStream);
		byte[] image = outStream.toByteArray();
		return image;
	}	
	// aws sdk
	public byte[] getFromAWSS3Storage(String name) throws Exception {
		
		AmazonS3 s3Client = new AmazonS3Client(new EnvironmentVariableCredentialsProvider());        
		S3Object object = s3Client.getObject(
		                  new GetObjectRequest(containerName, name));
		InputStream objectData = object.getObjectContent();
		return IOUtils.toByteArray(objectData);
	}
	
	// s3proxy using s3 sdk
	public byte[] getFromS3ProxyAzureStorage(String name) throws Exception {
	
		return this.getFromS3Proxy(name,  this.azureAccountName, this.azureAccessKey, this.s3AzureEndpoint);
	}
	public byte[] getFromS3ProxyAWSStorage(String name) throws Exception {
		
		return this.getFromS3Proxy(name,  this.awsAccessKey, this.awsAccessSecret, this.s3AwsEndpoint);
	}
	
	public byte[] getFromS3Proxy(String name, String key, String secret, String endpoint) throws Exception {
		System.setProperty("com.amazonaws.services.s3.disablePutObjectMD5Validation", "1"); 
		
		AWSCredentials credentials = new BasicAWSCredentials(key, secret);
		ClientConfiguration clientConfig = new ClientConfiguration().withSignerOverride("S3SignerType");
		clientConfig.setProtocol(Protocol.HTTP);
		AmazonS3 s3 = new AmazonS3Client(credentials, clientConfig);
		
	    s3.setEndpoint(endpoint);      
		S3Object object = s3.getObject(
		                  new GetObjectRequest(containerName, name));
		InputStream objectData = object.getObjectContent();
		return IOUtils.toByteArray(objectData);
	}
}
