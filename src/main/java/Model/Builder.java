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
    public Builder(int x, int y, Board board){
        blocked = false;

        // ancora sto colour
        // builderId?

        position = board.fullMap[x][y];  //problema del fullMap
        position.setValue(1);   //occupa la casella con la pedina
        position.setBuilder(this);

    }

    public void play(int xMove, int yMove, int xBuild, int yBuild, boolean isDome){  // questa la posso cambiare isDome la metto? o ce ne frega solo per Atlante?
        move(xMove, yMove);
        build(xMove, yMove, isDome);
    }

    public void move(int x, int y){
        tmp = this.position;
        tmp.setValue(1);        // move dovrebbe dirci che ha vinto??
        if(tmp.getLevel() > 0)
            tmp.setLevel(tmp.getLevel() +1);
        tmp.setBuilder(this);

        //togliamo la pedina
        position.setValue(0);
        if(position.getLevel() > 0)
            position.setLevel(position.getLevel() - 1);
        position.setBuilder(null);

        return;
    }

    public void build(int x, int y, boolean isDome, Board board){   // mi sa che ci serve un parametro isDome qua

        tmp = board.fullMap[x][y];

        if(isDome == true)
            tmp.setValue(3);   // 3 = cupola
        else
            tmp.setValue(2);   // 2 = edificio

        if(tmp.getLevel() > 0)
            tmp.setLevel(tmp.getLevel() + 1);

        return;

    }

}
