package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.models.*;

import java.util.List;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.*;

public class GetFoodReport implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;

    private final FoodRepository repository;
    private final FoodClient client;

    private long id;

    public GetFoodReport(FoodRepository repository, FoodClient client) {
        this.repository = repository;
        this.client = client;
    }

    @Override
    public String execute(List<String> parameters) throws FoodIdNotFoundException {
        validateInput(parameters);

        parseParameters(parameters);

        if (repository.checkFoodExistById(id)) {
            return repository.getFoodById(id);
        }

        Food food = client.getFoodReportById(id);
        if (food == null || food.getFdcId() == 0) {
            throw new FoodIdNotFoundException(NO_FOODS_WERE_FOUND_MESSAGE);
        }

        String response = formatExecutionResult(food);
        repository.saveFoodReportById(id, response);
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
            id = Long.parseLong(parameters.get(0));
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format(FAILED_PARSING_PARAMETERS_MESSAGE_FORMAT, GET_FOOD_REPORT_COMMAND));
        }
    }

    private String formatExecutionResult(Food food) {
        StringBuilder builder = new StringBuilder();
        builder.append(food.toString());
        builder.append(System.lineSeparator());

        Nutrient nutrient = food.getLabelNutrients();
        if (nutrient != null) {
            builder.append(nutrient);
        }

        return builder.toString().trim();
    }
}
