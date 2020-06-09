package it.polimi.ingsw.Server;

import java.io.IOException;
import java.net.Socket;

public class ServerClientHandler implements Runnable{

    private Socket socket;
    private Lobby lobby;

    public ServerClientHandler(final Socket socket, Lobby lobby) throws IOException {
        this.socket = socket;
        this.lobby = lobby;
    }

    @Override
    public void run () { //TODO controlliamo bene
        Client client = null;
        try {
            client = new Client(socket);
            lobby.addClient(client);
        } catch (IOException e) {
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

