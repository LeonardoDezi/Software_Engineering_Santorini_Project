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
    public static void send(String m, Socket socket) throws IOException {
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
    }
}


