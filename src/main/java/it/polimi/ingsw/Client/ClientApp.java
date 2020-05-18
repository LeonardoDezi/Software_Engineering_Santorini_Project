package it.polimi.ingsw.Client;

import java.io.IOException;


public class ClientApp {

    public static void main(String[] args){

        //TODO add the ip and the port from json
        // ip = jason.read ip
        // port = jason.read port
        String ip = null;
        Integer port = null;
        Client client = new Client(ip, port);
        try{
            client.startClient();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
