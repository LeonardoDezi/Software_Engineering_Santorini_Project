package it.polimi.ingsw.Server;

import java.io.IOException;

public class ServerApp {
    public static void main( String[] args )
    {
        MultiEchoServer server = new MultiEchoServer(1337); //la porta va data con il parser
        try {
            server.startServer();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }

    }



}
