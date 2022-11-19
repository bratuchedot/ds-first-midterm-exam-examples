package udp._init;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientSend {
    public static void main(String[] args) {
        // Create and send a packet
        try {
            InetAddress metalab = InetAddress.getByName("localhost");
            int port = 2134;
            String s = "My second UDP Packet";
            byte[] b = s.getBytes();
            DatagramPacket dp = new DatagramPacket(b, b.length, InetAddress.getByName("localhost"), 2134);
            DatagramSocket sender = new DatagramSocket();
            sender.send(dp);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
