package com.intesoft.syncworks.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

@Service
public class QrCodeService {
    private static final String LOGO_PATH = "static/images/demco.png";

    public byte[] generateQrCodeWithLogo(String qrContent) throws IOException, WriterException {
        int qrCodeSize = 500;


        // Generar el código QR
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);

        BufferedImage qrImage = new BufferedImage(qrCodeSize, qrCodeSize, BufferedImage.TYPE_INT_RGB);
        qrImage.createGraphics();

        // Pintar el código QR en la imagen
        Graphics2D graphics = (Graphics2D) qrImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, qrCodeSize, qrCodeSize);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < qrCodeSize; i++) {
            for (int j = 0; j < qrCodeSize; j++) {
                if (bitMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        System.out.println("antes de buscar la imagen");
        String logoPath = LOGO_PATH;
        // Agregar el logo en el centro
        addLogo(qrImage, logoPath);
        // Convertir la imagen a bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);

        return baos.toByteArray();
    }



    private void addLogo(BufferedImage qrImage, String logoPath) throws IOException {
        Graphics2D graphics = qrImage.createGraphics();
        int logoSize = 50;

        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream logoStream = classLoader.getResourceAsStream(logoPath);
        BufferedImage logo = ImageIO.read(logoStream);

        AffineTransform transform = new AffineTransform();
        transform.scale((double) logoSize / logo.getWidth(), (double) logoSize / logo.getHeight());
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        logo = op.filter(logo, null);

        int x = (qrImage.getWidth() - logoSize) / 2;
        int y = (qrImage.getHeight() - logoSize) / 2;

        graphics.drawImage(logo, x, y, null);
        graphics.dispose();
    }

    public MultipartFile convertBase64ToMultipart(String qrBase64) {
        String[] strings = qrBase64.split(",");
        byte[] qrBytes = Base64.getDecoder().decode(strings[1]);

        return new MultipartFile() {
            @Override
            public String getName() {
                return "qr";
            }

            @Override
            public String getOriginalFilename() {
                return "qr.png";
            }

            @Override
            public String getContentType() {
                return "image/png";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return qrBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return qrBytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(qrBytes);
            }

            @Override
            public void transferTo(java.io.File file) throws IOException, IllegalStateException {
                new FileOutputStream(file).write(qrBytes);
            }
        };
    }
}
