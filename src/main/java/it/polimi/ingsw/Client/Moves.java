package it.polimi.ingsw.Client;

import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Square;

import java.io.BufferedReader;
import java.util.ArrayList;

public class Moves {
    private ArrayList<Square> moves1;
    private ArrayList<Square> moves2;
    private Builder builder1;
    private  Builder builder2;
    private boolean isDome;
    private boolean female;
    private boolean skippable;
    private boolean update;

    /**
     * class used to comunicate to the client all the available moves of the actual phase.
     * @param builder1 is the first builder.
     * @param moves1 all the places where the first builder can move or build.
     * @param builder2 is the second builder.
     * @param moves2 all the places where the second builder can move or build.
     * @param isDome boolean that indicates if the player can build a dome thanks to a god power.
     * @param female to indicate that the female worker has a special effect.
     */
    public Moves(Builder builder1, ArrayList<Square> moves1, Builder builder2, ArrayList<Square> moves2, boolean isDome, boolean female){
        this.builder1 = builder1;
        this.builder2 = builder2;
        this.moves1 = moves1;
        this.moves2 = moves2;
        setIsDome(isDome);
        this.female = female;
    }

    /**
     * returns the first builder of the class.
     * @return the first builder of the class.
     */
    public Builder getBuilder1(){return builder1;}

    /**
     * sets the informations of the first builder of the class.
     * @param builder1 the builder that is going to be set as first builder.
     */
    public void setBuilder1(Builder builder1){
        this.builder1 = builder1;
    }

    public Builder getBuilder2(){return builder2;}

    public void setBuilder2(Builder builder2){
        this.builder2 = builder2;
    }

    public ArrayList<Square> getMoves1(){ return moves1;}

    public void setMoves1(ArrayList<Square> moves1){
        this.moves1 = moves1;
    }

    public ArrayList<Square> getMoves2(){ return moves2;}

    public void setMoves2(ArrayList<Square> moves2){
        this.moves2 = moves2;
    }

    public boolean getIsDome(){return isDome;}

    public void setIsDome(boolean isDome){this.isDome = isDome;}

    public boolean getFemale(){
        return female;
    }

    public void setFemale(boolean female){
        this.female = female;
    }

    public void setSkippable(boolean skippable){
        this.skippable=skippable;
    }

    public void setUpdate(boolean update){
        this.update=update;
    }

    public boolean getUpdate(){
        return update;
    }

    public boolean getSkippable(){
        return skippable;
    }
}
