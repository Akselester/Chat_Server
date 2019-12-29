public interface Observable {
    void notifyObservers (String message);
    void addObserver (Observer client, String clientName);
    void removeObserver (String clientName);
}
