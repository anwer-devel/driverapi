package espita.client1.proj1.service.impl;

import com.google.api.services.drive.model.File;

import espita.client1.proj1.model.GoogleDriveFileDTO;
import espita.client1.proj1.service.IGoogleDriveFile;
import espita.client1.proj1.utils.ConvertByteToMB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleDriveFileService implements IGoogleDriveFile {

    @Autowired
    GoogleFileManager googleFileManager;

    @Override
    public List<GoogleDriveFileDTO> getAllFile() throws IOException, GeneralSecurityException {

        List<GoogleDriveFileDTO> responseList = null;
        List<File> files = googleFileManager.listEverything();
        GoogleDriveFileDTO dto = null;

        if (files != null) {
            responseList = new ArrayList<>();
            for (File f : files) {
                dto = new GoogleDriveFileDTO();
                if (f.getSize() != null) {
                    dto.setId(f.getId());
                    dto.setName(f.getName());
                    dto.setThumbnailLink(f.getThumbnailLink());
                    dto.setSize(ConvertByteToMB.getSize(f.getSize()));
                    dto.setLink("https://drive.google.com/file/d/" + f.getId() + "/view?usp=sharing");
                    dto.setShared(f.getShared());

                    responseList.add(dto);
                }
            }
        }
        return responseList;
    }

    @Override
    public void deleteFile(String id) throws Exception {
        googleFileManager.deleteFileOrFolder(id);
    }

    @Override
    public void uploadFile(MultipartFile file, String filePath) {
     
        googleFileManager.uploadFile(file, filePath);
    }

    @Override
    public void downloadFile(String id, OutputStream outputStream) throws IOException, GeneralSecurityException {
        googleFileManager.downloadFile(id, outputStream);
    }

}
