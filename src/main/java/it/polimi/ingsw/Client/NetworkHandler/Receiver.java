package it.polimi.ingsw.Client.NetworkHandler;

import java.net.Socket;

public class Receiver {
    public String receive(Socket socket){
        //wait for the server answer on the socket
        return null;
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
