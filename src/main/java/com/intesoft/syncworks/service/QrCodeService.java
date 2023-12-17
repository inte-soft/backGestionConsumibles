package com.intesoft.syncworks.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class QrCodeService {
    private static final String LOGO_PATH = "src/main/resources/static/images/demco.png";

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

        BufferedImage logo = ImageIO.read(new File(logoPath));
        AffineTransform transform = new AffineTransform();
        transform.scale((double) logoSize / logo.getWidth(), (double) logoSize / logo.getHeight());
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        logo = op.filter(logo, null);

        int x = (qrImage.getWidth() - logoSize) / 2;
        int y = (qrImage.getHeight() - logoSize) / 2;

        graphics.drawImage(logo, x, y, null);
        graphics.dispose();
    }
}
