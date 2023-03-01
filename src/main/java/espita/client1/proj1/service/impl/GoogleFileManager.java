package espita.client1.proj1.service.impl;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;

import espita.client1.proj1.config.GoogleDriveConfig;
import espita.client1.proj1.entity.CV;
import espita.client1.proj1.entity.CVSkills;

import espita.client1.proj1.repository.CVRepository;
import espita.client1.proj1.repository.SkillsRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;


@Component
public class GoogleFileManager {
      
	
	private static final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
    private static final String DRIVE_FIELDS = "nextPageToken, files(id, name, mimeType)";

    @Autowired
    private GoogleDriveConfig googleDriveConfig;
    @Autowired
    private CVRepository cvRepository;
    @Autowired
    SkillsRepository skillRepository;

    // Get all file
    public List<File> listEverything() throws IOException, GeneralSecurityException {
        // Print the names and IDs for up to 10 files.
        FileList result = googleDriveConfig.getInstance().files().list()
                .setPageSize(1000) // Limit item want to get
                .setFields("nextPageToken, files(id, name, size, thumbnailLink, shared)") // get field of google drive file
                .execute();
        return result.getFiles();
    }

    // Get all folder
    public List<File> listFolderContent(String parentId) throws IOException, GeneralSecurityException {
        if (parentId == null) {
            parentId = "root";
        }
        String query = "'" + parentId + "' in parents";
        FileList result = googleDriveConfig.getInstance().files().list()
                .setQ(query)
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)") // get field of google drive folder
                .execute();
        return result.getFiles();
    }
    
    public List<File> listsubFolderContent(String folderId) throws IOException, GeneralSecurityException{

        FileList result =googleDriveConfig.getInstance().files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and trashed = false and '" + folderId + "' in parents")
                .setFields("files(id, name)")
                .execute();

        return result.getFiles();
    }
    
    public List<File> listsubsubFolderContent(String folderId, String selectedsubFolderName) throws IOException, GeneralSecurityException {
    	List<File> folders = new ArrayList<>();

    	FileList result =googleDriveConfig.getInstance().files().list()
                 .setQ("mimeType='application/vnd.google-apps.folder' and trashed = false and '" + folderId + "' in parents and name='" + selectedsubFolderName + "'")
                 .setFields("files(id, name)")
                 .execute();
    	 for (File file : result.getFiles()) {
             String subFolderId = file.getId();
             FileList subResult = googleDriveConfig.getInstance().files().list()
                     .setQ("mimeType='application/vnd.google-apps.folder' and trashed = false and '" + subFolderId + "' in parents")
                     .setFields(DRIVE_FIELDS)
                     .execute();
             folders.addAll(subResult.getFiles());
         }
    	

         return folders;
     }
    
    public List<File> getFoldersAndSubfolders(String folderId) throws IOException, GeneralSecurityException {
        List<File> result = new ArrayList<>();
        String query = "mimeType='application/vnd.google-apps.folder' and trashed=false";
        if (folderId != null && !folderId.isEmpty()) {
            query += " and '" + folderId + "' in parents";
        }
        FileList files =googleDriveConfig.getInstance().files().list().setQ(query).setFields("nextPageToken, files(id, name)").execute();
        result.addAll(files.getFiles());
        for (File file : files.getFiles()) {
            if (file.getMimeType().equals("application/vnd.google-apps.folder")) {
                result.addAll(getFoldersAndSubfolders(file.getId()));
            }
        }
        return result;
    }
    
   
     
    

    // Download file by id
    public void downloadFile(String id, OutputStream outputStream) throws IOException, GeneralSecurityException {
        if (id != null) {
            googleDriveConfig.getInstance().files()
                    .get(id).executeMediaAndDownloadTo(outputStream);
        }
    }

    // Delete file by id
    public void deleteFileOrFolder(String fileId) throws Exception {
        googleDriveConfig.getInstance().files().delete(fileId).execute();
    }

    // Set permission drive file
   private Permission setPermission(String type, String role){
        Permission permission = new Permission();
        permission.setType(type);
        permission.setRole(role);
        return permission;
    }


    // Upload file in folder 
    public String uploadFile(MultipartFile file, String folderName, List<String> skills, String fullname, String titre) {
        try {
        	  PDDocument document = Loader.loadPDF(file.getInputStream());
              String text = new PDFTextStripper().getText(document);
              document.close();
              String email = extractEmail(text);
              String phone = extractphone(text);
            String folderId = getFolderId(folderName);
            if (null != file) {

                File fileMetadata = new File();
                fileMetadata.setParents(Collections.singletonList(folderId));
                fileMetadata.setName(file.getOriginalFilename());
                File uploadedFile = googleDriveConfig.getInstance()
                        .files()
                        .create(fileMetadata, new InputStreamContent(
                                file.getContentType(),
                                new ByteArrayInputStream(file.getBytes()))
                        )
                        .setFields("id, webContentLink, webViewLink, thumbnailLink").execute();
                
                CV cv = new CV();
                cv.setFilename(fullname);
                cv.setTitre(titre);
                cv.setText(text);
                cv.setFolder(folderName);
               
                cv.setShareLink(uploadedFile.getWebContentLink());
                cv.setDownloadLink(uploadedFile.getWebViewLink());
                cv.setThumbnailLink(uploadedFile.getThumbnailLink());
                cv.setEmail(email);
                cv.setPhone(phone);
                List<CVSkills> cvSkills = new ArrayList<>();
                for (String skill : skills) {
                	CVSkills cvSkill = new CVSkills();
                    cvSkill.setName(skill);
                    cvSkill.setCv(cv);
                    cvSkills.add(cvSkill);
                }
                cv.setSkills(cvSkills);
                cvRepository.save(cv);

              
                return uploadedFile.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 // Upload file in subfolder 
    public String uploadsubFile(String folderId, String subfolderName, MultipartFile file, List<String> skills,
    		String fullname, String titre, String folderName) throws IOException, GeneralSecurityException {
    	// find the subfolder within the specified folder
    	 PDDocument document = Loader.loadPDF(file.getInputStream());
         String text = new PDFTextStripper().getText(document);
         document.close();
         String email = extractEmail(text);
         String phone = extractphone(text);
        FileList subfolders = googleDriveConfig.getInstance().files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and trashed = false and parents in '" + folderId + "' and name = '" + subfolderName + "'")
                .setFields("files(id)")
                .execute();
        
        // create the subfolder if it doesn't exist
        String subfolderId;
        if (subfolders.getFiles().isEmpty()) {
            File subfolder = new File();
            subfolder.setName(subfolderName);
            subfolder.setMimeType("application/vnd.google-apps.folder");
            subfolder.setParents(Collections.singletonList(folderId));
            File createdSubfolder = googleDriveConfig.getInstance().files().create(subfolder).setFields("id").execute();
            subfolderId = createdSubfolder.getId();
        } else {
            subfolderId = subfolders.getFiles().get(0).getId();
        }
      
        // upload the file to the subfolder
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());
        fileMetadata.setParents(Collections.singletonList(subfolderId));
        File uploadedFile = googleDriveConfig.getInstance().files().create(fileMetadata, new InputStreamContent(
                file.getContentType(),
                new ByteArrayInputStream(file.getBytes())))
                .setFields("id, webContentLink, webViewLink, thumbnailLink")
                .execute();
        
        CV cv = new CV();
        cv.setFilename(fullname);
        cv.setTitre(titre);
        cv.setText(text);
        cv.setFolder(folderName);
        cv.setSubfolder(subfolderName);
        cv.setShareLink(uploadedFile.getWebContentLink());
        cv.setDownloadLink(uploadedFile.getWebViewLink());
        cv.setThumbnailLink(uploadedFile.getThumbnailLink());
        cv.setEmail(email);
        cv.setPhone(phone);
        List<CVSkills> cvSkills = new ArrayList<>();
        for (String skill : skills) {
        	CVSkills cvSkill = new CVSkills();
            cvSkill.setName(skill);
            cvSkill.setCv(cv);
            cvSkills.add(cvSkill);
        }
        cv.setSkills(cvSkills);
        cvRepository.save(cv);
        
        return uploadedFile.getId();
    }
    
    // Upload file in subsubfolder 
    public String uploadsubsubFile(String folderId, String subfolderName, String subsubfolderName, String folderName1, MultipartFile file,
    		List<String> skills, String fullname, String titre) throws IOException, GeneralSecurityException {
        // find the subfolder within the specified folder
    	 PDDocument document = Loader.loadPDF(file.getInputStream());
         String text = new PDFTextStripper().getText(document);
         document.close();
         String email = extractEmail(text);
         String phone = extractphone(text);
        FileList subfolders = googleDriveConfig.getInstance().files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and trashed = false and parents in '" + folderId + "' and name = '" + subfolderName + "'")
                .setFields("files(id)")
                .execute();

        // create the subfolder if it doesn't exist
        String subfolderId;
        if (subfolders.getFiles().isEmpty()) {
            File subfolder = new File();
            subfolder.setName(subfolderName);
            subfolder.setMimeType("application/vnd.google-apps.folder");
            subfolder.setParents(Collections.singletonList(folderId));
            File createdSubfolder = googleDriveConfig.getInstance().files().create(subfolder).setFields("id").execute();
            subfolderId = createdSubfolder.getId();
        } else {
            subfolderId = subfolders.getFiles().get(0).getId();
        }

        // find the sub-subfolder within the subfolder
        FileList subsubfolders = googleDriveConfig.getInstance().files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and trashed = false and parents in '" + subfolderId + "' and name = '" + subsubfolderName + "'")
                .setFields("files(id)")
                .execute();

        // create the sub-subfolder if it doesn't exist
        String subsubfolderId;
        if (subsubfolders.getFiles().isEmpty()) {
            File subsubfolder = new File();
            subsubfolder.setName(subsubfolderName);
            subsubfolder.setMimeType("application/vnd.google-apps.folder");
            subsubfolder.setParents(Collections.singletonList(subfolderId));
            File createdSubsubfolder = googleDriveConfig.getInstance().files().create(subsubfolder).setFields("id").execute();
            subsubfolderId = createdSubsubfolder.getId();
        } else {
            subsubfolderId = subsubfolders.getFiles().get(0).getId();
        }

        // upload the file to the sub-subfolder
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());
        fileMetadata.setParents(Collections.singletonList(subsubfolderId));
        File uploadedFile = googleDriveConfig.getInstance().files().create(fileMetadata, new InputStreamContent(
                file.getContentType(),
                new ByteArrayInputStream(file.getBytes())))
                .setFields("id, webContentLink, webViewLink, thumbnailLink")
                .execute();
        CV cv = new CV();
        cv.setFilename(fullname);
        cv.setTitre(titre);
        cv.setText(text);
        cv.setFolder(folderName1);
        cv.setSubfolder(subfolderName);
        cv.setSubsubfolder(subsubfolderName);
        cv.setShareLink(uploadedFile.getWebContentLink());
        cv.setDownloadLink(uploadedFile.getWebViewLink());
        cv.setThumbnailLink(uploadedFile.getThumbnailLink());
        cv.setEmail(email);
        cv.setPhone(phone);
        List<CVSkills> cvSkills = new ArrayList<>();
        for (String skill : skills) {
        	CVSkills cvSkill = new CVSkills();
            cvSkill.setName(skill);
            cvSkill.setCv(cv);
            cvSkills.add(cvSkill);
        }
        cv.setSkills(cvSkills);
        cvRepository.save(cv);
        return uploadedFile.getId();
    }
    
	

    // get id folder google drive
    public String getFolderId(String folderName) throws Exception {
        String parentId = null;
        String[] folderNames = folderName.split("/");

        Drive driveInstance = googleDriveConfig.getInstance();
        for (String name : folderNames) {
            parentId = findOrCreateFolder(parentId, name, driveInstance);
        }
        return parentId;
    }

    private String findOrCreateFolder(String parentId, String folderName, Drive driveInstance) throws Exception {
        String folderId = searchFolderId(parentId, folderName, driveInstance);
        // Folder already exists, so return id
        if (folderId != null) {
            return folderId;
        }
        //Folder dont exists, create it and return folderId
        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        if (parentId != null) {
            fileMetadata.setParents(Collections.singletonList(parentId));
        }
        return driveInstance.files().create(fileMetadata)
                .setFields("id")
                .execute()
                .getId();
    }

    private String searchFolderId(String parentId, String folderName, Drive service) throws Exception {
        String folderId = null;
        String pageToken = null;
        FileList result = null;

        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        do {
            String query = " mimeType = 'application/vnd.google-apps.folder' ";
            if (parentId == null) {
                query = query + " and 'root' in parents";
            } else {
                query = query + " and '" + parentId + "' in parents";
            }
            result = service.files().list().setQ(query)
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageToken(pageToken)
                    .execute();

            for (File file : result.getFiles()) {
                if (file.getName().equalsIgnoreCase(folderName)) {
                    folderId = file.getId();
                }
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null && folderId == null);

        return folderId;
    }
    
    
    private String extractEmail(String text) {
    	String regexp ="[\\w.\\-]+@[\\w.\\-]+\\.\\w{2,}";
    	  Pattern pattern = Pattern.compile(regexp);
    	  Matcher matcher = pattern.matcher(text);
    	  if (!matcher.find()) return null;
    	  return matcher.group();
    	}

private String extractphone(String text) {
	String regexp = "(\\+\\d{3}\\s*)?\\d{2}(\\s*\\d{3}){2}";
	  Pattern pattern = Pattern.compile(regexp);
	  Matcher matcher = pattern.matcher(text);
	  if (!matcher.find()) return null;
	  return matcher.group();
	}

	
public List<CV> getAllCVs() {
	 List<CV> cvs = cvRepository.findAll();
	    return cvs;
}








	

	}

