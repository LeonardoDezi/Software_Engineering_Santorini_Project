package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.Parser.Message;

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

        //TODO ask the client for the name
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String line = reader.readLine();
        Message message = gson.fromJson(line, Message.class);
        this.username = message.getMessage();

        System.out.println(username);
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
