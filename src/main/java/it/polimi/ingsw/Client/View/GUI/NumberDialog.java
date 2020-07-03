package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.GUIClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static java.awt.BorderLayout.PAGE_END;



public class NumberDialog extends JFrame {

    /** the name of the file containing the background image */
    protected static final String IMAGENAME = new String("startDialog.png");
    /** the name of the file containing the start button icon */
    protected static final String BUTTONNAME = new String("Ok.png");


    private MainFrame frame;

    private int numberOfPlayers;

    private GUIClientController controller;

    private JTextArea textField;



    /** the actionListener assigned to the start button. When the button is pressed, it closes
     * the startDialog, sends the username to the server and creates a waitingDialog */
    private class ConfirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            NumberDialog.this.dispose();

            SwingWorker worker = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        controller.matchSetup(frame.getClient().getServerSocket());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    } catch (InvocationTargetException invocationTargetException) {
                        invocationTargetException.printStackTrace();
                    }
                    return null;
                }
            };

            worker.execute();


        }
    }


    public NumberDialog(MainFrame frame){

        this.frame = frame;


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


        ClassLoader cl = this.getClass().getClassLoader();


        Image img = IntroFrame.getImage(cl, IMAGENAME);
        img = IntroFrame.getScaledImage(img, 300, 300);
        JLabel imageLabel = new JLabel(new ImageIcon(img));


        add(imageLabel);

        JLayeredPane pane = this.getLayeredPane();

        JPanel mainPanel = new JPanel();
        mainPanel.setSize(300,300);
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout(50, 50));  //50
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 20, 40, 20));


            img = IntroFrame.getImage(cl, BUTTONNAME);
            img = IntroFrame.getScaledImage(img, 110, 45);

            JButton confirmButton = new JButton();
            confirmButton.setDisabledIcon(new ImageIcon(img));
            confirmButton.setIcon(new ImageIcon(img));
            confirmButton.setContentAreaFilled(false);
            confirmButton.setBorderPainted(false);
            confirmButton.addActionListener(new ConfirmListener());
            mainPanel.add(confirmButton, PAGE_END);


            textField = new JTextArea();
            textField.setOpaque(false);
            textField.setEditable(false);
            textField.setLineWrap(true);
            textField.setWrapStyleWord(true);
            textField.setFont(new Font("Diogenes", Font.BOLD, 14));
            mainPanel.add(textField, BorderLayout.CENTER);

            pane.add(mainPanel, Integer.valueOf(2));
            pack();
            setLocationRelativeTo(null);




        }




    public void setController(GUIClientController controller){
        this.controller = controller;
        textField.setText("The number of players is " + frame.getNumberOfPlayers());
    }



}
