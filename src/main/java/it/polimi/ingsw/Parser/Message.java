package it.polimi.ingsw.Parser;

import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Square;

import java.util.ArrayList;


public class Message {

    private int id;
    private ArrayList<Square> moves;
    private Builder builder;

    public int getId(){
        return id;
    }

    public void setId(){
        this.id = id;
    }

    public void setMoves(){
        this.moves = moves;
    }

    public void setBuilder(){
        this.builder = builder;
    }

}
