package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.clients.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.*;
import static bg.sofia.uni.fmi.food.analyzer.server.core.BarcodeConverter.decodeBarcodeImage;

public class GetFoodByBarcode implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS_ONE_CRITERIA = 1;
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS_TWO_CRITERIAS = 2;
    private static final String GET_FOOD_REPORT = "get-food-report";

    private static final String CODE_FLAG = "--code=";
    private static final String IMG_FLAG = "--img=";

    private final FoodRepository repository;
    private final FoodClient client;

    private String barcode;

    public GetFoodByBarcode(FoodRepository repository, FoodClient client) {
        this.repository = repository;
        this.client = client;
    }

    @Override
    public String execute(List<String> parameters) throws IOException {
        validateInput(parameters);

        parseParameters(parameters);

        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for barcode: ")
                .append(barcode)
                .append(" --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        if (repository.checkFoodExistByBarcode(barcode)) {
            return builder.append(repository.getFoodByBarcode(barcode)).toString();
        }

        List<Food> foods = new ArrayList<>(client.getFoodByBarcode(barcode));
        String response = formatExecutionResult(foods);
        repository.saveFoodReportByBarcode(barcode, response);
        return builder.append(response).toString();
    }

    private void validateInput(List<String> parameters) {
        int size = parameters.size();
        boolean containsFlags = parameters.stream().anyMatch(x -> x.contains(CODE_FLAG) || x.contains(IMG_FLAG));

        if (size != EXPECTED_NUMBER_OF_ARGUMENTS_ONE_CRITERIA
            && size != EXPECTED_NUMBER_OF_ARGUMENTS_TWO_CRITERIAS
            && !containsFlags) {
            throw new IllegalArgumentException(
                    String.format(INVALID_NUMBER_OF_ARGUMENTS_MESSAGE_FORMAT, EXPECTED_NUMBER_OF_ARGUMENTS_ONE_CRITERIA, parameters.size()));
        }
    }

    private void parseParameters(List<String> parameters) {
        try {
            boolean containsCodeFlags = parameters.stream().anyMatch(x -> x.contains(CODE_FLAG));
            if(containsCodeFlags) {
                barcode = parameters
                          .stream()
                          .filter(x -> x.contains(CODE_FLAG))
                          .map(param -> param.replaceAll(CODE_FLAG, EMPTY_STRING))
                          .findFirst()
                          .get();

                return;
            }

            String img = parameters
                         .stream()
                         .filter(x -> x.contains(IMG_FLAG))
                         .map(x -> x.replaceAll(IMG_FLAG, EMPTY_STRING))
                         .findFirst()
                         .get();

            barcode = decodeBarcodeImage(img);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format(FAILED_PARSING_PARAMETERS_MESSAGE_FORMAT, GET_FOOD_REPORT));
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
        }
        return builder.toString();
    }

    private void append(StringBuilder builder, String field, String value) {
        if (!value.equals("null") && !value.equals("")) {
            builder.append(field).append(": ").append(value).append(System.lineSeparator());
        }
    }
}
