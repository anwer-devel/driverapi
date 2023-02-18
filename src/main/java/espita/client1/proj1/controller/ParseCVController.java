/*package espita.client1.proj1.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import espita.client1.proj1.entity.Condidat;



@RestController
public class ParseCVController {
	
	@Autowired
   // private ResumeService resumeService;

    @PostMapping("/resume")
    public String saveResume(@RequestParam("file") MultipartFile file) throws IOException {
        PDDocument document = Loader.loadPDF(file.getInputStream());
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        //System.out.println(text);

        Condidat condida = new  Condidat();
        // implement parsing algorithms to extract information from the text
        
       return text;
    }

	

}*/
