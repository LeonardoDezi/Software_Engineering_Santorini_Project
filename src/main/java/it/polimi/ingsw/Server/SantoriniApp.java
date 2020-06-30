package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.Client;

import java.io.*;

public class SantoriniApp {
    //TODO spostare
    public static String COLOUR1 = "red";
    public static String COLOUR2 = "blue";
    public static String COLOUR3 = "white";
    public static void main( String[] args ) throws IOException {

        if (args.length > 0) {
            String option = args[0];
            System.out.println(option);
            if (option.equals("--server")) {
                runServer();
            } else {
                runCli();
            }
        }else {
            runGui();
        }
    }

    private static void runGui() {
        System.out.println("starting gui");
    }

    private static void runServer(){
        System.out.println("starting server");
            Server server = new Server(8080);
        try {
            server.startServer();
        } catch(IOException e) {
            System.err.println(e.getMessage());
         }
        }


    public static void runCli(){
        System.out.println("starting cli");
        Client client = new Client("localhost", 8080);
        try {
            client.startClient();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }


}
