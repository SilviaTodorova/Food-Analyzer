package bg.sofia.uni.fmi.food.analyzer.client;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import static bg.sofia.uni.fmi.food.analyzer.client.GlobalConstants.*;

public class FoodAnalyzerClient {
    public static void main(String[] args) {
        new FoodAnalyzerClient().run();
    }

    private void run() {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(HOST, PORT));
            System.out.printf(CONNECT_TO_SERVER_MESSAGE_FORMAT, HOST, PORT);

            ClientRunnable clientRunnable = new ClientRunnable(socketChannel);
            new Thread(clientRunnable).start();

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (true) {
                String command = scanner.nextLine();

                buffer.clear();
                buffer.put(command.getBytes());
                buffer.flip();
                socketChannel.write(buffer);

                if (command.equalsIgnoreCase(DISCONNECT_COMMAND)) {
                    break;
                }
            }

        } catch (Exception ex) {
            System.out.printf(ERROR_CONNECT_TO_SERVER_MESSAGE_FORMAT, HOST, PORT);
        }
    }
}

