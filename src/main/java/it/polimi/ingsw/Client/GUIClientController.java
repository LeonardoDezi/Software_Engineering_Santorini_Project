package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.NetworkHandler.GUINetInterface;
import it.polimi.ingsw.Client.View.CLI.CliBoard;
import it.polimi.ingsw.Client.View.ClientBoard;
import it.polimi.ingsw.Client.View.GUI.*;
import it.polimi.ingsw.Client.View.Pawn;
import it.polimi.ingsw.Parser.Sender;
import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Card;
import it.polimi.ingsw.Server.Model.Square;


import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;

public class GUIClientController {
    private GUINetInterface netInterface = new GUINetInterface(this);
    private Client client;
    public ArrayList<Card> possibleCards = new ArrayList<Card>();
    private ClientBoard clientBoard = new ClientBoard();
    private Square builderSquare;
    private ArrayList<Square> possibleMoves;
    private boolean dome;
    private Integer numberOfPlayers;

    private MainFrame frame;


    public GUIClientController(Client client, MainFrame frame) {
        this.client = client;
        this.frame = frame;
    }


    public void matchSetup(Socket socket) throws IOException, InterruptedException, InvocationTargetException {
        if(frame.stillPlaying){
            netInterface.getMatchSetup(socket, this);
        }
        //else if(disconnect) {

      //  }

    }




    /*
    public void matchSetup(Socket socket) throws IOException, InterruptedException, InvocationTargetException {
        setup = true;
        while(setup){
            netInterface.getMatchSetup(socket, this);
        }
    }
    */

    public void chooseNumberOfPlayers() throws IOException, InterruptedException, InvocationTargetException {


        SwingUtilities.invokeLater(new Runnable() {   //invokeandWait
            public void run() {
                frame.waitingDialog.setVisible(false);
                frame.playerNumberWindow.setVisible(true);
                frame.playerNumberWindow.setController();
            }
        });


    }


