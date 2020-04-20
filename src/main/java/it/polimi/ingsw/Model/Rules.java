package it.polimi.ingsw.Model;

import java.util.ArrayList;

public interface Rules {
    void movement();
    void building();
    ArrayList<Square> getMovementRange();
    ArrayList<Square> getBuildingRange();
    void winCondition();
    void loseCondition();
    public ArrayList<Square> getNext(Builder builder, Player player);
}
