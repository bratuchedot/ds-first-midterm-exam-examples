package tcp._init.threaded;

import tcp.chat.client.ReadThread;
import tcp.chat.client.WriteThread;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private String serverHostname;
    private int serverPort;

    public Client(String serverHostname, int serverPort) {
        this.serverHostname = serverHostname;
        this.serverPort = serverPort;
    }

    public void execute() {
        try {
            Socket socket = new Socket(serverHostname, serverPort);
            System.out.println("You have successfully connected to the server");
            new ClientReadThread(socket, this).start();
            new ClientWriteThread(socket, this).start();
        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String serverHostname = "localhost";
        int serverPort = 2022;
        Client client = new Client(serverHostname, serverPort);
        client.execute();
    }
}
