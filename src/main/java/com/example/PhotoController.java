package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoController {

	private final PhotoLibrary library; 
	private final DogePhotoManipulator manipulator;

	@Autowired
	public PhotoController(PhotoLibrary library)
	{
		this.library = library;
		this.manipulator = new DogePhotoManipulator();
	}

	@RequestMapping(value = "/azure", produces = "application/json")
	public byte[] doge3() throws Exception 
	{
		byte[] data = library.getFromAzureBlobStorage("invoice_azure.json");		
		return data;
	}
	@RequestMapping(value = "/aws", produces = "application/json")
	public byte[] doge4() throws Exception 
	{
		byte[] data = library.getFromAWSS3Storage("invoice_aws.json");		
		return data;
	}
	@RequestMapping(value = "/s3proxyazure", produces = "application/json")
	public byte[] doge5() throws Exception 
	{
		byte[] data = library.getFromS3ProxyAzureStorage("invoice_azure.json");		
		return data;
	}
	@RequestMapping(value = "/s3proxyaws", produces = "application/json")
	public byte[] doge6() throws Exception 
	{
		byte[] data = library.getFromS3ProxyAWSStorage("invoice_aws.json");		
		return data;
	}
	
	@RequestMapping(value = "/imagesazure", produces = "application/pdf")
	public byte[] doge7() throws Exception 
	{
		byte[] data = library.getFromAzureBlobStorage("azure.pdf");		
		return data;
	}
	@RequestMapping(value = "/imagesaws", produces = "application/pdf")
	public byte[] doge8() throws Exception 
	{
		byte[] data = library.getFromAWSS3Storage("aws.pdf");		
		return data;
	}
	@RequestMapping(value = "/images3proxyazure", produces = "application/pdf")
	public byte[] doge9() throws Exception 
	{
		byte[] data = library.getFromS3ProxyAzureStorage("azure.pdf");		
		return data;
	}
	@RequestMapping(value = "/images3proxyaws", produces = "application/pdf")
	public byte[] doge10() throws Exception 
	{
		byte[] data = library.getFromS3ProxyAWSStorage("aws.pdf");		
		return data;
	}
}
