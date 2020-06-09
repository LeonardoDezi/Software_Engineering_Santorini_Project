package it.polimi.ingsw.Server;

import it.polimi.ingsw.Observer.Observer;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Client implements Observer {
    private static AtomicInteger clientCount = new AtomicInteger(0);
    private Socket socket;
    public final int clientID;
    private String username;
    private int numberOfPlayers;

    public Client(Socket socket){
        this.clientID = clientCount.incrementAndGet();
        this.socket = socket;

        //socket
    }


    @Override
    public Integer update(Client client) { //TODO write the update method to update all the boards
        return null;
    }

    public Socket getSocket() {
        return socket;
    }


    public String getUsername() {
        return username;
    }
}
