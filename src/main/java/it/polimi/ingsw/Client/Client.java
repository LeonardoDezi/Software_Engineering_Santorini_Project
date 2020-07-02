package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.View.GUI.*;
import it.polimi.ingsw.Parser.Sender;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * represents the client that connects to the game
 */
public class Client {

    private int clientID;

    /** represents the players username */
    private static String username;

    /** represents the server ip to which the client connects */
    private String ip;

    /** represents the server port to which the client connects */
    private int port;

    /** represents the client own socket that interfaces with the server */
    private Socket serverSocket;

    private GUIClientController guiClientController;

    /**
     * create and initialize the client
     * @param ip is the server ip
     * @param port is the server port
     */
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * starts the client application
     * @throws IOException exception
     */
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

        try{
            serverSocket.close();
        } catch (IOException e){
            System.out.println("Disconnected from server");

        }

        System.out.println("Do you want to play another game? y/n");
        if (clientController.returnBoolean()){
            startClient();
        }
    }

    /**
     * gives back the client socket linked to the server
     * @return serverSocket
     */
    public Socket getServerSocket(){
        return this.serverSocket;
    }

    /**
     * ask to the client its username
     * @return username
     */
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

    public void startClient(String text, MainFrame frame) {

        this.serverSocket = new Socket();

        try {
            serverSocket = new Socket(ip, port);
        } catch (IOException e) {
            new FatalErrorWindow(frame);
            return;
        }


        Client.username = text;

        try {
            Sender.send(username, serverSocket);
        }catch(IOException e){
            frame.waitingDialog.setVisible(false);
            new FatalErrorWindow(frame);
            return;
        }

        guiClientController = new GUIClientController(this, frame);
        frame.setController( guiClientController);


        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                guiClientController.matchSetup(serverSocket);
                return null;
            }
        };

        worker.execute();


    }

    public GUIClientController getController(){ return this.guiClientController;}



}