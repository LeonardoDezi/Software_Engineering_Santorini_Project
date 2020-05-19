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
    public static final String SEX1 = "Male";
    public static final String SEX2 = "Female";
    protected Card card;  //perchè protected?
    public final String playerID;
    protected ArrayList<Builder> builders;
    public final String colour;
    protected final Game game;   // perchè protected?
    public final int clientID;


    /**
     * creates a new player and the array list that is used to save the players builders.
     * @param playerID is the id that identifies the player.
     */
    public Player(String playerID, String colour, Game game, int clientID) {

        this.playerID = playerID;
        this.colour = colour;
        this.clientID = clientID;
        builders = new ArrayList<>();
        this.game=game;

    }


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
     * is used to assign the builders to the player.
     * @param BuilderId is the id of one of the two builders.
     * @return the variable containing the id of the builder.
     * @throws IndexOutOfBoundsException if the player hasn't placed its builders yet.
     */


    //DA TESTARE
    public Builder getBuilder(int BuilderId) throws ArrayIndexOutOfBoundsException{

        if(BuilderId >= builders.size())
            throw new ArrayIndexOutOfBoundsException();
        else
            return builders.get(BuilderId);

    }



    public int getBuilderSize(){
        return builders.size();
    }



    /**
     * adds the new builder to builderList
     * @param position are the coordinates of the square where the new builder is.
     */
    public void addBuilder(Square position){
        String sex;

        if(builders.isEmpty())
            sex = SEX1;
        else
            sex = SEX2;

        builders.add(new Builder(position, colour, sex));
        //return builders.size() - 1;  // se magari vogliamo comunicare il numero della pedina che abbiamo appena messo
    }

//

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
            if (value.sex.equals(SEX2))
                builder = value;
        return builder;
    }

}
