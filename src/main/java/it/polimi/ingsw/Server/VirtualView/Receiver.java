package it.polimi.ingsw.Server.VirtualView;

import com.google.gson.Gson;
import it.polimi.ingsw.Parser.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver {
    public static String receive(Socket socket) throws IOException {
        //wait for the player answer on the socket
        //if the message is 0 return null(?)

        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String line = reader.readLine();
        Message message = gson.fromJson(line, Message.class);
        String m = message.getMessage();

        return m;
    }
}
