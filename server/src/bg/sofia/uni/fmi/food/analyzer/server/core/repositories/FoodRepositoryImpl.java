package bg.sofia.uni.fmi.food.analyzer.server.core.repositories;

import bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;

public class FoodRepositoryImpl implements FoodRepository {

    private String pathFoodNameDir;
    private String pathFoodIdDir;
    private String pathFoodBarcodeDir;

    public FoodRepositoryImpl(Path pathHome) {
        pathFoodNameDir = Paths.get(pathHome.toString(), FOOD_NAME_DIR).toString();
        pathFoodIdDir = Paths.get(pathHome.toString(), FOOD_ID_DIR).toString();
        pathFoodBarcodeDir = Paths.get(pathHome.toString(), FOOD_BARCODE_DIR).toString();
    }

    @Override
    public String getFoodByName(String name) {
        try {
            Path path = Paths.get(pathFoodNameDir, name);
            File file = new File(path.toString());
            return readFile(file);
        } catch (IOException ex) {
            throw new IllegalArgumentException("TODO");
        }

    }

    @Override
    public String getFoodById(long id) {
        try {
            Path path = Paths.get(pathFoodIdDir, String.valueOf(id));
            File file = new File(path.toString());
            return readFile(file);
        } catch (IOException ex) {
            throw new IllegalArgumentException("TODO");
        }
    }

    @Override
    public String getFoodByBarcode(String code) {
        try {
            Path path = Paths.get(pathFoodBarcodeDir, code);
            File file = new File(path.toString());
            return readFile(file);
        } catch (IOException ex) {
            throw new IllegalArgumentException("TODO");
        }

    }

    @Override
    public void saveFoodReportById(long id, String text) {
        try {
            Path path = Paths.get(pathFoodIdDir, String.valueOf(id));
            File file = new File(path.toString());
            writeInFile(file, text);
        } catch (IOException ex) {
            throw new IllegalArgumentException("TODO");
        }

    }

    @Override
    public void saveFoodByName(String name, String text) {
        try {
            Path path = Paths.get(pathFoodNameDir, name);
            File file = new File(path.toString());
            writeInFile(file, text);
        } catch (IOException ex) {
            throw new IllegalArgumentException("TODO");
        }

    }

    @Override
    public void saveFoodByBarcode(String code, String text) {
        try {
            Path path = Paths.get(pathFoodBarcodeDir, code);
            File file = new File(path.toString());
            writeInFile(file, text);
        } catch (IOException ex) {
            throw new IllegalArgumentException("TODO");
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
