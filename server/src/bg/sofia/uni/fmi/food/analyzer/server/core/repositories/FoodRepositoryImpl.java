package bg.sofia.uni.fmi.food.analyzer.server.core.repositories;

import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;

public class FoodRepositoryImpl implements FoodRepository {
    private static final String NO_DATA_MESSAGE_FORMAT = "No data for %s in cache";

    private final String pathFoodNameDir;
    private final String pathFoodIdDir;
    private final String pathFoodBarcodeDir;

    public FoodRepositoryImpl(Path pathHome) {
        pathFoodNameDir = Paths.get(pathHome.toString(), FOOD_NAME_DIR).toString();
        pathFoodIdDir = Paths.get(pathHome.toString(), FOOD_ID_DIR).toString();
        pathFoodBarcodeDir = Paths.get(pathHome.toString(), FOOD_BARCODE_DIR).toString();
    }

    @Override
    public String getFoodByName(String name) throws FoodNotFoundException {
        try {
            Path path = Paths.get(pathFoodNameDir, name);
            File file = new File(path.toString());
            return readFile(file);
        } catch (IOException ex) {
            throw new FoodNotFoundException(String.format(NO_DATA_MESSAGE_FORMAT, name));
        }

    }

    @Override
    public String getFoodById(long id) throws FoodIdNotFoundException {
        try {
            Path path = Paths.get(pathFoodIdDir, String.valueOf(id));
            File file = new File(path.toString());
            return readFile(file);
        } catch (IOException ex) {
            throw new FoodIdNotFoundException(String.format(NO_DATA_MESSAGE_FORMAT, id));
        }
    }

    @Override
    public String getFoodByBarcode(String code) throws FoodBarcodeNotFoundException {
        try {
            Path path = Paths.get(pathFoodBarcodeDir, code);
            File file = new File(path.toString());
            return readFile(file);
        } catch (IOException ex) {
            throw new FoodBarcodeNotFoundException(String.format(NO_DATA_MESSAGE_FORMAT, code));
        }

    }

    @Override
    public void saveFoodReportById(long id, String text) throws FoodIdNotFoundException {
        try {
            Path path = Paths.get(pathFoodIdDir, String.valueOf(id));
            File file = new File(path.toString());
            writeInFile(file, text);
        } catch (IOException ex) {
            throw new FoodIdNotFoundException(String.format(NO_DATA_MESSAGE_FORMAT, id));
        }
    }

    @Override
    public void saveFoodByName(String name, String text) throws FoodNotFoundException {
        try {
            Path path = Paths.get(pathFoodNameDir, name);
            File file = new File(path.toString());
            writeInFile(file, text);
        } catch (IOException ex) {
            throw new FoodNotFoundException(String.format(NO_DATA_MESSAGE_FORMAT, name));
        }
    }

    @Override
    public void saveFoodByBarcode(String code, String text) throws FoodBarcodeNotFoundException {
        try {
            Path path = Paths.get(pathFoodBarcodeDir, code);
            File file = new File(path.toString());
            writeInFile(file, text);
        } catch (IOException ex) {
            throw new FoodBarcodeNotFoundException(String.format(NO_DATA_MESSAGE_FORMAT, code));
        }
    }

    @Override
    public boolean checkFoodExistByName(String name) {
        Path path = Paths.get(pathFoodNameDir, name);
        File file = new File(path.toString());
        return file.exists();
    }

    @Override
    public boolean checkFoodExistById(long id) {
        Path path = Paths.get(pathFoodIdDir, String.valueOf(id));
        File file = new File(path.toString());
        return file.exists();
    }

    @Override
    public boolean checkFoodExistByBarcode(String code) {
        Path path = Paths.get(pathFoodBarcodeDir, code);
        File file = new File(path.toString());
        return file.exists();
    }

    private String readFile(File file) throws IOException {
        return Files.readString(file.toPath(), Charset.defaultCharset());
    }

    private void writeInFile(File file, String text) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.write(text);
        }
    }
}
