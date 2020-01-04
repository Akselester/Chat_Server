import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

@Log
public class Server implements Observable {
    public final static int PORT = 8080;
    private volatile static List<Observer> clients = new ArrayList<>();
    private static Connection database;

    @SuppressWarnings("InfiniteLoopStatement")
    public void start() {
        log.info("========== SERVER STARTS SUCCESSFULLY ==========");

        try {
            database = DriverManager.getConnection("jdbc:MySQL://localhost/chatdb?serverTimezone=UTC", "chat", "chat");
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientConnection connection = new ClientConnection(socket, this);
                new Thread(connection).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            log.info("Database connection problem");
            e.printStackTrace();
        } finally {
            if (database != null) {
                try {
                    database.close();
                } catch (SQLException e) {
                    log.info("Database closing problem");
                }
            }
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
