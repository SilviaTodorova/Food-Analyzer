package bg.sofia.uni.fmi.food.analyzer.server.core;

import bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants;
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
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.*;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;

import bg.sofia.uni.fmi.food.analyzer.server.core.clients.FoodClientImpl;
import bg.sofia.uni.fmi.food.analyzer.server.core.repositories.FoodRepositoryImpl;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.ImageNotFoundException;

public class FoodAnalyzerServer {
    private final CommandFactory commandFactory;
    private final CommandParser commandParser;

    private final FoodRepository repository;
    private final FoodClient clientFood;

    public FoodAnalyzerServer() {
        commandFactory = new CommandFactoryImpl();
        commandParser = new CommandParserImpl();

        Path path = FileSystems.getDefault().getPath(REPOSITORY_DIR).toAbsolutePath();
        repository = new FoodRepositoryImpl(path);
        clientFood = new FoodClientImpl(HttpClient.newHttpClient(), API_KEY);
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
                        System.out.println(ERROR_READ_CHANNEL_MESSAGE);
                    }
                    continue;
                }

                iterateClients(selector, buffer);
            }

        } catch (Exception ex) {
            System.out.println(ERROR_READ_CHANNEL_MESSAGE);
        }
    }

    private void iterateClients(Selector selector, ByteBuffer buffer) {
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

        while (keyIterator.hasNext()) {
            try {
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
                    System.out.printf("[ %s IP: %s]  %s%n",
                            LocalDateTime.now().format(DATE_FORMATTER),
                            sc.getLocalAddress(),
                            commandAsString);

                    if (commandAsString.equalsIgnoreCase(DISCONNECT_COMMAND)) {
                        sc.close();
                        continue;
                    }

                    processCommand(commandAsString, sc, buffer);
                } else if (key.isAcceptable()) {
                    ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                    SocketChannel accept = sockChannel.accept();
                    accept.configureBlocking(false);
                    accept.register(selector, SelectionKey.OP_READ);
                }

                keyIterator.remove();
            } catch (Exception ex) {
                System.out.println(ERROR_READ_CHANNEL_MESSAGE);
            }
        }
    }

    private void processCommand(String commandAsString, SocketChannel sc, ByteBuffer buffer)
            throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(SEPARATOR)
                .append(SEARCH_RESULTS)
                .append(DELIMITER)
                .append(SEPARATOR)
                .append(System.lineSeparator());

        try {
            String commandName = commandParser.parseCommand(commandAsString);
            Command command = commandFactory.createCommand(commandName, repository, clientFood);
            List<String> parameters = commandParser.parseParameters(commandAsString);

            String executionResult = command.execute(parameters) + System.lineSeparator();
            builder.append(executionResult);

            if (commandName.equals(CommandConstants.DISCONNECT_COMMAND)) {
                sc.close();
            }

            sendMessage(sc, buffer, builder.toString());

        } catch (FoodIdNotFoundException |
                FoodNotFoundException |
                FoodBarcodeNotFoundException |
                ImageNotFoundException ex) {

            builder.append(ex.getMessage()).append(System.lineSeparator());
            sendMessage(sc, buffer, builder.toString());
        } catch (Exception ex) {
            sendMessage(sc, buffer, ex.getMessage() + System.lineSeparator());
        }
    }

    private void sendMessage(SocketChannel sc, ByteBuffer buffer, String message) throws IOException {
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        sc.write(buffer);
    }
}
