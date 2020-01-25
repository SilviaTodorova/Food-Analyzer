package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.*;
import static bg.sofia.uni.fmi.food.analyzer.server.core.BarcodeConverter.decodeBarcodeImage;

public class GetFoodByBarcode implements Command {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS_ONE_CRITERIA = 1;
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS_TWO_CRITERIAS = 2;

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
    public String execute(List<String> parameters) {
        validateInput(parameters);

        parseParameters(parameters);

        StringBuilder builder = new StringBuilder();
        builder.append("-------- Search Results for barcode: ")
               .append(barcode)
               .append(" --------")
               .append(System.lineSeparator())
               .append(System.lineSeparator());

        if (repository.checkFoodExistByBarcode(barcode)) {
            return builder.append(repository.getFoodByBarcode(barcode)).toString();
        }

        List<Food> foods = new ArrayList<>(client.getFoodByBarcode(barcode));
        String response = formatExecutionResult(foods);
        repository.saveFoodByBarcode(barcode, response);
        return builder.append(response).toString();
    }

    private void validateInput(List<String> parameters) {
        int size = parameters.size();
        boolean containsFlags = parameters
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(x -> x.contains(CODE_FLAG) || x.contains(IMG_FLAG));

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
            boolean containsImgFlags = parameters.stream().anyMatch(x -> x.contains(IMG_FLAG));

            if(!containsCodeFlags && !containsImgFlags){
                throw new IllegalArgumentException(
                        String.format("TODO: %s", GET_FOOD_BY_BARCODE_COMMAND));
            }

            if (containsCodeFlags) {
                barcode = parameters
                        .stream()
                        .filter(x -> x.contains(CODE_FLAG))
                        .map(param -> param.replaceAll(CODE_FLAG, GlobalConstants.EMPTY_STRING))
                        .findFirst()
                        .get();

                return;
            }

            String img = parameters
                    .stream()
                    .filter(x -> x.contains(IMG_FLAG))
                    .map(x -> x.replaceAll(IMG_FLAG, GlobalConstants.EMPTY_STRING))
                    .findFirst()
                    .get();

            barcode = decodeBarcodeImage(img);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format(FAILED_PARSING_PARAMETERS_MESSAGE_FORMAT, GET_FOOD_BY_BARCODE_COMMAND));
        }
    }

    private String formatExecutionResult(List<Food> foods) {
        if (foods.size() == 0) {
            return NO_FOODS_WERE_FOUND_MESSAGE;
        }

        StringBuilder builder = new StringBuilder();
        for (Food food : foods) {
            append(builder, GlobalConstants.FDC_ID_FIELD, String.valueOf(food.getFdcId()));
            append(builder, GlobalConstants.GTIN_UPC_FIELD, String.valueOf(food.getGtinUpc()));
            append(builder, GlobalConstants.DESC_FIELD, String.valueOf(food.getDescription()));
        }
        return builder.toString();
    }

    private void append(StringBuilder builder, String field, String value) {
        if (!GlobalConstants.IS_NULL_OR_EMPTY_FIELD_PREDICATE.test(value)) {
            builder.append(field).append(": ").append(value).append(System.lineSeparator());
        }
    }
}
