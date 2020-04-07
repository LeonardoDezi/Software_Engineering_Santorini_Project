package Model;

public class Builder {
    private Square position;
    private boolean blocked;
    private String colour;
    private int builderId;
    private Square tmp;   // questa da aggiungere

    // credo che ormai questa non serve più
    // public void setPosition(Coordinates position) {
    //    this.position = position;
    //}

    public Square getPosition() {
        return position;
    }

    //il controller avrà già verificato che la posizione è libera e possibile

    public Builder(int x, int y, Square position) {
        blocked = false;

        // ancora sto colour
        // builderId?


        this.position = position;

        position.setValue(1);   //occupa la casella con la pedina
        position.setBuilder(this);

    }
}
