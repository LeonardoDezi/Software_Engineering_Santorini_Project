package it.polimi.ingsw.Server;

import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.Parser.Receiver;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Client implements Observer{
    private static AtomicInteger clientCount = new AtomicInteger(0);
    private Socket socket;
    public final int clientID;
    private String username;
    private int numberOfPlayers;

    public Client(Socket socket) throws IOException {
        this.clientID = clientCount.incrementAndGet();
        this.socket = socket;
        this.username = Receiver.receive(socket);
        //socket
    }


    @Override
    public Integer update(Client client) { //TODO write the update method to update all the boards
        return 0;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUsername() throws IOException {
        return username;
    }
}
