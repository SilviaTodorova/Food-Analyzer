package bg.sofia.uni.fmi.food.analyzer.server.core.repositories;

import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;

import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;

public class FoodRepositoryImpl implements FoodRepository {

    @Override
    public String getFoodByName(String name) {
        try {
            File file = new File(String.format(PATH_FOOD_NAME_DIR, name));
            return readFile(file);
        } catch (IOException ex) {
            throw new IllegalArgumentException("TODO");
        }

    }

    @Override
    public String getFoodById(Long id) {
        try {
            File file = new File(String.format(PATH_FOOD_ID_DIR, id));
            return readFile(file);
        } catch (IOException ex) {
            throw new IllegalArgumentException("TODO");
        }
    }

    @Override
    public String getFoodByBarcode(String code) {
        try {
            File file = new File(String.format(PATH_FOOD_BARCODE, code));
            return readFile(file);
        } catch (IOException ex) {
            throw new IllegalArgumentException("TODO");
        }

    }

    @Override
    public void saveFoodReportById(long id, String text) {
        try {
            File file = new File(String.format(PATH_FOOD_ID_DIR, id));
            writeInFile(file, text);
        } catch (IOException ex) {
            throw new IllegalArgumentException("TODO");
        }

    }

    @Override
    public void saveFoodReportByName(String name, String text) {
        try {
            File file = new File(String.format(PATH_FOOD_NAME_DIR, name));
            writeInFile(file, text);
        } catch (IOException ex) {
            throw new IllegalArgumentException("TODO");
        }

    }

    @Override
    public void saveFoodReportByBarcode(String code, String text) {
        try {
            File file = new File(String.format(PATH_FOOD_BARCODE, code));
            writeInFile(file, text);
        } catch (IOException ex) {
            throw new IllegalArgumentException("TODO");
        }

    }

    @Override
    public boolean checkFoodExistByName(String name) {
        File file = new File(String.format(PATH_FOOD_NAME_DIR, name));
        return file.exists();
    }

    @Override
    public boolean checkFoodExistById(Long id) {
        File file = new File(String.format(PATH_FOOD_ID_DIR, id));
        return file.exists();
    }

    @Override
    public boolean checkFoodExistByBarcode(String code) {
        File file = new File(String.format(PATH_FOOD_BARCODE, code));
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
