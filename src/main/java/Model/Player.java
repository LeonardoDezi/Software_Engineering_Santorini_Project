package Model;

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
    private Builder piece;  // serve solo per getBuilder magari si può togliere
    //private Game game;  //mi sa che non serve

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
        isCardDealer = false;
        isPlayerTurn = false;

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
     * @throws NullPointerException if the player hasnìt already placed it's builders.
     */
    public Builder getBuilder(int BuilderId){

        try {
            piece = builders.get(BuilderId);   //forse lo posso scrivere return ma non sono sicuro
        }catch(NullPointerException e) {
            System.out.println("Error: The player" + playerID + "hasn't deployed his/her pieces yet.");
        }

        return piece;   //forse si può togliere

    }

    public int getBuilderSize(){     // o questo o restituiamo direttamente la lista non sono sicuro
        return builders.size();
    }

    //il controller potrà accedere a size()?
    //come facciamo con il builderid?

    /**
     * assignes the god card choosen by the player to the player class.
     */
    public void takeCard(){/* TBD*/}

    /**
     * adds the newly pla
     * @param x is the x coordinate of the square where the new builder is.
     * @param y is the y
     * @param position
     */
    public void addBuilder(int x, int y, Square position, String colour){
        builders.add(new Builder(x,y, position, colour));
        //return builders.size() - 1;  // se magari vogliamo comunicare il numero della pedina che abbiamo appena messo
    }

    public String getColour(){
        return this.colour;
    }
}
