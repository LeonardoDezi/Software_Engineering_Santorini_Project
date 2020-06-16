package it.polimi.ingsw.Client.NetworkHandler;

import com.google.gson.Gson;
import it.polimi.ingsw.Parser.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver {
    public static String receive(Socket socket) throws IOException {
        //wait for the server answer on the socket

        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String line = reader.readLine();
        Message message = gson.fromJson(line, Message.class);
        String m = message.getMessage();

        return m;
    }
}
