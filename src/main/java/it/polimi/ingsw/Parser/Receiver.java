package it.polimi.ingsw.Parser;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class Receiver {
    public static String receive(Socket socket) throws IOException {

        try {
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            String line = reader.readLine();
            Message message = gson.fromJson(line, Message.class);
            String m = message.getMessage();

            return m;
        } catch (SocketException | NullPointerException e){
            //System.out.println("Error receiver");
            return "-1@";
        }

    }
}
