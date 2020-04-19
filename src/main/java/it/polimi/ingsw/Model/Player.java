package it.polimi.ingsw.Model;

import java.util.ArrayList;


/**
 * represents a player.
 * @version 1.5
 * @since 1.0
 */
public class Player {
    private boolean isPlayerTurn;
    //elemento di tipo carta
    private String playerID;
    protected ArrayList<Builder> builders;
    private boolean isCardDealer;
    public final String colour;

    /**
     * creates a new player and the array list that is used to save the players builders.
     * @param playerID is the id that identifies the player.
     */
    public Player(String playerID, String colour /*, Game game*/) {

        this.playerID = playerID;
        this.colour = colour;
        //this.game = game;
        builders = new ArrayList<>();
        //come fare colour?
        isCardDealer = false;    // serve ancora??
        isPlayerTurn = false;    // servirà mai?

    }

    //serve ancora? si deve vedere come si implementerà Turn
    /**
     * is used to keep track if is the player's turn or not.
     * @param isPlayerTurn is set to 1 if is the player's turn, to 0 if it isn't.
     */
    public void setIsTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }  // aggiungere il parametro nell'UML


    //metodo getcard

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
     * @throws IndexOutOfBoundsException if the player hasnìt already placed it's builders.
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
     * assignes the God card choosen by the player to the player class.
     */
    public void takeCard(){/* TBD*/}

    /**
     * adds the new builder to builderList
     * @param position are the coordinates of the square where the new builder is.
     */
    public void addBuilder(Square position){
        builders.add(new Builder(position, colour));
        //return builders.size() - 1;  // se magari vogliamo comunicare il numero della pedina che abbiamo appena messo
    }

    // forse conviene togliere entrambi insieme
    public void removeBuilder(int builderId){
        try {
            builders.remove(builderId);
        }catch(IndexOutOfBoundsException e) {
            System.out.println("The builder " + builderId + " has already been removed");
        }
    }

    public String getColour(){
        return this.colour;
    }
}
