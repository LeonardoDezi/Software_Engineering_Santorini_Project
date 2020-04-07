package Model;

/**
 * represents a builder.
 * @version 1.5
 * @since 1.0
 */

public class Builder {
    private Square position;
    private boolean blocked;
    private final String colour;
    private int builderId;
    private Square tmp;   // questa da aggiungere
    /**
     * represents a builder.
     */



    // credo che ormai questa non serve più
    // public void setPosition(Coordinates position) {
    //    this.position = position;
    //}
    /**
     * gets the position of the builder.
     * @return the square where the builder is at the moment of the call.
     */
    public Square getPosition() {
        return position;
    }

    //il controller avrà già verificato che la posizione è libera e possibile

    /**
     * creates the builder and puts it on the board at a given position.
     * @param x represents the x coordinate of the square where the builder has been put.
     * @param y represents the y coordinate of the square where the builder has been put.
     * @param position is used to store the builder position also inside the builder class.
     */
    public Builder(int x, int y, Square position, String colour){
        blocked = false;
        this.colour = colour;// ancora sto colour
        // builderId?
        this.position = position;
        position.setValue(1);   //occupa la casella con la pedina
        position.setBuilder(this);

    }

    public String getColour(){
        return this.colour;
    }
}
