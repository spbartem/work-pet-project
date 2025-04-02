package ru.fkr.workpetproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import ru.fkr.workpetproject.service.XmlParserService;

import java.io.IOException;

@RestController
@RequestMapping("/api/rostreetr")
public class XmlController {

    private final XmlParserService xmlParserService;

    public XmlController(XmlParserService xmlParserService) {
        this.xmlParserService = xmlParserService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadRosreestrFullInfoXmlFile(@RequestParam("file") MultipartFile file) {
        try {
            xmlParserService.parseAndSaveRosreestrFullInfo(file.getBytes());
            return ResponseEntity.ok("File processed and info saved!");
        } catch (IOException | SAXException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing XML file");
        }
    }
}

