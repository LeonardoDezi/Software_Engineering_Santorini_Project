package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.View.CLI.Colour;

import java.util.HashMap;

public class Cell {

    private int value;

    private int x;

    private int y;

    private int level;

    private Pawn pawn;
    
    private HashMap<Integer, String> buildings;
    
    private HashMap<Integer, String> levels;

    private String r ;

    public Cell(int i, int j){
        this.x = i;
        this.y = j;
        this.level = 0;
        this.pawn = null;
        this.value = 0;
        maps();
    }

    public String drawCell(int i, int j){


        Cell z = ClientBoard.getCell(i,j);

        if( z.value == 0){
            r = buildings.get(level);
            return r;
        }

        if ( value == 1){
            r = pawn.getColours().get(pawn.getColour()) + pawn.getGender().get(pawn.getSex()) + levels.get(level) ;
            return r;

        }

        if(z.value == 2){
            r = buildings.get(4) + Colour.ANSI_BLUE;
            return r;
        }

        return r;

    }

    public void maps() {
        buildings = new HashMap<>();
        levels = new  HashMap<>();

        buildings.put(0," ");
        buildings.put(1,"\u2460");
        buildings.put(2,"\u2461");
        buildings.put(3,"\u2462");
        buildings.put(4, "\u23fa");

        levels.put(0,"\u2080");
        levels.put(1,"\u2081");
        levels.put(2,"\u2082");
        levels.put(3,"\u2084");

    }

    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

    public void setValue(int squareValue){
        this.value = squareValue;
    }

    public void setLevel(int squareLevel){
        this.level = squareLevel;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public void setPawn(Pawn pawn){
        this.pawn = pawn;
    }
}
