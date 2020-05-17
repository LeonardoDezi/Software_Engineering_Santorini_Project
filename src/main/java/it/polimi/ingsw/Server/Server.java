package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.Model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private Map<String, ClientConnection> waitingConnection = new HashMap<>();
    private Map<ClientConnection, ClientConnection> playingConnection = new HashMap<>();

    //Deregister connection
    public synchronized void deregisterConnection(ClientConnection c) {
        ClientConnection opponent = playingConnection.get(c);
        if (opponent != null) {
            opponent.closeConnection();
        }
        playingConnection.remove(c);
        playingConnection.remove(opponent);
        Iterator<String> iterator = waitingConnection.keySet().iterator();
        while (iterator.hasNext()) {
            if (waitingConnection.get(iterator.next()) == c) {
                iterator.remove();
            }
        }
    }

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
                Client client = new Client(socket);
                lobby.addClient(client);
            } catch (IOException e) {
                break;
            }
        }
        executor.shutdown();
        serverSocket.close();
    }
}