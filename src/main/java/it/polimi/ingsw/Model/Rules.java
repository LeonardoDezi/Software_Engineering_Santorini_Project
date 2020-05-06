package it.polimi.ingsw.Model;

import java.util.ArrayList;


// secondo me questa la possiamo eliminare
public interface Rules {
    void movement(Square square1, Square square2);
    void building(int x, int y);
    ArrayList<Square> getPossibleMoves(Builder builder);
    ArrayList<Square> getBuildingRange(Builder builder);
    ArrayList<Square> proximity(Builder builder);
    ArrayList<Square> removeBuilderSquare(ArrayList<Square> proximity);
    ArrayList<Square> removeDomeSquare(ArrayList<Square> proximity);
    ArrayList<Square> removeTooHighPlaces(ArrayList<Square> proximity, Builder builder);
    void winCondition(Builder builder, int x ,int y);
    void loseCondition();
    public void setMaxHeight(int maxHeight);
}
