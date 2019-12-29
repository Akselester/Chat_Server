public interface Observable {
    void notifyObservers(String message, String whoTalks);

    void addObserver(Observer client);

    void removeObserver(Observer client);
}
