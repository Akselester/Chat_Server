import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Log
@AllArgsConstructor
public class ClientEntity implements Observer, Runnable {
    private Client client;
    private Socket socket;

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public void stopClient() {

    }

    @Override
    public void notifyObserver(String message) {
        try (PrintWriter printWriter = new PrintWriter(socket.getOutputStream())) {
            printWriter.write(message);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        log.info("New Client");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String input = reader.readLine();
                log.info(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
