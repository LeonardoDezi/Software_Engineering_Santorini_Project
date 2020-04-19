package it.polimi.ingsw.Model;
//dobbiamo modificare l'UML
//mi sa che non servono più numerosi flag
public class Dealer extends Player {

    private Player dealer;  // serve?
    private Deck deck;
    private String colour;  // serve?
    private Game game;


    public Dealer(String name, String colour, Game game) {
        super(name, colour);
        this.deck = game.getDeck();
        this.game = game;
    }

    // bisogna ancora fare test per chooseCards
    public void chooseCards(int card1, int card2, int card3){

        game.addChosenCard(deck.getCard(card1));
        game.addChosenCard(deck.getCard(card2));
        game.addChosenCard(deck.getCard(card3));

        return;

    }
}
