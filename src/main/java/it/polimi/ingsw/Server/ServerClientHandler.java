package it.polimi.ingsw.Server;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerClientHandler implements Runnable{

    private Socket socket;

    public ServerClientHandler(final Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run () {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            String line = reader.readLine();
            JSONObject jsonObject = new JSONObject(line);

            writer.write(jsonObject.toString() + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSocket();
        }
    }
    private void closeSocket(){
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}

