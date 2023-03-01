package espita.client1.proj1.service;


import org.springframework.web.multipart.MultipartFile;

import espita.client1.proj1.model.GoogleDriveFileDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Set;

public interface IGoogleDriveFile {
    List<GoogleDriveFileDTO> getAllFile() throws IOException, GeneralSecurityException;
    void deleteFile(String id) throws Exception;
    void uploadFile(MultipartFile file, String filePath,List<String> skills, String fullname, String titre);
    void downloadFile(String id, OutputStream outputStream) throws IOException, GeneralSecurityException;
	void uploadsubsubFile(String folderId, String subfolderName, String subsubfolderName,String folderName, MultipartFile file,
			List<String> skills, String fullname, String titre) throws IOException, GeneralSecurityException;
	void uploadsubFile(String folderId, String subfolderName, MultipartFile file, List<String> skills, String fullname,
			String titre, String folderName) throws IOException, GeneralSecurityException;
}