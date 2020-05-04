package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;

import java.util.ArrayList;

public class TurnManager {
    private Player currentPlayer;
    private Game game;
    private ArrayList<Player> playerList;
    public boolean gameEnded = false;
    private SpecialPhase1 specialPhase1;
    private SpecialPhase2 specialPhase2;
    private SpecialPhase3 specialPhase3;
    private MovementPhase movementPhase;
    private BuildingPhase buildingPhase;

    public TurnManager(Game game){
        this.game = game;
        this.playerList = game.getPlayerList();
        specialPhase1 = new SpecialPhase1();
        specialPhase2 = new SpecialPhase2();
        specialPhase3 = new SpecialPhase3();
        buildingPhase = new BuildingPhase();
        movementPhase = new MovementPhase(game.getRules(), game.getBoard());
    }

    public void letsPlay(){
        Builder builder1;
        Builder builder2;
        while(!gameEnded){
            for (int i=0; i < playerList.size(); i++) {
                currentPlayer=playerList.get(i);
                builder1=currentPlayer.getBuilder(0);
                builder2=currentPlayer.getBuilder(1);
                //special phase 1
                ArrayList<Square> moves1 = specialPhase1.getMoves(builder1);
                ArrayList<Square> moves2 = specialPhase1.getMoves(builder2);
                if( moves1 != null || moves2 != null){

                    //chiedere al player se vuole fare mossa speciale

                }
//movement phase
                moves1 = movementPhase.getMoves(builder1);
                moves2 = movementPhase.getMoves(builder2);

                if (moves1 == null && moves2 == null){

                    //lost
                }





                if(gameEnded){
                    break;
                }
            }
        }
        /* while(!gameEnded)
    for(player in players)
        doSomething(); */
    }

    public void addPlayers(Player player){

    }

}
