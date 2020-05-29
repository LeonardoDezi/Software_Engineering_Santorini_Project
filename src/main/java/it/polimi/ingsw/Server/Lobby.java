package it.polimi.ingsw.Server;

import it.polimi.ingsw.Observer.Observable;
import it.polimi.ingsw.Observer.Observer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Lobby extends Observable {
    private static LinkedList<Client> clients;
    private LinkedList<Observer> observers;

    public void Lobby(){
        clients = new LinkedList<Client>();

    }

    public void addClient(Client client) throws IOException {
        if(client==null) {
            throw new IllegalArgumentException("'newClient' was null");
        }
        synchronized(clients){
            if(clients.contains(client)) return;
            clients.add(client);
        }

        for( int i = 0; observers.get(i) != null; i++){
            observers.get(i).update();
        }
    }

    public void removeClient(Client client) {
        if (client == null)
            throw new IllegalArgumentException("'client' was null");
        synchronized (clients){
            if (!clients.contains(client))
                return;
            clients.remove(client);
        }
    }

    public Client getFirstClient(){
        Client firstClient = clients.get(0);
        this.removeClient(clients.get(0));
        return firstClient;
    }

    @Override
    public void addObserver(Observer observer) {

        this.observers.add(observer);

    }

    @Override
    public void removeObserver(Observer observer) {

        this.observers.remove(observer);

    }
}
