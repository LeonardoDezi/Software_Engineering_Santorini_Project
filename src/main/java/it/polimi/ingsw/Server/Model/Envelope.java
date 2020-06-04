package it.polimi.ingsw.Server.Model;

/** this class represents the object used by Client to communicate to the Server the move made by the player
 */
public class Envelope {
    /** represents the worker associated to the move that is received */
    private Builder builder;
    /** represents the move made by the worker. When the player makes no moves, move is set to null */
    private Square move = null;
    /** represents the type of construction that the player wants to make. */
    private boolean isDome = false;

    /** creates a new envelope
     * @param builder is the worker associated to the move that is received
     * @param move represents the move made by the worker
     * */
    public Envelope(Builder builder, Square move){
        this.builder = builder;
        this.move = move;
    }

    /**
     * @return the worker saved in envelope
     */
    public Builder getBuilder(){return builder;}

    /**
     * @return the move saved in envelope
     */
    public Square getMove(){ return move;}

    /**
     * @param move is the move made by the worker
     */
    public void setMove(Square move){ this.move = move;}

    /**
     * @return the parameter isDome saved in envelope
     */
    public boolean getIsDome(){return this.isDome;}

    /**
     * @param isDome represents the type of construction that the player wants to make.
     */
    public void setIsDome(boolean isDome){this.isDome = isDome;}

}
