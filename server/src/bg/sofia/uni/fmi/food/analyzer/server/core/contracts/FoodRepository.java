package bg.sofia.uni.fmi.food.analyzer.server.core.contracts;

public interface FoodRepository {
    String getFoodById(Long id);

    String getFoodByName(String name);

    String getFoodByBarcode(String code);

    void saveFoodReportById(long id, String content);

    void saveFoodReportByName(String name, String content);

    void saveFoodReportByBarcode(String code, String content);

    boolean checkFoodExistByName(String name);

    boolean checkFoodExistById(Long id);

    boolean checkFoodExistByBarcode(String code);
}
