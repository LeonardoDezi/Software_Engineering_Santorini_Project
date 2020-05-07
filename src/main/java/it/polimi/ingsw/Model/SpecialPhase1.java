package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase1 {

    private Board board;
    private Card card;
    private HashMap<String, Runnable> commands;
    private Rules basicRules;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    public SpecialPhase1(Card card, Rules rules, Board board){
        basicRules = rules;   // sistemare
        this.card = card;
        this.board = board;
        map();
    }

    public void map(){
        commands = new HashMap<>();
        commands.put("Prometeo", () -> prometeo());
        commands.put("Caronte", () -> caronte());
        commands.put("Basic", () ->basic());
    }


    public ArrayList<Square> genericMethod (Builder builder, Card card){

        if(builder == null)   // nel caso di builder non esistente
            return null;

        this.builder = builder;
        this.card = card;
        commands.get(card.specialPhase1).run();
        return possibleMoves;
    }

    public void prometeo(){

        possibleMoves = basicRules.getBuildingRange(builder);
        return;

    }

    public void caronte(){

        possibleMoves = basicRules.proximity(builder);


        for(int i =0; i < possibleMoves.size(); i++)

            if(possibleMoves.get(i).getValue() != 1)   // qualunque casella che non contenga una pedina
                possibleMoves.remove(i);

            else{

                Builder opponent = possibleMoves.get(i).getBuilder();
                Square playerPosition =  builder.getPosition();
                Square opponentPosition = opponent.getPosition();

                // non so come mettere gli square opposti
            }


    }


    public void basic(){
        basicRules.setMaxHeight(1);
        possibleMoves = null;
    }
}
