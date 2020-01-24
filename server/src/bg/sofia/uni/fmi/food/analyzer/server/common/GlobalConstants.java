package bg.sofia.uni.fmi.food.analyzer.server.common;

public class GlobalConstants {
    public static final String HOST = "localhost";
    public static final int PORT = 8080;
    public static final int SLEEP_MILLIS = 200;
    public static final int BUFFER_SIZE = 1024;
    public static final String API_KEY = "XQhDTxbht58sQOKHtTmpwV2EE9GFtvPLaiI8vtVO";

    public static final String DISCONNECT_COMMAND = "disconnect";

    // Server
    public static final String SERVER_RUNNING_MESSAGE_FORMAT = "server is running on %s:%d%n";

    public static final String ERROR_READ_CHANNEL_MESSAGE_FORMAT = "error while reading from channel%n";
    public static final String COMMAND_CANNOT_BE_NULL_OR_EMPTY = "Command cannot be null or empty.";
    public static final String NO_A_SUCH_FILE = "There is no a such file";
    public static final String NO_BARCODE_IN_THE_IMAGE = "There is no barcode in the image";
    public static final String PATH_FOOD_NAME_DIR = "D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-name\\%s";
    public static final String PATH_FOOD_ID_DIR = "D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-id\\%s";
    public static final String PATH_FOOD_BARCODE = "D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\database\\food-barcode\\%s";

}

