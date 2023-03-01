package espita.client1.proj1.service;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.drive.model.FileList;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.LanguageServiceClient;

import espita.client1.proj1.config.GoogleDriveConfig;
import espita.client1.proj1.entity.CV;

import espita.client1.proj1.repository.CVRepository;
import espita.client1.proj1.repository.SkillsRepository;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

@Service
public class CVService {
    
	 private static final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
	    private static final String DRIVE_FIELDS = "nextPageToken, files(id, name, mimeType)";
	    
    @Autowired
    private CVRepository cvRepository;
    @Autowired
    private SkillsRepository skillsRepository;
    @Autowired
    private GoogleDriveConfig googleDriveConfig;
    
    public List<String> getFoldersInSubFolder(String parentFolderId, String subFolderName) throws IOException, GeneralSecurityException {
        List<String> folderNames = new ArrayList<>();

        String pageToken = null;
        do {
            FileList result = googleDriveConfig.getInstance().files().list()
                    .setQ("mimeType='" + FOLDER_MIME_TYPE + "' and trashed = false and '" + parentFolderId + "' in parents and name='" + subFolderName + "'")
                    .setFields(DRIVE_FIELDS)
                    .setPageToken(pageToken)
                    .execute();

            for (File file : result.getFiles()) {
                String subFolderId = file.getId();
                FileList subResult = googleDriveConfig.getInstance().files().list()
                        .setQ("mimeType='" + FOLDER_MIME_TYPE + "' and trashed = false and '" + subFolderId + "' in parents")
                        .setFields(DRIVE_FIELDS)
                        .execute();

                for (File subFile : subResult.getFiles()) {
                    folderNames.add(subFile.getName());
                }
            }

            pageToken = result.getNextPageToken();
        } while (pageToken != null);

        return folderNames;
    }
    
    
    
   /* public CV extractCVInformation(String filePath) {
        try {
            PDDocument document = Loader.loadPDF(new File(filePath));
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            System.out.println(text);
            String email = extractEmail(text);
            String phone = extractphone(text);
          
            System.out.println(email);
            System.out.println(phone);
            
            CV cv = new CV();
            cv.setText(text);
            cv.setEmail(email);
            cv.setPhone(phone);
       
            return cvRepository.save(cv);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

	public List<Skills> getskills() {
		 List<Skills> cvs = skillsRepository.findAll();
		    return cvs;
	}
	
	private List<String> extractSkills(String text) {
	    List<String> skills = new ArrayList<>();
	    String regexp = "skills\\b(\\W+\\w+){0,10}(education|certificat)";
	    Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(text);
	    if (!matcher.find()) return skills;
	    String matchedText = matcher.group(0);

	    // Extraire les compétences de la chaîne extraite
	    String skillsRegexp = "(?<=skills\\b(\\W+\\w+){0,10})[^.,;:]+";
	    Pattern skillsPattern = Pattern.compile(skillsRegexp, Pattern.CASE_INSENSITIVE);
	    Matcher skillsMatcher = skillsPattern.matcher(matchedText);
	    while (skillsMatcher.find()) {
	        skills.add(skillsMatcher.group().trim());
	    }
	    return skills;
	}
	
	  

	

    
    
          Extract name, email, phone and skills from the text
            String name = extractName(text);
            String email = extractEmail(text);
            String phone = extractPhone(text);
            String skills = extractSkills(text);
            
            // Create and save the CV entity
            CV cv = new CV();
            cv.setName(name);
            cv.setEmail(email);
            cv.setPhone(phone);
            cv.setSkills(skills);
            return cvRepository.save(cv);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private String extractName(String text) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.matches("^\\p{L}+\\s+\\p{L}+.*")) {
                return line.trim();
            }
        }
        return null;
    }
    
    private String extractEmail(String text) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
                return line;
            }
        }
        return null;
    }
    
    private String extractPhone(String text) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.matches(".*\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}.*")) {
                return line.replaceAll("[^0-9]", "");
            }
        }
        return null;
    }
    
    private String extractSkills(String text) {
        String[] lines = text.split("\n");
        String skills = "";
        boolean isSkillsSection = false;
        for (String line : lines) {
            if (line.matches(".*(SKILLS|TECHNICAL SKILLS|COMPETENCES|LANGUAGES|LANGUES).*")) {
                isSkillsSection = true;
            } else if (line.matches(".*(EDUCATION|EXPERIENCE|WORK EXPERIENCE|PROFESSIONAL EXPERIENCE|TRAINING|FORMATION|CERTIFICATIONS|ACTIVITIES|PROJECTS).*")) {
                isSkillsSection = false;
            } else if (isSkillsSection) {
                skills += line + " ";
            }
        }
        return skills.trim();
    }*/
}
