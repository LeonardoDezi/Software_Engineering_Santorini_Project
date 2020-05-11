package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class WinPhase {

    private final Game game;
    private final Board board;
    private final Rules basicRules;
    private HashMap<String, Runnable> commands;


    private Card card;
    private Builder builder;

    public WinPhase(Game game){
        basicRules = game.getRules();   // assicurarsi che siano sempre le stesse rules
        this.game = game;
        this.board = game.getBoard();
        map();
    }



    public void map(){
        commands = new HashMap<>();
        commands.put("jump-down", this::pan);
        commands.put("towerCount", this::towerCount);
    }


    public void winCondition(){
        commands.get(card.winPhase).run();
        basicRules.winCondition();

    }

    public void pan(){
        // you also win if you Worker moves down two or more levels
    }
    public void towerCount(){

    }
    //come mettere gameEnded uguale a true?
}
