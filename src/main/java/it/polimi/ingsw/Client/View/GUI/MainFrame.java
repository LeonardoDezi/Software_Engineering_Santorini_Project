package it.polimi.ingsw.Client.View.GUI;
import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Client.GUIClientController;
import it.polimi.ingsw.Client.Moves;
import it.polimi.ingsw.Client.NetworkHandler.GUINetInterface;
import it.polimi.ingsw.Parser.Sender;
import it.polimi.ingsw.Server.Model.Board;
import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Envelope;
import it.polimi.ingsw.Server.Model.Square;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
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
import static java.awt.BorderLayout.CENTER;

/** this class represents the main Frame where the game is played */
public class MainFrame extends JFrame {


    /** represents the list of all the squares of the board*/
    private SquareButton[][]  squareList;

    /** the list of the images representing the blocks of the game */
    private final ArrayList<Image> blocks = new ArrayList<>();

    /** the list of the images representing the pieces of the game */
    private final ArrayList<Image> pieces = new ArrayList<>();

    /** the name of the player's card */
    private String playerCard;


    /** represents the border used to indicate that the button has been selected */
    private final static Border REDBORDER = new LineBorder(Color.RED, 3);


    /** represents the border used to indicate that the button can be selected */
    private final static Border YELLOWBORDER = new LineBorder(Color.YELLOW, 3);

    /** represents the image of the player's card */
    private JLabel cardLabel;

    /** represents the basic border of the control buttons */
    private final static Border BASICBORDER = LineBorder.createBlackLineBorder();
    /** represents the basic border of the board buttons */
    private final static Border EMPTYBORDER = new LineBorder(new Color(0,0,0,0), 3);


    /** the x coordinate of the squareButton selected */
    private int x;

    /** the y coordinate of the squareButton selected */
    private int y;

    /** indicates if a square has been selected */
    private boolean squareSelected = false;

    /** the waiting dialog used during the execution of the program */
    public WaitingDialog waitingDialog = new WaitingDialog(this);

    /** the cardChoosing dialog used during the execution of the program */
    public CardChoosingDialog cardChoosingDialog = new CardChoosingDialog(this);

    /** the player number window used during the execution of the program */
    public PlayerNumberWindow playerNumberWindow = new PlayerNumberWindow(this);

    /** the card picking dialog dialog used during the execution of the program */
    public CardPickingDialog cardPickingDialog = new CardPickingDialog(this);

    /** the number dialog used during the execution of the program */
    public NumberDialog numberDialog = new NumberDialog(this);

    /** the first player dialog used during the execution of the program */
    public FirstPlayerWindow firstPlayerWindow = new FirstPlayerWindow(this);

    /** the outcome dialog used during the execution of the program */
    public OutcomeDialog outcomeDialog = new OutcomeDialog(this);

    /** the fatal error window used during the execution of the program */
    public FatalErrorWindow fatalErrorWindow = new FatalErrorWindow(this);

    /** the number of players that play in the game */
    private int numberOfPlayers;

    /** the hashmap used to translate the card's name into its number */
    private HashMap<String, String> cardMap = new HashMap<>();


    private Client client;

    private GUIClientController controller;

    /** represents the list of cards chosen for this game */
    private ArrayList<String> cardList = new ArrayList<>();

    /** if true, it indicates that the board is still in setup mode and the actual game hasn't started yet */
    public boolean setup;

    /** if true, the client is stillPlaying and still connected to the server */
    public boolean stillPlaying = true;

    /** when pressed, the player will build a dome */
    public JButton domeButton;

    /** when pressed, the player will skip a phase */
    public JButton skipButton;

    /** displays messages regarding the current phase */
    public JTextArea messageArea;

    /** displays the information regarding the player who is currently playing */
    public JTextArea stateArea;

    /** displays the player's card */
    private JPanel cardPanel;

    /** represents the button where the player's first worker is placed */
    private SquareButton builder1Button;

    /** represents the button where the player's second worker is placed */
    private SquareButton builder2Button;

    /** the list of moves that the player's first worker can make */
    private ArrayList<Square> moves1;

    /** the list of moves that the player's second worker can make */
    private ArrayList<Square> moves2;

    /** indicates that we are in the phase where the user has to choose a worker */
    private boolean part1 = false;

    /** represents the worker that will make the move */
    private Builder playingBuilder;


