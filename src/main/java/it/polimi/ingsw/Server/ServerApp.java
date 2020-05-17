package it.polimi.ingsw.Server;

import java.io.IOException;

public class ServerApp {
    public static String RED = "red";
    public static String BLUE = "blue";
    public static String WHITE = "white";
    public static void main( String[] args )
    {


        Server server = new Server(1337); //la porta va data con il parser
        try {
            server.startServer();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
