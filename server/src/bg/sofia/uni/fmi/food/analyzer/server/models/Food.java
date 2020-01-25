package bg.sofia.uni.fmi.food.analyzer.server.models;

public class Food {
    private long fdcId;
    private String description;
    private String gtinUpc;

    public Food(long fdcId, String description, String gtinUpc) {
        this.fdcId = fdcId;
        this.description = description;
        this.gtinUpc = gtinUpc;
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
}
