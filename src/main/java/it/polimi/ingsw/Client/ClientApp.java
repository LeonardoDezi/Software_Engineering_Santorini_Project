package it.polimi.ingsw.Client;

import java.io.IOException;


public class ClientApp {

    public static void main(String[] args){

        Client client = new Client();
        try{
            client.startClient();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
