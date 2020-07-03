package it.polimi.ingsw.Client.View.GUI;
import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Client.GUIClientController;
import it.polimi.ingsw.Client.Moves;
import it.polimi.ingsw.Client.NetworkHandler.GUINetInterface;
import it.polimi.ingsw.Server.Model.Board;
import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Envelope;
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
import java.lang.reflect.InvocationTargetException;
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


    /** represents the list of all the squares of the board*/
    private SquareButton[][]  squareList;

    /** the list of the images representing the blocks of the game */
    private final ArrayList<Image> blocks = new ArrayList<>();

    /** the list of the images representing the pieces of the game */
    private final ArrayList<Image> pieces = new ArrayList<>();


    private String playerCard;


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

    /** indicates if a square has been selected */
    private boolean squareSelected = false;

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

    public boolean stillPlaying = true;

    public JButton domeButton;

    public JButton skipButton;

    private boolean specialDomeMove = false;


    private JPanel cardPanel;

    private SquareButton builder1Button;
    private SquareButton builder2Button;

    private ArrayList<Square> moves1;
    private ArrayList<Square> moves2;

    /** indicates that we are in the phase where the user has to choose a worker */
    private boolean part1 = false;

    private Builder playingBuilder;

    /** boolean used for the implementation of the Selene card. If true,  */
    private boolean female = false;

    private boolean isDome = false;


    public ArrayList<String> getCardList(){return cardList;}

    public void setNumberOfPlayers(int num){ this.numberOfPlayers = num;}

    public int getNumberOfPlayers(){ return numberOfPlayers;}


    /** the actionListener assigned to the square button. When the button is pressed, if the move has not been chosen
     * sets squareSelected to true and gets the coordinates of the SquareButton
     */

    private class SquareListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){

            SquareButton button = (SquareButton) e.getSource();

            if(squareSelected && button.getBorder().equals(YELLOWBORDER)){
                squareList[x][y].setBorder(YELLOWBORDER);   // set the previous button to YELLOWBORDER
            }


            if(button.getBorder().equals(YELLOWBORDER)){
                button.setBorder(REDBORDER);
                x = button.getXvalue();                     //new button to REDBORDER
                y = button.getYvalue();
                squareSelected = true;

            }
        }
    }





    /** the actionListener assigned to the undo button. When the button is pressed, if the move has been chosen
     * it resets the selected square button
     */
    private class Undo implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            if(squareSelected) {

                squareList[x][y].setBorder(YELLOWBORDER);

                if (squareList[x][y].equals(builder1Button)) {      // repaints the possible workers
                    if(builder2Button != null){
                        builder2Button.setBorder(YELLOWBORDER);
                    }
                    for(Square s : moves1)
                        squareList[s.x][s.y].setBorder(BASICBORDER);
                    part1 = true;
                    playingBuilder = null;
                } else if (squareList[x][y].equals(builder2Button)) {
                    builder1Button.setBorder(YELLOWBORDER);
                    for(Square s: moves2)
                        squareList[s.x][s.y].setBorder(BASICBORDER);
                    part1 = true;
                    playingBuilder = null;
                }

                squareSelected = false;
            }


            if(!(part1)){


                builder1Button.setBorder(YELLOWBORDER);

                if(builder2Button != null){
                    builder2Button.setBorder(YELLOWBORDER);
                }

                for(Square s : moves1)
                    squareList[s.x][s.y].setBorder(BASICBORDER);

                if(moves2 != null) {
                    for (Square s : moves2)
                        squareList[s.x][s.y].setBorder(BASICBORDER);
                }


                /*

                if (squareList[x][y].equals(builder1Button)) {      // repaints the possible workers
                    if(builder2Button != null){
                        builder2Button.setBorder(YELLOWBORDER);
                    }
                    for(Square s : moves1)
                        squareList[s.x][s.y].setBorder(BASICBORDER);
                    part1 = true;
                    playingBuilder = null;
                } else if (squareList[x][y].equals(builder2Button)) {
                    builder1Button.setBorder(YELLOWBORDER);
                    for(Square s: moves2)
                        squareList[s.x][s.y].setBorder(BASICBORDER);

                 */
                part1 = true;
                playingBuilder = null;
            }

            revalidate();
            repaint();
        }

    }





    private class Confirm implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (squareSelected) {

                squareSelected = false;

                if(part1) {                                             //choose possible move
                    squareList[x][y].setBorder(BASICBORDER);
                    playingBuilder = squareList[x][y].getSquare().getBuilder();

                    if(squareList[x][y].equals(builder1Button)){
                        if(builder2Button != null) {
                            builder2Button.setBorder(BASICBORDER);
                        }
                        paintPossibleSquares(moves1);
                    }else {
                        builder1Button.setBorder(BASICBORDER);
                        paintPossibleSquares(moves2);
                    }
                    part1 = false;
                    squareSelected = false;

                }else {


                    if(setup) {

                        Square square = new Square(x, y);
                        try {
                            getController().getNetInterface().sendSquare(square, getClient().getServerSocket());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }


                    }else{

                        Moves container = createContainer(new Square(x,y));

                        try {
                            getController().getNetInterface().sendMoves(container, getClient().getServerSocket());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                        playingBuilder = null;
                        moves1 = null;
                        moves2 = null;

                    }


                    for (int i = 0; i < Board.BOARDSIZEX; i++) {            //returns all the buttons to basic border
                        for (int j = 0; j < Board.BOARDSIZEY; j++) {
                            squareList[i][j].setBorder(BASICBORDER);
                        }
                    }


                    SwingWorker worker = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            if (setup) {
                                try {
                                    getController().getNetInterface().getMatchSetup(getClient().getServerSocket(), getController());
                                } catch (IOException | InterruptedException | InvocationTargetException ioException) {
                                    ioException.printStackTrace();
                                }
                            } else {
                                try {
                                    getController().play();
                                } catch (IOException | InvocationTargetException | InterruptedException ioException) {
                                    ioException.printStackTrace();
                                }
                            }
                            return null;
                        }
                    };

                    worker.execute();

                    squareSelected = false;

                }

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
    public MainFrame() {

        super("SANTORINI");


        this.setup = true;

        this.stillPlaying = true;


        JLayeredPane pane = getLayeredPane();

        setResizable(false);

        map();

        createBlocks();
        createPieces();

        BoardPanel panel = new BoardPanel();
        panel.setLayout(new BorderLayout());

        add(panel, BorderLayout.WEST);


        JPanel glass = new JPanel(new GridLayout(5,5));

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



        pack();
        setLocationRelativeTo(null);


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



    /** obtains the icons representing the pieces and saves them inside attribute pieces */
    public void createPieces() {
        Image image;
        ClassLoader cl = this.getClass().getClassLoader();


        String[] pieceName = new String[]{"Red_M", "Red_F", "Blue_M", "Blue_F", "White_M", "White_F"};

        for (String item : pieceName) {

            InputStream url = cl.getResourceAsStream(item + ".png");
            BufferedImage img = null;
            try {
                img = ImageIO.read(url);
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




    public void paintPossibleSquares(ArrayList<Square> list){

        for(Square s : list){
            int a = s.x;
            int b = s.y;
            squareList[a][b].setBorder(YELLOWBORDER);
            squareList[a][b].setEnabled(true);
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


    public void updateBoard(Square square) throws IOException, InvocationTargetException, InterruptedException {

        int a = square.x;
        int b = square.y;
        squareList[a][b].setSquare(square);
        repaintSquare(squareList[a][b]);


        if(setup){
            getController().getNetInterface().getMatchSetup(getClient().getServerSocket(), getController());
        }else
            getController().play();

    }

    public void updateBoard(Square firstSquare, Square secondSquare) throws InterruptedException, IOException, InvocationTargetException {

        int a = firstSquare.x;
        int b = firstSquare.y;
        squareList[a][b].setSquare(firstSquare);
        repaintSquare(squareList[a][b]);

        a = secondSquare.x;
        b = secondSquare.y;
        squareList[a][b].setSquare(secondSquare);
        repaintSquare(squareList[a][b]);


        if(setup){
            getController().getNetInterface().getMatchSetup(getClient().getServerSocket(), getController());
        }else
            getController().play();

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



        }


        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                revalidate();
                repaint();
                return null;
            }
        };

        worker.execute();


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
            builder1Button = squareList[a][b];
            builder1Button.setEnabled(true);
            builder1Button.addActionListener(new SquareListener());
            builder1Button.setBorder(YELLOWBORDER);
        }else
            builder1Button = null;

        Builder builder2 = moves.getBuilder2();
        if(builder2 != null){
            Square square = builder2.getPosition();
            int a = square.x;
            int b = square.y;
            builder2Button = squareList[a][b];
            builder2Button.setEnabled(true);
            builder2Button.addActionListener(new SquareListener());
            builder2Button.setBorder(YELLOWBORDER);
        }else
            builder2Button = null;


        moves1 = moves.getMoves1();
        moves2 = moves.getMoves2();

        part1 = true;
        revalidate();
        repaint();

    }


    public Moves createContainer(Square move){

        Moves container = new Moves(null, null, null, null, false, false);
        container.setBuilder1(playingBuilder);

        ArrayList<Square> chosen = new ArrayList<>();
        chosen.add(move);

        container.setMoves1(chosen);

        container.setIsDome(isDome);

        container.setFemale(female);

        return container;

    }


}