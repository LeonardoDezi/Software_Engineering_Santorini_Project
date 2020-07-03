package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Client.View.GUI.IntroFrame;

import javax.swing.*;
import java.io.*;

/**
 * executable of the program.
 */
public class SantoriniApp {

    /**
     * main method of the program.
     * @param args input arguments.
     */
    public static void main( String[] args )  {

        if (args.length > 0) {
            String option = args[0];
            System.out.println(option);
            if (option.equals("--server")) {
                runServer();
            } else {
                runCli();
            }
        }else {
            runGui();
        }
    }


    /**
     * starts the gui.
     */
    private static void runGui() {
        System.out.println("starting gui");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { createAndShowGUI(); }});
    }

    /**
     * starts the server
     */
    private static void runServer(){
        System.out.println("starting server");
            Server server = new Server(8080);

            server.startServer();
        }


    /**
     * starts the cli
     */
    public static void runCli(){
        System.out.println("starting cli");
        System.out.println("insert Server ip or url");

        String ip = new String();
        try{
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            ip = bufferRead.readLine();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        Client client = new Client(ip, 8080);
        try {
            client.startClient();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }


    /** starts the creation of the GUI and of the first window */
    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());

        new IntroFrame();

    }


}
