package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;

import java.util.ArrayList;
import java.util.List;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.*;

public class GetFood implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    private final FoodRepository repository;
    private final FoodClient client;

    private String name;

    public GetFood(FoodRepository repository, FoodClient client) {
        this.repository = repository;
        this.client = client;
    }

    @Override
    public String execute(List<String> parameters) throws FoodNotFoundException {
        validateInput(parameters);

        parseParameters(parameters);

        if (repository.checkFoodExistByName(name)) {
            return repository.getFoodByName(name);
        }

        List<Food> foods = new ArrayList<>(client.getFoodByName(name));

        if (foods.size() == 0) {
            throw new FoodNotFoundException(NO_FOODS_WERE_FOUND_MESSAGE);
        }

        StringBuilder builder = new StringBuilder();
        foods.stream().forEach(fd -> builder.append(fd).append(System.lineSeparator()));
        String response = builder.toString().trim();

        repository.saveFoodByName(name, response);

        return response;
    }

    private void validateInput(List<String> parameters) {
        if (parameters.size() < EXPECTED_NUMBER_OF_ARGUMENTS) {
            throw new IllegalArgumentException(
                    String.format(
                            INVALID_NUMBER_OF_ARGUMENTS_MESSAGE_FORMAT,
                            EXPECTED_NUMBER_OF_ARGUMENTS,
                            parameters.size()));
        }
    }

    private void parseParameters(List<String> parameters) {
        try {
            name = String.join(GlobalConstants.DELIMITER, parameters);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format(FAILED_PARSING_PARAMETERS_MESSAGE_FORMAT, GET_FOOD_COMMAND));
        }
    }
}
