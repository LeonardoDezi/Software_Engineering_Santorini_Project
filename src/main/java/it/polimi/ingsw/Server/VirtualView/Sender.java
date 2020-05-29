package it.polimi.ingsw.Server.VirtualView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Sender {
    public static void send(String message, Socket socket) throws IOException {
        if (message == null) {
            //TODO send -1
        }

        JSONObject jsonObject = new JSONObject();

        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        writer.write(jsonObject.toString() + "\n");
        writer.flush();
    }
}
