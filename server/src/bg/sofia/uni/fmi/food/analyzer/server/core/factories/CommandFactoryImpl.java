package bg.sofia.uni.fmi.food.analyzer.server.core.factories;

import bg.sofia.uni.fmi.food.analyzer.server.commands.GetFoodByBarcode;
import bg.sofia.uni.fmi.food.analyzer.server.commands.GetFood;
import bg.sofia.uni.fmi.food.analyzer.server.commands.GetFoodReport;
import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.commands.enums.CommandType;
import bg.sofia.uni.fmi.food.analyzer.server.core.clients.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.CommandFactory;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;

public class CommandFactoryImpl implements CommandFactory {
    private static final String INVALID_COMMAND = "Invalid command name: %s!";

    @Override
    public Command createCommand(String commandTypeAsString, FoodRepository repository, FoodClient client) {
            CommandType commandType = CommandType.valueOf(commandTypeAsString.replaceAll("[^A-Za-z0-9]", "").toUpperCase());
            switch (commandType) {
                case GETFOOD:
                    return new GetFood(repository, client);
                case GETFOODREPORT:
                    return new GetFoodReport(repository, client);
                case GETFOODBYBARCODE:
                    return new GetFoodByBarcode(repository, client);
                default:
                    throw new IllegalArgumentException(String.format(INVALID_COMMAND, commandTypeAsString));
            }
        }
}
