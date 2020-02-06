package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.ImageNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;

import java.util.ArrayList;
import java.util.List;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.*;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;
import static bg.sofia.uni.fmi.food.analyzer.server.core.BarcodeConverter.decodeBarcodeImage;

public class GetFoodByBarcode implements Command {
    private static final String INVALID_NUMBER_OF_ARGUMENTS_ONE_OR_TWO_MESSAGE_FORMAT =
            "Invalid number of arguments. Expected: %d or %d, Received: %d";

    private static final int EXPECTED_NUMBER_OF_ARGUMENTS_ONE_CRITERIA = 1;
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS_TWO_CRITERIAS = 2;
    private static final String INVALID_PARAMETERS = "Invalid parameters";

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
    public String execute(List<String> parameters)
            throws FoodBarcodeNotFoundException,
            ImageNotFoundException {
        validateInput(parameters);

        parseParameters(parameters);

        if (repository.checkFoodExistByBarcode(barcode)) {
            return repository.getFoodByBarcode(barcode);
        }

        List<Food> foods = new ArrayList<>(client.getFoodByBarcode(barcode));

        if (foods.size() == 0) {
            throw new FoodBarcodeNotFoundException(NO_FOODS_WERE_FOUND_MESSAGE);
        }

        StringBuilder builder = new StringBuilder();
        foods.stream().forEach(builder::append);
        String response = builder.toString().trim();

        repository.saveFoodByBarcode(barcode, response);
        return response;
    }

    private void validateInput(List<String> parameters) {
        int size = parameters.size();

        if (size != EXPECTED_NUMBER_OF_ARGUMENTS_ONE_CRITERIA
                && size != EXPECTED_NUMBER_OF_ARGUMENTS_TWO_CRITERIAS) {

            throw new IllegalArgumentException(
                    String.format(
                            INVALID_NUMBER_OF_ARGUMENTS_ONE_OR_TWO_MESSAGE_FORMAT,
                            EXPECTED_NUMBER_OF_ARGUMENTS_ONE_CRITERIA,
                            EXPECTED_NUMBER_OF_ARGUMENTS_TWO_CRITERIAS,
                            parameters.size()));
        }
    }

    private void parseParameters(List<String> parameters) throws ImageNotFoundException {
        boolean containsCodeFlags = parameters.stream().anyMatch(param -> param.contains(CODE_FLAG));
        boolean containsImgFlags = parameters.stream().anyMatch(param -> param.contains(IMG_FLAG));

        if (!containsCodeFlags && !containsImgFlags) {
            throw new IllegalArgumentException(INVALID_PARAMETERS);
        }

        if (containsCodeFlags) {
            barcode = parameters
                    .stream()
                    .filter(param -> param.contains(CODE_FLAG))
                    .map(param -> param.replaceAll(CODE_FLAG, EMPTY_STRING))
                    .filter(param -> !param.equals(EMPTY_STRING))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(INVALID_PARAMETERS));

            return;
        }

        String img = parameters
                .stream()
                .filter(param -> param.contains(IMG_FLAG))
                .map(param -> param.replaceAll(IMG_FLAG, EMPTY_STRING))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_PARAMETERS));

        barcode = decodeBarcodeImage(img);
    }
}
