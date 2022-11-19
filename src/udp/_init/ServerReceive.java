package udp._init;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerReceive {
    public static void main(String[] args) {
        // Receive and read a packet
        try {
            byte[] buffer = new byte[100];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            DatagramSocket ds = new DatagramSocket(2134);
            ds.receive(incoming);
            byte[] data = incoming.getData();
            String s = convertBytesToString(data);
            System.out.println("Port" + incoming.getPort() + " on" + incoming.getAddress() + " sent this message: "); System.out.println(s);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    // A utility method to convert the byte array
    // data into a string representation.
    public static String convertBytesToString(byte[] buffer) {
        if (buffer == null) return null;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (buffer[i] != 0) {
            sb.append((char) buffer[i]);
            i++;
        }
        return sb.toString();
    }
}
