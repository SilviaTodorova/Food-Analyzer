package bg.sofia.uni.fmi.food.analyzer.server.core;

import bg.sofia.uni.fmi.food.analyzer.server.exceptions.ImageNotFoundException;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.NO_A_SUCH_FILE;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.NO_BARCODE_IN_THE_IMAGE;

public class BarcodeConverter {

    public static String decodeBarcodeImage(String filePath) throws ImageNotFoundException {
        try {
            File file = new File(filePath);
            BufferedImage bufferedImage = ImageIO.read(file);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (IOException ex) {
            throw new ImageNotFoundException(NO_A_SUCH_FILE);
        } catch (Exception ex) {
            throw new ImageNotFoundException(NO_BARCODE_IN_THE_IMAGE);
        }
    }
}
