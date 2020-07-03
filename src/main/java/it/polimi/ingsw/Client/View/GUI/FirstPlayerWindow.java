package it.polimi.ingsw.Client.View.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    /** the mainFrame where the game will be played */
    private MainFrame mainFrame;

    /** represents the main panel of the window */
    private JPanel mainPanel;

    /** represents the player chosen as first player */
    private static String firstPlayerName;



    /** the actionListener assigned to the button. When the button is pressed, it closes
     * the window, sends firstPlayerName to the server and sets visible a waitingDialog */
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
        public void actionPerformed(ActionEvent e) {
            firstPlayerName = e.getActionCommand();
        }
    }




    /**
     * creates a new FirstPlayerWindow
     */
    public FirstPlayerWindow(MainFrame frame) {
        super("Player choice");

        this.mainFrame = frame;



        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    IntroFrame.exit(frame);
                } catch (IOException ioException) {
                    System.exit(1);
                }
            }
        };
        this.addWindowListener(exitListener);


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

        JTextArea title = new JTextArea();
        title.setOpaque(false);
        title.setEditable(false);
        title.setLineWrap(true);
        title.setWrapStyleWord(true);
        title.setFont(new Font("Diogenes", Font.BOLD, 14));
        title.setText("Choose the first player");
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


        pane.add(mainPanel, Integer.valueOf(2));
        pack();
        setLocationRelativeTo(null);


    }

    /** displays the names of the players connected to the server for this game */
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
