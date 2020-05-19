package it.polimi.ingsw.Client;

import org.json.JSONObject;
import java.io.*;
import java.net.Socket;

public class ClientApp {

    public static void main(String[] args) throws IOException {

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        Socket socket = new Socket(hostName,portNumber);
        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Hello World!");

        writer.write(jsonObject.toString() + "\n");
        writer.flush();

        String line = reader.readLine();
        jsonObject = new JSONObject(line);

        System.out.println("Received from Server:\n "+ jsonObject.toString(2));

        socket.close();

    }
}
     /*   //TODO add the ip and the port from json
        // ip = jason.read ip
        // port = jason.read port
        String ip = null;
        Integer port = null;
        Client client = new Client(ip, port);
        try{
            client.startClient();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }*/

