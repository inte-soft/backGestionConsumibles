package com.intesoft.syncworks.interfaces.web;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.zxing.WriterException;
import com.intesoft.syncworks.config.DriveConfig;
import com.intesoft.syncworks.exceptions.AppException;
import com.intesoft.syncworks.interfaces.dto.ErrorDto;
import com.intesoft.syncworks.interfaces.dto.UploadRequestDto;
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
import org.springframework.web.servlet.view.RedirectView;

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
    public ResponseEntity<?> uploadOt( @RequestParam("files") MultipartFile[] files,
                                                @RequestParam("folderName")String folderName,
                                                @RequestParam("fileNames")List<String> fileNames)  {
        try {
            UploadRequestDto uploadRequestDto = new UploadRequestDto();
            uploadRequestDto.setFileNames(fileNames);
            uploadRequestDto.setFolderName(folderName);
            byte[] qr = new byte[0];
            if (uploadRequestDto.getFileNames().size() != files.length) {
                return ResponseEntity.status(400).body(null); // Bad Request
            }

            File folder = driveService.createFolder(uploadRequestDto.getFolderName());
            List<String[]> filesinformation = new ArrayList<>();
            for (int i = 0; i < files.length+1; i++) {

                if (i == files.length) {
                    List<String> fileLinks = new ArrayList<>();
                    List<String> fileNamesList = new ArrayList<>();
                   for (int j = 0; j < filesinformation.size(); j++) {
                       String[] fileInformation = filesinformation.get(j);
                          fileLinks.add(fileInformation[2]);
                          fileNamesList.add(fileInformation[1]);

                   }
                   MultipartFile pdf = pdfCreator.createPdf(folder.getName(), fileNamesList, fileLinks);
                   filesinformation.add(driveService.uploadFileToDrive(pdf, folder.getName()+".pdf", folder.getId()));
                    qr = qrCodeService.generateQrCodeWithLogo(filesinformation.get(filesinformation.size()-1)[2]);
                }else {
                    MultipartFile file = files[i];
                    String fileName = uploadRequestDto.getFileNames().get(i);
                    // Here you should write the file to a local temporary file, then upload it to Drive
                    // For the sake of simplicity, we'll assume you have a method for this: uploadFileToDrive
                    filesinformation.add(driveService.uploadFileToDrive(file, fileName, folder.getId()));
                }
            }
            /*String qrBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(qr);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);


            return ResponseEntity.ok().headers(headers).body(qrBase64);*/

            Files.write(Paths.get("qrCode.png"), qr);
            return ResponseEntity.ok().body(new FileSystemResource("qrCode.png"));

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
}
