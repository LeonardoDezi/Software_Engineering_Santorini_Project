package it.polimi.ingsw.Client.View.GUI;
import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Client.GUIClientController;
import it.polimi.ingsw.Client.Moves;
import it.polimi.ingsw.Client.NetworkHandler.GUINetInterface;
import it.polimi.ingsw.Server.Model.Board;
import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Square;

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
import java.util.HashMap;

import static it.polimi.ingsw.Client.View.GUI.IntroFrame.getScaledImage;
import static it.polimi.ingsw.Server.Controller.GameInitializer.*;
import static it.polimi.ingsw.Server.Model.Player.SEX1;
import static it.polimi.ingsw.Server.Model.Player.SEX2;
import static it.polimi.ingsw.Server.SantoriniApp.*;

/** this class represents the main Frame where the game is played */
public class MainFrame extends JFrame {

    //TODO ancora non ho pensato a come scegliere i worker

    /** represents the list of all the squares of the board*/
    private SquareButton[][]  squareList;

    /** the list of the images representing the blocks of the game */
    private final ArrayList<Image> blocks = new ArrayList<>();

    /** the list of the images representing the pieces of the game */
    private final ArrayList<Image> pieces = new ArrayList<>();


    private String playerCard;

    /** the label representing the player's card */
   // private JLabel playerCard = new JLabel();

    /** represents the border used to indicate that the button can be selected */  //TODO da cancellare
    private final static Border WHITEBORDER = new LineBorder(Color.WHITE, 3);

    /** represents the border used to indicate that the button has been selected */
    private final static Border REDBORDER = new LineBorder(Color.RED, 3);


    /** represents the border used to indicate that the button can be selected */
    private final static Border YELLOWBORDER = new LineBorder(Color.YELLOW, 3);

    private final static Border BASICBORDER = new LineBorder(new Color(0,0,0,0), 3);


    /** the x coordinate of the squareButton selected */
    private int x;

    /** the y coordinate of the squareButton selected */
    private int y;

    /** indicates if a move has been chosen */
    private boolean moveChosen = false;

    public WaitingDialog waitingDialog = new WaitingDialog();

    public CardChoosingDialog cardChoosingDialog = new CardChoosingDialog(this);

    public PlayerNumberWindow playerNumberWindow = new PlayerNumberWindow(this);

    public CardPickingDialog cardPickingDialog = new CardPickingDialog(this);

    public NumberDialog numberDialog = new NumberDialog(this);

    public FirstPlayerWindow firstPlayerWindow = new FirstPlayerWindow(this);

    private int numberOfPlayers;

    private HashMap<String, String> cardMap = new HashMap<>();


    private Client client;

    private GUIClientController controller;

    private ArrayList<String> cardList = new ArrayList<>();

    public boolean setup;

    public boolean stillPlaying;

    public JButton domeButton;

    public JButton skipButton;

    private boolean specialDomeMove = false;


    private JPanel cardPanel;

    private SquareButton builder1Button;
    private SquareButton builder2Button;

    private ArrayList<Square> moves1;
    private ArrayList<Square> moves2;



    public ArrayList<String> getCardList(){return cardList;}

    public void setNumberOfPlayers(int num){ this.numberOfPlayers = num;}

    public int getNumberOfPlayers(){ return numberOfPlayers;}


