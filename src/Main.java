//rename project to "Client"
public class Main {

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
