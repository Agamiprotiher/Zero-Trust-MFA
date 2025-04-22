package com.zerotrust.mfa.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.File;
import java.nio.file.Path;

public class QRCodeUtil {
    public static void generateQRCode(String text, String filePath, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            Path path = new File(filePath).toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        } catch (Exception e) {
            throw new RuntimeException("QR Code generation failed", e);
        }
    }
}




