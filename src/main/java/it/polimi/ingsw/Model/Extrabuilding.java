package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Extrabuilding extends CardPower {

    public Extrabuilding(Game game, Card card) {
        super(game, card);
    }

    @Override //Per samequare
    public ArrayList<Square> getBuildingRange(Builder builder) {

        ArrayList<Square> possibleBuilds = proximity(builder);
        possibleBuilds = removeBuilderSquare(possibleBuilds);
        possibleBuilds = removeDomeSquare(possibleBuilds);

        return possibleBuilds;

    }
}
