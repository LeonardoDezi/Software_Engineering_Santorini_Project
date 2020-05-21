package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.Model.Board;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private int clientID;
    private String ip;
    private int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

  /*  private boolean active = true;

    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void setActive(boolean active) {
        this.active = active;
    }

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        if (inputObject instanceof String) {
                            System.out.println((String) inputObject);
                        } else if (inputObject instanceof Board) {
                            ((Board) inputObject).print();
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                } catch (Exception e) {
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    public Thread asyncWriteToSocket(final Scanner stdin, final PrintWriter socketOut) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        String inputLine = stdin.nextLine();
                        socketOut.println(inputLine);
                        socketOut.flush();
                    }
                } catch (Exception e) {
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    public void startConnection() throws IOException {

        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);

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
        }
    }*/

    public void startClient() throws IOException {

        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prova", "dai dai dai");

        writer.write(jsonObject.toString() + "\n");
        writer.flush();

        String line = reader.readLine();
        jsonObject = new JSONObject(line);

        System.out.println("Received from Server:\n " + jsonObject.toString(2));

        System.out.println("Connection closed");

        socket.close();


    }
}

/*        //TODO aggiungi runnable
        SwingUtilities.
        JFrame frame = new JFrame("Santorini");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Integer screenHeight = screenSize.height;
        Integer screenWidth = screenSize.width;
        frame.setSize(screenWidth, screenHeight);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


    }

}
*/