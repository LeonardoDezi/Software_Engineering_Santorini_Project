package Model;

public class Dealer {

    private Player dealer;
    private Deck deck;
    private Game game;


    public Dealer(Player player, Game game) {
        this.dealer = player;
        this.deck = game.getDeck();
        this.game = game;
    }

    public void chooseCards(int card1, int card2, int card3){

        game.addChosenCard(deck.getCard(card1));
        game.addChosenCard(deck.getCard(card2));
        game.addChosenCard(deck.getCard(card3));

        return;

    }
}
