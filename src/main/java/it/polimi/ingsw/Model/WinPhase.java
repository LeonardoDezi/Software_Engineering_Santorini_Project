package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class WinPhase {

    private Board board;
    private Card card;
    private HashMap<String, Runnable> commands;
    private Rules basicRules;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    public WinPhase(Card card, Rules rules, Board board){
        basicRules = rules;   // assicurarsi che siano sempre le stesse rules
        this.card = card;
        this.board = board;
        map();
    }

    public void map(){
        commands = new HashMap<>();
        commands.put("jump-down", () -> pan());
        commands.put("towerCount", () -> towerCount());
    }


    public void winCondition(){
        commands.get(Card.winPhase).run();
        basicRules.winCondition();

    }

    public void pan(){
        // you also win if you Worker moves down two or more levels
    }
    public void towerCount(){

    }
    //come mettere gameEnded uguale a true?
}
