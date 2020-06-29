package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.VirtualView.Sender;

import java.io.*;
import java.net.Socket;

public class Client {

    private int clientID;
    private static String username;
    private String ip;
    private int port;
    private Socket serverSocket;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void startClient() throws IOException {

        this.serverSocket = new Socket();
        try {
            serverSocket = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println("The server is not responding.\n Please retry later.\n");
            return;
        }


        System.out.println("Connection established");
        System.out.println("Insert Name");

        username = getUsername();

        Sender.send(username, serverSocket);

        ClientController clientController = new ClientController(this);
        clientController.matchSetup(serverSocket);
        clientController.play(serverSocket);

        //TODO ask for rematch, if yes newgame if no close app
    }


    public Socket getServerSocket(){
        return this.serverSocket;
    }

    public static String getUsername(){

        try{
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            username = bufferRead.readLine();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return username;
    }

}