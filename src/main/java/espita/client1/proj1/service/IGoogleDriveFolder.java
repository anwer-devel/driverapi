package espita.client1.proj1.service;



import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.api.services.drive.model.File;

import espita.client1.proj1.model.GoogleDriveFoldersDTO;

public interface IGoogleDriveFolder {
    List<GoogleDriveFoldersDTO> getAllFolder() throws IOException, GeneralSecurityException;
    void createFolder(String folderName) throws Exception;
    void deleteFolder(String id) throws Exception;
	List<GoogleDriveFoldersDTO> getSubfolders(String folderId) throws IOException, GeneralSecurityException;
	List<GoogleDriveFoldersDTO> getsubSubfolders(String folderId, String selectedsubFolderName)throws IOException, GeneralSecurityException;;
	
}