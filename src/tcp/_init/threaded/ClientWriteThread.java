package tcp._init.threaded;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientWriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private Client client;

    public ClientWriteThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String text;
        while (true) {
            text = scanner.nextLine();
            writer.println(text);
            if (text.equalsIgnoreCase("bye")) {
                System.out.println("You have disconnected from the server");
                break;
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error writing to server: " + e.getMessage());
        }
    }
}
