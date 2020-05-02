package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class MovementPhase implements Phase {
    private Rules rules;
    private Board board;

    public MovementPhase(Rules rules, Board board){
        this.rules = rules;
        this.board = board;
    }
    @Override
    public ArrayList<Square> getMoves(Player player) {
        Builder builder1 = player.getBuilder(0);
        if(builder1 == null){
            ArrayList<Square> firstBuilderMoves = null;
        }
        else{
            ArrayList<Square> firstBuilderMoves = rules.getPossibleMoves(builder1);
        }
        Builder builder2 = player.getBuilder(1);
        ArrayList<Square> secondBuilderMoves = rules.getPossibleMoves(builder2);

        if(player.card.effects.moveOpponent.equals()){

        }
    }
}
