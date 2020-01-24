package bg.sofia.uni.fmi.food.analyzer.server.models;

public class Food {
    private long fdcId;
    private String description;
    private String gtinUpc;
    private String ingredients;

    public Food(long fdcId, String description, String gtinUpc, String ingredients) {
        this.fdcId = fdcId;
        this.description = description;
        this.gtinUpc = gtinUpc;
        this.ingredients = ingredients;
    }

    public long getFdcId() {
        return fdcId;
    }

    public String getDescription() {
        return description;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }

    public String getIngredients() {
        return ingredients;
    }
}
