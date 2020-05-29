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

    public ServerClientHandler(final Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run () {
        try {

            JSONObject jsonObject = new JSONObject();
            String message = jsonObject.toString();
            Sender.send(message,socket);
            Receiver.receive(socket);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSocket();
        }
    }
    private void closeSocket(){
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}

