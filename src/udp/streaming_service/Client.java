package udp.streaming_service;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
    DatagramSocket socket;

    public Client() throws SocketException {
        this.socket = new DatagramSocket();
    }

    public void execute() {
        try {
            // Address and port of the server we're trying to connect to
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 2000;

            // Start two new threads (read&write) for the newly connected client
            new ClientWriteThread(socket, this, serverAddress, serverPort).start();
            new ClientReadThread(socket, this).start();

        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.execute();
        } catch (SocketException e) {
            System.out.println("Socket error: " + e.getMessage());
        }
    }
}
