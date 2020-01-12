package com.tdah.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.springframework.stereotype.Service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;
import com.tdah.service.IAzureStorageService;

@Service
public class AzureStorageServiceImpl implements IAzureStorageService{
	
	

	@Override
	public void uploadFile(String filePath, String nameFile) {
		
		try {
			CloudStorageAccount storageAccount = getConnection();
			CloudFileClient fileClient = storageAccount.createCloudFileClient();
			CloudFileShare share = fileClient.getShareReference("reportes");
			CloudFileDirectory rootDir = share.getRootDirectoryReference();	
			CloudFile cloudFile = rootDir.getFileReference(nameFile);
		    cloudFile.uploadFromFile(filePath);
		} catch (URISyntaxException | StorageException | IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public CloudFile downloadFile(String nameFile) {
		CloudFile file = null;
		try {
			CloudStorageAccount storageAccount = getConnection();
			CloudFileClient fileClient = storageAccount.createCloudFileClient();
			CloudFileShare share = fileClient.getShareReference("reportes");
			
			//Get a reference to the root directory for the share.
			CloudFileDirectory rootDir = share.getRootDirectoryReference();

			//Get a reference to the directory that contains the file
			//CloudFileDirectory sampleDir = rootDir.getDirectoryReference(nameFile);

			//Get a reference to the file you want to download
			file = rootDir.getFileReference(nameFile);
			//Write the contents of the file to the console.
			//System.out.println(file.downloadText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return file;
	}
	
	
	private CloudStorageAccount getConnection() {
		String storageConnectionString = "DefaultEndpointsProtocol=https;" + "AccountName=abelazure;"
				+ "AccountKey=KWTYBQBfPAO7TCD26CDYSZ/4cinUducXKTiHomDcqUySl8wFGzVtf7+HsUlyuKtw21kbhl+rwXyl4Lep0erZfw==";
		CloudStorageAccount storageAccount = null;
		try {
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
		} catch (InvalidKeyException | URISyntaxException e) {
			e.printStackTrace();
		}
		return storageAccount;
		
	}


	

}
