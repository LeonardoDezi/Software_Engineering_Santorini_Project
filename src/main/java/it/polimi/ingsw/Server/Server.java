package it.polimi.ingsw.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * this class represents the Server
 */
public class Server {

    private int port;
    public Lobby lobby;
    private GameMaster gameMaster;

    /**
     * creates and initialize the server.
     * @param port is the server port.
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * this method starts the server application with the lobby.
     */
    public void startServer() {

            //It creates threads when necessary, otherwise it re-uses existing one when possible
            ExecutorService executor = Executors.newCachedThreadPool();
            ServerSocket serverSocket;
            try{
                serverSocket = new ServerSocket(port);
            }catch (IOException e){
                System.err.println(e.getMessage()); //port not available
                return;
            }

            lobby = new Lobby();
            gameMaster = new GameMaster();
            lobby.addObserver(gameMaster);

            System.out.println("Server ready");
            while (true){
                try{
                    Socket socket = serverSocket.accept();
                    executor.submit( new ServerClientHandler(socket, lobby));
                }catch(IOException e){
                    break; //In case the serverSocket gets closed
                }
            }
        }
    }