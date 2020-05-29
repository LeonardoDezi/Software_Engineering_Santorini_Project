package it.polimi.ingsw.Server.VirtualView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver {
    public static String receive(Socket socket) throws IOException {
        //wait for the player answer on the socket
        //if the message is 0 return null(?)

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        String line = bufferedReader.readLine();
        JSONObject jsonObject = new JSONObject(line);

        return jsonObject.toString();
    }
}
