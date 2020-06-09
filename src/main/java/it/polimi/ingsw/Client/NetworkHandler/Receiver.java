package it.polimi.ingsw.Client.NetworkHandler;

import com.google.gson.Gson;
import it.polimi.ingsw.Parser.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver {
    private static String m;
    public static String receive(Socket socket){
        //wait for the server answer on the socket

        Gson gson = new Gson();

        try {
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

           String line = bufferedReader.readLine();

               Message message = gson.fromJson(line, Message.class);
               m = message.getMessage();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return m;
    }
}
