package it.polimi.ingsw.Server.Model;

import java.util.HashMap;

public class WinPhase {

    private final Game game;
    private final Board board;
    private HashMap<String, Runnable> commands;

    private Square initialPosition, position;

    private Player player;
    private Player participant;

    public WinPhase(Game game){
        this.game = game;
        this.board = game.getBoard();
        map();
    }



    public void map(){
        commands = new HashMap<>();
        commands.put(null, ()->{});
        commands.put("jumpDown", this::jumpDownCondition);
        commands.put("atLeastFiveTowers", this::atLeastFiveTowers);
    }

    public void checkBuild(Player player){
        this.player = player;
        for(Player participant : game.playerList){
            this.participant = participant;
            commands.get(participant.card.parameters.winBuilding).run();
            if(game.getGameEnded()) {
                break;
            }
        }
    }

    //non controlla che ci siano pedine o cupole negli Square, nè se ci sia la pedina giocante coinvolta
    public void checkMovement(Player player, Square initialPosition, Square position){
            this.player = player;
            this.initialPosition = initialPosition;
            this.position = position;
        for(Player participant : game.playerList){
            this.participant = participant;
            commands.get(participant.card.parameters.winMovement).run();
            if(game.getGameEnded()) {
                game.setWinningPlayer(participant);
                break;
            }
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
        if(board.completedTowers >= 5) {
            game.setGameEnded(true);
            game.setWinningPlayer(participant);
        }
    }

}
