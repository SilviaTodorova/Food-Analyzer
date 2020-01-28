package bg.sofia.uni.fmi.food.analyzer.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.food.analyzer.client.GlobalConstants.BUFFER_SIZE;
import static bg.sofia.uni.fmi.food.analyzer.client.GlobalConstants.DISCONNECTED_FROM_SERVER;


public class ClientRunnable implements Runnable {
    private final SocketChannel socket;

    public ClientRunnable(SocketChannel socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (true) {
                buffer.clear();
                socket.read(buffer);
                buffer.flip();
                String reply = new String(buffer.array(), 0, buffer.limit());
                System.out.print(reply);
            }
        } catch (IOException e) {
            System.out.println(DISCONNECTED_FROM_SERVER);
        }
    }
}
