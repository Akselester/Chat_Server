import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@Log
//@AllArgsConstructor
public class ClientConnection implements Observer, Runnable {
    private Client client;
    private Socket socket;
    private Server server;

    public ClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public void stopClient() {

    }

    @Override
    public void notifyObserver(String message) {

    }

    @Override
    public void run() {
        try {
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            authorise(clientInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean authorise(BufferedReader clientInput) throws IOException {
        String input;
        while (true) {
            if (clientInput.ready()) {
                input = clientInput.readLine();
                String login = input.split(" ")[0];
                String password = input.split(" ")[1];
                client = new Client(login, password);
                server.addObserver(this, login);
                log.info("New connection " + "\"" + login + "\"" + " added");
                return true;
            }
        }
    }

    public String getClientName() {
        return null;
    }
}
