package bg.sofia.uni.fmi.food.analyzer.server.core.contracts;

public interface FoodRepository {
    String getFoodById(long id) ;

    String getFoodByName(String name);

    String getFoodByBarcode(String code);

    void saveFoodReportById(long id, String text);

    void saveFoodByName(String name, String text);

    void saveFoodByBarcode(String code, String text);

    boolean checkFoodExistByName(String name);

    boolean checkFoodExistById(long id);

    boolean checkFoodExistByBarcode(String code);
}
