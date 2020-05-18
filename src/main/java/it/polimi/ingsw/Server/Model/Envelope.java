package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;

public class Envelope {
    private Builder builder;
    private Square move = null;
    private boolean isDome = false;

    public Envelope(Builder builder, Square move){
        this.builder = builder;
        this.move = move;
    }



    public Builder getBuilder(){return builder;}

    public Square getMove(){ return move;}

    public void setMove(Square move){ this.move = move;}

    public boolean getIsDome(){return this.isDome;}

    public void setIsDome(boolean isDome){this.isDome = isDome;}

}
