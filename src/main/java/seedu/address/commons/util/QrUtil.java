package seedu.address.commons.util;

import java.awt.image.BufferedImage;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Helper functions for generating QR codes.
 */
public class QrUtil {

    private static final int MAX_DATA_SIZE = 100;
    private static final int QR_SIZE = 256;

    /**
     * Generates a QR code based on the input string.
     * @param data A string representing the data to be encoded into a QR code
     * @return The encoded QR code
     * @throws WriterException if length of {@code data} is greater than MAX_DATA_SIZE or when {@code data} fails to
     * encode
     */
    public BufferedImage generateQr(String data) throws WriterException {
        if (data.length() > MAX_DATA_SIZE) {
            throw new WriterException("Data size too large");
        }
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE);

        //Solution below adapted from https://docs.oracle.com/javafx/2/image_ops/jfxpub-image_ops.htm
        WritableImage writableImage = new WritableImage(QR_SIZE, QR_SIZE);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for (int i = 0; i < bitMatrix.getHeight(); i++) {
            for (int j = 0; j < bitMatrix.getWidth(); j++) {
                if (bitMatrix.get(i, j)) {
                    pixelWriter.setColor(i, j, Color.BLACK);
                } else {
                    pixelWriter.setColor(i, j, Color.WHITE);
                }
            }
        }
        //@@author {wm28}-reused
        // Reused from https://community.oracle.com/thread/2450090?tstart=0 with minor modifications
        return SwingFXUtils.fromFXImage(writableImage, null);
        //@@author
    }
}
