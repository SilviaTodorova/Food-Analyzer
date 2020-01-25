package bg.sofia.uni.fmi.food.analyzer.server.core.contracts;

import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;

public interface FoodRepository {
    String getFoodById(long id) throws FoodIdNotFoundException;

    String getFoodByName(String name) throws FoodNotFoundException;

    String getFoodByBarcode(String code) throws FoodBarcodeNotFoundException;

    void saveFoodReportById(long id, String text) throws FoodIdNotFoundException;

    void saveFoodByName(String name, String text) throws FoodNotFoundException;

    void saveFoodByBarcode(String code, String text) throws FoodBarcodeNotFoundException;

    boolean checkFoodExistByName(String name);

    boolean checkFoodExistById(long id);

    boolean checkFoodExistByBarcode(String code);
}
