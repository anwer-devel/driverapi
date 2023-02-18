package espita.client1.proj1.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import espita.client1.proj1.entity.CV;
import espita.client1.proj1.repository.CVRepository;
import espita.client1.proj1.service.CVService;

@RestController
public class CVController {
    @Autowired
    private CVRepository cvRepository;
    @Autowired
    private CVService cvService;
    
    @PostMapping("/extractCV")
    public CV extractCV(@RequestParam("file") MultipartFile file) {
        try {
            File convertedFile = convertMultipartFileToFile(file);
            String filePath = convertedFile.getAbsolutePath();
            return cvService.extractCVInformation(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return convertedFile;
    }
    
  /*  @PostMapping("/search")
    public List<String> searchCv(@RequestParam("keyword") String keyword) {
        List<CV> cvs = cvRepository.findByTextContainingIgnoreCase(keyword);
        return cvs.stream().map(CV::getText).collect(Collectors.toList());
    }*/
}
