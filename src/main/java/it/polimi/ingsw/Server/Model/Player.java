package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;

//decidere cosa fare per removeBuilder
//QUIII
/**
 * represents a player.
 * @version 1.5
 * @since 1.0
 */
public class Player {
    private boolean isPlayerTurn;
    protected Card card;  //perchè protected?
    public final String playerID;   // secondo me possiamo metterlo public e cancellare getPlayerID
    protected ArrayList<Builder> builders;   // perchè  protected?
    public final String colour;
    protected Game game;   // perchè protected?
    protected BasicRules rules; //QUIII
    public final int clientID;


    /**
     * creates a new player and the array list that is used to save the players builders.
     * @param playerID is the id that identifies the player.
     */
    public Player(String playerID, String colour, Game game,int clientID) {

        this.playerID = playerID;
        this.colour = colour;
        this.clientID = clientID;
        builders = new ArrayList<>();
        isPlayerTurn = false;    // servirà mai?
        this.game=game;

    }

    //serve ancora? si deve vedere come si implementerà Turn
    /**
     * is used to keep track if is the player's turn or not.
     * @param isPlayerTurn is set to 1 if is the player's turn, to 0 if it isn't.
     */
    public void setIsTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }  // aggiungere il parametro nell'UML

    /**
     * assigns the God card chosen by the player to the player class.
     */
    public void chooseCard(int cardNumber){
        this.card = game.getChosenCard(cardNumber);
    }   // non lo so come soluzione non mi convince completamente

    public Card getCard(){
        return this.card;
    }

    /**
     * is used to know the player id.
     * @return a string containing the id of the player.
     */
    public String getPlayerID() {
        return playerID;
    }

    /**
     * is used to assign the builders to the player.
     * @param BuilderId is the id of one of the two builders.
     * @return the variable containing the id of the builder.
     * @throws IndexOutOfBoundsException if the player hasn't placed its builders yet.
     */
    public Builder getBuilder(int BuilderId){    //non so se mettere come builderId 0/1 o 1/2 o magari mettere enumerazioni. il problema delle enum è che non cambiano quando un player viene rimosso

        Builder piece;
        try {
            piece = builders.get(BuilderId);   //forse lo posso scrivere return ma non sono sicuro
        }catch(IndexOutOfBoundsException e) {
            System.out.println("Error: The player " + playerID + " hasn't deployed his/her pieces yet or this piece has been removed");
            return null;
        }

        return piece;   //forse si può togliere

    }

    public int getBuilderSize(){     // o questo o restituiamo direttamente la lista non sono sicuro
        return builders.size();
    }

    //il controller potrà accedere a size()?
    //come facciamo con il builderid?


    /**
     * adds the new builder to builderList
     * @param position are the coordinates of the square where the new builder is.
     */
    public void addBuilder(Square position){
        builders.add(new Builder(position, colour));
        //return builders.size() - 1;  // se magari vogliamo comunicare il numero della pedina che abbiamo appena messo
    }




    public void removeBuilder(int builderId){
        try {
            Square position = builders.get(builderId).getPosition();
            position.resetSquare();
            builders.remove(builderId);
        }catch(IndexOutOfBoundsException e) {
            System.out.println("The builder " + builderId + "doesn't exist");
        }
    }

}
