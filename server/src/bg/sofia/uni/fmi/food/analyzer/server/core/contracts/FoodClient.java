package bg.sofia.uni.fmi.food.analyzer.server.core.contracts;

import bg.sofia.uni.fmi.food.analyzer.server.models.Food;
import bg.sofia.uni.fmi.food.analyzer.server.models.FoodReport;

import java.util.Collection;

public interface FoodClient {
    Collection<Food> getFoodByName(String name);

    FoodReport getFoodReportById(long id);

    Collection<Food> getFoodByBarcode(String barcode);
}
