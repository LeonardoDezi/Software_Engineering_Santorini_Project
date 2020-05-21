package it.polimi.ingsw.Parser;

import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Square;

import java.util.ArrayList;


public class Message {

    private ArrayList<Square> moves1;
    private ArrayList<Square> moves2;
    private Builder builder1;
    private Builder builder2;
    private boolean isDome;



    public void setMoves(){
        this.moves1 = moves1;
        this.moves2 = moves2;
    }

    public void setBuilder(){
        this.builder1 = builder1;
        this.builder2 = builder2;
    }

}
