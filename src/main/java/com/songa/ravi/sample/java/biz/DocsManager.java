package com.songa.ravi.sample.java.biz;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

@Component
public class DocsManager {
	
	public static final String storageConnectionString = "DefaultEndpointsProtocol=https;"
			+ "AccountName=teststorageravi03;"
			+ "AccountKey=SfPD3y/6u+8Ll7phUNgjLicZh0gzvScnVG97phjKuOowBA/xyrEeiAwGV9ioHz6AXHM3kYYd8Vb3T8qK0P4Ccw==";
	
	
	public byte[] getPng() {
		
		System.out.println("Download Example");

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		
		try {
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference("sample");
			CloudBlockBlob dlBlob = container.getBlockBlobReference("test/1.png");
			
			System.out.println(dlBlob.getProperties().getContentType());
			return downloadImage(dlBlob);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	private byte[] downloadImage(CloudBlockBlob imgBlob){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			imgBlob.download(bos);
		} catch (StorageException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	
	private byte[] convertImage(InputStream in) throws IOException {

        byte[] buff = new byte[10000];

        int bytesRead = 0;

        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        while((bytesRead = in.read(buff)) != -1) {
           bao.write(buff, 0, bytesRead);
        }

        byte[] data = bao.toByteArray();

 		return data;
		
	}
	
	
	public void postPng(InputStream file, String fileName) {
		
		System.out.println("Upload Example");

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		
		try {
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference("sample");
			CloudBlockBlob dlBlob = container.getBlockBlobReference(fileName);
			byte[] byteArray = convertImage(file);
			ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
			dlBlob.upload(bis, byteArray.length);
			System.out.println(dlBlob.getProperties().getContentType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
