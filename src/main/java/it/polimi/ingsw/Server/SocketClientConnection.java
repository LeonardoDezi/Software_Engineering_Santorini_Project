package it.polimi.ingsw.Server;


import it.polimi.ingsw.Observer.Observable;
import it.polimi.ingsw.Observer.Observer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.net.Socket.*;

public class SocketClientConnection extends Observable implements ClientConnection, Runnable{

    private Socket socket;
    private ObjectOutputStream out;
    private Server server;
    private ArrayList<Client> observer;

    private boolean active = true;

    public SocketClientConnection(Socket socket) {
        this.socket = socket;
    }

    private synchronized boolean isActive(){
        return active;
    }

    private synchronized void send(Object message, Client client) {
        try {
            //TODO json.serialize and send message
        } catch (Exception e) {
            e.printStackTrace();
        } //TODO change catch with the right one
       /* } catch(IOException e){
            System.err.println(e.getMessage());
        }*/

    }

    @Override
    public synchronized void closeConnection(Client client) {
        send("Connection closed!", client);
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    @Override
    public void addObserver(Client client) {
        this.observer.add(client);
    }

    private void close(Client client) {
        closeConnection(client);
        System.out.println("Deregistering client" + client.);
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    @Override
    public void asyncSend(final Object message){
        new Thread(new Runnable() {
            @Override
            public void run(){
            for(int i=0; i< observer.size(); i++){
                send(message);
            }
            }
        }).start();
    }

    @Override
    public void run() {
        Scanner in;
        String name;
        try{
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            send("Welcome!\nWhat is your name?");
            String read = in.nextLine();
            name = read;
            server.lobby(this, name);
            while(isActive()){
                read = in.nextLine();
                notify(read);
            }
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        }finally{
            close();
        }
    }
}