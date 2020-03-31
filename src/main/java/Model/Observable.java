package Model;

public interface Observable {
    void addObserver(Observer);
    void notify();
}
