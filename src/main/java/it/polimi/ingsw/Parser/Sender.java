package it.polimi.ingsw.Parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import static java.lang.Thread.sleep;

/** this class sends the messages */
public class Sender {

    /**
     * this method takes a string message and transforms it to a json message and sends it
     * @param m is the string message
     * @param socket is the socket to which is sending the message
     * @throws IOException if the connection ends
     */
    public static void send(String m, Socket socket) throws IOException {
        try {

            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            Message message = new Message();
            message.setMessage(m);
            Gson gson = new GsonBuilder().serializeNulls().create();
            String string = gson.toJson(message);
            JsonObject jsonObject = JsonParser.parseString(string).getAsJsonObject();
            sleep(100);
            writer.write(jsonObject.toString() + "\n");
            writer.flush();
        } catch (SocketException | InterruptedException e){
           // System.out.println("Error sender");
        }
    }
}


