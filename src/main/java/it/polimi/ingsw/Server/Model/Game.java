package it.polimi.ingsw.Server.Model;
import it.polimi.ingsw.Parser.ParserManager;
import it.polimi.ingsw.Server.VirtualView.NetInterface;

import java.io.IOException;
import java.util.ArrayList;

/**
 * represents the game. this class is used as root of the model to connect every entity inside of it.
 * @version 1.0
 * @since 1.5
 */
public class Game {

    /** represents the maximum number of workers that a player can have */
    private static final int MAXNUMBUILDERS = 2;

    /** this boolean indicates if the game has ended */
    private boolean gameEnded;

    /** represents the board where the game is played */
    protected Board gameBoard;

    /** represents the list of the players that play in the match */
    protected ArrayList<Player> playerList;

    /** represents the list of the cards used in this match */
    private ArrayList<Card> chosenCards;

    /** represents the deck of all the cards that can be used in a game */
    private final ArrayList<Card> deck;

    /** represents the Challenger of the game's match */
    private Dealer dealer;

    /** represents the player who wins the game */
    private Player winningPlayer;

    /** represents the number of players in the game */
    public final int numberOfPlayers;

    /** represents the rules followed by all the players, regardless of their cards */
    protected BasicRules basic;

    /** represents the interface used by the server to communicate with the clients */
    private final NetInterface netInterface;


    /**
     * creates a new game.
     * @param numberOfPlayers is the number of players that will partecipate in this match.
     */
    public Game(int numberOfPlayers, NetInterface netInterface) {     //da mettere un identificativo partita?
        gameBoard = new Board(netInterface);
        gameEnded = false;
        playerList = new ArrayList<>();
        chosenCards = new ArrayList<>();
        basic = new BasicRules(gameBoard, this);
        winningPlayer = null;
        this.numberOfPlayers=numberOfPlayers;
        this.netInterface = netInterface;

        ParserManager parserManager = new ParserManager();
        parserManager.uploadCards();
        deck = parserManager.getDeck();

    }


    /** gets the list of the players of the game
     * @return is the list of the players playing
     */
    public ArrayList<Player> getPlayerList() {
       return this.playerList;
    }

    /** gets the cards chosen by the Challenger
     * @return is the list of the cards that will be used in this game
     */
    public ArrayList<Card> getChosenCards(){return this.chosenCards;}

    /** gets gameEnded
     * @return is the boolean that indicated if the game has ended
     */
    public boolean getGameEnded(){return this.gameEnded;}

    /**
     * communicates if the game has ended, setting gameEnded
     * @param value If True, indicates that the game has ended.
     */
    public void setGameEnded(boolean value){this.gameEnded = value;}


    /**
     * adds a new player to the game.
     * @param player is the player object.
     * @return 1, if the player has been successifully added, 0 otherwise
     */
    public int addPlayer(Player player){

        if(this.playerList.size() < numberOfPlayers){
            this.playerList.add(player);
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * removes a player from the game.
     * @param player is the player object.
     */
    public void removePlayer(Player player) {
        for (int i = 0; i < player.getBuilderSize(); i++) {
            player.removeBuilder(i);
            i--;
        }
        playerList.remove(player);
    }




    /**
     * is used to get the deck of all the god cards.
     * @return the deck of cards.
     */
    public ArrayList<Card> getDeck() {
        return deck;
    }

    /**
     * is used to get a card from the deck
     * @param cardNumber is the number associated to the card
     * @return the card associated to cardNumber
     */
    public Card getDeckCard(int cardNumber){return deck.get(cardNumber -1);}

    /** used to get the basic rules used in the game
     * @return the basic rules of the game
     */
    public BasicRules getBasic(){
        return basic;
    }



    /**
     * adds the card chosen by the Challenger at the beginning of the game to the list of the available cards that can be chosen by a player.
     * @param cardNumber is the number that identifies the card chosen by the Challenger.
     */
    //i numeri devono essere legittimi
    public void addChosenCard(int cardNumber){
        Card card = deck.get(cardNumber - 1);
        chosenCards.add(card);
    }



    /**
     * puts a new worker on the board.
     * @param player is the player that is deploying the worker.
     * @param placement is the square where the player wants to put the worker.
     * @return the message regarding the outcome of the operation
     */
    public String deployBuilder(Player player, Square placement) throws IOException {
        int x = placement.x;
        int y = placement.y;
        String message;
        if(player.getBuilderSize() == MAXNUMBUILDERS){
            message = "Error:" + player.playerID + "has already deployed all the builders";
        } else {
            if(gameBoard.fullMap[x][y].getValue() != 0)
                message = "Error: Square already occupied by another player";
            else {
                player.addBuilder(placement);   //aggiungi alla lista dei builder del giocatore
                netInterface.updateBoard(placement, null);
                message = "Builder deployed";  //non possiamo lasciare questo
            }
        }

        return message;
    }


    /**
     * set the Challenger of the game
     * @param player is the player that is going to be the challenger
     */
    public void setDealer(Player player){ this.dealer = (Dealer) player; }

    /**
     * get the Challenger of the game
     * @return the Challenger of the game
     */
    public Dealer getDealer(){return this.dealer;}

    /**
     * get the card from the list of cards chosen by the Challenger
     * @param cardPosition is the index of chosenCards where the card is saved
     * @return the card saved at the index of chosenCards
     */
    public Card getChosenCard(int cardPosition){ return chosenCards.get(cardPosition); }

    /**
     * get the dimension of the list of cards chosen by the Challenger
     * @return the number of cards chosen by the challenger
     */
    public int getChosenCardsSize(){return chosenCards.size();}

    /**
     * @return the board where the game is played
     */
    public Board getBoard(){ return this.gameBoard;}   // serve ancora?

    /**
     * @return the basic rules of the game
     */
    public BasicRules getRules(){ return this.basic; }

    /**
     * @return the player who won the game. it is null if nobody has not won yet.
     */
    public Player getWinningPlayer(){return winningPlayer;}

    /**
     * set the winning player of the game
     * @param player is the player to be set as winner
     */
    public void setWinningPlayer(Player player){ this.winningPlayer = player;}
}
