package it.polimi.ingsw.Client;

import java.io.*;

public class ClientApp {

    public static void main(String[] args) throws IOException {

        Client client = new Client("localhost", 8080);
        try {
            client.startClient();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
