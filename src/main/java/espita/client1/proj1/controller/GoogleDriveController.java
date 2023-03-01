package espita.client1.proj1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import espita.client1.proj1.entity.CV;
import espita.client1.proj1.entity.Skills;
import espita.client1.proj1.model.GoogleDriveFileDTO;
import espita.client1.proj1.model.GoogleDriveFoldersDTO;
import espita.client1.proj1.repository.CVRepository;
import espita.client1.proj1.repository.SkillsRepository;
import espita.client1.proj1.service.CVService;
import espita.client1.proj1.service.impl.GoogleDriveFileService;
import espita.client1.proj1.service.impl.GoogleDriveFolderService;
import espita.client1.proj1.service.impl.GoogleFileManager;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
public class GoogleDriveController {
	
	@Autowired
    private CVRepository cvRepository;
	
	@Autowired
    private SkillsRepository skillsRepository;
	
	@Autowired
    GoogleFileManager googleFileManager;


	@Autowired
	GoogleDriveFileService googleDriveFileService;


	@Autowired
	GoogleDriveFolderService googleDriveFolderService;
	@Autowired
	 CVService cvservice;
    
	@Autowired
	public GoogleDriveController(GoogleDriveFileService googleDriveFileService,CVService cvservice,
			GoogleDriveFolderService googleDriveFolderService) {
		this.googleDriveFolderService = googleDriveFolderService;
		this.googleDriveFileService = googleDriveFileService;
		this.cvservice=cvservice;
		
	}

	// Get all file on drive
	@GetMapping("/listfille")
	public ResponseEntity<List<GoogleDriveFileDTO>> listEverything() throws IOException, GeneralSecurityException {

		List<GoogleDriveFileDTO> listFile = googleDriveFileService.getAllFile();
		// List<GoogleDriveFoldersDTO> listFolder =
		// googleDriveFolderService.getAllFolder();

		// mav.addObject("DTO_FOLDER", listFolder);
		// mav.addObject("DTO_FILE", listFile);
		return ResponseEntity.ok(listFile);
	}

