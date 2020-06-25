package it.polimi.ingsw.Server.Model;

/**
 * represents a worker.
 * @version 1.5
 * @since 1.0
 */

public class Builder {
    /** represents the position where the worker is placed */
    private Square position;

    /** represents the colour of the player who owns the worker */
    private final String colour;

    /** represents the sex of the worker */
    public final String sex;



    /**
     * creates the worker and puts it on the board at a given position.
     * @param position is used to store the worker's position also inside the builder class.
     * @param colour is used to the player's colour inside the builder class.
     * @param sex is used to define the sex of the new worker
     */
    public Builder(Square position, String colour, String sex){  //position deve essere legittima a questo punto
        this.colour = colour;
        this.sex = sex;
        this.position = position;
        position.setValue(1);   //occupa la casella con la pedina
        position.setBuilder(this);

    }

    /**
     * gets the position of the builder.
     * @return the square where the builder is at the moment of the call.
     */
    public Square getPosition() {
        return position;
    }


    /**
     * gets the colour of the builder.
     * @return the String representing the colour.
     */
    public String getColour(){
        return this.colour;
    }

    /**
     * sets the new position of the worker
     * @param position is the square where the worker will be
     */
    protected void setPosition(Square position){
        this.position = position;
    }

    public String getSex(){
        return sex;
    }
}
