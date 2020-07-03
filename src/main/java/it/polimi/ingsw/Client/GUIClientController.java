package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.NetworkHandler.GUINetInterface;
import it.polimi.ingsw.Client.View.CLI.ClientBoard;
import it.polimi.ingsw.Client.View.GUI.*;
import it.polimi.ingsw.Server.Model.Card;
import it.polimi.ingsw.Server.Model.Square;


import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;

import static it.polimi.ingsw.Server.Controller.GameInitializer.COLOUR1;
/**
 * represents the controller in the client side
 */
public class GUIClientController {
    /** represents the netInterface on the client side linked to the controller */
    private GUINetInterface netInterface = new GUINetInterface(this);
    /** represent the client to which the controller is linked */
    private Client client;
    /** represents the possible cards available to the player */
    public ArrayList<Card> possibleCards = new ArrayList<Card>();

    /** the main frame where the game is played */
    private MainFrame frame;


    public MainFrame getFrame(){return this.frame;}

    /**
     * create and initialize the client controller
     */
    public GUIClientController(Client client, MainFrame frame) {
        this.client = client;
        this.frame = frame;
    }

    /** method invoked during the setup phase to get information from the server */
    public void matchSetup(Socket socket) throws IOException, InterruptedException, InvocationTargetException {
        if(frame.stillPlaying){
            netInterface.getMatchSetup(socket, this);
        }
    }



/** opens the playerNumberWindow and makes the user choose the number of players for the game */
    public void chooseNumberOfPlayers() {


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.waitingDialog.setVisible(false);
                frame.playerNumberWindow.setVisible(true);
                frame.playerNumberWindow.setController();
            }
        });


    }

/** opens the cards choosing dialog and makes the player choose the cards for the game */
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
     * starts the game phase for the client, who calls this method every time the client has to get information from the server.
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
         }

     }

/** method invoked when the player has won */
     public void win() throws IOException {
         frame.stillPlaying = false;
         frame.getClient().getServerSocket().close();   //close socket connection
         frame.outcomeDialog.messageArea.setText("Congratulations! You have won! Would you like to play again?");
         frame.outcomeDialog.setVisible(true);
     }

    /** method invoked when the player has lost */
     public void lose(String winnerID) throws IOException {
         frame.stillPlaying = false;
         frame.getClient().getServerSocket().close();
         if((winnerID.equals("null"))){
             frame.outcomeDialog.messageArea.setText("Sorry, you lost. Would you like to play again?");
         }else
             frame.outcomeDialog.messageArea.setText("Sorry, you lost.\n" + winnerID + " has won.");

         frame.outcomeDialog.setVisible(true);
     }

    /** method invoked when the player has been disconnected */
    public void disconnected() throws IOException {
        frame.stillPlaying = false;
        frame.getClient().getServerSocket().close();   //close socket connection
        frame.waitingDialog.setVisible(false);
        frame.outcomeDialog.messageArea.setText("Sorry, you have been disconnected to the server. Would you like to play again?");
        frame.outcomeDialog.setVisible(true);
    }



        /**
         * sets the number of players of the game locally.
         * @param numberOfPlayers is the number of players in game.
         */
        public void setNumberOfPlayers(Integer numberOfPlayers) throws InvocationTargetException, InterruptedException {
            frame.setNumberOfPlayers(numberOfPlayers);
            frame.numberDialog.setController(this);
            frame.waitingDialog.setVisible(false);
            frame.numberDialog.setVisible(true);
        }


        /**
         * asks the Challenger to choose the first player that will start the game
         * @param players
         * @throws IOException
         */
        public void chooseBeginner(ArrayList<String> players) {

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    frame.waitingDialog.setVisible(false);
                    frame.firstPlayerWindow.displayNames(players);
                    frame.firstPlayerWindow.setVisible(true);
                }
            });

        }

        /**
         * shows the mainframe with the possible squares where the player can place its workers
         */
        public void placeBuilder(ArrayList<Square> freeSquares, int number){

            frame.waitingDialog.setVisible(false);
            frame.displayCard();
            frame.paintPossibleSquares(freeSquares);
            frame.setVisible(true);


        }


    /** prints on the mainFrame the information regarding the current player */
    public void printMatchInfo(String playerID, String playerColour, String playerCard) throws InterruptedException, IOException, InvocationTargetException {

        if(playerColour.equals(COLOUR1))
            playerColour = "Yellow";

        frame.stateArea.setText("Current player: " + playerID + "\n" + "Current player's colour: " +playerColour + "\n"
                                    + "Current player's deity: " + playerCard);

        play();
    }
}
