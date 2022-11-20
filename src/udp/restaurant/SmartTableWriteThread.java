package udp.restaurant;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class SmartTableWriteThread extends Thread {
    private DatagramSocket socket;
    private SmartTable smartTable;
    private InetAddress serverAddress;
    private int serverPort;
    private byte[] buffer;

    public SmartTableWriteThread(DatagramSocket socket, SmartTable smartTable, InetAddress serverAddress, int serverPort) {
        this.socket = socket;
        this.smartTable = smartTable;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.buffer = new byte[4096];
    }

    public void run() {
        System.out.println("Type \"new\" to register");
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

            if (message.toLowerCase().contains("order")) {
                System.out.println("Type: \"order\" <article-number> <article-number>... for another order or");
                System.out.println("Type: \"bill\" to get the bill");
            }

            if (message.toLowerCase().contains("bill")) {
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
