package it.polimi.ingsw.Server;

import it.polimi.ingsw.Observer.Observable;
import it.polimi.ingsw.Observer.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class waits for new players and notifies the game master when they arrive to redirect them.
 */
public class Lobby extends Observable {
    private ArrayList<Client> clients = new ArrayList<Client>();
    private LinkedList<Observer> observers = new LinkedList<Observer>();

    /**
     * adds a new client to the lobby if the client is not already connected.
     * @param client is the new client.
     * @throws IOException from update method.
     * @throws InterruptedException from update method.
     */
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

    /**
     * removes a client from the lobby.
     * @param client is the client that is being removed.
     */
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

    /**
     * notifies the game master thant a new client has connected and needs to be sent in a game.
     * @param client is the client.
     * @throws IOException from update method.
     * @throws InterruptedException from update method.
     */
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
