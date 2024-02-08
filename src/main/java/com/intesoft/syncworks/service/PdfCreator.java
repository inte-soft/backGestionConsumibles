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
        try (
                InputStream templateStream = getClass().getClassLoader().getResourceAsStream("plantillas/plantilla.pdf");
                PDDocument document = PDDocument.load(templateStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            PDPage page = document.getPage(0);

            try (
                    PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)
            ) {
                contentStream.setFont(PDType1Font.TIMES_BOLD, 10);
                float yPosition = 600;
                float margin = 20;
                float width = page.getMediaBox().getWidth() - 2 * margin;

                String text =
                        "DEMCO INGENIERÍA, es una empresa dinámica dedicada al diseño,  construcción  y  puesta en servicio de subestaciones y tableros\n" +
                        "eléctricos  en  media  y  baja  tensión,  desarrollando  proyectos  con  altas  especificaciones  en ingeniería, en alianza con reconocidas\n" +
                        "empresas  del  sector  eléctrico. Entregamos  a  nuestros   clientes   soluciones   completas   e  integrales  respaldados por  procesos  de\n" +
                        "ingeniería  y  automatización, ágiles y  con importantes alianzas con reconocidas empresas del sector.Somos una empresa Colombiana\n" +
                        "con proyección  hacia el  futuro, contamos  con  productos  de  calidad,  precios  competitivos,  recurso  humano  calificado, capacidad\n" +
                        "operativa y respuesta oportuna a nuestros cliente.                                                                                                                                            \n";

                String[] lines = text.split("\n");

                for (String line : lines) {
                    float stringWidth = PDType1Font.TIMES_BOLD.getStringWidth(line) / 1000 * 10;
                    float xOffset = margin + (width - stringWidth) / 2;

                    contentStream.beginText();
                    contentStream.newLineAtOffset(xOffset, yPosition);
                    contentStream.showText(line);
                    contentStream.endText();
                    yPosition -= 15;
                }

                // Restaurar el espacio adicional después del bloque de texto
                yPosition -= 30;

                // Comenzar nuevo bloque de texto para "Proyecto: "
                contentStream.beginText();
                contentStream.setFont(PDType1Font.COURIER_OBLIQUE, 14);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Proyecto: " + folderName);
                contentStream.endText();
                yPosition -= 20;

                for (int i = 0; i < fileNames.size(); i++) {
                    String fileName = fileNames.get(i);
                    String fileLink = fileLinks.get(i);

                    PDColor blackColor = new PDColor(new float[]{0, 0, 0}, PDDeviceRGB.INSTANCE);
                    contentStream.setNonStrokingColor(blackColor);

                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("Archivo: " + fileName);
                    contentStream.endText();
                    yPosition -= 15;

                    PDColor blueColor = new PDColor(new float[]{0, 0, 1}, PDDeviceRGB.INSTANCE);
                    contentStream.setNonStrokingColor(blueColor);

                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(fileLink);
                    contentStream.endText();
                    yPosition -= 15;

                    PDAnnotationLink link = new PDAnnotationLink();
                    PDActionURI actionURI = new PDActionURI();
                    actionURI.setURI(fileLink);
                    link.setAction(actionURI);

                    PDRectangle position = new PDRectangle();
                    position.setLowerLeftX(margin);
                    position.setLowerLeftY(yPosition);
                    position.setUpperRightX(page.getMediaBox().getWidth() - margin);
                    position.setUpperRightY(yPosition + 30);
                    link.setRectangle(position);

                    page.getAnnotations().add(link);

                    yPosition -= 15;
                }
            }

            document.save(byteArrayOutputStream);
            document.close();

            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

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
            float image1Width = 150;
            float image1Height = 75;
            float image1Margin = 40;

            float image2Width = 100;
            float image2Height = 50;
            float image2Margin = 40;

            float image3Width = 100;
            float image3Height = 50;
            float image3Margin = 40;

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
            e.printStackTrace();
        }
    }
}
