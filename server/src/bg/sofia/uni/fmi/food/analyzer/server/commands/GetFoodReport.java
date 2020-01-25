package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants;
import bg.sofia.uni.fmi.food.analyzer.server.core.clients.FoodClientImpl;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.models.FoodReport;
import bg.sofia.uni.fmi.food.analyzer.server.models.Nutrient;

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
    public String execute(List<String> parameters) {
        validateInput(parameters);

        parseParameters(parameters);

        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for fdcId: ")
                .append(id)
                .append(" --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        FoodReport report;
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
                    String.format(FAILED_PARSING_PARAMETERS_MESSAGE_FORMAT, GET_FOOD_REPORT_COMMAND));
        }
    }

    private String formatExecutionResult(FoodReport report) {
        if (report == null) {
            return NO_FOODS_WERE_FOUND_MESSAGE;
        }

        StringBuilder builder = new StringBuilder();
        append(builder, GlobalConstants.DESC_FIELD, report.getDescription());
        append(builder, GlobalConstants.INGREDIENTS_FIELD, report.getIngredients());

        for (Nutrient nut : report.getLabelNutrients()) {
            builder.append(nut.getName()).append(": ").append(String.format("%.2f", nut.getValue())).append(System.lineSeparator());
        }

        return builder.toString();
    }

    private void append(StringBuilder builder, String field, String value) {
        if (!GlobalConstants.IS_NULL_OR_EMPTY_FIELD_PREDICATE.test(value)) {
            builder.append(field).append(": ").append(value).append(System.lineSeparator());
        }
    }
}
