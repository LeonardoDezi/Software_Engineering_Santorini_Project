package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.NetworkHandler.Receiver;
import it.polimi.ingsw.Client.NetworkHandler.Sender;
import it.polimi.ingsw.Server.Model.Board;
import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Card;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private int clientID;
    private String username;
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
            e.printStackTrace();
        }
        //TODO ask the player for the username
        this.username = "username";
        System.out.println("Connection established");
        ClientController clientController = new ClientController(this);
        clientController.matchSetup(serverSocket); //TODO here starts the client for the game
        clientController.play(serverSocket);

        //TODO ask for rematch, if yes newgame if no close app
    }

    public Socket getServerSocket(){
        return this.serverSocket;
    }
}