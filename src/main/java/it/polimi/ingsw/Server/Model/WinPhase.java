package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class WinPhase {

    private final Game game;
    private final Board board;
    private final BasicRules basicRules;
    private HashMap<String, Runnable> commands;


    private Card card;
    private Builder builder;
    int levelStart, levelEnd;

    public WinPhase(Game game){
        basicRules = game.getRules();   // assicurarsi che siano sempre le stesse rules
        this.game = game;
        this.board = game.getBoard();
        map();
    }



    public void map(){
        commands = new HashMap<>();
        commands.put("jumpDown", this::jumpDownCondition);
        commands.put("atLeastFiveTowers", this::atLeastFiveTowers);
    }


    public void winCheck(int levelStart, int levelEnd){
        if(levelStart != -1 && levelEnd != -1) {    //magari poi questa la cambio
            this.levelStart = levelStart;
            this.levelEnd = levelEnd;
            commands.get(card.parameters.winPhase).run();
            basicRules.winCondition(levelStart, levelEnd);
        }

    }

    public void jumpDownCondition(){   //Pan
        if(levelStart - levelEnd >= 2)
            game.setGameEnded(true);
    }

    public void atLeastFiveTowers(){    //Crono
        if(board.completedTowers >= 5)
            game.setGameEnded(true);
    }

}