    /** the actionListener assigned to the square button. When the button is pressed, if the move has not been chosen
     * sets moveChosen to true and gets the coordinates of the SquareButton
     */
    private class SquareListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            SquareButton button = (SquareButton) e.getSource();
            if(button.getBorder().equals(YELLOWBORDER) && !(moveChosen)){
                button.setBorder(REDBORDER);
                x = button.getXvalue();
                y = button.getYvalue();
                moveChosen = true;

                if(button.equals(builder1Button) || button.equals(builder2Button)){

                }

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
                button.setBorder(YELLOWBORDER);
                moveChosen = false;
                x = -1;
                y = -1;
            }
        }
    }

    private class Confirm implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            if(moveChosen) {

                moveChosen = false;
                Square square = new Square(x,y);

                try {
                    getController().getNetInterface().sendSquare(square, getClient().getServerSocket());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }


                for(int i = 0 ; i < Board.BOARDSIZEX; i++) {
                    for (int j = 0; j < Board.BOARDSIZEY; j++) {

                        squareList[i][j].setBorder(BASICBORDER);
                        squareList[i][j].setEnabled(false);
                    }
                }

                revalidate();
                repaint();


                SwingWorker worker =new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        if(setup){
                            getController().getNetInterface().getMatchSetup(getClient().getServerSocket(), getController());
                        }else
                            getController().getNetInterface().getMoves(getClient().getServerSocket());
                        return null;
                    }
                };

                worker.execute();



            }

        }
    }




    public void map(){
        cardMap.put("Apollo", "1");
        cardMap.put("Artemis", "2");
        cardMap.put("Athena", "3");
        cardMap.put("Atlas", "4");
        cardMap.put("Demeter", "5");
        cardMap.put("Hephaestus", "6");
        cardMap.put("Minotaur", "7");
        cardMap.put("Pan", "8");
        cardMap.put("Prometheus", "9");
        cardMap.put("Charon", "10");
        cardMap.put("Chronus", "11");
        cardMap.put("Hestia", "12");
        cardMap.put("Selene", "13");
        cardMap.put("Zeus", "14");
    }


    public Client getClient(){return client;}
    public void setClient(Client client){this.client = client;}
    public void setController(GUIClientController controller){ this.controller = controller;}
    public GUIClientController getController(){ return  this.controller;}

    public String translate(String name){ return cardMap.get(name);}

    public void setPlayerCard(String name){ this.playerCard = name;}

    /** creates a new MainFrame */
    public MainFrame(Client client) {

        super("SANTORINI");


        this.setup = true;

        this.stillPlaying = true;

        this.client = client;
        JLayeredPane pane = getLayeredPane();

        setResizable(false);

        map();

        createBlocks();
        createPieces();

        BoardPanel panel = new BoardPanel();
        panel.setLayout(new BorderLayout());

        add(panel, BorderLayout.WEST);


        JPanel glass = new JPanel(new GridLayout(5,5));

       //glass.addMouseListener(ma);
       //glass.addMouseMotionListener(ma);

        squareList = new SquareButton[Board.BOARDSIZEX][Board.BOARDSIZEY];

        for(int i = 0 ; i < Board.BOARDSIZEX; i++) {
            for (int j = 0; j < Board.BOARDSIZEY; j++) {
                squareList[i][j] = new SquareButton(i,j);
                squareList[i][j].setContentAreaFilled(false);
                squareList[i][j].setBorderPainted(true);    //TODO rivedere
                squareList[i][j].setEnabled(true);
                squareList[i][j].addActionListener(new SquareListener());

                glass.add(squareList[i][j]);
            }
        }

        glass.setOpaque(false);
        glass.setSize(500, 500);
        glass.setLocation(91, 95);


        pane.add(glass, Integer.valueOf(2));

        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        add(controlPanel, BorderLayout.SOUTH);
        //pack();



        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(new Undo());
        controlPanel.add(undoButton, BorderLayout.SOUTH);

        JButton confirmButton = new JButton(("Confirm"));
        confirmButton.addActionListener(new Confirm());
        controlPanel.add(confirmButton, BorderLayout.SOUTH);


        domeButton = new JButton("DOME");
        domeButton.setVisible(false);
        controlPanel.add(domeButton, BorderLayout.SOUTH);


        skipButton = new JButton("SKIP");
        domeButton.setVisible(false);
        controlPanel.add(domeButton, BorderLayout.SOUTH);

        cardPanel = new JPanel(new BorderLayout());
        cardPanel.setSize(180, 500);
        add(cardPanel, BorderLayout.EAST);



/*
        displayCard();
        JPanel piecePanel = new JPanel();
        piecePanel.add(playerCard);
        add(piecePanel, BorderLayout.EAST);
*/
        pack();
        setLocationRelativeTo(null);


       // prova1();
       // tryMethod();

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
        String cardNumber = translate(playerCard);
        BufferedImage img;
        URL url = cl.getResource(cardNumber + ".png");

        try {
            img = ImageIO.read(url);     //TODO togliere il try/catch
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int W = img.getWidth();
        int H = img.getHeight();

        JLabel label = new JLabel(new ImageIcon(getScaledImage(img, W / 8, H / 8)));
        cardPanel.add(label, BorderLayout.CENTER);
        pack();

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


    public void placeBuilder(ArrayList<Square> freeSquares){

        for(Square s : freeSquares){
            int x = s.x;
            int y = s.y;
            squareList[x][y].setBorder(YELLOWBORDER);
            squareList[x][y].setEnabled(true);
        }

        /*
        JTextArea textField = new JTextArea();
        textField.setText("FATAL ERROR: impossibile proseguire la partita");
        textField.setOpaque(false);
        textField.setEditable(false);
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
        textField.setFont(new Font("Diogenes", Font.BOLD, 14));
        textField.setText("FATAL ERROR: impossibile proseguire la partita");
        add(textField, BorderLayout.EAST);


         */
        revalidate();
        repaint();

    }


    public void updateBoard(Square square){


        SwingWorker worker =new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                if(setup){
                    getController().getNetInterface().getMatchSetup(getClient().getServerSocket(), getController());
                }else
                    if(stillPlaying)    //TODO else?
                        getController().play();
                return null;
            }
        };

        worker.execute();

        int a = square.x;
        int b = square.y;
        squareList[a][b].setSquare(square);
        repaintSquare(squareList[a][b]);

    }

    public void updateBoard(Square firstSquare, Square secondSquare){

        int a = firstSquare.x;
        int b = firstSquare.y;
        squareList[a][b].setSquare(firstSquare);
        repaintSquare(squareList[a][b]);

        a = secondSquare.x;
        b = secondSquare.y;
        squareList[a][b].setSquare(firstSquare);
        repaintSquare(squareList[a][b]);


        SwingWorker worker =new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                if(setup){
                    getController().getNetInterface().getMatchSetup(getClient().getServerSocket(), getController());
                }else
                    getController().getNetInterface().getMoves(getClient().getServerSocket());
                return null;
            }
        };

        worker.execute();

    }



    public void repaintSquare(SquareButton button){

        JLabel label;

        int level = button.getSquare().getLevel();

        if(level != 4) {
            if (level == 0) {
                button.setIcon(null);
            } else
                button.setIcon(new ImageIcon(blocks.get(level)));
        }

        int value = button.getSquare().getValue();

        if(value == 0){   // unoccupied block

            if(button.getTopLabel() != null) {
                button.remove(button.getTopLabel());
                button.setTopLabel(null);
            }

        }else if(value == 2){   //dome
            label = new JLabel(new ImageIcon(blocks.get(0)));
            button.setTopLabel(label);
            button.add(label);
            label.setAlignmentX((float) 0.5);

        }else{   //value == 1 : there's a worker

            Builder builder = button.getSquare().getBuilder();
            label = getPiece(builder.getColour(), builder.getSex());

            button.setTopLabel(label);
            button.add(label);   //TODO sostituirà con quello precedente?
            label.setAlignmentX((float) 0.5);

            revalidate();
            repaint();


        }


    }

    public JLabel getPiece(String colour, String sex){

        if(colour.equals(COLOUR1)) {
            if (sex.equals(SEX1))
                return new JLabel(new ImageIcon(pieces.get(0)));
            else
                return new JLabel(new ImageIcon(pieces.get(1)));
        }else if(colour.equals(COLOUR2)){
            if(sex.equals(SEX1))
                return new JLabel(new ImageIcon(pieces.get(2)));
            else
                return new JLabel(new ImageIcon(pieces.get(3)));

        }else if(colour.equals(COLOUR3)){
            if(sex.equals(SEX1))
                return new JLabel(new ImageIcon(pieces.get(4)));
            else
                return new JLabel(new ImageIcon(pieces.get(5)));
        }

        return null;
    }


    public void setMoves(Moves moves){

        if(moves.getFemale()) {                    //if the chosenCard is selene
            specialDomeMove = true;
        }

        Builder builder1 = moves.getBuilder1();
        if(builder1 != null){
            Square square = builder1.getPosition();
            int a = square.x;
            int b = square.y;
            squareList[a][b] = builder1Button;
        }else
            builder1Button = null;

        Builder builder2 = moves.getBuilder1();
        if(builder2 != null){
            Square square = builder2.getPosition();
            int a = square.x;
            int b = square.y;
            squareList[a][b] = builder2Button;
        }else
            builder2Button = null;


        moves1 = moves.getMoves1();
        moves2 = moves.getMoves2();

        if(builder1Button != null)
            builder1Button.setBorder(YELLOWBORDER);

        if(builder2Button != null)
            builder2Button.setBorder(YELLOWBORDER);

        revalidate();
        repaint();

    }


}