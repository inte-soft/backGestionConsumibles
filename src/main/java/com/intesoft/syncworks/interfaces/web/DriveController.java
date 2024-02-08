package com.intesoft.syncworks.interfaces.web;

import com.google.api.services.drive.model.File;
import com.google.zxing.WriterException;
import com.intesoft.syncworks.exceptions.AppException;
import com.intesoft.syncworks.interfaces.dto.*;
import com.intesoft.syncworks.service.DriveService;
import com.intesoft.syncworks.service.PdfCreator;
import com.intesoft.syncworks.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/drive")
public class DriveController {

    private final DriveService driveService;
    private final PdfCreator pdfCreator;

    private final QrCodeService qrCodeService;


    @Autowired
    public DriveController(DriveService driveService) {

        this.driveService = driveService;
        this.pdfCreator = new PdfCreator();
        this.qrCodeService = new QrCodeService();
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

    @PostMapping("/upload-ot")
    public ResponseEntity<?> uploadOt(@RequestParam("ot") String ot,
                                      @RequestParam("name") String nombre,
                                      @RequestParam("names") List<String> nombres,
                                      @RequestParam("files") MultipartFile[] files) {
        try {
            // Crea la carpeta en Google Drive con el nombre recibido

            MultipartFile[] archivos = files;

            File folder = driveService.createFolder((ot + " "+ nombre).toString());
            System.out.println("se creo carpeta");

            List<String[]> filesInformation = new ArrayList<>();
            List<String> fileLinks = new ArrayList<>();
            for (int i = 0; i < archivos.length; i++) {
                MultipartFile file = archivos[i];
                String fileName = nombres.get(i);
                filesInformation.add(driveService.uploadFileToDrive(file, fileName, folder.getId()));
                System.out.println("se subio archivo");
                fileLinks.add(driveService.getFileLink(filesInformation.get(i)[0]));
                System.out.println("se obtuvo link");
            }


            MultipartFile pdf = pdfCreator.createPdf(folder.getName(), nombres, fileLinks);
                   filesInformation.add(driveService.uploadFileToDrive(pdf, folder.getName()+".pdf", folder.getId()));
                   System.out.println("se subio pdf");

            byte[] qr = new byte[0];
                    qr = qrCodeService.generateQrCodeWithLogo(filesInformation.get(filesInformation.size()-1)[2]);

                    System.out.println("se creo qr");

            String qrBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(qr);
            System.out.println("se convirtio qr a base64");
            //transformar qr en base64 a multipartfile
            MultipartFile qrMultipart = qrCodeService.convertBase64ToMultipart(qrBase64);
            System.out.println("se convirtio qr a multipart");
            driveService.uploadFileToDrive(qrMultipart, "qr", folder.getId() );
            System.out.println("se subio qr a drive");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);


            return ResponseEntity.ok().headers(headers).body(qrBase64);

            /*Files.write(Paths.get("qrCode.png"), qr);
            return ResponseEntity.ok().body(new FileSystemResource("qrCode.png"));*/

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDto("Error al procesar la solicitud: " + e.getMessage()));


        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDto> handleAppException(AppException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorDto);
    }

    //metodo para obtener el listado de carpetas que hay en drive con su id y nombre con limite de registros a entregar
    @GetMapping("/folders{limit}")
    public ResponseEntity<List<Folder>> listFolders(@PathVariable Integer limit) throws IOException {
        List<Folder> folders = driveService.listFolders(limit);
        return ResponseEntity.ok().body(folders);
    }


    @GetMapping("/folder/search/{folderName}")
    public ResponseEntity<List<Folder>> searchFolder(@PathVariable String folderName) throws IOException {
       List<Folder> folders = driveService.searchFolders(folderName);
        return ResponseEntity.ok().body(folders);
    }

    //metodo para listar archivos dentro de una carpeta
    @GetMapping("/files/{folderId}")
    public ResponseEntity<List<File>> listFilesInFolder(@PathVariable String folderId) throws IOException {
        List<File> files = driveService.listFilesInFolder(folderId);
        return ResponseEntity.ok().body(files);
    }

    //metodo para obtener enlace de un archivo en google drive
    @GetMapping("/get-link/{fileId}")
    public ResponseEntity<String> getFileLink(
            @PathVariable String fileId) throws IOException {
        String fileLink = driveService.getFileLink(fileId);
        return ResponseEntity.ok().body(fileLink);
    }

    //metodo para subir un unico archivo archivo a una carpeta en google drive y modificar el pdf de la carpeta con el nuevo archivo
    @PostMapping("/upload-file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("name") String fileName,
                                        @RequestParam("folderId") String folderId) {
        try {
            // Sube el archivo a Google Drive
            String[] fileInformation = driveService.uploadFileToDrive(file, fileName, folderId);
            List<File> filesInFolder = driveService.listFilesInFolder(folderId);
            List<String> fileNames = new ArrayList<>();
            List<String> fileLinks = new ArrayList<>();
            for (File fileInFolder : filesInFolder) {
               // obtener el nombre y el links de cada archivo en la lista
                fileNames.add(fileInFolder.getName());
                fileLinks.add(driveService.getFileLink(fileInFolder.getId()));
            }

            MultipartFile pdf = pdfCreator.createPdf(driveService.getFolderName(folderId), fileNames, fileLinks);
            driveService.updateFile(driveService.searchFilePP(folderId), pdf);

            return ResponseEntity.ok().body(driveService.listFilesInFolder(folderId));


        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDto("Error al procesar la solicitud: " + e.getMessage()));
        }
    }


    @GetMapping("/download-qr{folderId}")
    public ResponseEntity<?> downloadQr(@PathVariable String folderId) throws IOException {
        byte[] qr = driveService.downloadQr(folderId);
        String qrBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(qr);
        return ResponseEntity.ok().body(qrBase64);
    }

    //metodo para borrar una carpeta con sus archivos en google drive
    @DeleteMapping("/delete-folder/{folderId}")
    public ResponseEntity<ResponseDto> deleteFolder(@PathVariable String folderId) throws IOException {
        driveService.deleteFolder(folderId);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setInfo("Carpeta eliminada", 200, "message");
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/delete-file/{fileId}")
    public ResponseEntity<ResponseDto> deleteFile(@PathVariable String fileId) throws IOException {
        driveService.deleteFile(fileId);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setInfo("Archivo eliminado", 200, "message");
        return ResponseEntity.ok().body(responseDto);
    }
}
