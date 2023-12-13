package com.intesoft.syncworks.interfaces.web;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.intesoft.syncworks.config.DriveConfig;
import com.intesoft.syncworks.service.DriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
@RequestMapping("/drive")
public class DriveController {

    private final DriveService driveService;

    @Autowired
    public DriveController(DriveService driveService) {
        this.driveService = driveService;
    }

    @GetMapping("/files")
    public ResponseEntity<List<File>> listFiles() throws IOException, GeneralSecurityException {
        try {
            List<File> files = driveService.listFiles();
            return ResponseEntity.ok().body(files);
        } catch (IOException e) {
            // Log the error or return an appropriate response
            return ResponseEntity.status(500).body(null);
        }
    }
}
