import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Log
public class MyServer implements Observable {
    public final static int PORT = 8080;
    private volatile static List<Observer> observerList = new ArrayList<>();

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        System.out.println("========= SERVER START ==========");
        try {
            new Thread(() -> {
                while (true) {
                    int i;
                    boolean isRemoved = false;
                    for (i = 0; i < observerList.size(); i++) {
                        if (!observerList.get(i).isConnected()) {
                            isRemoved = true;
                            break;
                        }
                    }
                    if (isRemoved) {
                        log.info("User removed");
                        observerList.remove(i);
                    }
                }
            }).start();
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientEntity clientEntity = new ClientEntity(new Client(), socket);
                new Thread(clientEntity).start();
                observerList.add(clientEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer :
                observerList) {
            observer.notifyObserver(message);
        }
    }
}
