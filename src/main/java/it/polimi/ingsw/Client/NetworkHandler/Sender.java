package it.polimi.ingsw.Client.NetworkHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Sender {
    public void send(String message, Socket socket){
        if(message==null){
          //TODO send -1
        }
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();

        try {
            writer.write(jsonObject.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