    /** boolean used to indicate that the player wants to build a dome */
    private boolean buildDome = false;


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
                x = button.getXvalue();                     // sets the new button to REDBORDER
                y = button.getYvalue();
                squareSelected = true;

            }


        }
    }


    /** the actionListener assigned to the square button. When the button is pressed, it sets the buildDome to true, set to false if it has already been done
     */
    private class Dome implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){

            JButton button = (JButton) e.getSource();

            if(button.getBorder().equals(BASICBORDER)) {
                button.setBorder(REDBORDER);                                   //red = true
                buildDome = true;
            }else if(button.getBorder().equals((REDBORDER))){
                button.setBorder(BASICBORDER);                                  //basic = false
                buildDome = false;
            }


        }
    }



    /** the actionListener assigned to the skip button. When the button is pressed, it communicates that the user wants to skip the specialphase
     */
    private class Skip implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){

            try {
                Sender.send("0", getClient().getServerSocket());
            } catch (IOException ioException) {
                fatalErrorWindow.setVisible(true);
                MainFrame.this.setVisible(false);
            }


            SwingWorker worker = new SwingWorker() {
                @Override
                protected Object doInBackground(){
                    try {
                        getController().play();
                    } catch (IOException | InvocationTargetException | InterruptedException ioException) {
                        fatalErrorWindow.setVisible(true);
                        MainFrame.this.setVisible(false);
                    }
                    return null;
                }
            };

            worker.execute();

            skipButton.setEnabled(false);
            skipButton.setVisible(false);


            for (int i = 0; i < Board.BOARDSIZEX; i++) {            //returns all the buttons to basic border
                for (int j = 0; j < Board.BOARDSIZEY; j++) {
                    squareList[i][j].setBorder(EMPTYBORDER);
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

            if(squareSelected) {

                squareList[x][y].setBorder(YELLOWBORDER);

                if (squareList[x][y].equals(builder1Button)) {      // repaints the possible workers
                    if(builder2Button != null){
                        builder2Button.setBorder(YELLOWBORDER);
                    }
                    for(Square s : moves1)
                        squareList[s.x][s.y].setBorder(EMPTYBORDER);
                    part1 = true;
                    playingBuilder = null;
                } else if (squareList[x][y].equals(builder2Button)) {
                    builder1Button.setBorder(YELLOWBORDER);
                    for(Square s: moves2)
                        squareList[s.x][s.y].setBorder(EMPTYBORDER);
                    part1 = true;
                    playingBuilder = null;
                }

                squareSelected = false;
            }


            if(!(part1)){

                if(builder1Button != null && builder2Button != null) {    //necessary in order to not have a nullPointerException during the placement of the builders
                    builder1Button.setBorder(YELLOWBORDER);

                    if (builder2Button != null) {
                        builder2Button.setBorder(YELLOWBORDER);
                    }

                    for (Square s : moves1)
                        squareList[s.x][s.y].setBorder(EMPTYBORDER);

                    if (moves2 != null) {
                        for (Square s : moves2)
                            squareList[s.x][s.y].setBorder(EMPTYBORDER);
                    }

                    part1 = true;
                    playingBuilder = null;
                }
            }

            revalidate();
            repaint();
        }

    }





    /** the actionListener assigned to the confirm button. When the button is pressed, it can display the worker's possible moves,
     * select a worker or send the selected move
     */
    private class Confirm implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (squareSelected) {

                squareSelected = false;

                if(part1) {                                             //choose possible move
                    squareList[x][y].setBorder(EMPTYBORDER);
                    playingBuilder = squareList[x][y].getSquare().getBuilder();

                    if(squareList[x][y].equals(builder1Button)){
                        if(builder2Button != null) {
                            builder2Button.setBorder(EMPTYBORDER);
                        }
                        paintPossibleSquares(moves1);
                    }else {
                        if(builder1Button != null) {
                            builder1Button.setBorder(EMPTYBORDER);
                        }
                        paintPossibleSquares(moves2);
                    }
                    part1 = false;
                    squareSelected = false;

                }else {


                    if(setup) {                                   //setup phase

                        Square square = new Square(x, y);
                        try {
                            getController().getNetInterface().sendSquare(square, getClient().getServerSocket());
                        } catch (IOException ioException) {
                            fatalErrorWindow.setVisible(true);
                            MainFrame.this.setVisible(false);
                        }


                    }else{                                             //normal phase

                        Moves container = createContainer(new Square(x,y));

                        try {
                            getController().getNetInterface().sendMoves(container, getClient().getServerSocket());
                        } catch (IOException ioException) {
                            fatalErrorWindow.setVisible(true);
                            MainFrame.this.setVisible(false);
                        }

                        playingBuilder = null;
                        moves1 = null;
                        moves2 = null;
                        buildDome = false;
                        /**
                         * boolean used to manage Selene's behaviour. If true, then the playing worker is male
                         * and the player can choose whether to normally build with the playing worker or
                         * to build a dome with the female one
                         */

                        domeButton.setEnabled(false);
                        domeButton.setBorder(BASICBORDER);
                        domeButton.setVisible(false);

                        skipButton.setEnabled(false);
                        skipButton.setVisible(false);

                        squareSelected = false;

                    }


                    for (int i = 0; i < Board.BOARDSIZEX; i++) {            //returns all the buttons to basic border
                        for (int j = 0; j < Board.BOARDSIZEY; j++) {
                            squareList[i][j].setBorder(EMPTYBORDER);
                        }
                    }


                    SwingWorker worker = new SwingWorker() {                             // waits for next step
                        @Override
                        protected Object doInBackground() throws Exception {
                            if (setup) {
                                try {
                                    getController().getNetInterface().getMatchSetup(getClient().getServerSocket(), getController());
                                } catch (IOException | InterruptedException | InvocationTargetException ioException) {
                                    fatalErrorWindow.setVisible(true);
                                    MainFrame.this.setVisible(false);
                                }
                            } else {
                                try {
                                    getController().play();
                                } catch (IOException | InvocationTargetException | InterruptedException ioException) {
                                    fatalErrorWindow.setVisible(true);
                                    MainFrame.this.setVisible(false);
                                }
                            }
                            return null;
                        }
                    };

                    worker.execute();

                    messageArea.setText(null);

                }

            }

        }

    }




    /** fills cardMap with the cards's name and number */
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

    /** returns the number associated to the card's name */
    public String translate(String name){ return cardMap.get(name);}

    public void setPlayerCard(String name){ this.playerCard = name;}


    /** creates a new MainFrame */
    public MainFrame(){

        super("SANTORINI");


        this.setup = true;

        this.stillPlaying = true;


        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    getClient().getServerSocket().close();
                    MainFrame.this.dispose();
                } catch (IOException ioException) {
                    System.exit(1);
                }
            }
        };
        this.addWindowListener(exitListener);


        JLayeredPane pane = getLayeredPane();

        setResizable(false);

        map();

        cardPanel = new JPanel(new BorderLayout());

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
                squareList[i][j].setBorderPainted(true);
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


        JButton confirmButton = new JButton(("Confirm"));
        confirmButton.addActionListener(new Confirm());
        controlPanel.add(confirmButton, BorderLayout.SOUTH);


        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(new Undo());
        controlPanel.add(undoButton, BorderLayout.SOUTH);


        domeButton = new JButton("DOME");
        domeButton.setVisible(false);
        domeButton.setBorder(BASICBORDER);
        domeButton.addActionListener(new Dome());
        controlPanel.add(domeButton, BorderLayout.SOUTH);


        skipButton = new JButton("SKIP");
        skipButton.setVisible(false);
        skipButton.addActionListener(new Skip());
        controlPanel.add(skipButton, BorderLayout.SOUTH);

        stateArea = new JTextArea();
        stateArea.setOpaque(true);
        stateArea.setEditable(false);
        stateArea.setLineWrap(true);
        stateArea.setWrapStyleWord(true);
        stateArea.setSize(270,180);
        stateArea.setBorder(EMPTYBORDER);
        stateArea.setFont(new Font("Diogenes", Font.BOLD, 14));
        stateArea.setVisible(true);



        messageArea = new JTextArea();
        messageArea.setOpaque(true);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setSize(270,180);
        messageArea.setBorder(EMPTYBORDER);
        messageArea.setFont(new Font("Diogenes", Font.BOLD, 14));
        messageArea.setVisible(true);

        cardPanel.add(stateArea, BorderLayout.NORTH);
        cardPanel.add(messageArea, BorderLayout.PAGE_START);

        pack();

        cardPanel.setSize(250, 500);
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


    /** displays the card chosen by the player */
    public void displayCard(){

        ClassLoader cl = this.getClass().getClassLoader();
        String cardNumber = translate(playerCard);
        BufferedImage img;
        URL url = cl.getResource(cardNumber + ".png");

        try {
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int W = img.getWidth();
        int H = img.getHeight();

        cardLabel = new JLabel(new ImageIcon(getScaledImage(img, W / 8, H / 8)));
        cardPanel.add(cardLabel, CENTER);
        pack();

    }



    /** paints yellow the possible squares where the player can place its workers */
    public void paintPossibleSquares(ArrayList<Square> list){

        for(Square s : list){
            int a = s.x;
            int b = s.y;
            squareList[a][b].setBorder(YELLOWBORDER);
            squareList[a][b].setEnabled(true);
        }

        pack();

    }

    /** updates the board with the values of the given square */
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

    /** updates the board with the values of the given squares */
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


    /** repaints a square according to the move received by the server */
    public void repaintSquare(SquareButton button){

        JLabel label;

        int level = button.getSquare().getLevel();

        if(level == 4)
            level = level - 1;

        if (level == 0) {
            button.setIcon(null);
        } else
            button.setIcon(new ImageIcon(blocks.get(level)));


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

            if(button.getTopLabel() != null)
                button.remove(button.getTopLabel());

            button.setTopLabel(label);

            button.add(label);
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


    /** returns the JLabel associated to the worker based on its colour and sex */
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

/** memorizes the workers and the possible moves received by the server and sets the border of the workers that can be used to yellow */
    public void setMoves(Moves moves){

        if(moves.getFemale()) {                    //special Selene condition: first worker = male, second worker = female

            messageArea.setVisible(true);
            messageArea.setText("SELENE: you can either normally build with your male worker or build domes with your female one");

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

    }

    /** creates the container with the move chosen by the player, in order to be sent to the server */
    public Moves createContainer(Square move){

        ArrayList<Square> chosen = new ArrayList<>();
        chosen.add(move);

        return  new Moves(playingBuilder, chosen, null, null, buildDome, false);

    }


}