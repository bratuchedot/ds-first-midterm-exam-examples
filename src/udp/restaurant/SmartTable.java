package udp.restaurant;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SmartTable {
    private DatagramSocket socket;

    public SmartTable() throws SocketException {
        this.socket = new DatagramSocket();
    }

    public void execute() {
        try {
            // Address and port of the server we're trying to connect to
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 1234;

            // Start two new threads (read&write) for the newly connected client
            new SmartTableWriteThread(socket, this, serverAddress, serverPort).start();
            new SmartTableReadThread(socket, this).start();
        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        try {
            SmartTable smartTable = new SmartTable();
            smartTable.execute();
        } catch (SocketException e) {
            System.out.println("Socket error: " + e.getMessage());
        }
    }
}
