package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.BorderLayout.*;


/** this class represents the window where the user can enter its username */
public class StartWindow extends JFrame {
    /** the textArea where the user will write its username */
    private final JTextArea usernameTextField;
    /** the textArea where the user will write the ip address */
    private final JTextArea ipTextField;

    /** the text representing the user's username */
    private String username;

    /** the text representing the ip address */
    private String ipAddress;

    /** the frame where the game will be played*/
    private MainFrame mainFrame;


    /** the name of the file containing the background image */
    protected static final String IMAGENAME = new String("startDialog.png");
    /** the name of the file containing the start button icon */
    protected static final String BUTTONNAME = new String("Start.png");



    /** the actionListener assigned to the start button. When the button is pressed, it closes
     * the startDialog and sends the username and the ip address to the server and sets visible the waiting dialog */
    private class ConfirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event){

            username = usernameTextField.getText();
            ipAddress = ipTextField.getText();

            if(!(username.isEmpty()) && !(ipAddress.isEmpty())) {

                StartWindow.this.dispose();

                mainFrame.setClient(new Client(ipAddress, 8080));
                mainFrame.getClient().startClient(username, mainFrame);
                mainFrame.waitingDialog.setVisible(true);

            }
        }
    }



    /**
     * creates a new StartDialog
     * */
    public StartWindow(MainFrame frame) {
        super("Player choice");


        this.mainFrame = frame;

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
        mainPanel.setLayout(new BorderLayout(50, 50));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JTextArea textField = new JTextArea();
        textField.setOpaque(false);
        textField.setEditable(false);
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
        textField.setFont(new Font("Diogenes", Font.BOLD, 14));
        textField.setText("Welcome! Please enter your username and the IP address");
        mainPanel.add(textField, PAGE_START);


        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout(20, 20));
        textPanel.setOpaque(false);

        usernameTextField = new JTextArea();
        usernameTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textPanel.add(usernameTextField , BorderLayout.PAGE_START);

        ipTextField = new JTextArea();
        ipTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textPanel.add(ipTextField, PAGE_END);


        mainPanel.add(textPanel, CENTER);




        img = IntroFrame.getImage(cl, BUTTONNAME);
        img = IntroFrame.getScaledImage(img, 110, 45);

        JButton confirmButton = new JButton();
        confirmButton.setDisabledIcon(new ImageIcon(img));
        confirmButton.setIcon(new ImageIcon(img));
        confirmButton.setContentAreaFilled(false);
        confirmButton.setBorderPainted(false);
        confirmButton.addActionListener(new ConfirmListener());
        mainPanel.add(confirmButton, SOUTH);


        pane.add(mainPanel, Integer.valueOf(2));
        pack();
        setLocationRelativeTo(null);

        setVisible(true);

    }


}


