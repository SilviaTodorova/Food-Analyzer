package bg.sofia.uni.fmi.food.analyzer.server.core;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.NO_A_SUCH_FILE;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.NO_BARCODE_IN_THE_IMAGE;

public class BarcodeConverter {

    public static String decodeBarcodeImage(String filePath) {
        try {
            File file = new File(filePath);
            BufferedImage bufferedImage = ImageIO.read(file);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (IOException e) {
            throw new RuntimeException(NO_A_SUCH_FILE);
        } catch (NotFoundException e) {
            throw new RuntimeException(NO_BARCODE_IN_THE_IMAGE);
        }
    }
}
