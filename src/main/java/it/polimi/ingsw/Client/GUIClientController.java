package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.NetworkHandler.GUINetInterface;
import it.polimi.ingsw.Client.View.ClientBoard;
import it.polimi.ingsw.Client.View.GUI.*;
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
    private Integer numberOfPlayers;

    private MainFrame frame;


    public MainFrame getFrame(){return this.frame;}

    public GUIClientController(Client client, MainFrame frame) {
        this.client = client;
        this.frame = frame;
    }


    public void matchSetup(Socket socket) throws IOException, InterruptedException, InvocationTargetException {
        if(frame.stillPlaying){
            netInterface.getMatchSetup(socket, this);
        }
    }




    public void chooseNumberOfPlayers() {


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.waitingDialog.setVisible(false);
                frame.playerNumberWindow.setVisible(true);
                frame.playerNumberWindow.setController();
            }
        });


    }


    public void dealerChoice(){


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
    public void playerChoice(){

        for (int i = 0; i < possibleCards.size(); i++) {
            String cardName = possibleCards.get(i).getName();
            frame.getCardList().add(cardName);
        }

        SwingUtilities.invokeLater(new Runnable() {
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
     public void play() throws IOException, InvocationTargetException, InterruptedException {
         Moves moves = netInterface.getMoves(client.getServerSocket());

         if(frame.stillPlaying) {
             SwingUtilities.invokeAndWait(new Runnable() {
                 public void run() {

                     if (moves.getSkippable()) {
                         frame.skipButton.setVisible(true);
                         frame.skipButton.setEnabled(true);
                     }


                     if (moves.getIsDome()) {
                         frame.domeButton.setVisible(true);
                         frame.domeButton.setEnabled(true);
                     }

                     frame.setMoves(moves);
                 }
             });
         }//else{

            // disconnected();

         //}

     }


     public void win() throws IOException {
         frame.stillPlaying = false;
         frame.getClient().getServerSocket().close();   //close socket connection
         frame.outcomeDialog.messageArea.setText("Congratulations! You have won! Would you like to play again?");
         frame.outcomeDialog.setVisible(true);
     }

     public void lose(String winnerID) throws IOException {
         frame.stillPlaying = false;
         frame.getClient().getServerSocket().close();
         if(!(winnerID.equals("null"))){
             frame.outcomeDialog.messageArea.setText("Sorry, you lost. Would you like to play again?");
         }else
             frame.outcomeDialog.messageArea.setText("Sorry, you lost.\n" + winnerID + " has won.");

         frame.outcomeDialog.setVisible(true);
     }

    public void disconnected() throws IOException {
        frame.stillPlaying = false;
        frame.getClient().getServerSocket().close();   //close socket connection
        frame.outcomeDialog.messageArea.setText("Sorry, you have been disconnected to the server. Would you like to play again?");
        frame.outcomeDialog.setVisible(true);
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

        }

        /**
         *
         * @param freeSquares
         * @param number
         * @throws IOException
         */
        public void placeBuilder(ArrayList<Square> freeSquares, int number) throws IOException, InvocationTargetException, InterruptedException {

            frame.waitingDialog.setVisible(false);
            frame.displayCard();
            frame.paintPossibleSquares(freeSquares);
            frame.setVisible(true);


        }

        //TODO cancellare?
        public void updateBoard(Square firstSquare, Square secondSquare) throws InterruptedException, IOException, InvocationTargetException {
            frame.updateBoard(firstSquare, secondSquare);
        }


        //TODO cancellare?
        public void updateBoard(Square firstSquare) throws InterruptedException, IOException, InvocationTargetException {
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





}
