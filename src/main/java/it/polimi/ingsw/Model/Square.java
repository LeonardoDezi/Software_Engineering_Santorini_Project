package it.polimi.ingsw.Model;

/**
 * represents a square on the board
 * @version 1.0
 * @since 1.0
 */


//Penso che Square non abbia bisogno di Test dato che i suoi metodi sono tutti dei getter e dei setter
//al massimo il costruttore

public class Square {

    private int value;
    public final int x;   //dove ci servivano x e y?
    public final int y;
    private int level;
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
     * changes the type of object that is on the square.
     * 0 signifies that there is nothing on it.
     * 1 signifies that there is a player on it.
     * 2 signifies that there is a dome on it.
     * @param value is the kind of object that is now on top af the square.
     */
    public void setValue(int value) {
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
    /*If the last occupied cell contains a construction, level will represent the highest free cell
      If the last occupied cell contains a builder, level will represent its position.*/
    public void setLevel(int level){
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
     * puts a builder on the square.
     * @param builder is the id of the builder that has been put on the square
     */
    public void setBuilder(Builder builder){
        this.builder = builder;
    }

    /**
     * is used to know the id of the builder on the square.
     * @return the id of the builder on the square.
     */
    public Builder getBuilder(){
        return this.builder;
    }

    public void resetSquare(){
        this.value = 0 ;  //rimetti la casella come libera
        this.builder = null;  // cancella il riferimento al builder
    }

}
