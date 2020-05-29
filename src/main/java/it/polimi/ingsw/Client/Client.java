package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.Model.Board;
import it.polimi.ingsw.Server.Model.Card;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client implements Runnable {

    private int clientID;
    private String username;
    private String ip;
    private int port;
    private Socket serverSocket;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

  /*  private boolean active = true;

    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void setActive(boolean active) {
        this.active = active;
    }

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        if (inputObject instanceof String) {
                            System.out.println((String) inputObject);
                        } else if (inputObject instanceof Board) {
                            ((Board) inputObject).print();
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                } catch (Exception e) {
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    public Thread asyncWriteToSocket(final Scanner stdin, final PrintWriter socketOut) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        String inputLine = stdin.nextLine();
                        socketOut.println(inputLine);
                        socketOut.flush();
                    }
                } catch (Exception e) {
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    public void startConnection() throws IOException {

        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());

    }*/


    public void startClient() throws IOException {
        run();
    }

    @Override
    public void run() {
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

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO ask for rematch, if yes newgame if no close app
    }

    public Socket getServerSocket(){
        return this.serverSocket;
    }
}


        /*

        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(serverSocket.getOutputStream(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prova", "dai dai dai");

        try {
            writer.write(jsonObject.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject = new JSONObject(line);

        System.out.println("Received from Server:\n " + jsonObject.toString(2));

        System.out.println("Connection closed");



    } */
