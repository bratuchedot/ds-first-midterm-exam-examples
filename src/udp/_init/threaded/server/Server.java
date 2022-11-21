package udp._init.threaded.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {
    private int port;
    private DatagramSocket socket;
    private byte[] buffer;

    public Server(int port) throws SocketException {
        this.port = port;
        this.socket = new DatagramSocket(port);
        this.buffer = new byte[4096];
    }

    public void execute() {
        try {
            System.out.println("Server is listening on port " + port);
            while (true) {
                // Listen for a new connection
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                // Print the message
                System.out.println(convertBytesToString(buffer));

                // Clear the buffer after every request
                clearBuffer();
            }
        } catch (IOException e) {
            System.out.println("Error in Server: " + e.getMessage());
        }
    }

    // A utility method to convert the byte array
    // data into a string representation.
    private String convertBytesToString(byte[] buffer) {
        if (buffer == null) return null;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (buffer[i] != 0) {
            sb.append((char) buffer[i]);
            i++;
        }
        return sb.toString();
    }

    private void clearBuffer() {
        this.buffer = new byte[4096];
    }

    public static void main(String[] args) {
        int port = 1234;
        try {
            Server server = new Server(port);
            server.execute();
        } catch (SocketException e) {
            System.out.println("Socket error: " + e.getMessage());
        }
    }
}
