package it.polimi.ingsw.Client.View.GUI;
import it.polimi.ingsw.Client.Client;

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
import java.net.URL;
import java.util.ArrayList;

import static it.polimi.ingsw.Client.View.GUI.IntroFrame.getScaledImage;

/** this class represents the main Frame where the game is played */
public class MainFrame extends JFrame {

    //TODO ancora non ho pensato a come scegliere i worker

    /** represents the list of all the squares of the board*/
    private SquareButton[][]  squareList;
    /** the list of the images representing the blocks of the game */
    private final ArrayList<Image> blocks = new ArrayList<>();
    /** the list of the images representing the pieces of the game */
    private final ArrayList<Image> pieces = new ArrayList<>();
    /** the label representing the player's card */
    private JLabel playerCard = new JLabel();
    /** represents the border used to indicate that the button can be selected */
    private final static Border WHITEBORDER = new LineBorder(Color.WHITE, 3);
    /** represents the border used to indicate that the button has been selected */
    private final static Border REDBORDER = new LineBorder(Color.RED, 3);
    /** the x coordinate of the squareButton selected */
    private int x;
    /** the y coordinate of the squareButton selected */
    private int y;
    /** indicates if a move has been chosen */
    private boolean moveChosen = false;


    private Client client;

    /** the actionListener assigned to the square button. When the button is pressed, if the move has not been chosen
     * sets moveChosen to true and gets the coordinates of the SquareButton
     */
    private class SquareListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            SquareButton button = (SquareButton) e.getSource();
            if(button.getBorder().equals(WHITEBORDER) && !(moveChosen)){
                button.setBorder(REDBORDER);
                x = button.getXvalue();
                y = button.getYvalue();
                moveChosen = true;
            }
        }
    }

    /** the actionListener assigned to the undo button. When the button is pressed, if the move has been chosen
     * it resets the selected square button
     */
    private class Undo implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(moveChosen) {
                JButton button = squareList[x][y];
                button.setBorder(WHITEBORDER);
                moveChosen = false;
                x = -1;
                y = -1;
            }
        }
    }


    public Client getClient(){return client;}
    public void setClient(Client client){this.client = client;}

    /** creates a new MainFrame */
    public MainFrame(Client client) {

        super("SANTORINI");

        this.client = client;
        JLayeredPane pane = getLayeredPane();

        setResizable(false);


        createBlocks();
        createPieces();

        BoardPanel panel = new BoardPanel();
        panel.setLayout(new BorderLayout());

        add(panel, BorderLayout.WEST);


        //TODO controllare che non ci siano problemi di dimensioni e coordinate
        JPanel glass = new JPanel(new GridLayout(5,5));

       //glass.addMouseListener(ma);
       //glass.addMouseMotionListener(ma);

        squareList = new SquareButton[5][5];   //TODO cambiare i 5 con boardsize

        for(int j = 4 ; j >= 0; j--) {
            for (int i = 0; i < 5; i++) {
                squareList[i][j] = new SquareButton(i,j);
                squareList[i][j].setContentAreaFilled(false);   //Do not call setOpaque(false)
                squareList[i][j].setBorderPainted(true);    //poi lo cambio
                squareList[i][j].setEnabled(true);
                squareList[i][j].addActionListener(new SquareListener());
                //squareList[i][j].setTransferHandler(new TransferHandler("icon"));  //TODO

                glass.add(squareList[i][j]);
            }
        }

        glass.setOpaque(false); // Set to true to see it
        glass.setBackground(Color.GREEN);
        glass.setSize(500, 500);
        glass.setLocation(91, 95);


        pane.add(glass, Integer.valueOf(2));

        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        add(controlPanel, BorderLayout.SOUTH);
        //pack();



        JButton undoButton = new JButton("Annulla");
        undoButton.addActionListener(new Undo());
        controlPanel.add(undoButton, BorderLayout.SOUTH);

        JButton confirmButton = new JButton(("Conferma"));
        undoButton.setSize(678, 30);
        controlPanel.add(confirmButton, BorderLayout.SOUTH);

        JButton domeButton = new JButton("CUPOLA");
        domeButton.setSize(678, 30);
        controlPanel.add(domeButton, BorderLayout.SOUTH);

        displayCard();
        JPanel piecePanel = new JPanel();
        piecePanel.add(playerCard);
        add(piecePanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);


        prova1();
        tryMethod();

        //tryMethod2();

    }


    /** obtains the icons representing the blocks and saves them inside attribute blocks */
    public void createBlocks() {
        Image image;

        ClassLoader cl = this.getClass().getClassLoader();


        String[] blockName = new String[]{"Dome", "A", "B", "C"};

        for (String item : blockName) {

            InputStream url = cl.getResourceAsStream(item + ".png");
            BufferedImage img = null;
            try {
                img = ImageIO.read(url);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if(item.equals("Dome"))
                image = getScaledImage(img, 65, 65);  //85
            else
                image = getScaledImage(img, 100, 100);

            blocks.add(image);

        }

    }


    public void prova1(){
        //squareList[0][1].setBorder(WHITEBORDER);
        squareList[1][2].setBorder(WHITEBORDER);
        squareList[2][2].setBorder(WHITEBORDER);
        squareList[3][2].setBorder(WHITEBORDER);
    }

    /** obtains the icons representing the pieces and saves them inside attribute pieces */
    public void createPieces() {
        Image image;
        ClassLoader cl = this.getClass().getClassLoader();


        String[] pieceName = new String[]{"Red_M", "Red_F", "Blue_M", "Blue_F", "White_M", "White_F"};

        for (String item : pieceName) {

            InputStream url = cl.getResourceAsStream(item + ".png");
            BufferedImage img = null;
            try {
                img = ImageIO.read(url);     //TODO togliere il try/catch
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            image = getScaledImage(img, 70, 70);  //85

            pieces.add(image);

        }
    }


    /** display the card chosen by the player */
    public void displayCard(){

        ClassLoader cl = this.getClass().getClassLoader();
        String cardName = "01";
        BufferedImage img;
        URL url = cl.getResource(cardName + ".png");

        try {
            img = ImageIO.read(url);     //TODO togliere il try/catch
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int W = img.getWidth();
        int H = img.getHeight();

        playerCard = new JLabel(new ImageIcon(getScaledImage(img, W / 8, H / 8)));

    }


    //metodo di prova
    public void tryMethod(){



        squareList[1][2].setIcon((new ImageIcon(blocks.get(1))));
        squareList[1][2].setDisabledIcon((new ImageIcon(blocks.get(1))));

        squareList[2][2].setIcon((new ImageIcon(blocks.get(2))));
        squareList[2][2].setDisabledIcon((new ImageIcon(blocks.get(2))));

        squareList[3][2].setIcon((new ImageIcon(blocks.get(3))));
        squareList[3][2].setDisabledIcon((new ImageIcon(blocks.get(3))));

        JLabel label;
        int num = 0;

        for(int i=0 ; i<5; i++){
            label = new JLabel(new ImageIcon(pieces.get(i)));
            //label.addMouseListener(listener);
           // label.setTransferHandler(new TransferHandler("icon"));  //QUI
            squareList[i][2].add(label);
            label.setAlignmentX((float) 0.5);
           // pack();
        }

        label = new JLabel(new ImageIcon(pieces.get(5)));
       // label.addMouseListener(listener);
        //label.setTransferHandler(new TransferHandler("icon"));  //QUI
        squareList[0][3].add(label);
        label.setAlignmentX((float) 0.5);

        squareList[0][1].setIcon(new ImageIcon(blocks.get(3)));
        squareList[0][1].setDisabledIcon(new ImageIcon(blocks.get(3)));
        label = new JLabel(new ImageIcon(blocks.get(0)));
        squareList[0][1].add(label);
        label.setAlignmentX((float) 0.5);



    }

  /*  public void tryMethod2(){

        JLabel label = new JLabel(new ImageIcon(pieces.get(0)));
        label.addMouseListener(listener);
        label.setTransferHandler(new TransferHandler("icon"));  //QUI
        piecePanel.add(label, BorderLayout.CENTER);
        pack();

        label = new JLabel(new ImageIcon(pieces.get(1)));
        label.addMouseListener(listener);
        label.setTransferHandler(new TransferHandler("icon"));  //QUI
        piecePanel.add(label, BorderLayout.CENTER);
        pack();
    }

*/


}