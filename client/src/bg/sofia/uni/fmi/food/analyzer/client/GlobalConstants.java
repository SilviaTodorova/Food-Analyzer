package bg.sofia.uni.fmi.food.analyzer.client;

class GlobalConstants {
    public static final String HOST = "localhost";
    public static final int PORT = 8080;
    public static final int BUFFER_SIZE = 1024;

    public static final String DISCONNECT_COMMAND = "disconnect";
    public static final String ERROR_CONNECT_TO_SERVER_MESSAGE_FORMAT =
            "=> cannot connect to server on %s:%s, make sure that the server is started%n";
    public static final String DISCONNECTED_FROM_SERVER = "=> disconnected from server";
    public static final String CONNECT_TO_SERVER_MESSAGE_FORMAT = "=> connected to server running on %s:%s%n";
}

