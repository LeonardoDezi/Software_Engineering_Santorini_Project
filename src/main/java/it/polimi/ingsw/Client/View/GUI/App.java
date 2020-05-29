package it.polimi.ingsw.Client.View.GUI;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class App {



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());

        JFrame mainFrame = new JFrame("SANTORINI");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MyPanel mainPanel = new MyPanel();
        mainFrame.add(mainPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);

        new StartDialog(mainFrame);


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