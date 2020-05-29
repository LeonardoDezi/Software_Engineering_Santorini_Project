package it.polimi.ingsw.Server.Model;

/**
 * represents the Challenger of the game.
 * It will decide the cards used during the match, and the first player who is going to start
 * @version 1.5
 * @since 1.0
 */
public class Dealer extends Player {

    /** creates the Challenger.
     * @param name is the name that identifies the Challenger.
     * @param colour is the colour that will be associated to the Challenger
     * @param game is the game that the Challenger is playing
     * @param clientID is the id that will be associated to the Challenger
     */
    public Dealer(String name, String colour, Game game, Integer clientID) {
        super(name, colour, game, clientID);
    }

    /** is used to select the cards that will be used in a three-player game
     * @param card1 is the first card selected
     * @param card2 is the second card selected
     * @param card3 is the third card selected
     */
    public void drawCards (int card1, int card2, int card3){

        game.addChosenCard(card1);
        game.addChosenCard(card2);
        game.addChosenCard(card3);

    }

    /** is used to select the cards that will be used in a two-player game
     * @param card1 is the first card selected
     * @param card2 is the second card selected
     */
    //i numeri devono essere legittimi
    public void drawCards(int card1, int card2){
        game.addChosenCard(card1);
        game.addChosenCard(card2);
    }
}
