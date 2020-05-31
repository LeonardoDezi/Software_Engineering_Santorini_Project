package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.NetworkHandler.NetInterface;
import it.polimi.ingsw.Server.Model.*;

import java.net.Socket;
import java.util.ArrayList;

public class ClientController {
    private NetInterface netInterface = new NetInterface(this);
    private Client client;
    private Boolean stillPlaying;
    public Boolean setup;
    public ArrayList<Card> possibleCards;
    private ArrayList<Square> moves1;
    private ArrayList<Square> moves2;
    private Builder builder1;
    private  Builder builder2;

    public ClientController(Client client) {
        this.client = client;
    }

    /**
     * this method loops until all the players are ready to play
     * @param socket is the socket from where the clients gets the information for the setup
     */
    public void matchSetup(Socket socket){
        setup = true;
        while(setup){
            netInterface.getMatchSetup(socket, this);
        }
    }

    public void dealerChoice(){
        //TODO in possiblecards there are all the 15 card, ask the player whitch 2 or 3 he wants and put them in
        // card1 card2 and card3
        Integer card1 = 1;
        Integer card2 = 2;
        Integer card3 = 3;
        netInterface.sendCard(card1, card2, card3, client.getServerSocket());
    }

    public void playerChoice(){
        //TODO in possiblecards there are all the available card, ask the player whitch does he want and put it in
        // card1
        Integer card1 = 1;
        netInterface.sendCard(card1, client.getServerSocket());
    }

    /**
     * starts the game phase for the client and keeps spinning until the end of the game.
     * @param socket is the socket used to talk with the server.
     */
    public void play(Socket socket){
        stillPlaying=true;
        while(stillPlaying){
            Moves moves = netInterface.getMoves(socket);
            Envelope envelope = chooseMove(moves);
            netInterface.sendMoves(envelope, client.getServerSocket());
        }
        //TODO close the match
        //print message "you have lost"
    }

    /**
     * Shows on the screen all the possible moves that the player can do and waits for the choice.
     * @param moves all the moves that the player can do.
     * @return the single move chosen by the player with the builder that is going to do that move.
     */
    public Envelope chooseMove(Moves moves){
        //TODO moves contains all the moves that the player can do, return an envelope with the chosen move
    return  null;
    }

    /**
     * sets the stillPlaying flag to false to end the game.
     */
    public void lost(){
        this.stillPlaying=false;
    }

}
