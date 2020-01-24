package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.clients.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;
import bg.sofia.uni.fmi.food.analyzer.server.models.FoodReport;
import bg.sofia.uni.fmi.food.analyzer.server.models.Nutrient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.*;

public class GetFoodReport implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    private static final String GET_FOOD_REPORT = "get-food-report";

    private final FoodRepository repository;
    private final FoodClient client;
    private final Gson gson;

    private Long id;

    public GetFoodReport(FoodRepository repository, FoodClient client) {
        this.repository = repository;
        this.client = client;
        this.gson = new Gson();
    }

    @Override
    public String execute(List<String> parameters) {
        validateInput(parameters);

        parseParameters(parameters);

        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for fdcId: ")
                .append(id)
                .append(" --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        FoodReport report = null;
        if (repository.checkFoodExistById(id)) {
            return builder.append(repository.getFoodById(id)).toString();
        }

        report = client.getFoodReportById(id);
        String response = formatExecutionResult(report);
        repository.saveFoodReportById(id, response);
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
            id = Long.parseLong(parameters.get(0));
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format(FAILED_PARSING_PARAMETERS_MESSAGE_FORMAT, GET_FOOD_REPORT));
        }
    }

    private String formatExecutionResult(FoodReport report) {
        if (report == null) {
            return NO_FOODS_WERE_FOUND_MESSAGE;
        }

        StringBuilder builder = new StringBuilder();
        append(builder, DESCRIPTION_FIELD, String.valueOf(report.getDescription()));
        append(builder, INCREDIENTS_FIELD, String.valueOf(report.getIngredients()));

        for (Nutrient nut : report.getLabelNutrients()) {
            if(nut.getName().equals("calories")
               || nut.getName().equals("protein")
               || nut.getName().equals("fat")
               || nut.getName().equals("carbohydrates")
               || nut.getName().equals("fiber")) {
                append(builder, nut.getName(), String.format("%.2f", nut.getValue()));
            }
        }
        return builder.toString();
    }

    private void append(StringBuilder builder, String field, String value) {
        if (!value.equals("null") && !value.equals("")) {
            builder.append(field).append(": ").append(value).append(System.lineSeparator());
        }
    }
}
