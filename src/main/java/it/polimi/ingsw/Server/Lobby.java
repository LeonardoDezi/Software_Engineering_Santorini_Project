package it.polimi.ingsw.Server;

import java.util.LinkedList;
import java.util.List;

public class Lobby implements Observable{
    private static LinkedList<Client> clients;
    private LinkedList<Observer> observers;

    public void Lobby(){
        clients = new LinkedList<Client>();

    }

    public void addClient(Client client){
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
        clients.removeFirst();
        return firstClient;
    }

    @Override
    public void add(Observer observer) {

        this.observers.add(observer);

    }

    @Override
    public void remove(Observer observer) {

        this.observers.remove(observer);

    }
}
