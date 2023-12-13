package com.intesoft.syncworks.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.intesoft.syncworks.config.DriveConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class DriveService {

    private Drive drive;

    @Autowired
    public DriveService(DriveConfig driveConfig)  {
        try {
            this.drive = driveConfig.getInstance();
        } catch (IOException | GeneralSecurityException e) {
            // Handle the exception here, logging the error or taking appropriate action
            e.printStackTrace();
        }
    }

    public List<File> listFiles() throws IOException {
        return drive.files().list().setFields("files(id, name)").execute().getFiles();
    }
}