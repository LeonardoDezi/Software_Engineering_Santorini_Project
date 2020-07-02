package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.View.CLI.Colour;

import java.util.HashMap;

/**
 * represents a single cell inside the ClientBoard.
 */
public class Cell {

    /** represents what is on the top of the cell. Blank space = 0, Worker = 1, Dome = 2 */
    private int value;

    /** represents the x coordinate of the cell */
    private int x;

    /** represents the y coordinate of the cell*/
    private int y;

    /**If the cell does not contain a pawn, level will represent the number of constructions placed in the cell,
     * otherwise it represents the level where the pawn is placed.
     * Ground Level = 0, First Level = 1, Second Level = 2, Third Level = 3
     * We define construction as either a block or a dome
     */
    private int level;

    /** represents the worker placed in this square. If the square is unoccupied, builder is null */
    private Pawn pawn;

    /** represent the map of all the possible types of buildings associated to a level */
    private HashMap<Integer, String> buildings;

    /** represent the map of all the possible levels a pawn can be on*/
    private HashMap<Integer, String> levels;

    /** represent all the possible cell representations in cli*/
    private String r ;

    /** represent the colour of a cell in cli */
    private int colour;

    /**
     * creates a new cell and initializes it as a ground level cell with nothing on it.
     * @param i sets the x coordinate of the cell.
     * @param j sets the y coordinate of the cell.
     */
    public Cell(int i, int j){
        this.x = i;
        this.y = j;
        this.level = 0;
        this.pawn = null;
        this.value = 0;
        this.colour = 0;
        maps();
    }

    /**
     * this method draws what's in the cell and according the values
     * @param i is the x coordinate of the cell
     * @param j is the y coordinate of the
     * @return string r is the representation of the cell
     */
    public String drawCell(int i, int j){

        if (colour == 0) {

            Cell z = ClientBoard.getCell(i, j);

            if (z.value == 0) {
                r = buildings.get(level);
                return r;
            }

            if (value == 1) {
                r = pawn.getColours().get(pawn.getColour()) + pawn.getGender().get(pawn.getSex()) + levels.get(level);
                return r;

            }

            if (z.value == 2) {
                r = Colour.ANSI_BLUE + buildings.get(4) ;
                return r;
            }
        }

        if (colour == 1){

            Cell z = ClientBoard.getCell(i, j);

            if (z.value == 0) {
                r = Colour.ANSI_GREEN + buildings.get(level);
                return r;
            }

            if (value == 1) {
                r = Colour.ANSI_GREEN + pawn.getGender().get(pawn.getSex()) + levels.get(level);
            }

            if (value == 2) {
                r = Colour.ANSI_GREEN + buildings.get(4);
            }
        }

        return r;
    }

    /** initializes the hashMaps related to the cell */
    public void maps() {
        buildings = new HashMap<>();
        levels = new  HashMap<>();

        buildings.put(0,"\u24ea ");
        buildings.put(1,"\u2460 ");
        buildings.put(2,"\u2461 ");
        buildings.put(3,"\u2462 ");
        buildings.put(4, "\u23fa ");

        levels.put(0,"\u2080");
        levels.put(1,"\u2081");
        levels.put(2,"\u2082");
        levels.put(3,"\u2083");

    }

    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * changes the type of object that is on the top of the cell.
     * 0 signifies that there is nothing on it.
     * 1 signifies that there is a pawn on it.
     * 2 signifies that there is a dome on it.
     * @param squareValue is the kind of object that is now on top of the cell.
     */
    public void setValue(int squareValue){
        this.value = squareValue;
    }

    /**
     * is used to change the height of the cell.
     * @param squareLevel is the new height of the cell.
     */
    public void setLevel(int squareLevel){
        this.level = squareLevel;
    }

    public Pawn getPawn() {
        return pawn;
    }

    /**
     * puts a pawn on the cell.
     * @param pawn is the pawn to put on the cell.
     */
    public void setPawn(Pawn pawn){
        this.pawn = pawn;
    }

    /**
     * sets the colour of the cell
     * @param colour is the colour into which the cell change
     */
    public void setColour(int colour){
        this.colour = colour;
    }
}
