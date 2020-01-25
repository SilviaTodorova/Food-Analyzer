package bg.sofia.uni.fmi.food.analyzer.server.exceptions;

public class FoodNotFoundException extends Exception {
    public FoodNotFoundException(String message) {
        super(message);
    }
}