package it.polimi.ingsw.Model;

import java.util.ArrayList;

public abstract class CardPower implements Rules {
    protected Rules rules;

    @Override
    public abstract ArrayList<Square> getNext(Builder builder, Player player);

}
