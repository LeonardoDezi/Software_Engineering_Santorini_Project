package it.polimi.ingsw.Server;

import it.polimi.ingsw.Observer.Observable;
import it.polimi.ingsw.Observer.Observer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Lobby extends Observable {
    private ArrayList<Client> clients;
    private LinkedList<Observer> observers = new LinkedList<Observer>();

    public void lobby() throws IOException {
        clients = new ArrayList<Client>();
    }

    public void addClient(Client client) throws IOException {

     //   if(client==null) {

    //        throw new IllegalArgumentException("'newClient' was null");
    //    }

           // if(clients.contains(client)){
             //   return;
            //}
        System.out.println("prima di add");
        clients.add(0, client);
        System.out.println("sono qui");
        update(getFirstClient());
        removeClient(getFirstClient());
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
