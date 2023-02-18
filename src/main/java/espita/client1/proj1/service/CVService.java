package espita.client1.proj1.service;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import espita.client1.proj1.entity.CV;
import espita.client1.proj1.repository.CVRepository;

@Service
public class CVService {
    
    @Autowired
    private CVRepository cvRepository;
    
    public CV extractCVInformation(String filePath) {
        try {
            PDDocument document = Loader.loadPDF(new File(filePath));
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            System.out.println(text);
            String email = extractEmail(text);
            System.out.println(email);
            
            CV cv = new CV();
            cv.setText(text);
            cv.setEmail(email);
            
            return cvRepository.save(cv);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private String extractEmail(String text) {
    	String regexp = "(\\+\\d{3}\\s*)?\\d{2}(\\s*\\d{3}){2}";
    	  Pattern pattern = Pattern.compile(regexp);
    	  Matcher matcher = pattern.matcher(text);
    	  if (!matcher.find()) return null;
    	  return matcher.group();
    	}

    
    
          /*  Extract name, email, phone and skills from the text
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
