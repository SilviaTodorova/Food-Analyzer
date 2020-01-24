package bg.sofia.uni.fmi.food.analyzer.server.common;

import java.util.function.Predicate;

public class GlobalConstants {
    public static final String HOST = "localhost";
    public static final int PORT = 8080;
    public static final int SLEEP_MILLIS = 200;
    public static final int BUFFER_SIZE = 1024;
    public static final String API_KEY = "XQhDTxbht58sQOKHtTmpwV2EE9GFtvPLaiI8vtVO";

    public static final String DISCONNECT_COMMAND = "disconnect";

    public static final String SERVER_RUNNING_MESSAGE_FORMAT = "server is running on %s:%d%n";
    public static final String ERROR_READ_CHANNEL_MESSAGE_FORMAT = "error while reading from channel%n";

    public static final Predicate<String> IS_NULL_OR_EMPTY_FIELD_PREDICATE = field -> field.equals("null") || field.isEmpty();

    public static final String COMMAND_CANNOT_BE_NULL_OR_EMPTY = "Command cannot be null or empty.";

    public static final String NO_A_SUCH_FILE = "There is no a such file";
    public static final String NO_BARCODE_IN_THE_IMAGE = "There is no barcode in the image";

    public static final String PATH_FOOD_NAME_DIR = "D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-name\\%s";
    public static final String PATH_FOOD_ID_DIR = "D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-id\\%s";
    public static final String PATH_FOOD_BARCODE = "D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-barcode\\%s";


    public static final String DELIMITER = " ";
    public static final String EMPTY_STRING = "";

    public static final String NUTRIENTS_FIELD = "labelNutrients";
    public static final String VALUE_FIELD = "value";
    public static final String CALORIES_FIELD = "calories";
    public static final String PROTEIN_FIELD = "protein";
    public static final String FAT_FIELD = "fat";
    public static final String CARBOHYDRATES_FIELD = "carbohydrates";
    public static final String FIBER_FIELD = "fiber";
    public static final String FDC_ID_FIELD = "fdcId";
    public static final String GTIN_UPC_FIELD = "gtinUpc";
    public static final String DESC_FIELD = "description";
    public static final String INGREDIENTS_FIELD = "ingredients";
}

