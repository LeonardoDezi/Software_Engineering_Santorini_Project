package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Server.Client;

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
}