	// Upload file to subfolder
	@PostMapping(value = "/upload/subfile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> uploadsubFile(@RequestParam("folderId") String folderId,
			@RequestParam("subfolderName") String subfolderName, @RequestParam("file") MultipartFile file,
			@RequestParam(value = "skills", required = false) List<String> skills,
			@RequestParam("fullname") String fullname,
			@RequestParam("titre") String titre,@RequestParam("folder") String folderName) {
		try {
			googleDriveFileService.uploadsubFile(folderId,subfolderName, file,skills,fullname,titre,folderName);
			return ResponseEntity.ok().body("File uploaded successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("File upload failed: " + e.getMessage());
		}
	}
	// Upload file to subsubfolder
	@PostMapping("/uploadsubsubfile")
	public ResponseEntity<String> uploadsubFile(@RequestParam("folderId") String folderId,
	        @RequestParam("subfolderName") String subfolderName,
	        @RequestParam("subsubfolderName") String subsubfolderName,
	        @RequestParam("file") MultipartFile file,@RequestParam("folder") String folderName,
	        @RequestParam(required=false, name="skills") List<String> skills
	        ,@RequestParam("fullname") String fullname,@RequestParam("titre") String titre) {
	    try {
	        googleDriveFileService.uploadsubsubFile(folderId, subfolderName, subsubfolderName,folderName, file, skills,fullname,titre);
	        return ResponseEntity.ok().body("File uploaded successfully!");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("File upload failed: " + e.getMessage());
	    }
	}

	// Upload file to public
	@PostMapping(value = "/upload/file", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload,
			@RequestPart("filePath") String pathFile,
			@RequestParam(value = "skills", required = false) List<String> skills,
			@RequestParam("fullname") String fullname,
			@RequestParam("titre") String titre) {

		System.out.println(pathFile);
		if (pathFile.equals("")) {
			pathFile = "Root"; // Save to default folder if the user does not select a folder to save - you can
								// change it
		}
		System.out.println(pathFile);
		googleDriveFileService.uploadFile(fileUpload, pathFile,skills,fullname,titre);
		return fileUpload.getName();
	}

	// Delete file by id
	@GetMapping("/delete/file/{id}")
	public void deleteFile(@PathVariable String id) throws Exception {
		googleDriveFileService.deleteFile(id);

	}

	// Download file
	@GetMapping("/download/file/{id}")
	public void downloadFile(@PathVariable String id, HttpServletResponse response)
			throws IOException, GeneralSecurityException {
		googleDriveFileService.downloadFile(id, response.getOutputStream());
	}

	// Get all root folder on drive
	@GetMapping("/list/folders") // or {"/list/folders","/list/folders/{parentId}"}
	public ResponseEntity<List<GoogleDriveFoldersDTO>> listFolder() throws IOException, GeneralSecurityException {

		List<GoogleDriveFoldersDTO> listFolder = googleDriveFolderService.getAllFolder();

		return ResponseEntity.ok(listFolder);
	}

	@GetMapping("/subfolders/{folderId}")
	public ResponseEntity<List<GoogleDriveFoldersDTO>> getSubfolders(@PathVariable("folderId") String folderId)
			throws IOException, GeneralSecurityException {

		List<GoogleDriveFoldersDTO> listFolder = googleDriveFolderService.getSubfolders(folderId);

		return ResponseEntity.ok(listFolder);
	}
	@GetMapping("/subfolders/subb/{folderId}/{selectedsubFolderName}")
	public ResponseEntity<List<GoogleDriveFoldersDTO>> getsubSubfolders(@PathVariable String folderId,@PathVariable String selectedsubFolderName)
			throws IOException, GeneralSecurityException {

		List<GoogleDriveFoldersDTO> listFolder = googleDriveFolderService.getsubSubfolders(folderId,selectedsubFolderName);

		return ResponseEntity.ok(listFolder);
	}
	
	/* @GetMapping("/subsubfolders")
	    public List<String> getFoldersInSubFolder(@RequestParam("folderId") String parentFolderId, @RequestParam("selectedsubFolderName") String subFolderName) throws IOException, GeneralSecurityException {
	        return cvservice.getFoldersInSubFolder(parentFolderId, subFolderName);
	    }*/

	// Create folder
	@PostMapping("/create/folder")
	public ResponseEntity<String> createFolder(@RequestParam("folderName") String folderName) throws Exception {
		googleDriveFolderService.createFolder(folderName);
		return ResponseEntity.ok("parentId: " + folderName);
	}
	
	 /*  @PostMapping("/createFolder")
	    public ResponseEntity<File> createFolder(
	            @RequestParam("folderName") String folderName,
	            @RequestParam("folderid") String parentFolderId,
	            @RequestParam("subFolderName") String subFolderName,
	            @RequestParam("subsubFolderName") String subsubFolderName) {
	        try {
	            File folder = googleDriveFolderService.createFolders(folderName, parentFolderId);
	            if (subFolderName != null) {
	                File subFolder = googleDriveFolderService.createFolders(subFolderName, folder.getId());
	                if (subFolderName != null) {
	                	googleDriveFolderService.createFolders(subsubFolderName, subFolder.getId());
	                }
	            }
	            return new ResponseEntity<>(folder, HttpStatus.OK);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }*/
	

	// Delete folder by id
	@GetMapping("/delete/folder/{id}")
	public void deleteFolder(@PathVariable String id) throws Exception {
		googleDriveFolderService.deleteFolder(id);

	}
	
	  @GetMapping("/getallcv")
	    public ResponseEntity<List<CV>> getAllCVs() {
		  List<CV> cvs = cvRepository.findAll();
	      return ResponseEntity.ok().body(cvs);
	    }
	  @GetMapping("/getskills")
	    public ResponseEntity<List<Skills>> getskills() {
		  List<Skills> cvs = skillsRepository.findAll();
	      return ResponseEntity.ok().body(cvs);
	    }
	  
	  @PostMapping("/creatskills/{skills}")
		    public Skills createSkills(@PathVariable String skills) {
		        Skills skillss = new Skills(skills);
		        return skillsRepository.save(skillss);
		    }

	    
	  
	  @GetMapping("/folderr/{folder}")
	    public List<CV> getCVsByFolder(@PathVariable String folder) {
		  
		  return cvRepository.findByFolder(folder);
	  }
	  
	  @GetMapping("/subfolder/{subfolder}")
	  public List<CV> getCVsBySubfolder(@PathVariable String subfolder) {
	      // Utilisation de la méthode findBySubfolder() de CVRepository pour récupérer les CVs du sous-dossier spécifié
	      return cvRepository.findBySubfolder(subfolder);
	  }

	  @GetMapping("/subsubfolder/{subsubfolder}")
	  public List<CV> getCVsBySubsubfolder(@PathVariable String subsubfolder) {
	      // Utilisation de la méthode findBySubsubfolder() de CVRepository pour récupérer les CVs du sous-sous-dossier spécifié
	      return cvRepository.findBySubsubfolder(subsubfolder);
	  }
		  
		  
	  
	
}