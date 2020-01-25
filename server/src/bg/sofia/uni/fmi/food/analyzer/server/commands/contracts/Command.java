package bg.sofia.uni.fmi.food.analyzer.server.commands.contracts;

import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;

import java.util.List;

public interface Command {
    String execute(List<String> parameters) throws FoodIdNotFoundException, FoodBarcodeNotFoundException, FoodNotFoundException;
}
