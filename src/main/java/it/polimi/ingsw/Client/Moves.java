package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Square;

import java.util.ArrayList;

public class Moves {
    private ArrayList<Square> moves1;
    private ArrayList<Square> moves2;
    private Builder builder1;
    private  Builder builder2;
    private boolean isDome;
    private boolean female;

    public Moves(Builder builder1, ArrayList<Square> moves1, Builder builder2, ArrayList<Square> moves2, boolean isDome, boolean female){
        this.builder1 = builder1;
        this.builder2 = builder2;
        this.moves1 = moves1;
        this.moves2 = moves2;
        setIsDome(isDome);
        this.female = female;
    }

    public Builder getBuilder1(){return builder1;}

    public Builder getBuilder2(){return builder2;}

    public ArrayList<Square> getMove1(){ return moves1;}

    public ArrayList<Square> getMove2(){ return moves2;}


    public boolean getIsDome(){return this.isDome;}

    public void setIsDome(boolean isDome){this.isDome = isDome;}
}
