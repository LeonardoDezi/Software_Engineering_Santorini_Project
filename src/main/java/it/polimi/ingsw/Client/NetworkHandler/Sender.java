package it.polimi.ingsw.Client.NetworkHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.Parser.Message;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Sender {
    public static void send(String message, Socket socket){
        if(message==null){
          //TODO send -1
        }

        Message m = new Message();
        m.setMessage(message);

        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(m);

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();

        try {
            writer.write(jsonObject.toString());
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


