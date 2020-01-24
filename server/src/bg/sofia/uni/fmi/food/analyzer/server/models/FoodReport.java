package bg.sofia.uni.fmi.food.analyzer.server.models;

import java.util.ArrayList;
import java.util.List;

public class FoodReport {
    private final long fdcId;
    private final String description;
    private final String ingredients;

    private final List<Nutrient> nutrients;

    public FoodReport(long fdcId, String description, String ingredients) {
        this.fdcId = fdcId;
        this.description = description;
        this.ingredients = ingredients;

        this.nutrients = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public long getFdcId() {
        return fdcId;
    }

    public void addNutrient(Nutrient nutrient) {
        nutrients.add(nutrient);
    }

    public List<Nutrient> getLabelNutrients() {
        return nutrients;
    }
}
