package it.polimi.ingsw.Server.Model;

/**
 * represents a square on the board
 * @version 1.0
 * @since 1.0
 */


public class Square {

    /** represents what is on the top of the square. Blank space = 0, Worker = 1, Dome = 2 */
    private int value;

    /** represents the x coordinate of the square */
    public final int x;

    /** represents the y coordinate of the square*/
    public final int y;

    /**If the square does not contain a worker, level will represent the number of constructions placed in the square,
     * otherwise it represents the level where the worker is placed.
     * Ground Level = 0, First Level = 1, Second Level = 2, Third Level = 3
     * We define construction as either a block or a dome
     */
    private int level;

    /** represents the worker placed in this square. If the square is unoccupied, builder is null */
    private Builder builder;

    /**
     * creates a new square and initializes it as a ground level square with nothing on it.
     * @param i sets the x coordinate of the square.
     * @param j sets the y coordinate of the square.
     */
    public Square(int i, int j) {
        this.x = i;
        this.y = j;
        this.level = 0;
        this.builder = null;
        this.value = 0;
    }

    /**
     * changes the type of object that is on the top of the square.
     * 0 signifies that there is nothing on it.
     * 1 signifies that there is a worker on it.
     * 2 signifies that there is a dome on it.
     * @param value is the kind of object that is now on top of the square.
     */
    protected void setValue(int value) {
        this.value = value;
    }

    /**
     * is used to know if there is something on top of the square and what it is.
     * @return the number that signifies what is on top of the square.
     */
    public int getValue(){ return this.value;}

    /**
     * is used to change the height of the square.
     * @param level is the new height of the square.
     */
    protected void setLevel(int level){
        this.level = level;
    }

    /**
     * is used to know the height of the square.
     * @return the height of the square.
     */
    public int getLevel(){
        return this.level;
    }

    /**
     * puts a worker on the square.
     * @param builder is the worker to put on the square
     */
    protected void setBuilder(Builder builder){ this.builder = builder; }

    /**
     * is used to know the worker on the square.
     * @return the object of the worker on the square.
     */
    protected Builder getBuilder(){
        return this.builder;
    }


    /**
     * is used to remove a worker from the square, and to delete all the traces of its presence.
     */
    protected void resetSquare(){
        this.value = 0 ;
        this.builder = null;
    }

}
