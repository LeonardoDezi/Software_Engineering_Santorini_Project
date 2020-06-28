package it.polimi.ingsw.Client.View.GUI;

import javax.swing.*;

/** this class contains the main method used to start the creation of the GUI */
public class App {

    /** main method used to start the creation of the GUI*/
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() { createAndShowGUI(); }});
    }

    /** starts the creation of the GUI and of the first window */
    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());

        new IntroFrame();

    }


}




//TODO
/*
    button.setOnAction(new EventHandler<ActionEvent>(){
        @Override
        public void handle(){
            System.out.println ("ciao");

        });

 */