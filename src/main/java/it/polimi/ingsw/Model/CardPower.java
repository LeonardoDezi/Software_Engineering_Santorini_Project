package it.polimi.ingsw.Model;

import java.util.ArrayList;

public abstract class CardPower implements Rules {
    protected Rules rules;

    @Override
    public ArrayList<Square> getPossibleMoves(Builder builder) {
        return rules.getPossibleMoves(builder);
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
