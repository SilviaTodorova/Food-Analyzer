package bg.sofia.uni.fmi.food.analyzer.server.core.factories;

import bg.sofia.uni.fmi.food.analyzer.server.commands.GetFoodByBarcode;
import bg.sofia.uni.fmi.food.analyzer.server.commands.GetFood;
import bg.sofia.uni.fmi.food.analyzer.server.commands.GetFoodReport;
import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.commands.enums.CommandType;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.CommandFactory;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;

import java.util.Arrays;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.INVALID_COMMAND;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.EMPTY_STRING;

public class CommandFactoryImpl implements CommandFactory {
    private static final String REGEX_LETTERS = "[^A-Za-z0-9]";

    @Override
    public Command createCommand(String commandTypeAsString, FoodRepository repository, FoodClient client) {
        String commandTypeAsStringUpperCase = commandTypeAsString.replaceAll(REGEX_LETTERS, EMPTY_STRING).toUpperCase();
        boolean validEnum = Arrays.stream(CommandType.values()).anyMatch(en->en.name().equals(commandTypeAsStringUpperCase));

        if(!validEnum) {
            throw new IllegalArgumentException(String.format(INVALID_COMMAND, commandTypeAsString));
        }

        CommandType commandType = CommandType.valueOf(commandTypeAsStringUpperCase);
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
