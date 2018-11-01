package seedu.address.commons.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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
     *
     * Limits data to MAX_DATA_SIZE to make QR code suitable for the current QR_SIZE
     */
    public static void generateQR(String data) {
        try {
            if (data.length() > MAX_DATA_SIZE) {
                throw new WriterException("Data size too large");
            }
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE);


            //Solution below adapted from https://docs.oracle.com/javafx/2/image_ops/jfxpub-image_ops.htm
            WritableImage writableImage = new WritableImage(256, 256);
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
            File file = new File("qr.png");
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(bufferedImage, "png", file);
            //@@author
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
