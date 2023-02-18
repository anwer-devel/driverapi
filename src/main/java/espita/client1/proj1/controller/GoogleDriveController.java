package espita.client1.proj1.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import espita.client1.proj1.model.GoogleDriveFileDTO;
import espita.client1.proj1.model.GoogleDriveFoldersDTO;
import espita.client1.proj1.service.impl.GoogleDriveFileService;
import espita.client1.proj1.service.impl.GoogleDriveFolderService;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
public class GoogleDriveController {

    @Autowired
    GoogleDriveFileService googleDriveFileService;

    @Autowired
    GoogleDriveFolderService googleDriveFolderService;

    // Get all file on drive
    @GetMapping("/listfille")
    public ResponseEntity<List<GoogleDriveFileDTO>> listEverything() throws IOException, GeneralSecurityException {
    	

        List<GoogleDriveFileDTO> listFile = googleDriveFileService.getAllFile();
        //List<GoogleDriveFoldersDTO> listFolder = googleDriveFolderService.getAllFolder();
             
       // mav.addObject("DTO_FOLDER", listFolder);
       // mav.addObject("DTO_FILE", listFile);
        return ResponseEntity.ok(listFile);
    }

    // Upload file to public
    @PostMapping(value = "/upload/file",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE} )
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload,
                                @RequestParam("filePath") String pathFile
                               ) {

        System.out.println(pathFile);
        if (pathFile.equals("")){
            pathFile = "Root"; // Save to default folder if the user does not select a folder to save - you can change it
        }
        System.out.println(pathFile);
        googleDriveFileService.uploadFile(fileUpload, pathFile);
        return fileUpload.getName() ;
    }

    // Delete file by id
    @GetMapping("/delete/file/{id}")
    public void deleteFile(@PathVariable String id) throws Exception {
        googleDriveFileService.deleteFile(id);
       
    }

    // Download file
    @GetMapping("/download/file/{id}")
    public void downloadFile(@PathVariable String id, HttpServletResponse response) throws IOException, GeneralSecurityException {
        googleDriveFileService.downloadFile(id, response.getOutputStream());
    }

    // Get all root folder on drive
    @GetMapping("/list/folders") // or {"/list/folders","/list/folders/{parentId}"}
    public ResponseEntity<List<GoogleDriveFoldersDTO>> listFolder() throws IOException, GeneralSecurityException {
      
        List<GoogleDriveFoldersDTO> listFolder = googleDriveFolderService.getAllFolder();
       
        return ResponseEntity.ok(listFolder);
    }

    // Create folder
    @PostMapping("/create/folder")
    public  ResponseEntity<String> createFolder(@RequestParam("folderName") String folderName) throws Exception {
        googleDriveFolderService.createFolder(folderName);
        return ResponseEntity.ok("parentId: "+folderName);
    }

    // Delete folder by id
    @GetMapping("/delete/folder/{id}")
    public void deleteFolder(@PathVariable String id) throws Exception {
        googleDriveFolderService.deleteFolder(id);
       
    }
}