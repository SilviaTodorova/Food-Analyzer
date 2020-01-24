package bg.sofia.uni.fmi.food.analyzer.server.models;

public class Nutrient {
    private final String name;
    private final double value;

    public Nutrient(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
