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

    public void update(Client client) throws IOException {
        for( int i = 0; observers.get(i) != null; i++){
            Integer status = observers.get(i).update(client);
            for(int j=0; j<10; j++){
                if(status!=0){
                    break;
                }
                if(j==9){
                    System.out.print("Problems in match creation, please reset client");
                    client.getSocket().close();
                    return;
                }
                status=observers.get(i).update(client);
            }
        }
    }

}
