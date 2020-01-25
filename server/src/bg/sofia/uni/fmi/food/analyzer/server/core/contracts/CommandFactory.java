package bg.sofia.uni.fmi.food.analyzer.server.core.contracts;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.clients.FoodClientImpl;

public interface CommandFactory {
    Command createCommand(String commandTypeAsString, FoodRepository repository, FoodClient client);
}
