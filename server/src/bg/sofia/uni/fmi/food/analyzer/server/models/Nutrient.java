package bg.sofia.uni.fmi.food.analyzer.server.models;

public class Nutrient {
    private String name;
    private double value;

    public Nutrient(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    private void setValue(double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }
}
