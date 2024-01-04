package com.intesoft.syncworks.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
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
        //fileNames.add("");
        //fileLinks.add("");
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);

            addHeaderImages(document, page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {

                contentStream.setFont(PDType1Font.TIMES_BOLD, 10);
                float yPosition = 600;  // Posición Y inicial

                contentStream.beginText();
                contentStream.newLineAtOffset(20, yPosition);
                String text = "DEMCO INGENIERÍA, es una empresa dinámica dedicada al diseño, construcción y puesta en servicio de subestaciones y tableros\n" +
                        " eléctricos en media y baja tensión, desarrollando proyectos con altas especificaciones en ingeniería, en alianza con reconocidas\n" +
                        "empresas del sector eléctrico.\n" +
                        "Entregamos a nuestros clientes soluciones completas e integrales respaldados por procesos de ingeniería y automatización, ágiles y\n" +
                        "con importantes alianzas con reconocidas empresas del sector.\n" +
                        "Somos una empresa Colombiana con proyección hacia el futuro, contamos con productos de calidad, precios competitivos, recurso humano\n" +
                        "calificado, capacidad operativa y respuesta oportuna a nuestros cliente.";

                String[] lines = text.split("\n");


                for (String line : lines) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -15);
                    yPosition -= 15;
                }
                contentStream.newLineAtOffset(0, -30);
                yPosition -= 30;
                contentStream.setFont(PDType1Font.COURIER_OBLIQUE, 14);
                contentStream.showText("Proyecto: " + folderName);
                contentStream.newLineAtOffset(0, -20);


                for (int i = 0; i < fileNames.size(); i++) {
                    String fileName = fileNames.get(i);
                    String fileLink = fileLinks.get(i);
                    PDColor blackColor = new PDColor(new float[]{0, 0, 0}, PDDeviceRGB.INSTANCE);
                    contentStream.setNonStrokingColor(blackColor);
                    contentStream.showText("Archivo: " + fileName);
                    contentStream.newLineAtOffset(0, -15);
                    // Establece el color del texto del enlace a azul
                    PDColor blueColor = new PDColor(new float[]{0, 0, 1}, PDDeviceRGB.INSTANCE);
                    contentStream.setNonStrokingColor(blueColor);

                    // Muestra el texto del enlace en azul
                    contentStream.showText(fileLink);
                    contentStream.newLineAtOffset(0, -15);

                    // Crea un hipervínculo para la URL del archivo
                    PDAnnotationLink link = new PDAnnotationLink();
                    PDActionURI actionURI = new PDActionURI();
                    actionURI.setURI(fileLink);
                    link.setAction(actionURI);

                    yPosition -= 15;

                    // Establece el rectángulo del enlace (ancho completo y posición ajustada)
                    PDRectangle position = new PDRectangle();
                    position.setLowerLeftX(20);
                    position.setLowerLeftY(yPosition - 30); // Ajusta según la fuente y el tamaño
                    position.setUpperRightX(page.getMediaBox().getWidth()); // Ancho completo
                    position.setUpperRightY(yPosition); // Ajusta según la fuente y el tamaño
                    link.setRectangle(position);

                    page.getAnnotations().add(link);



                    yPosition -= 15;
                }

                contentStream.endText();
            }

            document.save(byteArrayOutputStream);
            document.close();

            // Convierte el ByteArrayOutputStream a un InputStream para crear un MultipartFile
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            // Implementación simple de MultipartFile
            return ByteArrayMultipartFile(inputStream, "output.pdf");
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
    private void addHeaderImages(PDDocument document, PDPage page) {
        try {
            // Configura las posiciones y tamaños de las imágenes según tus necesidades
            float image1Width = 150;  // ajusta según el tamaño deseado para la imagen 1
            float image1Height =75;  // ajusta según el tamaño deseado para la imagen 1
            float image1Margin = 40;  // ajusta según el margen deseado para la imagen 1

            float image2Width = 100;  // ajusta según el tamaño deseado para la imagen 2
            float image2Height = 50;  // ajusta según el tamaño deseado para la imagen 2
            float image2Margin = 40;  // ajusta según el margen deseado para la imagen 2

            float image3Width = 100;  // ajusta según el tamaño deseado para la imagen 3
            float image3Height = 50;  // ajusta según el tamaño deseado para la imagen 3
            float image3Margin = 40;  // ajusta según el margen deseado para la imagen 3

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                // Carga las imágenes desde el classpath
                InputStream image1Stream = getClass().getClassLoader().getResourceAsStream("static/images/demcoLetras.png");
                InputStream image2Stream = getClass().getClassLoader().getResourceAsStream("static/images/hechoColombia.png");
                InputStream image3Stream = getClass().getClassLoader().getResourceAsStream("static/images/bureaoBeritas.png");

                PDImageXObject image1 = PDImageXObject.createFromByteArray(document, image1Stream.readAllBytes(), "Demco Letras Logo");
                PDImageXObject image2 = PDImageXObject.createFromByteArray(document, image2Stream.readAllBytes(), "Hecho Colombia Logo");
                PDImageXObject image3 = PDImageXObject.createFromByteArray(document, image3Stream.readAllBytes(), "Bureau Logo");

                // Agrega las imágenes con sus respectivos tamaños y márgenes
                contentStream.drawImage(image1, image1Margin, page.getMediaBox().getHeight() - image1Margin - image1Height, image1Width, image1Height);
                contentStream.drawImage(image2, image2Margin + image1Width, page.getMediaBox().getHeight() - image2Margin - image2Height, image2Width, image2Height);
                contentStream.drawImage(image3, image3Margin + image2Margin + image1Width + image2Width, page.getMediaBox().getHeight() - image3Margin - image3Height, image3Width, image3Height);
            }
        } catch (IOException e) {
            // Manejar la excepción de manera adecuada
            e.printStackTrace();
        }
    }
}
