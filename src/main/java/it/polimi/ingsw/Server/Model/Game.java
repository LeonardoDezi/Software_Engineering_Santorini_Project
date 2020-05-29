package it.polimi.ingsw.Server.Model;
import it.polimi.ingsw.Parser.ParserManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * represents the game. this class is used as root of the model to connect every entity inside of it.
 * @version 1.0
 * @since 1.5
 */
public class Game {
    //rivedere tutti i private, protected e public
    private static final int MAXNUMBUILDERS = 2;
    private boolean gameEnded;
    protected Board gameBoard;
    protected ArrayList<Player> playerList;
    private ArrayList<Card> chosenCards;
    private final ArrayList<Card> deck;
    private Dealer dealer;
    private Player winningPlayer;
                           // identificatore numerico per differenziare le partite?
    public final Integer numberOfPlayers;
    protected BasicRules basic;

    /**
     * creates a new game.
     */
    public Game(int numberOfPlayers) {     //da mettere un identificativo partita?
        gameBoard = new Board();
        gameEnded = false;
        playerList = new ArrayList<>();
        chosenCards = new ArrayList<>();
        basic = new BasicRules(gameBoard, this);
        winningPlayer = null;
        this.numberOfPlayers=numberOfPlayers;

        ParserManager parserManager = new ParserManager();
        parserManager.uploadCards();
        deck = parserManager.getDeck();

    }


    public ArrayList<Player> getPlayerList() {
       return this.playerList;
    }

    public ArrayList<Card> getChosenCards(){return this.chosenCards;}

    public boolean getGameEnded(){return this.gameEnded;}

    public void setGameEnded(boolean value){this.gameEnded = value;}

    /**
     * adds a new player to the game.
     * @param player is the player object.
     */


    //dealer?    // non controlla se due player abbiano clientID uguali
    public Integer addPlayer(Player player){

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
     * @param player is the player id.
     */
    public void removePlayer(Player player) {     //non consideriamo il caso in cui il giocatore non sia presente nella lista
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

    public Card getDeckCard(int cardNumber){return deck.get(cardNumber -1);}

    public BasicRules getBasic(){
        return basic;
    }



    /**
     * adds the card chosen by the dealer at the beginning of the game to the list of the available cards for the other players.
     * @param cardNumber is the number that identifies the card chosen by the dealer.
     */
    //i numeri devono essere legittimi
    public void addChosenCard(int cardNumber){
        Card card = deck.get(cardNumber - 1);
        chosenCards.add(card);
    }



    /**
     * puts a new builder on the board.
     * @param player is the player that is deploying the builder.
     * @param placement is the square where the player wants to put the builder
     */
    public String deployBuilder(Player player, Square placement){
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
                message = "Builder deployed";  //non possiamo lasciare questo
            }
        }

        return message;
    }


    //TODO verificare che non dia problemi
    public void setDealer(Player player){ this.dealer = (Dealer) player; }

    public Dealer getDealer(){return this.dealer;}


    public Card getChosenCard(int cardPosition){ return chosenCards.get(cardPosition); }

    public int getChosenCardsSize(){return chosenCards.size();}

    public Board getBoard(){ return this.gameBoard;}   // serve ancora?

    public BasicRules getRules(){ return this.basic; }

    public Player getWinningPlayer(){return winningPlayer;}

    public void setWinningPlayer(Player player){ this.winningPlayer = player;}
}
