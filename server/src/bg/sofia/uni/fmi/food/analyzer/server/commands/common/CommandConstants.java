package bg.sofia.uni.fmi.food.analyzer.server.commands.common;

import java.util.function.Predicate;

public class CommandConstants {
    public static final String INVALID_NUMBER_OF_ARGUMENTS_MESSAGE_FORMAT = "Invalid number of arguments. Expected: %d, Received: %d";
    public static final String FAILED_PARSING_PARAMETERS_MESSAGE_FORMAT = "Failed to parse %s command parameters.";
    public static final String NO_FOODS_WERE_FOUND_MESSAGE = "No foods were found!";

    public static final String GET_FOOD_COMMAND = "get-food";
    public static final String GET_FOOD_REPORT_COMMAND = "get-food-report";
    public static final String GET_FOOD_BY_BARCODE_COMMAND = "get-food-by-barcode";
}
