public interface Observer {
    boolean isConnected();
    void stopClient();
    void notifyObserver(String message);
}