    public void dealerChoice() throws InterruptedException {


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.waitingDialog.setVisible(false);
                frame.cardChoosingDialog.setNumberOfPlayers();
                frame.cardChoosingDialog.setVisible(true);
            }
        });


    }


    public Client getClient() {
        return this.client;
    }

    public GUINetInterface getNetInterface() {
        return this.netInterface;
    }


    /**
     * calls the method to ask the player which card he wants to choose from the cards chosen by the Challenger.
     * throws IOException
     */
    public void playerChoice() throws IOException, InvocationTargetException, InterruptedException {

        for (int i = 0; i < possibleCards.size(); i++) {
            String cardName = possibleCards.get(i).getName();
            frame.getCardList().add(cardName);
        }

        SwingUtilities.invokeLater(new Runnable() {     //INVOKE AND WAIT
            public void run() {
                frame.waitingDialog.setVisible(false);
                frame.cardPickingDialog.displayCards();
                frame.cardPickingDialog.setVisible(true);
            }
        });


    }


    /**
     * starts the game phase for the client and keeps spinning until the end of the game.
     */
     public void play() throws IOException {
         Moves moves = netInterface.getMoves(client.getServerSocket());


         SwingUtilities.invokeLater(new Runnable() {           //TODO invoke and wait
             public void run() {

                 if(moves.getSkippable()) {
                     frame.skipButton.setVisible(true);
                     frame.skipButton.setEnabled(true);
                 }

                 if(moves.getFemale()){

                 }

                 if(moves.getIsDome()){
                     frame.domeButton.setVisible(true);
                     frame.domeButton.setEnabled(true);
                 }

                 frame.setMoves(moves);
             }
         });


     }


    /*public void play(Socket socket) throws IOException {
        Moves moves = new Moves(null, null, null, null, false, false);

        Moves envelope;
        if (moves.getSkippable()) {
            System.out.println("Do you want to perform special card effect? y/n");
            if (!returnBoolean()) {
            //   Sender.send("0", client.getServerSocket());
            } else {
                envelope = chooseMove(moves);
            //    netInterface.sendMoves(envelope, client.getServerSocket());
            }
        } else if (moves.getFemale()) {
          //  envelope = getSpecialBuild(moves);
           // netInterface.sendMoves(envelope, client.getServerSocket());
            envelope = chooseMove(moves);
            //netInterface.sendMoves(envelope, client.getServerSocket());
        }

        moves =netInterface.getMoves(socket);
    }  */



        /**
         * Shows on the screen all the possible moves that the player can do and waits for the choice.
         * @param moves all the moves that the player can do.
         * @return the single move chosen by the player with the builder that is going to do that move.
         */
        public Moves chooseMove(Moves moves) throws IOException {
            if(moves.getMoves1() != null){
                for (int i=0; i<moves.getMoves1().size(); i++){
                    for (int j=0; j<5; j++){
                        for (int k=0; k<5; k++){
                            if (clientBoard.getCell(j, k).getX() == moves.getMoves1().get(i).x && clientBoard.getCell(j, k).getY() == moves.getMoves1().get(i).y){
                                clientBoard.getCell(j, k).setColour(1);
                            }
                        }
                    }
                }
            }
            if(moves.getMoves2() != null){
                for (int i=0; i<moves.getMoves2().size(); i++){
                    for (int j=0; j<5; j++){
                        for (int k=0; k<5; k++){
                            if (clientBoard.getCell(j, k).getX() == moves.getMoves2().get(i).x && clientBoard.getCell(j, k).getY() == moves.getMoves2().get(i).y) {
                                clientBoard.getCell(j, k).setColour(1);
                            }
                        }
                    }
                }
            }
            CliBoard.drawBoard(clientBoard);
            this.resetBoard();

            if (moves.getMoves2() != null){
                System.out.println("Pick a worker: position (x,y)");

                builderSquare = getPosition();
                possibleMoves = chosenBuilder(moves,builderSquare);

                while (possibleMoves == null){
                    System.out.println("Invalid pick, chose more wisely");
                    builderSquare = getPosition();
                    possibleMoves = chosenBuilder(moves,builderSquare);

                }
                CliBoard.drawBoard(clientBoard);

                this.resetBoard();

            }else {
                builderSquare = moves.getBuilder1().getPosition();
                possibleMoves = chosenBuilder(moves,builderSquare);

                CliBoard.drawBoard(clientBoard);

                this.resetBoard();
            }


            System.out.println("Select move: (x,y)");
            if(moves.getIsDome()){
                System.out.println("You can build a dome by your god's card power");
            }

            Square chosen = getPosition();

            while(!searchArray(possibleMoves,chosen)){
                System.out.println("Invalid pick, chose more wisely");
                chosen = getPosition();
            }

            ArrayList<Square> selected = new ArrayList<Square>();
            selected.add(chosen);

            if(moves.getIsDome()){
                System.out.println("Do you want to build a Dome? y/n");
                dome = returnBoolean();
            }
            else {
                dome = false;
            }

            Moves chosenMove = new Moves(returnBuilder(builderSquare,moves),selected,null,null,dome,false);

            return  chosenMove;
        }

        /**
         * sets the stillPlaying flag to false to end the game.
         */
        public void lost(){
            frame.stillPlaying=false;
        }

        /**
         * sets the number of players of the game locally.
         * @param numberOfPlayers is the number of players in game.
         */
        public void setNumberOfPlayers(Integer numberOfPlayers) throws InvocationTargetException, InterruptedException {
            this.numberOfPlayers = numberOfPlayers;
            frame.setNumberOfPlayers(numberOfPlayers);
            frame.numberDialog.setController(this);
            frame.waitingDialog.setVisible(false);
            frame.numberDialog.setVisible(true);
        }

        /**
         * shows on screen all the 15 cards and waits for the Challenger choice.
         * @return the 2 or 3 chosen cards.
         * @throws IOException
         */
        public ArrayList<Integer> chosenCards() throws IOException {
            ArrayList<Integer> chosenCards = new ArrayList<Integer>();
            for (int i = 0; i<numberOfPlayers; i++){
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int card = Integer.parseInt(reader.readLine());
                chosenCards.add(i,card);
            }
            return chosenCards;
        }


        /**
         * asks the Challenger to choose
         * @param players
         * @throws IOException
         */
        public void chooseBeginner(ArrayList<String> players) throws IOException, InvocationTargetException, InterruptedException {

            SwingUtilities.invokeLater(new Runnable() {           //invoke and wait
                public void run() {
                    frame.waitingDialog.setVisible(false);
                    frame.firstPlayerWindow.displayNames(players);
                    frame.firstPlayerWindow.setVisible(true);
                    //new FirstPlayerWindow(frame);
                }
            });
/*
            String chosenOne = players.get(player-1);
            netInterface.sendFirstPlayer(chosenOne,client.getServerSocket());

 */
        }

        /**
         *
         * @param freeSquares
         * @param number
         * @throws IOException
         */
        public void placeBuilder(ArrayList<Square> freeSquares, int number) throws IOException, InvocationTargetException, InterruptedException {
            System.out.println("Place your builders: positions (x,y)");


            SwingUtilities.invokeAndWait(new Runnable() {   //invokeLater
                @Override
                public void run() {
                    frame.waitingDialog.setVisible(false);
                    frame.displayCard();
                    frame.placeBuilder(freeSquares);
                    frame.setVisible(true);
                }
            });


        }

        public void updateBoard(Square firstSquare, Square secondSquare) {
            frame.updateBoard(firstSquare, secondSquare);
        }

        public void updateBoard(Square firstSquare) {
            frame.updateBoard(firstSquare);
        }

        private void resetBoard(){
            for (int i=0; i<5; i++){
                for (int j=0; j<5; j++){
                    clientBoard.getCell(i,j).setColour(0);
                }
            }
        }

        public Square getPosition() throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String[] inputs = reader.readLine().split(",");
            int[] array = new int[inputs.length];
            for (int i=0; i<array.length; i++){
                array[i] = Integer.parseInt(inputs[i]);
            }
            int x = array[1]-1;
            int y = array[0]-1;
            return new Square(x, y);
        }

        public ArrayList<Square> chosenBuilder(Moves moves, Square builderSquare){

            if (builderSquare.x == moves.getBuilder1().getPosition().x && builderSquare.y == moves.getBuilder1().getPosition().y){
                for (int i=0; i<moves.getMoves1().size(); i++){
                    for (int j=0; j<5; j++){
                        for (int k=0; k<5; k++){
                            if (clientBoard.getCell(j, k).getX() == moves.getMoves1().get(i).x && clientBoard.getCell(j, k).getY() == moves.getMoves1().get(i).y){
                                clientBoard.getCell(j, k).setColour(1);
                            }
                        }
                    }
                }
                return moves.getMoves1();
            }

            if (builderSquare.x == moves.getBuilder2().getPosition().x && builderSquare.y == moves.getBuilder2().getPosition().y){
                for (int i=0; i<moves.getMoves2().size(); i++){
                    for (int j=0; j<5; j++){
                        for (int k=0; k<5; k++){
                            if (clientBoard.getCell(j, k).getX() == moves.getMoves2().get(i).x && clientBoard.getCell(j, k).getY() == moves.getMoves2().get(i).y){
                                clientBoard.getCell(j, k).setColour(1);
                            }
                        }
                    }
                }
                return moves.getMoves2();
            }
            return null;
        }

        public boolean searchArray(ArrayList<Square> possibleMoves, Square chosen){
            for(int i=0; i<possibleMoves.size(); i++){
                if(chosen.x == possibleMoves.get(i).x && chosen.y == possibleMoves.get(i).y){
                    return true;
                }
            }
            return false;
        }

        public Builder returnBuilder(Square builderSquare, Moves moves){
            if(builderSquare.x == moves.getBuilder1().getPosition().x && builderSquare.y == moves.getBuilder1().getPosition().y){
                return moves.getBuilder1();
            }
            if (builderSquare.x == moves.getBuilder2().getPosition().x && builderSquare.y == moves.getBuilder2().getPosition().y){
                return moves.getBuilder2();
            }
            else
                return null;

        }

        public boolean returnBoolean() throws IOException{

            String flag;

            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            flag = bufferRead.readLine();

            if (flag.equals("y") || flag.equals("yes")){
                return true;
            }
            return false;

        }


        public void startMatch(){frame.setup = false;}


















}
