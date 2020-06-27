package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.NetworkHandler.NetInterface;
import it.polimi.ingsw.Client.View.CLI.CliBoard;
import it.polimi.ingsw.Client.View.ClientBoard;
import it.polimi.ingsw.Client.View.Pawn;
import it.polimi.ingsw.Server.Model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedWriter;
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
    private ClientBoard clientBoard = new ClientBoard();

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

    /**
     * calls the method to ask the Challenger which cards he want to use for the game and sends them back to the server.
     * @throws IOException
     */
    public void dealerChoice() throws IOException {
        System.out.println("Choose "+ this.numberOfPlayers +" cards for the game");
        ArrayList<Integer> chosenCards = this.chosenCards();
        netInterface.sendCard(chosenCards, client.getServerSocket());
    }

    /**
     * calls the method to ask the player which card he wants to choose from the cards chosen by the Challenger.
     * @throws IOException
     */
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

    /**
     * sets the number of players of the game locally.
     * @param numberOfPlayers is the number of players in game.
     */
    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
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
     * Shows on screen the 2 or 3 cards chosen by the Challenger (or the remaining ones) and waits for the player choice.
     * @return The chosen card.
     * @throws IOException
     */
    public Integer pickACard() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Integer card = Integer.parseInt(reader.readLine());
        return card;
    }

    /**
     * asks the Challenger to choose
     * @param players
     * @throws IOException
     */
    public void chooseBeginner(ArrayList<String> players) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int player = Integer.parseInt(reader.readLine());
        String chosenOne = players.get(player-1);
        netInterface.sendFirstPlayer(chosenOne,client.getServerSocket());
    }

    public void placeBuilder(ArrayList<Square> freeSquares, int number) throws IOException {
        System.out.println("Place your builders: positions (x,y)");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] inputs = reader.readLine().split(",");
        int[] array = new int[inputs.length];
        for (int i=0; i<array.length; i++){
            array[i] = Integer.parseInt(inputs[i]);
        }
        int x = array[0]-1;
        int y = array[1]-1;
        for (int i=0; i<freeSquares.size(); i++){
            int tempX = freeSquares.get(i).x;
            int tempY = freeSquares.get(i).y;
            if( x == tempX && y == tempY){
                Square square = freeSquares.get(i);
                netInterface.sendSquare(square,client.getServerSocket());
            }
        }
    }

    public void updateBoard(Square firstSquare, Square secondSquare, Builder builder1, Builder builder2) {
    }

    public void updateBoard(Square firstSquare, Builder builder1) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (clientBoard.getCell(i, j).getX() == firstSquare.x && clientBoard.getCell(i, j).getY() == firstSquare.y) {
                    clientBoard.getCell(i, j).setValue(firstSquare.getValue());
                    clientBoard.getCell(i, j).setLevel(firstSquare.getLevel());
                    Pawn pawn = new Pawn(builder1.getColour(),builder1.getSex());
                    clientBoard.getCell(i, j).setPawn(pawn);
                    CliBoard.drawBoard(clientBoard);
                }
            }
        }
    }
}