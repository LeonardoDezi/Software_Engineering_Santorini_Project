package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Server.Model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private Lobby lobby;

    public Server(int port) {
        this.port = port;
    }

    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newCachedThreadPool();

    public void startServer() throws IOException {

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        Lobby lobby = new Lobby();
        System.out.println("Server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();

                executor.submit(new SocketClientConnection(socket));
                Client client = new Client();
                lobby.addClient(client);
            } catch (IOException e) {
                break;
            }
        }
        executor.shutdown();
        serverSocket.close();
    }
}