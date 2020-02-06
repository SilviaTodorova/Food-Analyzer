package bg.sofia.uni.fmi.food.analyzer.server.common;

import java.time.format.DateTimeFormatter;

public class GlobalConstants {
    public static final String HOST = "localhost";
    public static final int PORT = 8080;
    public static final int SLEEP_MILLIS = 200;
    public static final int BUFFER_SIZE = 131072;

    public static final String ERROR_READ_CHANNEL_MESSAGE = "error while reading from channel";

    public static final String SERVER_RUNNING_MESSAGE_FORMAT = "server is running on %s:%d%n";
    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static final String NO_A_SUCH_FILE = "There is no a such file";
    public static final String NO_BARCODE_IN_THE_IMAGE = "There is no barcode in the image";

    public static final String DELIMITER = " ";
    public static final String EMPTY_STRING = "";

    public static final String RESOURCES_DIR = "resources";
    public static final String REPOSITORY_DIR = "repository";
    public static final String REPOSITORY_TESTS_DIR = "repository-tests";
    public static final String FOOD_NAME_DIR = "food-name";
    public static final String FOOD_ID_DIR = "food-id";
    public static final String FOOD_BARCODE_DIR = "food-barcode";

    public static final String API_KEY = "XQhDTxbht58sQOKHtTmpwV2EE9GFtvPLaiI8vtVO";
    public static final String API_URL = "https://api.nal.usda.gov/fdc/v1";

    public static final String CALORIES_FIELD = "calories";
    public static final String PROTEIN_FIELD = "protein";
    public static final String FAT_FIELD = "fat";
    public static final String CARBOHYDRATES_FIELD = "carbohydrates";
    public static final String FIBER_FIELD = "fiber";
    public static final String FDC_ID_FIELD = "fdcId";
    public static final String GTIN_UPC_FIELD = "gtinUpc";
    public static final String DESC_FIELD = "description";
    public static final String INGREDIENTS_FIELD = "ingredients";
    public static final String FOODS = "foods";
}

