package dk.easv.event_tickets_sea.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    /**
     * Generates a QR code as a JavaFX Image (for print preview).
     */
    public static Image generateQRCode(String content, int size) {
        try {
            BitMatrix bitMatrix = encode(content, size);
            WritableImage image = new WritableImage(size, size);
            PixelWriter pixelWriter = image.getPixelWriter();
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    pixelWriter.setArgb(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return image;
        } catch (WriterException e) {
            System.err.println("QRCodeGenerator: Failed to generate QR image: " + e.getMessage());
            return null;
        }
    }

    /**
     * Generates a QR code as a base64-encoded PNG string for embedding in HTML emails.
     * Usage in HTML: <img src="data:image/png;base64,XXXX" />
     */
    public static String generateQRCodeBase64(String content, int size) {
        try {
            BitMatrix bitMatrix = encode(content, size);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (Exception e) {
            System.err.println("QRCodeGenerator: Failed to generate QR base64: " + e.getMessage());
            return null;
        }
    }

    private static BitMatrix encode(String content, int size) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 1);
        return new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
    }
}
