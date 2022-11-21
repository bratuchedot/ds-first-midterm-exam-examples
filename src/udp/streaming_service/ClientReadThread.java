package udp.streaming_service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientReadThread extends Thread {
    private DatagramSocket socket;
    private Client client;
    private byte[] buffer;

    public ClientReadThread(DatagramSocket socket, Client client) {
        this.socket = socket;
        this.client = client;
        this.buffer = new byte[4096];
    }

    public void run() {
        while (true) {
            try {
                // Receive a response
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                socket.receive(response);

                // Convert the response to string and print the message
                String message = convertBytesToString(buffer);
                System.out.println(message);

                if (message.toLowerCase().contains("stopped")) {
                    break;
                }

                // Clear the buffer after every response.
                clearBuffer();
            } catch (IOException e) {
                System.out.println("Error reading from the server: " + e.getMessage());
                break;
            }
        }
    }

    private void clearBuffer() {
        this.buffer = new byte[4096];
    }

    public String convertBytesToString(byte[] buffer) {
        if (buffer == null) return null;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (buffer[i] != 0) {
            sb.append((char) buffer[i]);
            i++;
        }
        return sb.toString();
    }
}
