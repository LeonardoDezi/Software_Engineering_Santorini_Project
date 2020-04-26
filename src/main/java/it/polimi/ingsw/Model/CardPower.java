package it.polimi.ingsw.Model;

import java.util.ArrayList;

public abstract class CardPower implements Rules {
    protected Rules basic;    //al momento sto mettendo gli attributi private e protected a cazzo
    protected Card card;
    protected Game game;     // al momento li sto mettendo a cazzo

    public CardPower(Game game, Card card) {
        this.basic = game.basic;
    }



    //e sti metodi BOOOOOH
    @Override
    public ArrayList<Square> getPossibleMoves(Builder builder) {
        return basic.getPossibleMoves(builder);
    }

    @Override
    public ArrayList<Square> getBuildingRange(Builder builder) {
        return null;
    }


    @Override
    public void movement(Builder builder, int x, int y) {

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
