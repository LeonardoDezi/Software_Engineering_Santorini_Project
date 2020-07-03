package it.polimi.ingsw.Server;

import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.Parser.Receiver;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * this class is the representation of the client in the server side
 */
public class Client implements Observer{
    private static AtomicInteger clientCount = new AtomicInteger(0);
    private Socket socket;
    public final int clientID;
    private String username;
    private int numberOfPlayers;

    /**
     * creates and initialize the class.
     * @param socket the socket on which the client is connected.
     * @throws IOException from receive method.
     */
    public Client(Socket socket) throws IOException {
        this.clientID = clientCount.incrementAndGet();
        this.socket = socket;
        this.username = Receiver.receive(socket);
        //socket
    }


    @Override
    public Integer update(Client client) {
        return 0;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUsername() {
        return username;
    }
}
