package it.polimi.ingsw.Model;

import java.util.ArrayList;

public abstract class CardPower implements Rules {
    protected Rules basic;
    protected Card card;
    protected Game game;

    public CardPower(Game game, Card card) {
        this.basic = game.basic;
    }


    @Override
    public ArrayList<Square> getPossibleMoves(Builder builder) {
        return basic.getPossibleMoves(builder);
    }

    @Override
    public ArrayList<Square> getBuildingRange(Builder builder) {
        return null;
    }


    @Override
    public void movement(Square square1, Square square2) {

    }

    @Override
    public void building(int x, int y) {

    }

    @Override
    public void winCondition(Builder builder, int x, int y) {

    }

    @Override
    public void loseCondition() {

    }
}
