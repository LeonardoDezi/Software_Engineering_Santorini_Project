package it.polimi.ingsw.Client.NetworkHandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver {
    public String receive(Socket socket){
        //wait for the server answer on the socket

        BufferedReader bufferedReaderreader = null;
        try {
            bufferedReaderreader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line = null;
        try {
            line = bufferedReaderreader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(line);

        return jsonObject.toString();
    }

   /* Scanner stdin = new Scanner(System.in);

        try {
        Thread t0 = asyncReadFromSocket(socketIn);
        Thread t1 = asyncWriteToSocket(stdin, socketOut);
        t0.join();
        t1.join();
    } catch (InterruptedException | NoSuchElementException e) {
        System.out.println("Connection closed from the client side");
    } finally {
        stdin.close();
        socketIn.close();
        socketOut.close();
        socket.close();
    }*/

}
