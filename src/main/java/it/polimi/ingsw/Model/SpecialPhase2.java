package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase2 {
    private Board board;
    private Card card;
    private HashMap<String, Runnable> commands;
    private Rules basicRules;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    public SpecialPhase2(Board board){
        this.board = board;
        map();
    }

    public void map(){
        commands = new HashMap<>();
        commands.put("doubleMove", () -> artemis());
    }

    public void artemis(){

    }
}
