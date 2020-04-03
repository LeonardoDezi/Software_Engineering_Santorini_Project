package Model;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;

    public Card getCard(int num){
        return cards.get(num);
    }

}
