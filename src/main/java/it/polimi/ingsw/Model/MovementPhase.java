package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class MovementPhase extends Phase {
    private Rules rules;
    private Board board;

    public MovementPhase(Rules rules, Board board) {
        this.rules = rules;
        this.board = board;
    }

    @Override
    public ArrayList<Square> getMoves(Builder builder) {
        if( /*carta = */){

        }

        ArrayList<Square> builderMoves = rules.getPossibleMoves(builder);
        return builderMoves;
    }
}