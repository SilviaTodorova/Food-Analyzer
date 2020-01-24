package bg.sofia.uni.fmi.food.analyzer.server.core;

import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;
import bg.sofia.uni.fmi.food.analyzer.server.models.FoodReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;

public class FoodRepositoryImpl implements FoodRepository {
    @Override
    public String getFoodByName(String name) {
        File foodFile = new File(String.format("D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-name\\%s", name));

        try {
            return Files.readString(foodFile.toPath(), Charset.defaultCharset());
        } catch (IOException e) {
//            logger.error(String.format(READING_ERROR, foodFile.getPath()), e);
//            return EMPTY_FILE;
            return null;
        }
    }

    @Override
    public String getFoodById(Long id) {
        File foodFile = new File(String.format("D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-id\\%s", id));

        try {
            return Files.readString(foodFile.toPath(), Charset.defaultCharset());
        } catch (IOException e) {
//            logger.error(String.format(READING_ERROR, foodFile.getPath()), e);
//            return EMPTY_FILE;
            return null;
        }
    }

    @Override
    public String getFoodByBarcode(String code) {
        File foodFile = new File(String.format("D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-barcode\\%s", code));

        try {
            return Files.readString(foodFile.toPath(), Charset.defaultCharset());
        } catch (IOException e) {
//            logger.error(String.format(READING_ERROR, foodFile.getPath()), e);
//            return EMPTY_FILE;
            return null;
        }
    }

    @Override
    public void saveFoodReportById(long id, String content) {
        File foodFile = new File(String.format("D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-id\\%s", id));

        try (PrintWriter printWriter = new PrintWriter(foodFile)) {
            printWriter.write(content);
        } catch (FileNotFoundException e) {
            // logger.error(String.format(WRITING_ERROR, foodFile.getPath()), e);
        }
    }

    @Override
    public void saveFoodReportByName(String name, String content) {
        File foodFile = new File(String.format("D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-name\\%s", name));

        try (PrintWriter printWriter = new PrintWriter(foodFile)) {
            printWriter.write(content);
        } catch (FileNotFoundException e) {
            // logger.error(String.format(WRITING_ERROR, foodFile.getPath()), e);
        }
    }

    @Override
    public void saveFoodReportByBarcode(String code, String content) {
        File foodFile = new File(String.format("D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-barcode\\%s", code));

        try (PrintWriter printWriter = new PrintWriter(foodFile)) {
            printWriter.write(content);
        } catch (FileNotFoundException e) {
            // logger.error(String.format(WRITING_ERROR, foodFile.getPath()), e);
        }
    }

    @Override
    public boolean checkFoodExistByName(String name) {
        File foodFile = new File(String.format("D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-name\\%s", name));
        return foodFile.exists();
    }

    @Override
    public boolean checkFoodExistById(Long id) {
        File foodFile = new File(String.format("D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-id\\%s", id));
        return foodFile.exists();
    }

    @Override
    public boolean checkFoodExistByBarcode(String code) {
        File foodFile = new File(String.format("D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-barcode\\%s", code));
        return foodFile.exists();
    }
}
