package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.VirtualView.Receiver;
import it.polimi.ingsw.Server.VirtualView.Sender;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
        Client client = new Client(socket);
        try {
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

