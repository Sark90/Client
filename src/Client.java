import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    private static final String SIGN = "[Client] ";
    private static final int SERV_PORT = 4444;
    private static Socket fromserver = null;
    private static InetAddress servAddr = null;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader inu;
    private String fuser,fserver;

    public Client() {
        super();
        start();
    }
    public void run() {
        connect();
        initIO();
        //Receiver receiver = new Receiver(); //??? Locks console
        try {
            while ((fserver = in.readLine()) != null) {
                System.out.println(fserver);
                //while ((fuser = inu.readLine()) != null) {
                    fuser = inu.readLine();
                    if (fuser.equalsIgnoreCase("close")) break;
                    if (fuser.equalsIgnoreCase("exit")) break;
                    out.println(fuser);
                //}
            }
            //receiver.join();
            //closeIO();
        } catch (IOException e) {
            System.out.println(e);
        } /*catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        closeIO();
    }

    private void closeIO() {
        try {
            out.close();
            in.close();
            inu.close();
            fromserver.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initIO() {
        try {
            in  = new BufferedReader(new InputStreamReader(fromserver.getInputStream()));
            out = new PrintWriter(fromserver.getOutputStream(),true);
            inu = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Receiver extends Thread {
        Receiver() {
            super();
            start();
        }

        @Override
        public void run() {
            try {
                String fserver;
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(fromserver.getInputStream()));
                while ((fserver = in.readLine()) != null) {
                    System.out.println(fserver);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connect() {
        while (fromserver == null) {
            try {
                servAddr = InetAddress.getLocalHost();
                System.out.println(SIGN + "Connecting to... " + servAddr);
                fromserver = new Socket(servAddr, SERV_PORT);
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("Failed. Waiting... ");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        System.out.println(SIGN + "Connected.");
    }
}
