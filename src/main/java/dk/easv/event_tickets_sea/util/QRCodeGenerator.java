package dk.easv.event_tickets_sea.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    /**
     * Generates a QR code as a JavaFX Image.
     * @param content  the text/code to encode
     * @param size     width and height in pixels
     * @return a JavaFX Image of the QR code
     */
    public static Image generateQRCode(String content, int size) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints);

            WritableImage image = new WritableImage(size, size);
            PixelWriter pixelWriter = image.getPixelWriter();

            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    pixelWriter.setArgb(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return image;

        } catch (WriterException e) {
            System.err.println("QRCodeGenerator: Failed to generate QR code: " + e.getMessage());
            return null;
        }
    }
}
