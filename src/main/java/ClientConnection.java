import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Log
public class ClientConnection implements Observer, Runnable {
    private Client client;
    private Socket socket;
    private Server server;
    private PrintWriter output;

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
        output.println(message);
        output.flush();
    }

    @Override
    public void run() {
        try {
            output = new PrintWriter(socket.getOutputStream());
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            authorise(clientInput);
            listen(clientInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen(BufferedReader clientInput) throws IOException {
        String input;
        while ((input = clientInput.readLine()) != null) {
            server.notifyObservers(input, client.getLogin());
        }
    }

    private void authorise(BufferedReader clientInput) throws IOException {
        String input;
        while ((input = clientInput.readLine()) != null) {
            String login = input.split(" ")[0];
            String password = input.split(" ")[1];
            client = new Client(login, password);
            server.addObserver(this);
            log.info("New connection " + "\"" + login + "\"" + " added");
            return;
        }
    }
}
