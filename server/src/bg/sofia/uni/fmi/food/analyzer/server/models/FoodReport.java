package bg.sofia.uni.fmi.food.analyzer.server.models;

import java.util.ArrayList;
import java.util.List;

public class FoodReport extends Food{
    private List<Nutrient> labelNutrients;

    public FoodReport(long fdcId, String description, String gtinUpc, String ingredients) {
        super(fdcId, description, gtinUpc, ingredients);
        this.labelNutrients = new ArrayList<>();
    }

    public List<Nutrient> getLabelNutrients() {
        return labelNutrients;
    }

    public void setLabelNutrients(List<Nutrient> labelNutrients) {
        this.labelNutrients = labelNutrients;
    }
}
