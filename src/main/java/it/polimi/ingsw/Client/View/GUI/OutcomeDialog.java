package it.polimi.ingsw.Client.View.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import static java.awt.BorderLayout.PAGE_END;
import static java.awt.BorderLayout.SOUTH;

public class OutcomeDialog extends JDialog {

    /**
     * the name of the file containing the background image
     */
    protected static final String IMAGENAME = new String("startDialog.png");
    /**
     * the name of the file containing the yes icon
     */
    protected static final String YES = new String("Yes.png");
    /**
     * the name of the file containing the yes icon
     */
    protected static final String NO = new String("No.png");
    /**
     * the frame where the game has been played
     */
    private MainFrame frame;

    public JTextArea messageArea;


    /**
     * the actionListener assigned to the start button. When the button is pressed, it closes
     * the startDialog, sends the username to the server and creates a waitingDialog
     */
    private class Yes implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            OutcomeDialog.this.dispose();
            MainFrame frame = null;
            try {
                frame = new MainFrame();
            } catch (IOException ioException) {
                System.out.println("ERROR");
                System.exit(-1);
            }
            new StartWindow(frame);
        }
    }


    /**
     * the actionListener assigned to the start button. When the button is pressed, it closes
     * the startDialog, sends the username to the server and creates a waitingDialog
     */
    private class No implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            OutcomeDialog.this.dispose();
            System.exit(0);
        }
    }


    /**
     * creates a new StartDialog
     *
     * @param frame is the mainFrame on which the game will be played
     */
    public OutcomeDialog(MainFrame frame) {

        super(frame);
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
        img = IntroFrame.getScaledImage(img, 400, 300);
        JLabel imageLabel = new JLabel(new ImageIcon(img));


        add(imageLabel);

        JLayeredPane pane = this.getLayeredPane();

        JPanel mainPanel = new JPanel();
        mainPanel.setSize(400, 300);
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout(50, 50));  //50
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 50, 40, 20));


        messageArea = new JTextArea();
        messageArea.setOpaque(false);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Diogenes", Font.BOLD, 14));
        mainPanel.add(messageArea, BorderLayout.CENTER);


        img = IntroFrame.getImage(cl, YES);
        img = IntroFrame.getScaledImage(img, 110, 45);

        JButton yesButton = new JButton();
        yesButton.setDisabledIcon(new ImageIcon(img));
        yesButton.setIcon(new ImageIcon(img));
        yesButton.setContentAreaFilled(false);
        yesButton.setBorderPainted(false);
        yesButton.addActionListener(new Yes());


        img = IntroFrame.getImage(cl, NO);
        img = IntroFrame.getScaledImage(img, 110, 45);

        JButton noButton = new JButton();
        noButton.setDisabledIcon(new ImageIcon(img));
        noButton.setIcon(new ImageIcon(img));
        noButton.setContentAreaFilled(false);
        noButton.setBorderPainted(false);
        noButton.addActionListener(new No());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        buttonPanel.setOpaque(false);
        mainPanel.add(buttonPanel, PAGE_END);


        pane.add(mainPanel, Integer.valueOf(2));
        pack();
        setLocationRelativeTo(null);

        setVisible(false);


    }
}






