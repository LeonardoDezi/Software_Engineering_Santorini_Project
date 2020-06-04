package it.polimi.ingsw.Server.Model;

import java.util.HashMap;

/**
 * This class is used to verify if the move made results in the player's victory
 */
public class WinPhase {
    /** represents the game*/
    private final Game game;
    /** represents the board of the game */
    private final Board board;
    /** represents the hashMap containing the keys related to the methods checkMovement() and checkBuild() */
    private HashMap<String, Runnable> commands;
    /** represents the position where the playing builder used to be */
    private Square initialPosition;
    /** represents the position where the playing builder has been moved */
    private Square position;
    /** represents the player who made the move */
    private Player player;
    /** represents the player who is currently being examined */
    private Player participant;

    /** creates a new winPhase
     * @param game represents the game
     */
    public WinPhase(Game game){
        this.game = game;
        this.board = game.getBoard();
        map();
    }


    /** initializes the hashMap related to winPhase */
    public void map(){
        commands = new HashMap<>();
        commands.put(null, ()->{});
        commands.put("jumpDown", this::jumpDownCondition);
        commands.put("atLeastFiveTowers", this::atLeastFiveTowers);
    }

    /** checks if the building move just performed results in a player's victory
     * @param player is the player who made the building move
     */
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

    /**
     * checks if the movement just performed results in a player's victory
     * @param player is the player who made the movement
     * @param initialPosition is the place where the playing worker used to be before the movement
     * @param position is the place where the playing worker currently is.
     */

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

    /**
     * If the player moved its worker down two or more levels, this method sets the player as the winner
     */
    public void jumpDownCondition(){   //Pan
        if(player.equals(participant)) {
            int levelStart = initialPosition.getLevel();
            int levelEnd = position.getLevel();
            if (levelStart - levelEnd >= 2)
                game.setGameEnded(true);
        }
    }

    /**
     * If there are at least five complete towers on the board, this method sets the player who calls it as the winner
     * one, even if the player didn't make the winning move
     */
    public void atLeastFiveTowers(){    //Crono
        if(board.completedTowers >= 5) {
            game.setGameEnded(true);
            game.setWinningPlayer(participant);
        }
    }

}
