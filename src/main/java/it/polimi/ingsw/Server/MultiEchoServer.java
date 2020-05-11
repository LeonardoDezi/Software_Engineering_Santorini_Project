package it.polimi.ingsw.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiEchoServer {
    private int port;

    public MultiEchoServer(int port){
        this.port = port;
    }

    public void startServer() throws IOException{
        //It creates threads when necessary, otherwise it re-uses existing one when possible
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        Lobby lobby = new Lobby();
        GameMaster gameMaster = new GameMaster();
        gameMaster.run();
        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            System.err.println(e.getMessage()); //port not available
            return;
        }
        System.out.println("Server ready");


        while (true){
            try{
                Socket socket = serverSocket.accept();
                Client client = new Client(socket);
                lobby.addClient(client);
            }catch(IOException e){
                break; //In case the serverSocket gets closed
            }
        }
        executor.shutdown();
        serverSocket.close();
    }
}
