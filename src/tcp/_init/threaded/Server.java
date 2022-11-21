package tcp._init.threaded;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("A new request has been received");

                ServerWriteThread serverWriteThread = new ServerWriteThread(socket, this);
                serverWriteThread.start();
                ServerReadThread serverReadThread = new ServerReadThread(socket, this);
                serverReadThread.start();
            }
        } catch (IOException e) {
            System.out.println("Error in Server: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = 2022;
        Server server = new Server(port);
        server.execute();
    }
}
