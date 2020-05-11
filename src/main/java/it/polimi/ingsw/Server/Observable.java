package it.polimi.ingsw.Server;

public interface Observable {

    public void add(Observer observer);
    public void remove(Observer observer);


}
