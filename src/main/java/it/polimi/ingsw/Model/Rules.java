package it.polimi.ingsw.Model;

import java.util.ArrayList;

public interface Rules {
    void movement(Builder builder, int x ,int y);
    void building(int x, int y);
    ArrayList<Square> getPossibleMoves(Builder builder);
    ArrayList<Square> getBuildingRange(Builder builder);
    void winCondition(Builder builder, int x ,int y);
    void loseCondition();
}
