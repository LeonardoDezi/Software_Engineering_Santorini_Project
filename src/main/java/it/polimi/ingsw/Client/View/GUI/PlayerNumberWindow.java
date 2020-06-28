package it.polimi.ingsw.Client.View.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static it.polimi.ingsw.Client.View.GUI.IntroFrame.getScaledImage;
import static java.awt.BorderLayout.PAGE_END;

/** this class represents the dialog where the Challenger chooses how many players will participate in the game */
public class PlayerNumberWindow extends JFrame {
    /** the name of the file containing the go button icon */
    protected static final String BUTTONNAME = new String("Avanti.png");
    /** the radioButton representing the "2" option */
    JRadioButton twoPlayersButton = new JRadioButton("2");
    /** the radioButton representing the "3" option */
    JRadioButton threePlayersButton = new JRadioButton("3");


    private MainFrame mainFrame;


    private static boolean blocked = true;

    private static String numberOfPlayers;

    public static boolean getBlocked(){return blocked;};

    public static String getNumberOfPlayers(){return numberOfPlayers;}


    /** the actionListener assigned to the button. When the button is pressed, it closes
     * the startDialog, sends the number of players to the server and creates a waitingDialog */
    private class ConfirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(numberOfPlayers != null) {
                blocked = false;
                PlayerNumberWindow.this.dispose();   //TODO inserire l'implemetazione mancante
                //  new WaitingDialog(frame);
                //new CardChoosingDialog(frame);
            }
        }
    }

    /** the actionListener assigned to the radioButton. When the button is pressed. It saves the variable
     * associated to the radioButton*/
    private static class RadioButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {   //TODO inserire implementazione mancante
            numberOfPlayers = e.getActionCommand();
        }
    }



    /**
     * creates a new playerNumber Dialog
     */
    public PlayerNumberWindow(MainFrame frame) {
        super("Player choice");

        this.mainFrame = frame;

        setResizable(false);
        JLayeredPane pane = this.getLayeredPane();


        ClassLoader cl = this.getClass().getClassLoader();

        Image img = IntroFrame.getImage(cl, StartWindow.IMAGENAME);
        img = getScaledImage(img, 300, 300);
        JLabel imageLabel = new JLabel(new ImageIcon(img));

        add(imageLabel);

        JPanel mainPanel = new JPanel();
        mainPanel.setSize(300, 300);
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout(50, 50));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 10, 30, 10));

        twoPlayersButton.setMnemonic(KeyEvent.VK_2);
        threePlayersButton.setMnemonic(KeyEvent.VK_3);

        twoPlayersButton.setActionCommand("2");
        threePlayersButton.setActionCommand("3");

        ButtonGroup group = new ButtonGroup();
        group.add(twoPlayersButton);
        group.add(threePlayersButton);


        twoPlayersButton.addActionListener(new RadioButtonListener());
        threePlayersButton.addActionListener(new RadioButtonListener());

        JPanel radioPanel = new JPanel(new GridLayout(0,1));
        radioPanel.setBackground(Color.WHITE);
        radioPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        radioPanel.add(twoPlayersButton);
        radioPanel.add(threePlayersButton);
        mainPanel.add(radioPanel, BorderLayout.CENTER);

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
        setVisible(true);


    }

    public static void main(String[] args) {
        new PlayerNumberWindow(null);
    }

}
