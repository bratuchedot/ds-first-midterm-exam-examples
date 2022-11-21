package udp.streaming_service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

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

                // Get client address and client port
                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();

                // Handle the request
                String requestToString = convertBytesToString(buffer);
                String[] parts = requestToString.split("\\s+");
                if (parts[0].equalsIgnoreCase("list")) {
                    handleList(clientAddress, clientPort);
                } else if (parts[0].equalsIgnoreCase("play")) {
                    // FIXME: send movie frames
                    handlePlay(clientAddress, clientPort, parts);
                } else if (parts[0].equalsIgnoreCase("stop")) {
                    handleStop(clientAddress, clientPort);
                }
                // TODO: receive a message for a movie position
                else {
                    handleInvalidRequest(clientAddress, clientPort);
                }

                // Clear the buffer after every request
                clearBuffer();
            }
        } catch (IOException e) {
            System.out.println("Error in Server: " + e.getMessage());
        }
    }

    private void handleStop(InetAddress clientAddress, int clientPort) throws IOException {
        clearBuffer();
        String message = "You have stopped watching a movie";
        buffer = message.getBytes();
        DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
        socket.send(response);
        System.out.println("Client with number " + clientAddress + ":" + clientPort + " has stopped playing a movie");
    }

    private void handlePlay(InetAddress clientAddress, int clientPort, String[] parts) throws IOException {
        String movieNumber = parts[1];
        String message = "";
        if (movieNumber.equals("1")) {
            message = "Playing Man of Steel";
        } else if (movieNumber.equals("2")) {
            message = "Playing Wonder Woman";
        } else if (movieNumber.equals("3")) {
            message = "Hulk";
        } else if (movieNumber.equals("4")) {
            message = "Spider-man";
        } else if (movieNumber.equals("5")) {
            message = "Batman";
        }
        System.out.println(message + " for client with number " + clientAddress + ":" + clientPort);
        clearBuffer();
        buffer = message.getBytes();
        DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
        socket.send(response);
    }

    private void handleInvalidRequest(InetAddress clientAddress, int clientPort) throws IOException {
        clearBuffer();
        String message = "Invalid command";
        buffer = message.getBytes();
        DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
        socket.send(response);
    }

    private void handleList(InetAddress clientAddress, int clientPort) throws IOException {
        System.out.println("Client with number " + clientAddress + ":" + clientPort + "request for the list of movies");
        clearBuffer();
        String movies = getMovies();
        buffer = movies.getBytes();
        DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
        socket.send(response);
        System.out.println("Movies sent to client with number " + clientAddress + ":" + clientPort);
    }

    private String getMovies() {
        StringBuilder sb = new StringBuilder();
        sb.append("Movies\n");
        sb.append("Name:            | Frames:\n");
        sb.append("1. Man of Steel  | 10\n");
        sb.append("2. Wonder Woman  | 10\n");
        sb.append("3. Hulk          | 9\n");
        sb.append("4. Spider-man    | 9\n");
        sb.append("5. Batman        | 8\n\n");
        sb.append("Type: \"play\" <movie-number>\n");
        sb.append("Ex: play 1");
        return sb.toString();
    }

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
        int port = 2000;
        try {
            Server server = new Server(port);
            server.execute();
        } catch (SocketException e) {
            System.out.println("Socket error: " + e.getMessage());
        }

    }
}
