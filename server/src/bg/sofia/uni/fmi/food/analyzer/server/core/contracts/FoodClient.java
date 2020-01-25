package bg.sofia.uni.fmi.food.analyzer.server.core.contracts;

import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;
import bg.sofia.uni.fmi.food.analyzer.server.models.FoodReport;

import java.util.Collection;

public interface FoodClient {
    Collection<Food> getFoodByName(String name) throws FoodNotFoundException;

    FoodReport getFoodReportById(long id) throws FoodIdNotFoundException;

    Collection<Food> getFoodByBarcode(String barcode) throws FoodBarcodeNotFoundException;
}
