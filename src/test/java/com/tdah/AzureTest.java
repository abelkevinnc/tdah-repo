package com.tdah;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;

@SpringBootTest
public class AzureTest {
	@Test
	void contextLoads() throws StorageException {

		String storageConnectionString = "DefaultEndpointsProtocol=https;" + "AccountName=abelazure;"
				+ "AccountKey=KWTYBQBfPAO7TCD26CDYSZ/4cinUducXKTiHomDcqUySl8wFGzVtf7+HsUlyuKtw21kbhl+rwXyl4Lep0erZfw==";

		try {
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
			System.out.println(storageAccount.getFileEndpoint());

			// Create the Azure Files client.
			CloudFileClient fileClient = storageAccount.createCloudFileClient();
			// Get a reference to the file share

			CloudFileShare share = fileClient.getShareReference("reportes");

			CloudFileDirectory rootDir = share.getRootDirectoryReference();

			//Get a reference to the sampledir directory
			CloudFileDirectory sampleDir = rootDir.getDirectoryReference("grado_estudios");

			// Define the path to a local file.
		    final String filePath = "C:\\windows-version.txt";
		
		    CloudFile cloudFile = sampleDir.getFileReference("Readme2.txt");
		    cloudFile.uploadFromFile(filePath);


		} catch (InvalidKeyException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
