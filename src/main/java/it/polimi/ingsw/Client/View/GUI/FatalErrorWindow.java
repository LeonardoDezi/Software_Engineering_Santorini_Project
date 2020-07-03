package it.polimi.ingsw.Client.View.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import static java.awt.BorderLayout.PAGE_END;

public class FatalErrorWindow extends JFrame {


    /** the name of the file containing the background image */
    protected static final String IMAGENAME = new String("startDialog.png");
    /** the name of the file containing the start button icon */
    protected static final String BUTTONNAME = new String("Ok.png");
    /** the frame where the game will be played*/
    private MainFrame frame;



    /** the actionListener assigned to the start button. When the button is pressed, it closes
     * the startDialog, sends the username to the server and creates a waitingDialog */
    private class ConfirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            FatalErrorWindow.this.dispose();
            new StartWindow(frame);
        }
    }





    public FatalErrorWindow(MainFrame frame) {
        super();

        this.frame = frame;

        frame.waitingDialog.setVisible(false);

        setResizable(false);


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


        JTextArea textField = new JTextArea();
        textField.setOpaque(false);
        textField.setEditable(false);
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
        textField.setFont(new Font("Diogenes", Font.BOLD, 14));
        textField.setText("FATAL ERROR: Unable to connect to the server");
        mainPanel.add(textField, BorderLayout.CENTER);

        pane.add(mainPanel, Integer.valueOf(2));
        pack();
        setLocationRelativeTo(null);




    }


}






