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

    public static final int MAX_DATA_SIZE = 100;
    public static final int QR_SIZE = 256;

    /**
     * Generates a QR code based on the input string. The current output is to an image file, however, depending
     * on the implementation of the mail sending API, this can be changed to return something more suitable.
     * <p>
     * Limits data to MAX_DATA_SIZE to make QR code suitable for the current QR_SIZE
     */
    public static BufferedImage generateQr(String data) throws WriterException {
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
