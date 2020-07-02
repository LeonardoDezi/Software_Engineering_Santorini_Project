package it.polimi.ingsw.Server;

import it.polimi.ingsw.Observer.Observable;
import it.polimi.ingsw.Observer.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;


public class Lobby extends Observable {
    private ArrayList<Client> clients = new ArrayList<Client>();
    private LinkedList<Observer> observers = new LinkedList<Observer>();


    public void addClient(Client client) throws IOException, InterruptedException {

       if(client==null) {

            throw new IllegalArgumentException("'newClient' was null");
    }

            if(clients.contains(client)){
               return;
            }
        clients.add(client);
            if(observers.isEmpty()){
                System.out.print("Error");
            }
        update(getFirstClient());
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

    public void update(Client client) throws IOException, InterruptedException {
        for( int i = 0; i<observers.size(); i++){
            System.out.println(observers.size());
            Integer status = observers.get(i).update(client);

            if(status == 0){
                System.out.print("Problems in match creation, please reset client.\n");
                client.getSocket().close();
            }
        }
    }

}
