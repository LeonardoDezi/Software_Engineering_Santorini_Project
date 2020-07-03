package it.polimi.ingsw.Server;

import java.io.IOException;
import java.net.Socket;

/**
 * this class adds the new clients to the lobby
 */
public class ServerClientHandler implements Runnable{

    private Socket socket;
    private Lobby lobby;

    /**
     * creates and initializes the class
     */
    public ServerClientHandler(final Socket socket, Lobby lobby) throws IOException {
        this.socket = socket;
        this.lobby = lobby;
    }

    /**
     * creates a new client and adds it to the lobby
     */
    @Override
    public void run () {
        try {
            Client client = new Client(socket);
            lobby.addClient(client);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void closeSocket(){
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}

