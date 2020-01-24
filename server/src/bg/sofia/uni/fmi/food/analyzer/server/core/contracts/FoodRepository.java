package bg.sofia.uni.fmi.food.analyzer.server.core.contracts;

import java.io.IOException;

public interface FoodRepository {
    String getFoodById(Long id) throws IOException;

    String getFoodByName(String name) throws IOException;

    String getFoodByBarcode(String code) throws IOException;

    void saveFoodReportById(long id, String text) throws IOException;

    void saveFoodReportByName(String name, String text) throws IOException;

    void saveFoodReportByBarcode(String code, String text) throws IOException;

    boolean checkFoodExistByName(String name);

    boolean checkFoodExistById(Long id);

    boolean checkFoodExistByBarcode(String code);
}
