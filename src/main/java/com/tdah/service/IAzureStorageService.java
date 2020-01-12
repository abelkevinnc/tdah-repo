package com.tdah.service;

import com.microsoft.azure.storage.file.CloudFile;

public interface IAzureStorageService {
	void uploadFile(String path, String nameFile);
	CloudFile downloadFile(String nameFile);
}
