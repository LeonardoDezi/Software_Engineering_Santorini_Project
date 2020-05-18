package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;

public class Envelope {
    private ArrayList<Square> movesList;
    private Builder builder;
    private Square move = null;
    private boolean isDome = false;

    public Envelope(ArrayList<Square> moves, Builder builder){
        this.movesList = moves;
        this.builder = builder;
    }

    public ArrayList<Square> getMovesList(){return movesList;}

    public Builder getBuilder(){return builder;}

    public Square getMove(){ return move;}

    public void setMove(Square move){ this.move = move;}

    public boolean getIsDome(){return this.isDome;}

    public void setIsDome(boolean isDome){this.isDome = isDome;}

}
