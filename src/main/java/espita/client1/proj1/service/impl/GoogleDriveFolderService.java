package espita.client1.proj1.service.impl;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import espita.client1.proj1.model.GoogleDriveFoldersDTO;
import espita.client1.proj1.service.IGoogleDriveFolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleDriveFolderService implements IGoogleDriveFolder {

    @Autowired
    GoogleFileManager googleFileManager;

    @Override
    public List<GoogleDriveFoldersDTO> getAllFolder() throws IOException, GeneralSecurityException {

        List<File> files = googleFileManager.listFolderContent("root");
        List<GoogleDriveFoldersDTO> responseList = null;
        GoogleDriveFoldersDTO dto = null;

        if (files != null) {
            responseList = new ArrayList<>();
            for (File f : files) {
                dto = new GoogleDriveFoldersDTO();
                dto.setId(f.getId());
                dto.setName(f.getName());
                dto.setLink("https://drive.google.com/drive/u/3/folders/"+f.getId());
                responseList.add(dto);
            }
        }
        return responseList;
    }
    @Override
    public List<GoogleDriveFoldersDTO> getSubfolders(String folderId) throws IOException , GeneralSecurityException{
       
    	 List<File> files = googleFileManager.listsubFolderContent(folderId);
         List<GoogleDriveFoldersDTO> responseList = null;
         GoogleDriveFoldersDTO dto = null;

         if (files != null) {
             responseList = new ArrayList<>();
             for (File f : files) {
                 dto = new GoogleDriveFoldersDTO();
                 dto.setId(f.getId());
                 dto.setName(f.getName());
                 dto.setLink("https://drive.google.com/drive/u/3/folders/"+f.getId());
                 responseList.add(dto);
             }
         }
         return responseList;
    }
    @Override
	public List<GoogleDriveFoldersDTO> getsubSubfolders(String folderId, String selectedsubFolderName) throws IOException, GeneralSecurityException {
		 List<File> files = googleFileManager.listsubsubFolderContent(folderId,selectedsubFolderName);
         List<GoogleDriveFoldersDTO> responseList = null;
         GoogleDriveFoldersDTO dto = null;

         if (files != null) {
             responseList = new ArrayList<>();
             for (File f : files) {
                 dto = new GoogleDriveFoldersDTO();
                 dto.setId(f.getId());
                 dto.setName(f.getName());
                 dto.setLink("https://drive.google.com/drive/u/3/folders/"+f.getId());
                 responseList.add(dto);
             }
         }
         return responseList;
    }
    
 

    @Override
    public void createFolder(String folderName) throws Exception {
        String folderId = googleFileManager.getFolderId(folderName);
        System.out.println(folderId);
    }

    @Override
    public void deleteFolder(String id) throws Exception {
        googleFileManager.deleteFileOrFolder(id);
    }

	
	
	
}
