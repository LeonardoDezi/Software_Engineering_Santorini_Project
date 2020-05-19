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
        this.game=game;

    }

    //serve ancora? si deve vedere come si implementerà Turn
    /**

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
        String sex;

        if(builders.isEmpty())
            sex = "male";
        else
            sex = "female";

        builders.add(new Builder(position, colour, sex));
        //return builders.size() - 1;  // se magari vogliamo comunicare il numero della pedina che abbiamo appena messo
    }



    //accertarci che il builder esista sempre
    public void removeBuilder(int builderId){

        Square position = builders.get(builderId).getPosition();
        position.resetSquare();
        builders.remove(builderId);

    }

    public String getColour(){
        return this.colour;
    }



    public Builder getFemale(){
        Builder builder = null;
        for (Builder value : builders)
            if (value.sex == "female")
                builder = value;
        return builder;
    }

}
