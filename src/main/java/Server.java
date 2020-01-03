import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

@Log
public class Server implements Observable {
    public final static int PORT = 8080;
    private volatile static List<Observer> clients = new ArrayList<>();

    @SuppressWarnings("InfiniteLoopStatement")
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
    public void notifyObservers(String message, String whoTalks) {
        for (Observer client :
                clients) {
            client.notifyObserver(whoTalks + " >> " + message);
        }
    }

    @Override
    public void addObserver(Observer connection) {
        clients.add(connection);
    }

    @Override
    public void removeObserver(Observer connection) {
        if (clients.remove(connection)) {
            log.info("Client removed");
        }
    }
}
