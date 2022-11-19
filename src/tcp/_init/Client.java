package tcp._init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        // The client part
        try {
            Socket s = new Socket("localhost", 2345);
            InputStream in = s.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            int c;
            while ((c = br.read()) != -1) {
                System.out.print((char) c);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
