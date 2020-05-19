package it.polimi.ingsw.Server.Model;

/**
 * represents a builder.
 * @version 1.5
 * @since 1.0
 */

public class Builder {
    private Square position;
    private final String colour;
    public final String sex;

    // forse bisogna indicare anche a quale player appartiene per la GUI


    /**
     * creates the builder and puts it on the board at a given position.
     * @param position is used to store the builder position also inside the builder class.
     */
    public Builder(Square position, String colour, String sex){
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

    //il controller avrà già verificato che la posizione è libera e possibile

    /**
     * gets the colour of the builder.
     * @return the String representing the colour.
     */
    public String getColour(){
        return this.colour;
    }

    public void setPosition(Square position){
        this.position = position;
    }
}
