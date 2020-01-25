package bg.sofia.uni.fmi.food.analyzer.server.exceptions;

public class FoodBarcodeNotFoundException extends Exception {
    public FoodBarcodeNotFoundException(String message) {
        super(message);
    }
}