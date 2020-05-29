package it.polimi.ingsw.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Observable<T> {

    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    public void updateAll() throws IOException {
        synchronized (observers) {
            for(Observer observer : observers){
                observer.update();
            }
        }
    }

}
