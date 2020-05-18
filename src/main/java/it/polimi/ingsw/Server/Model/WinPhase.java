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
    private Square initialPosition, position;

    private Player player;
    private Player participant;

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

    public void checkBuild(Player player){
        this.player = player;
        for(Player participant : game.playerList){
            this.participant = participant;
            commands.get(participant.card.parameters.winBuilding).run();
            if(game.getGameEnded())
                game.setWinningPlayer(participant);
        }
    }

    public void checkMovement(Player player, Square initialPosition, Square position){
            this.player = player;
            this.initialPosition = initialPosition;
            this.position = position;
        for(Player participant : game.playerList){
            this.participant = participant;
            commands.get(participant.card.parameters.winBuilding).run();
            if(game.getGameEnded())
                game.setWinningPlayer(participant);
        }
    }

    public void jumpDownCondition(){   //Pan
        if(player.equals(participant)) {
            int levelStart = initialPosition.getLevel();
            int levelEnd = position.getLevel();
            if (levelStart - levelEnd >= 2)
                game.setGameEnded(true);
        }
    }

    public void atLeastFiveTowers(){    //Crono
        if(board.completedTowers >= 5)
            game.setGameEnded(true);
    }

}
