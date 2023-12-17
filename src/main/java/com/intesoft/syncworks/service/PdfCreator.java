package com.intesoft.syncworks.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

//service class for pdf creation
@Service
public class PdfCreator {

   public MultipartFile createPdf(String folderName, List<String> fileNames, List<String> fileLinks) {
       try (PDDocument document = new PDDocument();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

           PDPage page = new PDPage();
           document.addPage(page);

           try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
               contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
               float yPosition = 700;  // Posición Y inicial

               contentStream.beginText();
               contentStream.newLineAtOffset(20, yPosition);

               contentStream.showText("Folder Name: " + folderName);
               yPosition -= 20;

               for (int i = 0; i < fileNames.size(); i++) {
                   String fileName = fileNames.get(i);
                   String fileLink = fileLinks.get(i);

                   contentStream.showText("File Name: " + fileName);
                   contentStream.newLineAtOffset(0, -15);

                   // Agrega un hipervínculo a una URL externa
                   PDAnnotationLink link = new PDAnnotationLink();
                   PDRectangle position = new PDRectangle();
                   position.setLowerLeftX(20);  // Ajusta según tus necesidades
                   position.setLowerLeftY(yPosition - 15);

                   float linkWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(fileLink) / 1000 * 12; // Ajusta el tamaño de la fuente según sea necesario
                   position.setUpperRightX(position.getLowerLeftX() + linkWidth);
                   position.setUpperRightY(position.getLowerLeftY() + 15);

                   link.setRectangle(position);

                   PDActionURI actionURI = new PDActionURI();
                   actionURI.setURI(fileLink);
                   link.setAction(actionURI);

                   page.getAnnotations().add(link);
                   contentStream.showText("File Link: " + fileLink);
                   contentStream.newLineAtOffset(0, -15);

                   yPosition -= 15;
               }

               contentStream.endText();
           }

           document.save(byteArrayOutputStream);
           document.close();

           // Convierte el ByteArrayOutputStream a un InputStream para crear un MultipartFile
           InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

           // Implementación simple de MultipartFile
           return  ByteArrayMultipartFile(inputStream, "output.pdf");
       } catch (IOException e) {
           e.printStackTrace();
           return null;
       }
   }


   public MultipartFile ByteArrayMultipartFile(InputStream inputStream, String name) {
       return new MultipartFile() {
           @Override
           public String getName() {
               return name;
           }

           @Override
           public String getOriginalFilename() {
               return name;
           }

           @Override
           public String getContentType() {
               return "application/pdf";
           }

           @Override
           public boolean isEmpty() {
               return false;
           }

           @Override
           public long getSize() {
               try {
                   return inputStream.available();
               } catch (IOException e) {
                   e.printStackTrace();
                   return 0;
               }
           }

           @Override
           public byte[] getBytes() throws IOException {
               return inputStream.readAllBytes();
           }

           @Override
           public InputStream getInputStream() throws IOException {
               return inputStream;
           }

           @Override
           public void transferTo(java.io.File file) throws IOException, IllegalStateException {
               // No es necesario implementar este método
           }
       };
   }
}
