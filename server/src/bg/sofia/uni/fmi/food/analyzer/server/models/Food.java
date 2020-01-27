package bg.sofia.uni.fmi.food.analyzer.server.models;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.FORMAT;
import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.IS_NULL_OR_EMPTY_PREDICATE;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;

public class Food {
    private long fdcId;
    private String gtinUpc;
    private String description;
    private String ingredients;
    private Nutrient labelNutrients;

    public Food() {
        // Empty body
    }

    public Food(long fdcId, String gtinUpc, String description, String ingredients, Nutrient labelNutrients) {
        this.fdcId = fdcId;
        this.gtinUpc = gtinUpc;
        this.description = description;
        this.ingredients = ingredients;
        this.labelNutrients = labelNutrients;
    }

    public long getFdcId() {
        return fdcId;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }

    public String getDescription() {
        return description;
    }

    public Nutrient getLabelNutrients() {
        return labelNutrients;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        append(builder, FDC_ID_FIELD, String.valueOf(getFdcId()));
        append(builder, GTIN_UPC_FIELD, getGtinUpc());
        append(builder, DESC_FIELD, getDescription());
        return builder.toString();
    }

    private void append(StringBuilder builder, String field, String value) {
        if (!IS_NULL_OR_EMPTY_PREDICATE.test(value)) {
            builder.append(field).append(FORMAT).append(value).append(System.lineSeparator());
        }
    }
}
