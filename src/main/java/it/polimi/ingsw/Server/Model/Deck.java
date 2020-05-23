package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card>[] deck;

    public ArrayList<Card>[] getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card>[] deck) {
        this.deck = deck;
    }
}