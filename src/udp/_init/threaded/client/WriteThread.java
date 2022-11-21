package udp._init.threaded.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class WriteThread extends Thread {
    private DatagramSocket socket;
    private Client client;
    private InetAddress serverAddress;
    private int serverPort;
    private byte[] buffer;

    public WriteThread(DatagramSocket socket, Client client, InetAddress serverAddress, int serverPort) {
        this.socket = socket;
        this.client = client;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.buffer = new byte[4096];
    }

    public void run() {
        System.out.println("Type \"hi\" to say hi");
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

            if (message.toLowerCase().contains("bye")) {
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
