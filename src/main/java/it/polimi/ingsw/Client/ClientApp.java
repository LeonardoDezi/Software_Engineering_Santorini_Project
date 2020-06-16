package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.View.CLI.Cli;

import java.io.*;

public class ClientApp {

    public static void main(String[] args) throws IOException {

    /*    System.out.println("Select game mode");
        if(args.length>0){
            Cli.startCli();
        }else
            Cli.startCli();
*/
        Client client = new Client("localhost", 8080);
        try {
            client.startClient();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
