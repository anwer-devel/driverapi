package espita.client1.proj1.service;


import org.springframework.web.multipart.MultipartFile;

import espita.client1.proj1.model.GoogleDriveFileDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.List;

public interface IGoogleDriveFile {
    List<GoogleDriveFileDTO> getAllFile() throws IOException, GeneralSecurityException;
    void deleteFile(String id) throws Exception;
    void uploadFile(MultipartFile file, String filePath);
    void downloadFile(String id, OutputStream outputStream) throws IOException, GeneralSecurityException;
}