package it.polimi.ingsw.Server;

import it.polimi.ingsw.Observer.Observer;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Client implements Observer {
    private static AtomicInteger clientCount = new AtomicInteger(0);
    private Socket socket;
    public final int clientID;
    private String username;

    public Client(Socket socket){
        this.clientID = clientCount.incrementAndGet();
        this.socket = socket;
        //TODO ask the client for the name and save it
        //socket
    }


    @Override
    public void update() { //TODO write the update method to update all the boards

    }

    public Socket getSocket() {
        return socket;
    }


    public String getUsername() {
        return username;
    }
}
