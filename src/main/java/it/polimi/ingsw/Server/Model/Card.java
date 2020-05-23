package it.polimi.ingsw.Server.Model;


public class Card {
    private Integer number;
    public String name;
    public CardParameters[] parameters;

    public int getNumber(){
        return number;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public CardParameters[] getParameters(){
        return parameters;
    }

    public void setParameters(CardParameters[] parameters){
        this.parameters = parameters;
    }

}
