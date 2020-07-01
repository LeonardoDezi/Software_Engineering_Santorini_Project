package it.polimi.ingsw.Parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

public class Sender {
    public static void send(String m, Socket socket) throws IOException {
        try {
            if(m==null){
                //TODO send -1
            }

            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            Message message = new Message();
            message.setMessage(m);
            Gson gson = new GsonBuilder().serializeNulls().create();
            String string = gson.toJson(message);
            JsonObject jsonObject = JsonParser.parseString(string).getAsJsonObject();
            writer.write(jsonObject.toString() + "\n");
            writer.flush();
        } catch (SocketException e){
           // System.out.println("Error sender");
        }
    }
}


