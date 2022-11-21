package tcp._init.threaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private Server server;

    public ServerReadThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            System.out.println("Error getting input stream: " + e.getMessage());
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                System.out.println(response);
            } catch (IOException e) {
                System.out.println("Error reading from the client: " + e.getMessage());
                break;
            }
        }
    }
}
