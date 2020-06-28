package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;

//TODO decidere cosa fare per removeBuilder

/**
 * represents a player.
 * @version 1.5
 * @since 1.0
 */
public class Player {
    /** represents the first sex that a worker can have */
    public static final String SEX1 = "Male";

    /** represents the second sex that a worker can have */
    public static final String SEX2 = "Female";

    /** represents the card chosen by the player */
    protected Card card = null;

    /** represents the name chosen by the user for playing */
    public final String playerID;

    /** represents the list of workers associated to the player */
    protected ArrayList<Builder> builders;

    /** represents the colour associated to the player */
    public final String colour;

    /** represents the game that the player is playing */
    protected final Game game;

    /** represents the id of the player, used to identify it in an absolute way, compared to the other players */
    public final int clientID;



    /**
     * creates a new player and the array list that is used to save the player's workers.
     * @param playerID is the name that identifies the player.
     * @param colour is the colour that will be associated to the player
     * @param game is the game that the player is playing
     * @param clientID is the id that will be associated to the player
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
     * @param possibleCards represents the list of cards from which the player can choose
     * @param cardPosition represents the position of the card inside the arrayList possibleCards
     * @return the list of cards that can still be chosen by the other players
     */

    public ArrayList<Card> chooseCard(ArrayList<Card> possibleCards, int cardPosition){   //cardPosition è la posizione della carta nell'array
        if(card == null) {
            this.card = possibleCards.get(cardPosition);
            possibleCards.remove(cardPosition);
        }

        return possibleCards;
    }

    /** is used to get the card associated to the player
     * @return is the card of the player
     */
    public Card getCard(){
        return this.card;
    }

    /** is used to set the card associated to the player
     * @param card is the card that will be associated to the player
     */
    public void setCard(Card card){this.card = card;}


    /**
     * is used to get a worker of the player.
     * @param BuilderId is the position of the worker inside the ArrayList builders.
     * @return the worker requested
     * @throws IndexOutOfBoundsException if there is no worker saved in builders, at the index represented by builderId.
     */
    //TODO magari modificarlo per IndexOutOfBounds e non ARRAYIndexOuTOfBounds
    public Builder getBuilder(int BuilderId) throws ArrayIndexOutOfBoundsException{

        if(BuilderId >= builders.size())
            throw new ArrayIndexOutOfBoundsException();
        else
            return builders.get(BuilderId);

    }


    /** is used to get the number of workers associated to the player.
     * @return the dimension of the arraylist builders.
     */
    public int getBuilderSize(){
        return builders.size();
    }



    /**
     * adds a new worker to the arraylist builders.
     * If the new worker is the first one associated to the player, its sex will be SEX1, otherwise SEX2
     * @param position is the square where the new border will be.
     */
    public void addBuilder(Square position){
        String sex;

        if(builders.isEmpty())
            sex = SEX1;
        else
            sex = SEX2;

        builders.add(new Builder(position, colour, sex));

    }

    /**
     * removes a worker from the list builders and from the square that contained it
     * @param builderId is the index of the list builders where the worker is saved
     */

    public void removeBuilder(int builderId){

        Square position = builders.get(builderId).getPosition();
        position.resetSquare();
        builders.remove(builderId);

    }

    /** is used to get the colour associated to the player
     * @return the colour of the player
     */
    public String getColour(){
        return this.colour;
    }

    /** is used to get the female worker associated to the player.
      * @return the female worker. Returns null if there is no female worker.
     */
    public Builder getFemale(){
        Builder builder = null;
        for (Builder value : builders)
            if (value.sex.equals(SEX2))
                builder = value;
        return builder;
    }

}
