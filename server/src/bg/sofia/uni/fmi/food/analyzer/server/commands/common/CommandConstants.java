package bg.sofia.uni.fmi.food.analyzer.server.commands.common;

import java.util.function.Predicate;

public class CommandConstants {
    public static final String INVALID_NUMBER_OF_ARGUMENTS_MESSAGE_FORMAT = "Invalid number of arguments. Expected: %d, Received: %d";
    public static final String FAILED_PARSING_PARAMETERS_MESSAGE_FORMAT = "Failed to parse %s command parameters.";

    public static final String DELIMITER = " ";
    public static final Predicate<String> IS_NULL_OR_EMPTY_FIELD_PREDICATE = field -> field.equals("null") || field.isEmpty();
    public static final String EMPTY_STRING = "";
    public static final String NO_FOODS_WERE_FOUND_MESSAGE = "No foods were found!";
    public static final String FDC_ID_FIELD = "fdcId";
    public static final String GTIN_UPC_FIELD = "gtinUpc";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String INCREDIENTS_FIELD = "incredients";
}
