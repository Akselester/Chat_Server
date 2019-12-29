import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Log
public class Server implements Observable {
    public final static int PORT = 8080;
    private volatile static Map<String, Observer> clients = new HashMap<>();

    public void start() {
        log.info("========== SERVER STARTS SUCCESSFULLY ==========");

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientConnection connection = new ClientConnection(socket, this);
                new Thread(connection).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyObservers(String message) {

    }

    @Override
    public void addObserver(Observer connection, String clientName) {
        clients.put(clientName, connection);
    }

    @Override
    public void removeObserver(String clientName) {
        clients.remove(clientName);
    }
}
