package it.polimi.ingsw.Server.Model;


public class Card {
    private Integer number;
    public String name;
    protected CardParameters parameters;

    public int getNumber(){
        return number;
    }

    public String getName(){
        return name;
    }

    public CardParameters getParameters(){
        return parameters;
    }

}
