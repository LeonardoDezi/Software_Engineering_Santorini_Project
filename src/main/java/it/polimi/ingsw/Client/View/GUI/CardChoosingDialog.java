package it.polimi.ingsw.Client.View.GUI;

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
import java.util.ArrayList;

import static it.polimi.ingsw.Client.View.GUI.IntroFrame.getScaledImage;
import static it.polimi.ingsw.Client.View.GUI.StartWindow.IMAGENAME;


/** this class represents the dialog where the Challenger chooses the cards that will be used in the game */
public class CardChoosingDialog extends JDialog {

    /** represents the main panel of the window */
    private final JPanel mainPanel;
    /** represents the panel where the cards are saved */
    private final JPanel cardPanel;

    /** represents the border that indicates that the card has been selected */
    private final static Border REDBORDER = new LineBorder(Color.RED, 5);
    /** represents the basic border of the card */
    private final static Border BASICBORDER = LineBorder.createBlackLineBorder();
    /** the mainFrame where the game will be played */
    private final JFrame frame;


    public static final int MAXCARDNUMBER = 3;   //TODO cambiarlo

    /** the name of the file containing the  button icon */
    protected static final String CONFIRMBUTTONNAME = new String("Conferma.png");
    /** the name of the file containing the  button icon */
    protected static final String UNDOBUTTONNAME = new String("Annulla.png");

    /** the number of cards selected */
    private int cardNumber = 0;
    /** the list of cards selected */
    private ArrayList<String> cardList = new ArrayList<String>();
    /** the list of the buttons inside the window */
    private ArrayList<JButton> buttonList = new ArrayList<JButton>();
    /** the label indicating an error */
    private JLabel errorLabel = new JLabel("Errore: Non hai ancora scelto abbastanza carte.");


    /** creates a new cardChoosingDialog
     * @param frame is the mainFrame
     */
    public CardChoosingDialog(JFrame frame) {

        super(frame, "Player choice");
        this.frame = frame;
        //this.view = view;

        setResizable(false);

        this.setSize(new Dimension(850, 490));


        ClassLoader cl = this.getClass().getClassLoader();

        Image img = IntroFrame.getImage(cl, IMAGENAME);
        img = getScaledImage(img, this.getWidth(), this.getHeight());
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        add(imageLabel);

        JLayeredPane pane = this.getLayeredPane();

        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        mainPanel.setSize(this.getSize());

        JLabel label = new JLabel(("SCEGLI UNA CARTA"));
        label.setFont(new Font("Papyrus", Font.BOLD, 16));
        label.setForeground(Color.BLACK);
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.add(label);
        mainPanel.add(titlePanel);

        cardPanel = new JPanel(new GridLayout(2,7));


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

    /*
        JButton confirmButton = new JButton("Conferma");
        JButton undoButton = new JButton("Annulla");
                                                            //TODO cancellare
        undoButton.addActionListener(new Undo());
        confirmButton.addActionListener(new Confirm());

     */
        displayCards();

        mainPanel.add(cardPanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(errorLabel);
        buttonPanel.setOpaque(false);
        mainPanel.add(buttonPanel);

        mainPanel.setOpaque(false);

        errorLabel.setVisible(false);


        pane.add(mainPanel, Integer.valueOf(2));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }


    /** displays the cards on the window */
    private void displayCards() {

        ClassLoader cl = this.getClass().getClassLoader();
        String[] cardNames = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12","13", "14"};


        ArrayList<JButton> cardDeck = new ArrayList<JButton>();

        for (String item : cardNames) {

            InputStream url = cl.getResourceAsStream(item + ".png");
            BufferedImage img = null;
            try {
                img = ImageIO.read(url);     //TODO togliere il try/catch
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            int W = img.getWidth();
            int H = img.getHeight();


            JButton button = new JButton(new ImageIcon(getScaledImage(img, W / 8, H / 8)));
            button.setBorder(BASICBORDER);
            button.addActionListener(new CardChooser());
            button.setActionCommand(item);

            cardDeck.add(button);

        }

        for (JButton button : cardDeck)
            cardPanel.add(button, ComponentOrientation.LEFT_TO_RIGHT);

    }



    /** the actionListener assigned to the undo button. When the button is pressed, it deletes
     * the last card selected from cardList and updates cardNumber*/
    private class Undo implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            errorLabel.setVisible(false);
            mainPanel.revalidate();
            mainPanel.repaint();

            if(!(buttonList.isEmpty())) {
                JButton button = buttonList.get(buttonList.size() - 1);
                button.setBorder(BASICBORDER);
                buttonList.remove(button);
                cardNumber --;
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

            if (cardNumber < MAXCARDNUMBER) {

                errorLabel.setVisible(true);
                mainPanel.revalidate();
                mainPanel.repaint();

            }else{

                for(JButton button : buttonList){
                    String text = button.getActionCommand();
                    cardList.add(text);
                }
                //TODO invia le carte scelte
                frame.setVisible(true);
                CardChoosingDialog.this.dispose();

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

            if (cardNumber < MAXCARDNUMBER) {
                JButton button = (JButton) e.getSource();
                if(button.getBorder().equals(BASICBORDER)) {
                    button.setBorder(REDBORDER);
                    buttonList.add(button);
                    cardNumber++;
                }
            }
        }
    }


}