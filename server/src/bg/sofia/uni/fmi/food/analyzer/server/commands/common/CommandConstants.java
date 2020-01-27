package bg.sofia.uni.fmi.food.analyzer.server.commands.common;

import java.util.function.Predicate;

public class CommandConstants {
    public static final Predicate<String> IS_NULL_OR_EMPTY_PREDICATE =
            field -> field == null || field.isEmpty();

    public static final String INVALID_COMMAND = "Invalid command name: '%s'!";

    public static final String INVALID_NUMBER_OF_ARGUMENTS_MESSAGE_FORMAT =
            "Invalid number of arguments. Expected: %d, Received: %d";

    public static final String FAILED_PARSING_PARAMETERS_MESSAGE_FORMAT =
            "Failed to parse %s command parameters.";

    public static final String NO_FOODS_WERE_FOUND_MESSAGE = "No foods were found!";

    public static final String COMMAND_CANNOT_BE_NULL_OR_EMPTY = "Command cannot be null or empty.";

    public static final String DISCONNECT_COMMAND = "disconnect";
    public static final String GET_FOOD_COMMAND = "get-food";
    public static final String GET_FOOD_REPORT_COMMAND = "get-food-report";
    public static final String GET_FOOD_BY_BARCODE_COMMAND = "get-food-by-barcode";

    public static final String SEPARATOR = "--------";
    public static final String SEARCH_RESULTS_FOR = " Search Results for ";
    public static final String SEARCH_RESULTS = " Search Results ";
    public static final String FORMAT = ": ";
}
