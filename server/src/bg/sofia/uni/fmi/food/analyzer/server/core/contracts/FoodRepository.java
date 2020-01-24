package bg.sofia.uni.fmi.food.analyzer.server.core.contracts;

public interface FoodRepository {
    String getFoodById(Long id) ;

    String getFoodByName(String name);

    String getFoodByBarcode(String code);

    void saveFoodReportById(long id, String text);

    void saveFoodReportByName(String name, String text);

    void saveFoodReportByBarcode(String code, String text);

    boolean checkFoodExistByName(String name);

    boolean checkFoodExistById(Long id);

    boolean checkFoodExistByBarcode(String code);
}
