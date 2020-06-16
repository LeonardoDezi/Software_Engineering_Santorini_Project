package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.NetworkHandler.NetInterface;
import it.polimi.ingsw.Server.Model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ClientController {
    private NetInterface netInterface = new NetInterface(this);
    private Client client;
    private Boolean stillPlaying;
    public Boolean setup;
    public ArrayList<Card> possibleCards = new ArrayList<Card>();
    private ArrayList<Square> moves1;
    private ArrayList<Square> moves2;
    private Builder builder1;
    private  Builder builder2;
    private Integer numberOfPlayers;

    public ClientController(Client client) {
        this.client = client;
    }

    /**
     * this method loops until all the players are ready to play
     * @param socket is the socket from where the clients gets the information for the setup
     */
   public void matchSetup(Socket socket) throws IOException {
        setup = true;
        while(setup){
            netInterface.getMatchSetup(socket, this);
        }
    }

    public void dealerChoice() throws IOException {
        System.out.println("Choose "+ this.numberOfPlayers +" cards for the game");
        ArrayList<Integer> chosenCards = this.chosenCards();
        netInterface.sendCard(chosenCards, client.getServerSocket());
    }

    public void playerChoice() throws IOException {
        System.out.println("Pick a card for the game");
        Integer card = this.pickACard();
        netInterface.sendCard(card, client.getServerSocket());
    }

    /**
     * starts the game phase for the client and keeps spinning until the end of the game.
     * @param socket is the socket used to talk with the server.
     */
    public void play(Socket socket) throws IOException {
        stillPlaying=true;
        while(stillPlaying){
            Moves moves = netInterface.getMoves(socket);
            if(moves!=null){
                Envelope envelope = chooseMove(moves);
                netInterface.sendMoves(envelope, client.getServerSocket());
            }
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

    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public ArrayList<Integer> chosenCards() throws IOException {
        ArrayList<Integer> chosenCards = new ArrayList<Integer>();
        for (int i = 0; i<numberOfPlayers; i++){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int card = Integer.parseInt(reader.readLine());
            chosenCards.add(i,card);
        }
        return chosenCards;
    }

    public Integer pickACard() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Integer card = Integer.parseInt(reader.readLine());
        return card;
    }

    public void chooseBeginner(ArrayList<String> players) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int player = Integer.parseInt(reader.readLine());
        String chosenOne = players.get(player-1);
        netInterface.sendFirstPlayer(chosenOne,client.getServerSocket());
    }
}
