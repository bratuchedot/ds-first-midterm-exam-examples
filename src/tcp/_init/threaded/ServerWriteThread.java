package tcp._init.threaded;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerWriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private Server server;

    public ServerWriteThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String message = "";
        while (true) {
            message = scanner.nextLine();
            writer.println(message);

            if (message.equalsIgnoreCase("bye")) {
                System.out.println("You have stopped the communication with the client");
                break;
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error writing to client: " + e.getMessage());
        }
    }
}
