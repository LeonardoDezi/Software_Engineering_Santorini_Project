package it.polimi.ingsw.Server.Model;


public class Card {
    private Integer number;
    public String name;
    protected CardParameters parameters;
    private String description;

    public int getNumber(){
        return number;
    }

    public String getName(){
        return name;
    }

    public CardParameters getParameters(){
        return parameters;
    }

    public String getDescription( ){
        return description;
    }

}
