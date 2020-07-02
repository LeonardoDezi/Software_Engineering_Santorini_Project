package it.polimi.ingsw.Client.View.GUI;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Client.GUIClientController;
import it.polimi.ingsw.Client.NetworkHandler.GUINetInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import static it.polimi.ingsw.Client.View.GUI.IntroFrame.getScaledImage;
import static it.polimi.ingsw.Client.View.GUI.StartWindow.IMAGENAME;


/** this class represents the dialog where the Challenger chooses the cards that will be used in the game */
public class CardPickingDialog extends JDialog {

    /** represents the main panel of the window */
    private final JPanel mainPanel;
    /** represents the panel where the cards are saved */
    private final JPanel cardPanel;

    /** represents the border that indicates that the card has been selected */
    private final static Border REDBORDER = new LineBorder(Color.RED, 5);
    /** represents the basic border of the card */
    private final static Border BASICBORDER = LineBorder.createBlackLineBorder();
    /** the mainFrame where the game will be played */
    private final MainFrame frame;

    /** the name of the file containing the  button icon */
    protected static final String CONFIRMBUTTONNAME = new String("Conferma.png");
    /** the name of the file containing the  button icon */
    protected static final String UNDOBUTTONNAME = new String("Annulla.png");


    /** the button of the card chosen by the player */
    private JButton chosenButton = null;
    /** the label indicating an error */
    private JLabel errorLabel = new JLabel("Error: You haven't picked enough cards yet.");




    /** creates a new cardChoosingDialog
     * @param frame is the mainFrame
     */
    public CardPickingDialog(MainFrame frame) {

        super(frame, "Player choice");
        this.frame = frame;


        setResizable(false);

        this.setSize(new Dimension(760, 420));


        ClassLoader cl = this.getClass().getClassLoader();

        Image img = IntroFrame.getImage(cl, IMAGENAME);
        img = getScaledImage(img, this.getWidth(), this.getHeight());
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        add(imageLabel);

        JLayeredPane pane = this.getLayeredPane();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(50, 50));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        mainPanel.setSize(this.getSize());

        JLabel label = new JLabel(("CHOOSE A CARD"));
        label.setFont(new Font("Papyrus", Font.BOLD, 16));
        label.setForeground(Color.BLACK);
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.add(label);
        mainPanel.add(titlePanel, BorderLayout.PAGE_START);


        cardPanel = new JPanel();
        cardPanel.setOpaque(false);


        img = IntroFrame.getImage(cl, CONFIRMBUTTONNAME);
        img = IntroFrame.getScaledImage(img, 110, 45);

        JButton confirmButton = new JButton();
        confirmButton.setDisabledIcon(new ImageIcon(img));
        confirmButton.setIcon(new ImageIcon(img));
        confirmButton.setContentAreaFilled(false);
        confirmButton.setBorderPainted(false);
        confirmButton.addActionListener(new Confirm());

        img = IntroFrame.getImage(cl, UNDOBUTTONNAME);
        img = IntroFrame.getScaledImage(img, 110, 45);

        JButton undoButton = new JButton();
        undoButton.setDisabledIcon(new ImageIcon(img));
        undoButton.setIcon(new ImageIcon(img));
        undoButton.setContentAreaFilled(false);
        undoButton.setBorderPainted(false);
        undoButton.addActionListener(new Undo());


        mainPanel.add(cardPanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(errorLabel);
        buttonPanel.setOpaque(false);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);

        mainPanel.setOpaque(false);

        errorLabel.setVisible(false);


        pane.add(mainPanel, Integer.valueOf(2));
        pack();
        setLocationRelativeTo(null);

    }


    /** displays the cards on the window */
    public void displayCards() {

        ClassLoader cl = this.getClass().getClassLoader();
        ArrayList<JButton> cardDeck = new ArrayList<JButton>();

        for(int i=0; i < frame.getCardList().size(); i++) {

            String cardName = frame.getCardList().get(i);
            String num = frame.translate(cardName) + ".png";
            BufferedImage img = IntroFrame.getImage(cl, num);

            int W = img.getWidth();
            int H = img.getHeight();

            JButton button = new JButton(new ImageIcon(getScaledImage(img, W / 8, H / 8)));
            button.setBorder(BASICBORDER);
            button.setContentAreaFilled(false);
            button.addActionListener(new CardChooser());
            button.setActionCommand(Integer.toString(i + 1));

            cardDeck.add(button);
        }

        for (JButton b : cardDeck)
            cardPanel.add(b, ComponentOrientation.LEFT_TO_RIGHT);

    }



    /** the actionListener assigned to the undo button. When the button is pressed, it deletes
     * the last card selected from cardList and updates cardNumber*/
    private class Undo implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            errorLabel.setVisible(false);
            mainPanel.revalidate();
            mainPanel.repaint();

            if(chosenButton != null) {
                chosenButton.setBorder(BASICBORDER);
                chosenButton = null;
            }
        }
    }

    /** the actionListener assigned to the confirm button. When the button is pressed, it sends cardList to
     * the server, unless the user has not selected enough cards yet
     */
    private class Confirm implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

            errorLabel.setVisible(false);
            mainPanel.revalidate();
            mainPanel.repaint();

            if (chosenButton == null) {

                errorLabel.setVisible(true);
                mainPanel.revalidate();
                mainPanel.repaint();

            }else{

                String text = chosenButton.getActionCommand();
                int num = Integer.parseInt(text);


                try {
                    frame.getController().getNetInterface().sendCard(num, frame.getClient().getServerSocket());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                frame.setPlayerCard( frame.getCardList().get(num - 1));


                CardPickingDialog.this.dispose();
                frame.waitingDialog.setVisible(true);

                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        try {
                            frame.getClient().getController().matchSetup(frame.getClient().getServerSocket());
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


    /** the actionListener assigned to the card buttons. When the button is pressed, it adds the selected card to cardList
     *  unless the user has already selected enough cards.
     */
    private class CardChooser implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            errorLabel.setVisible(false);
            mainPanel.revalidate();
            mainPanel.repaint();

            if (chosenButton == null) {
                JButton button = (JButton) e.getSource();
                if(button.getBorder().equals(BASICBORDER)) {
                    button.setBorder(REDBORDER);
                    chosenButton = button;
                }
            }

        }
    }



}
