package udp.streaming_service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientWriteThread extends Thread {
    private DatagramSocket socket;
    private Client client;
    private InetAddress serverAddress;
    private int serverPort;
    private byte[] buffer;

    public ClientWriteThread(DatagramSocket socket, Client client, InetAddress serverAddress, int serverPort) {
        this.socket = socket;
        this.client = client;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.buffer = new byte[4096];
    }

    public void run() {
        System.out.println("Type \"list\" to get a list of movies");
        while (true) {
            // Write a message
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();

            // Send the message to the server
            try {
                buffer = message.getBytes();
                DatagramPacket request = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
                socket.send(request);
            } catch (IOException e) {
                System.out.println("Error writing to the server: " + e.getMessage());
            }

            if (message.toLowerCase().contains("play")) {
                System.out.println("Type: \"play\" <movie-number> ... to play another movie");
                System.out.println("Type: \"stop\" to stop watching");
            }

            if (message.toLowerCase().contains("stop")) {
                break;
            }

            // Clear the buffer after every message
            clearBuffer();
        }
    }

    private void clearBuffer() {
        this.buffer = new byte[4096];
    }
}
