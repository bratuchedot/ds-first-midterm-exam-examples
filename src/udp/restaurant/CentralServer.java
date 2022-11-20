package udp.restaurant;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

public class CentralServer {
    private int port;
    private DatagramSocket socket;
    private byte[] buffer;
    private Map<String, List<String>> smartTables;

    public CentralServer(int port) throws SocketException {
        this.port = port;
        this.socket = new DatagramSocket(port);
        this.buffer = new byte[4096];
        this.smartTables = new HashMap<>();
    }

    public void execute() {
        try {
            System.out.println("Server is listening on port " + port);
            while (true) {
                // Listen for a new connection
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // Get client address and client port
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                // Handle the request
                String requestToString = convertBytesToString(buffer);
                String[] parts = requestToString.split("\\s+");
                if (parts[0].equalsIgnoreCase("new")) {
                    handleNew(clientAddress, clientPort);
                } else if (parts[0].equalsIgnoreCase("order")) {
                    handleOrder(clientAddress, clientPort, parts);
                } else if (parts[0].equalsIgnoreCase("bill")) {
                    handleBill(clientAddress, clientPort);
                } else {
                    handleInvalidRequest(clientAddress, clientPort);
                }

                // Clear the buffer after every request
                clearBuffer();
            }
        } catch (IOException e) {
            System.out.println("Error in Server: " + e.getMessage());
        }
    }

    private void handleInvalidRequest(InetAddress clientAddress, int clientPort) throws IOException {
        clearBuffer();
        String message = "Invalid command";
        buffer = message.getBytes();
        DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
        socket.send(response);
    }

    private void handleBill(InetAddress clientAddress, int clientPort) throws IOException {
        String smartTableNumber = clientAddress + ":" + clientPort;
        int bill = 0;
        for (String item : smartTables.get(smartTableNumber)) {
            if (item.equalsIgnoreCase("Pizza")) {
                bill += 5;
            } else if (item.equalsIgnoreCase("Burger")) {
                bill += 3;
            } else if (item.equalsIgnoreCase("Beef")) {
                bill += 8;
            } else if (item.equalsIgnoreCase("Coca-Cola")) {
                bill += 1;
            } else if (item.equalsIgnoreCase("Sprite")) {
                bill += 1;
            }
        }
        clearBuffer();
        String billToString = String.format("Your bill is $%d", bill);
        buffer = billToString.getBytes();
        DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
        socket.send(response);
        System.out.println("Bill sent to table " + smartTableNumber);
        smartTables.remove(smartTableNumber);
    }

    private void handleOrder(InetAddress clientAddress, int clientPort, String[] parts) {
        String smartTableNumber = clientAddress + ":" + clientPort;
        Arrays.stream(parts).skip(1).forEach(article -> {
            if (article.equals("1")) {
                smartTables.get(smartTableNumber).add("Pizza");
            } else if (article.equals("2")) {
                smartTables.get(smartTableNumber).add("Pizza");
            } else if (article.equals("3")) {
                smartTables.get(smartTableNumber).add("Beef");
            } else if (article.equals("4")) {
                smartTables.get(smartTableNumber).add("Coca-Cola");
            } else if (article.equals("5")) {
                smartTables.get(smartTableNumber).add("Sprite");
            }
        });
        System.out.println("Table " + smartTableNumber + " has ordered: " + smartTables.get(smartTableNumber).toString());
    }

    private void handleNew(InetAddress clientAddress, int clientPort) throws IOException {
        String smartTableNumber = clientAddress + ":" + clientPort;
        System.out.println("New smart table with number " + smartTableNumber);
        smartTables.put(smartTableNumber, new ArrayList<>());

        clearBuffer();
        String menu = getMenu();
        buffer = menu.getBytes();
        DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
        socket.send(response);
        System.out.println("Menu sent to table " + smartTableNumber);
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

    // Method to clear the buffer
    private void clearBuffer() {
        this.buffer = new byte[4096];
    }

    private String getMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("Menu\n");
        sb.append("Item:        | Price:\n");
        sb.append("1. Pizza     | $5\n");
        sb.append("2. Burger    | $3\n");
        sb.append("3. Beef      | $8\n");
        sb.append("4. Coca-Cola | $1\n");
        sb.append("5. Sprite    | $1\n\n");
        sb.append("Type: \"order\" <article-number> <article-number>...\n");
        sb.append("Ex: order 1 4");
        return sb.toString();
    }

    public static void main(String[] args) {
        int port = 1234;
        try {
            CentralServer centralServer = new CentralServer(port);
            centralServer.execute();
        } catch (SocketException e) {
            System.out.println("Socket error: " + e.getMessage());
        }
    }
}
