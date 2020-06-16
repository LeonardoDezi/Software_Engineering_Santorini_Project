package it.polimi.ingsw.Server.Model;

/**
 * represents a single card.
 * @version 1.5
 * @since 1.0
 */
public class Card {
    /** represents the number associated to the card */
    private int number;

    /** represents the name of the card */
    public String name;

    /** represents the parameters associated to the card */
    protected CardParameters parameters;

    /** represents a descriptions of the effects given by the card */
    private String description;

    /** is used to get the number of the card
     * @return the number of the card
     */
    public int getNumber(){
        return number;
    }

    /** is used to get the name of the card
     * @return the name of the card
     */
    public String getName(){
        return name;
    }

    /** is used to get the parameters of the card
     * @return the parameters of the card
     */
    public CardParameters getParameters(){
        return parameters;
    }

    /** is used to get the description of the card
     * @return the description of the card
     */
    public String getDescription(){
        return description;
    }

    /** is used to set the description of the card */
    public void setDescription(String description) {
        this.description = description;
    }

    public void setNumber(Integer number){
        this.number = number;
    }
}
