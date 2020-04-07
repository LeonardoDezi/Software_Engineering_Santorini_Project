package Model;
/*04/04/2020
Ho sistemato move e build e messo dealer come sottoclasse di player. ho spostato deployBuilder in Game e varie altre funzionalità.
non ho ancora sistemato takeCard e non ho toccato la logica dei turni. molti attributi e metodi sono csmbiati rispetto all'Uml originale quindi dobbiamo
cambiarlo.
 */


import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;

import java.util.ArrayList;

/**
 * represents the game. this class is used as root of the model to connect every entity inside of it.
 * @version 1.0
 * @since 1.5
 */
public class Game {
    private Model.Board gameBoard;
    protected ArrayList<Player> playerList;
    private ArrayList<Card> chosenCards;
    private Deck deck;     //lo lasciamo?
    private Dealer dealer;
    private Player player;     // si può togliere?
    // identificatore numerico per differenziare le partite?
    private Square position;
    private BasicRules rules;

    /**
     * creates a new game.
     */
    public Game(){     //da mettere un identificativo partita?
        gameBoard = new Model.Board();
        playerList = new ArrayList<>();
        chosenCards = new ArrayList<>();
        rules = new BasicRules(gameBoard, this);
    }

    /**
     * adds a new player to the game.
     * @param name is the id of the player.
     */
    public void addPlayer(String name, String colour, Game game){
        if(playerList.size() < 3) {

            if(playerList.size()==0)  //ancora nessun giocatore presente
                playerList.add(new Dealer(name, colour, game)); //il primo giocatore sarà sempre il dealer
            else
                playerList.add(new Player(name, colour));
            return;

        }else{
            //TBD
        }
    }

    /**
     * removes a player from the game.
     * @param player is the player id.
     */
    public void removePlayer(Player player){
        playerList.remove(player);
        return;
    }

    // da rifare

    /**
     * is used to get the deck of all the god cards.
     * @return the deck of cards.
     */
    public Deck getDeck() {      // da aggiungere  nell'UML se vogliamo mettere la variabile private
        return deck;
    }

    /**
     * calls the move metod of the board to move a builder.
     * @param playerIndex is the player id of that builder.
     * @param builderId is the id of the builder that is moving.
     * @param x is the x coordinate where the builder is going to move.
     * @param y is the y coordinate where the builder is going to move.
     */
    public void move(int playerIndex, int builderId, int x, int y){
        player = playerList.get(playerIndex);   // recupera giocatore
        gameBoard.move(player.getBuilder(builderId), x, y);
    }

    /**
     * calls the move metod of the board to build something.
     * @param playerIndex is the id of the player that is building something.
     * @param x is the x coordinate of the square where the player wants to build.
     * @param y is the y coordinate of the square where the player wants to build.
     * @param isDome is set to 1 when the player builds a dome that is not on the 4th floor.
     */
    public void build (int playerIndex, int builderId, int x, int y, boolean isDome){
        player = playerList.get(playerIndex);   // recupera giocatore
        gameBoard.build(x, y, isDome);
    }

    /**
     * adds the card chosen by the dealer at the beginning of the game to the list of the available cards for the other players.
     * @param card is the card choosen by the dealer.
     */
    public void addChosenCard(Card card){    // da aggiungere all'UML
        chosenCards.add(card);
        return;
    }


    // arrivati a questo punto siamo certi che la posizione data è possibile?

    /**
     * puts a new builder on the board.
     * @param player is the player that is deploying the builder.
     * @param x is the x coordinate of the square where the player wants to put the builder
     * @param y is the y coordinate of the square where the player wants to put the builder
     */
    public void deployBuilder(Player player, int x, int y){

        this.player = player;
        //if(isPlayerTurn == true) {   serve?
        String colour = player.getColour();

        if(player.getBuilderSize() == 2){   //ATTENZIONE: DOBBIAMO ACCERTARCI CHE size() sarà sempre o 0 o 2 in qualche modo
            System.out.println("Error:" + player.getPlayerID() + "has already deployed all the builders");   //non possiamo lasciare questo
            //    isPlayerTurn = false;   Serve? compito del controller magari?
        } else {
            position = gameBoard.fullMap[x][y];  //mettere fullMap public o private?
            if(position.getValue() == 1)
                System.out.println("Error: Square already occupied by another player");
            else
                player.addBuilder(x, y, position , colour);   //aggiungi alla lista dei builder del giocatore
            System.out.println("Builder deployed");  //non possiamo lasciare questo
        }

    }

}
