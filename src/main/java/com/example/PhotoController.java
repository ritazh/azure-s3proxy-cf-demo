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
		byte[] photo = library.getFromAzureBlobStorage("albums-azure.json");		
		return photo;
	}
	@RequestMapping(value = "/aws", produces = "application/json")
	public byte[] doge4() throws Exception 
	{
		byte[] photo = library.getFromAWSS3Storage("albums.json");		
		return photo;
	}
	@RequestMapping(value = "/s3proxyazure", produces = "application/json")
	public byte[] doge5() throws Exception 
	{
		byte[] photo = library.getFromS3ProxyAzureStorage("albums-s3proxy.json");		
		return photo;
	}
	@RequestMapping(value = "/s3proxyaws", produces = "application/json")
	public byte[] doge6() throws Exception 
	{
		byte[] photo = library.getFromS3ProxyAWSStorage("albums.json");		
		return photo;
	}
	
	@RequestMapping(value = "/imagesazure", produces = "application/pdf")
	public byte[] doge7() throws Exception 
	{
		byte[] photo = library.getFromAzureBlobStorage("azure.pdf");		
		return photo;
	}
	@RequestMapping(value = "/imagesaws", produces = "application/pdf")
	public byte[] doge8() throws Exception 
	{
		byte[] photo = library.getFromAWSS3Storage("aws.pdf");		
		return photo;
	}
	@RequestMapping(value = "/images3proxyazure", produces = "application/pdf")
	public byte[] doge9() throws Exception 
	{
		byte[] photo = library.getFromS3ProxyAzureStorage("azure.pdf");		
		return photo;
	}
	@RequestMapping(value = "/images3proxyaws", produces = "application/pdf")
	public byte[] doge10() throws Exception 
	{
		byte[] photo = library.getFromS3ProxyAWSStorage("aws.pdf");		
		return photo;
	}
}
