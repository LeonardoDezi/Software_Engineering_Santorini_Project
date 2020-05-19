package it.polimi.ingsw.Server;

import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    public static String RED = "red";
    public static String BLUE = "blue";
    public static String WHITE = "white";
    public static void main( String[] args ) throws IOException{

        int portNumber = Integer.parseInt(args[1]);

        ServerSocket serverSocket= new ServerSocket(portNumber);

        try {
            while(true) {
                Socket socket = serverSocket.accept();
                startHandler(socket);
            }
        } finally {
            serverSocket.close();
        }

    /*    Server server = new Server(1337); //la porta va data con il parser
        try {
            server.startServer();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }*/
    }

    private static void startHandler(final Socket socket) throws IOException{
        Thread thread = new Thread() {
            @Override
            public void run() {
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
        };

        thread.start();


    }
}
