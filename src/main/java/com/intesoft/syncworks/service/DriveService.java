package com.intesoft.syncworks.service;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.intesoft.syncworks.config.DriveConfig;
import com.intesoft.syncworks.interfaces.dto.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DriveService {

    private Drive drive;
    private final DriveConfig driveConfig;

    @Autowired
    public DriveService(DriveConfig driveConfig) {
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


    public String[] uploadFileToDrive(MultipartFile file, String fileName, String folderId) throws IOException {
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
        return "https://drive.google.com/drive/folders/" + folderId;
    }

    // metodo para obtener enlace de archivo en google drive
    public String getFileLink(String fileId) throws IOException {
        // Crea el permiso para que cualquier usuario pueda ver la carpeta
        com.google.api.services.drive.model.Permission permission = new com.google.api.services.drive.model.Permission();
        permission.setType("anyone");
        permission.setRole("reader");

        // Envía la solicitud de permisos
        drive.permissions().create(fileId, permission).execute();
        return "https://drive.google.com/file/d/" + fileId;
    }


    public List<Folder> listFolders(Integer limit) {

        try {
            List<Folder> folders = new ArrayList<>();

            List<File> files = drive.files().list()
                    .setQ("mimeType='application/vnd.google-apps.folder'")
                    .setFields("files(id, name, createdTime)")
                    .setOrderBy("createdTime desc")
                    .setPageSize(limit)
                    .execute()
                    .getFiles();
            for (File file : files) {
                Folder folder = new Folder();
                folder.setId(file.getId());
                folder.setOt(file.getName().substring(0, 5));
                folder.setName(file.getName().substring(6));
                folders.add(folder);
            }

            return folders;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<File> listFilesInFolder(String folderId) {
        try {
            List<File> files =  drive.files().list()
                    .setQ("'" + folderId + "' in parents")
                    .setFields("files(id, name)")
                    .execute()
                    .getFiles();
            String folderName = getFolderName(folderId);
            List<File> files2 = files.stream().filter(file -> !file.getName().equals(folderName+".pdf")).toList();
            return files2.stream().filter(file -> !file.getName().equals("qr")).toList();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //metodo para buscar un archivo por su nombre en un listado de archivos y devolver el id de ese archivo
    public String searchFile(String fileName, List<File> files) {
        for (File file : files) {
            if (file.getName().equals(fileName)) {
                return file.getId();
            }
        }
        return null;
    }

    public String getFolderName(String folderId) {
        try {
            return drive.files().get(folderId).execute().getName();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteFile(String fileId) {
        try {
            drive.files().delete(fileId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //metodo para sobrescribir un archivo en google drive
    public void updateFile(String fileId, MultipartFile file) throws IOException {
        // Convierte el MultipartFile a ByteArrayContent
        ByteArrayContent mediaContent = new ByteArrayContent(file.getContentType(), file.getBytes());

        // Actualiza el archivo en Google Drive
        drive.files().update(fileId, null, mediaContent).execute();
    }

    //metodo para buscar un archivo que tiene el nombre de la carpeta + .pdf y devolver el id de ese archivo
    public String searchFilePP(String folderId) {
        try {
            String folderName = getFolderName(folderId);
            List<File> files = drive.files().list()
                    .setQ("'" + folderId + "' in parents")
                    .setFields("files(id, name)")
                    .execute()
                    .getFiles();
            for (File file : files) {
                if (file.getName().equals(folderName+".pdf")) {
                    return file.getId();
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] downloadQr(String folderId) {
        try {
            List<File> files = drive.files().list()
                    .setQ("'" + folderId + "' in parents")
                    .setFields("files(id, name)")
                    .execute()
                    .getFiles();
            for (File file : files) {
                if (file.getName().equals("qr")) {
                    return drive.files().get(file.getId()).executeMediaAsInputStream().readAllBytes();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void deleteFolder(String folderId) {
        //se debe borrar la carpeta y los archivos que en esta hay
        try {
            List<File> files = drive.files().list()
                    .setQ("'" + folderId + "' in parents")
                    .setFields("files(id, name)")
                    .execute()
                    .getFiles();
            for (File file : files) {
                deleteFile(file.getId());
            }
            deleteFile(folderId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Folder> searchFolders(String folderName) {
    try {
        List<Folder> folders = new ArrayList<>();
        List<File> files = drive.files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and name contains '" + folderName + "'")
                .setFields("files(id, name)")
                .execute()
                .getFiles();
        for (File file : files) {
            Folder folder = new Folder();
            folder.setId(file.getId());
            folder.setOt(file.getName().substring(0, 5));
            folder.setName(file.getName());
            folders.add(folder);
        }
        return folders;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}
}
