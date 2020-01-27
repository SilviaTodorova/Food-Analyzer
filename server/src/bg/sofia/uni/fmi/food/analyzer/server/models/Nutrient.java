package bg.sofia.uni.fmi.food.analyzer.server.models;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.FORMAT;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;

public class Nutrient {
    private Value calories;
    private Value protein;
    private Value fat;
    private Value carbohydrates;
    private Value fiber;

    public Nutrient() {
        // Empty body
    }

    public Value getCalories() {
        return calories;
    }

    public void setCalories(Value calories) {
        this.calories = calories;
    }

    public Value getProtein() {
        return protein;
    }

    public void setProtein(Value protein) {
        this.protein = protein;
    }

    public Value getFat() {
        return fat;
    }

    public void setFat(Value fat) {
        this.fat = fat;
    }

    public Value getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Value carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Value getFiber() {
        return fiber;
    }

    public void setFiber(Value fiber) {
        this.fiber = fiber;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        append(builder, CALORIES_FIELD, getCalories());
        append(builder, CARBOHYDRATES_FIELD, getCarbohydrates());
        append(builder, FAT_FIELD, getFat());
        append(builder, FIBER_FIELD, getFiber());
        append(builder, PROTEIN_FIELD, getProtein());
        return builder.toString().trim();
    }

    private void append(StringBuilder builder, String field, Value value) {
        if (value != null) {
            builder.append(field)
                    .append(FORMAT)
                    .append(String.format("%.2f", value.getValue()))
                    .append(System.lineSeparator());
        }
    }
}