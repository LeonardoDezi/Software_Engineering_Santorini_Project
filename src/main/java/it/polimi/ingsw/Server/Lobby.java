package it.polimi.ingsw.Server;

import java.util.LinkedList;
import java.util.List;

public class Lobby {
    private static LinkedList<Client> clients;

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
        notify();
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

    public Client getfirstClient(){
        Client firstCLient = clients.get(0);
        clients.removeFirst();
        return firstCLient;
    }

}
