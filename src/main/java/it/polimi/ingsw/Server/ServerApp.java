package it.polimi.ingsw.Server;

import java.io.*;

public class ServerApp {
    public static String COLOUR1 = "red";
    public static String COLOUR2 = "blue";
    public static String COLOUR3 = "white";
    public static void main( String[] args ) throws IOException{

        Server server = new Server(8080);
        try {
            server.startServer();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
