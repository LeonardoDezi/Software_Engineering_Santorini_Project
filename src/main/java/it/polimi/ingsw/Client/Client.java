package it.polimi.ingsw.Client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.Client.View.CLI.Cli;
import it.polimi.ingsw.Parser.Message;
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
            e.printStackTrace();
        }

        System.out.println("Connection established");
        System.out.println("Choose game mode");
        System.out.println("Write 1 for command line \nWrite 2 for graphic");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int gametype = Integer.parseInt(bufferedReader.readLine());

        if (gametype == 1) {
            Cli.startCli();
        }

        //TODO ask the player for the username

        OutputStreamWriter writer = new OutputStreamWriter(serverSocket.getOutputStream(), "UTF-8");
        Message message = new Message();
        message.setMessage(username);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String string = gson.toJson(message);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(string).getAsJsonObject();
        writer.write(jsonObject.toString() + "\n");
        writer.flush();

        ClientController clientController = new ClientController(this);
        clientController.matchSetup(serverSocket);//TODO here starts the client for the game
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