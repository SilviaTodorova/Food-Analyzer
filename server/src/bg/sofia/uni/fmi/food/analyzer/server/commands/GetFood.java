package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.clients.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.*;

public class GetFood implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    private static final String GET_FOOD_BY_NAME = "get-food";

    private final FoodRepository repository;
    private final FoodClient client;

    private String name;

    public GetFood(FoodRepository repository, FoodClient client) {
        this.repository = repository;
        this.client = client;
    }

    @Override
    public String execute(List<String> parameters) throws IOException, InterruptedException {
        validateInput(parameters);

        parseParameters(parameters);

        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for ")
                .append(name)
                .append(" --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        if (repository.checkFoodExistByName(name)) {
            return builder.append(repository.getFoodByName(name)).toString();
        }

        List<Food> foods = new ArrayList<>(client.getFoodByName(name));
        String response = formatExecutionResult(foods);
        repository.saveFoodReportByName(name, response);

        return builder.append(response).toString();
    }

    private void validateInput(List<String> parameters) {
        if (parameters.size() < EXPECTED_NUMBER_OF_ARGUMENTS) {
            throw new IllegalArgumentException(
                    String.format(INVALID_NUMBER_OF_ARGUMENTS_MESSAGE_FORMAT, EXPECTED_NUMBER_OF_ARGUMENTS, parameters.size()));
        }
    }

    private void parseParameters(List<String> parameters) {
        try {
            name = String.join(DELIMITER, parameters);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format(FAILED_PARSING_PARAMETERS_MESSAGE_FORMAT, GET_FOOD_BY_NAME));
        }
    }

    private String formatExecutionResult(List<Food> foods) {
        if (foods.size() == 0) {
            return NO_FOODS_WERE_FOUND_MESSAGE;
        }

        StringBuilder builder = new StringBuilder();
        for (Food food : foods) {
            append(builder, FDC_ID_FIELD, String.valueOf(food.getFdcId()));
            append(builder, GTIN_UPC_FIELD, String.valueOf(food.getGtinUpc()));
            append(builder, DESCRIPTION_FIELD, String.valueOf(food.getDescription()));
            builder.append(System.lineSeparator());
        }

        return builder.toString().trim();
    }

    private void append(StringBuilder builder, String field, String value) {
        if (!IS_NULL_OR_EMPTY_FIELD_PREDICATE.test(value)) {
            builder.append(field).append(": ").append(value).append(System.lineSeparator());
        }
    }
}
