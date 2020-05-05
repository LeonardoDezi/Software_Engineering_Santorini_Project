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

    public SpecialPhase1(Board board){
        this.board = board;
        map();
    }

    public void map(){
        commands = new HashMap<>();
        commands.put("Prometeo", () -> prometeo());
        commands.put("Caronte", () -> caronte());
        commands.put("Basic", () -> return null;);
    }


    public ArrayList<Square> genericMethod (Builder builder, Card card){
        this.builder = builder;
        this.card = card;
        commands.get(card.specialPhase1).run();
        return possibleMoves;
    }

    public void prometeo(){
        possibleMoves = basicRules.getBuildingRange(builder);
        return;

    }
}
