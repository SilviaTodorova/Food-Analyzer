package bg.sofia.uni.fmi.food.analyzer.server.core;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.CommandFactory;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.CommandParser;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.core.factories.CommandFactoryImpl;
import bg.sofia.uni.fmi.food.analyzer.server.core.providers.CommandParserImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;

import bg.sofia.uni.fmi.food.analyzer.server.core.clients.FoodClientImpl;
import bg.sofia.uni.fmi.food.analyzer.server.core.repositories.FoodRepositoryImpl;

public class FoodAnalyzerServer {
    private final CommandFactory commandFactory;
    private final CommandParser commandParser;
    private final FoodRepository repository;
    private final FoodClient clientFood;

    private static final Path PATH = FileSystems.getDefault().getPath(REPOSITORY_DIR).toAbsolutePath();

    public FoodAnalyzerServer() {
        commandFactory = new CommandFactoryImpl();
        commandParser = new CommandParserImpl();
        repository = new FoodRepositoryImpl(PATH);

        HttpClient client = HttpClient.newHttpClient();
        clientFood = new FoodClientImpl(client, API_KEY);
    }

    public void test() {
        List<String> cmds = new ArrayList<>();

        cmds.add("get-food beef noodle soup");
        cmds.add("get-food-report 415269");
        cmds.add("get-food-report 570429");
        // cmds.add("get-food-by-barcode --img=D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\resources\\chia.png");
        cmds.add("get-food-by-barcode --img=D:\\Photos\\BarcodeImage.jpg --code=009800146130");

        for (String commandAsString : cmds) {
            String commandName = commandParser.parseCommand(commandAsString);
            Command command = commandFactory.createCommand(commandName, repository, clientFood);
            List<String> parameters = commandParser.parseParameters(commandAsString);

            String executionResult;
            try {
                executionResult = command.execute(parameters);
            } catch (Exception ex) {
                executionResult = ex.getMessage();
            }

            System.out.println(executionResult);
        }
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.bind(new InetSocketAddress(HOST, PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            System.out.printf(SERVER_RUNNING_MESSAGE_FORMAT, HOST, PORT);

            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    try {
                        Thread.sleep(SLEEP_MILLIS);
                    } catch (InterruptedException e) {
                        System.out.printf(ERROR_READ_CHANNEL_MESSAGE_FORMAT);
                    }
                    continue;
                }

                iterateClients(selector, buffer);
            }

        } catch (Exception ex) {
            System.out.printf(ERROR_READ_CHANNEL_MESSAGE_FORMAT);
        }
    }

    private void iterateClients(Selector selector, ByteBuffer buffer) throws IOException {
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();

                buffer.clear();
                int r = sc.read(buffer);
                if (r <= 0) {
                    sc.close();
                    break;
                }

                buffer.flip();

                String commandAsString = new String(buffer.array(), 0, buffer.limit());

                if (commandAsString.equalsIgnoreCase(DISCONNECT_COMMAND)) {
                    sc.close();
                    continue;
                }

                try {
                    processCommand(commandAsString, sc, buffer);
                } catch (Exception ex) {
                    // Write
                    buffer.clear();
                    buffer.put(ex.getMessage().getBytes());
                    buffer.flip();
                    sc.write(buffer);
                }


            } else if (key.isAcceptable()) {
                ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                SocketChannel accept = sockChannel.accept();
                accept.configureBlocking(false);
                accept.register(selector, SelectionKey.OP_READ);
            }

            keyIterator.remove();
        }
    }

    private void processCommand(String commandAsString, SocketChannel sc, ByteBuffer buffer) throws IOException {
        if (commandAsString == null || commandAsString.trim().equals("")) {
            throw new IllegalArgumentException(COMMAND_CANNOT_BE_NULL_OR_EMPTY);
        }

        String commandName = commandParser.parseCommand(commandAsString);
        Command command = commandFactory.createCommand(commandName, repository, clientFood);
        List<String> parameters = commandParser.parseParameters(commandAsString);
        String executionResult = command.execute(parameters);

        if (commandName.equals(DISCONNECT_COMMAND)) {
            sc.close();
        }

        // Write
        buffer.clear();
        buffer.put(executionResult.getBytes());
        buffer.flip();
        sc.write(buffer);
    }
}
