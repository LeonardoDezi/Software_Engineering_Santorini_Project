package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.GUIClientController;
import it.polimi.ingsw.Parser.Sender;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;

import static it.polimi.ingsw.Client.View.GUI.IntroFrame.getScaledImage;
import static java.awt.BorderLayout.PAGE_END;


/** this class represents the dialog where the Challenger chooses how many players will participate in the game */
public class FirstPlayerWindow extends JFrame {
    /** the name of the file containing the go button icon */
    protected static final String BUTTONNAME = new String("Avanti.png");






    private MainFrame mainFrame;


    private JPanel mainPanel;


    private static String firstPlayerName;




    /** the actionListener assigned to the button. When the button is pressed, it closes
     * the startDialog, sends the number of players to the server and creates a waitingDialog */
    private class ConfirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            if(firstPlayerName != null) {

                try {
                    Socket serverSocket = mainFrame.getClient().getServerSocket();
                    mainFrame.getController().getNetInterface().sendFirstPlayer(firstPlayerName, serverSocket);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                FirstPlayerWindow.this.dispose();

                mainFrame.waitingDialog.setVisible(true);


                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        try {
                            System.out.println("jhg");
                            mainFrame.getController().matchSetup(mainFrame.getClient().getServerSocket());
                        } catch (IOException | InterruptedException | InvocationTargetException ioException) {
                            ioException.printStackTrace();
                        }
                        return null;
                    }
                };

                worker.execute();

            }
        }

    }

    /** the actionListener assigned to the radioButton. When the button is pressed. It saves the variable
     * associated to the radioButton*/
    private static class RadioButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {   //TODO inserire implementazione mancante
            firstPlayerName = e.getActionCommand();
        }
    }




    /**
     * creates a new playerNumber Dialog
     */
    public FirstPlayerWindow(MainFrame frame) {
        super("Player choice");

        this.mainFrame = frame;


        setResizable(false);
        JLayeredPane pane = this.getLayeredPane();


        ClassLoader cl = this.getClass().getClassLoader();

        Image img = IntroFrame.getImage(cl, StartWindow.IMAGENAME);
        img = getScaledImage(img, 300, 300);
        JLabel imageLabel = new JLabel(new ImageIcon(img));

        add(imageLabel);

        mainPanel = new JPanel();
        mainPanel.setSize(300, 300);
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout(50, 50));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));

        /*
        twoPlayersButton.setMnemonic(KeyEvent.VK_2);
        threePlayersButton.setMnemonic(KeyEvent.VK_3);

        twoPlayersButton.setActionCommand("2");
        threePlayersButton.setActionCommand("3");

        ButtonGroup group = new ButtonGroup();
        group.add(twoPlayersButton);
        group.add(threePlayersButton);


        twoPlayersButton.addActionListener(new RadioButtonListener());
        threePlayersButton.addActionListener(new RadioButtonListener());

         */
/*        JPanel radioPanel = new JPanel(new GridLayout(3,0));
        //JPanel radioPanel = new JPanel(new GridLayout(0,2));
        radioPanel.setBackground(Color.WHITE);
        radioPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //radioPanel.add(twoPlayersButton);
        //radioPanel.add(threePlayersButton);
        mainPanel.add(radioPanel, BorderLayout.CENTER);
*/
        JLabel title = new JLabel(" Scegli il numero di giocatori della partita");
        title.setFont(new Font("Diogenes", Font.BOLD, 13));
        mainPanel.add(title, BorderLayout.PAGE_START);


        img = IntroFrame.getImage(cl, BUTTONNAME);
        img = IntroFrame.getScaledImage(img, 110, 45);


        JButton confirmButton = new JButton();
        confirmButton.setDisabledIcon(new ImageIcon(img));
        confirmButton.setIcon(new ImageIcon(img));
        confirmButton.setContentAreaFilled(false);
        confirmButton.setBorderPainted(false);
        confirmButton.addActionListener(new ConfirmListener());
        mainPanel.add(confirmButton, PAGE_END);



    /*
        JButton confirmButton = new JButton("Go");
        confirmButton.addActionListener(new ConfirmListener());
        mainPanel.add(confirmButton, BorderLayout.PAGE_END);
    */
        pane.add(mainPanel, Integer.valueOf(2));
        pack();
        setLocationRelativeTo(null);


    }

    public void displayNames(ArrayList<String> playerList){

        ButtonGroup group = new ButtonGroup();
        JPanel radioPanel = new JPanel(new GridLayout(3,0));
        radioPanel.setBackground(Color.lightGray);
        radioPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        for(String name : playerList){
            JRadioButton button = new JRadioButton(name);
            button.setActionCommand(name);
            button.addActionListener(new RadioButtonListener());
            group.add(button);
            radioPanel.add(button);
        }

        mainPanel.add(radioPanel, BorderLayout.CENTER);

    }



}
