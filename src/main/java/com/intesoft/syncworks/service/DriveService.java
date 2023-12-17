package com.intesoft.syncworks.service;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.intesoft.syncworks.config.DriveConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class DriveService {

    private Drive drive;
    private final DriveConfig driveConfig;
    @Autowired
    public DriveService(DriveConfig driveConfig)  {
        try {
            this.driveConfig = driveConfig;  // Initialize driveConfig field
            this.drive = driveConfig.getInstance();
        } catch (IOException | GeneralSecurityException e) {
            // Handle the exception here, logging the error or taking appropriate action
            e.printStackTrace();
            throw new RuntimeException("Error initializing DriveService", e);
        }
    }

    public List<File> listFiles() throws IOException {
        return drive.files().list().setFields("files(id, name)").execute().getFiles();
    }

    public File createFolder(String folderName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        File file = drive.files().create(fileMetadata)
                .setFields("id, name")
                .execute();
        setPermission(file.getId());
        return file;
    }



    public String[] uploadFileToDrive(MultipartFile file, String fileName, String  folderId) throws IOException {
        // Crea el objeto File con la información del archivo y la carpeta
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        // Convierte el MultipartFile a ByteArrayContent
        ByteArrayContent mediaContent = new ByteArrayContent(file.getContentType(), file.getBytes());

        // Sube el archivo a Google Drive
        File uploadedFile = drive.files().create(fileMetadata, mediaContent)
                .setFields("id, name")
                .execute();
        setPermission(uploadedFile.getId());
        return new String[]{uploadedFile.getId(), uploadedFile.getName(), getFileLink(uploadedFile.getId())};


    }

    // metodo para dar permisos de lectura a una carpeta en google drive
    public void setPermission(String folderId) throws IOException {
        // Crea el permiso para que cualquier usuario pueda ver la carpeta
        com.google.api.services.drive.model.Permission permission = new com.google.api.services.drive.model.Permission();
        permission.setType("anyone");
        permission.setRole("reader");

        // Envía la solicitud de permisos
        drive.permissions().create(folderId, permission).execute();
    }

    // metodo para obtener enlace de carpeta en google drive
    public String getFolderLink(String folderId) throws IOException {
        // Crea el permiso para que cualquier usuario pueda ver la carpeta
        com.google.api.services.drive.model.Permission permission = new com.google.api.services.drive.model.Permission();
        permission.setType("anyone");
        permission.setRole("reader");

        // Envía la solicitud de permisos
        drive.permissions().create(folderId, permission).execute();
        return "https://drive.google.com/drive/folders/"+folderId;
    }

    // metodo para obtener enlace de archivo en google drive
    public String getFileLink(String fileId) throws IOException {
        // Crea el permiso para que cualquier usuario pueda ver la carpeta
        com.google.api.services.drive.model.Permission permission = new com.google.api.services.drive.model.Permission();
        permission.setType("anyone");
        permission.setRole("reader");

        // Envía la solicitud de permisos
        drive.permissions().create(fileId, permission).execute();
        return "https://drive.google.com/file/d/"+fileId;
    }


}
