package it.polimi.ingsw.Server.Model;

import static org.junit.Assert.*;

import it.polimi.ingsw.Server.VirtualView.NetInterface;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

//ATTENZIONE: prima di eseguire i test, commentare updateBoard() in Board.move() e Board.build() (righe 96 e 73)

/**
 * this class is used to test the behaviour of Player
 */
public class PlayerTest {
    /** the first player used in the game*/
    private  Player player1;
    /** the second player used in the game */
    private Player player2;
    /** the two-player game used in the test */
    private Game game;
    /** the netInterface used in the test */
    private NetInterface netInterface = new NetInterface();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /** creates the game */
    @Before
    public void createPlayers(){
        game = new Game(2, netInterface);
        player1 = new Player("Marco", "Red", game, 0);
        player2 = new Player("Leonardo", "Green", game, 1);
    }


    /** checks that when the player is created, the list of associated workers is empty */
    @Test
    public void checkEmptyBuilders(){
        exception.expect(ArrayIndexOutOfBoundsException.class);
        Builder builder = player1.getBuilder(0);
        assertTrue(player1.builders.isEmpty());
    }


    /** checks the behaviour of addBuilder() and getBuilder() */
    @Test
    public void checkAddAndGetBuilder(){   //li fondo per convenienza
        int num = player2.getBuilderSize();
        Square square = new Square(1, 2);
        player2.addBuilder(square);
        assertEquals(num + 1, player2.getBuilderSize() ); // controlla che un nuovo builder sia stato aggiunto

        Builder builder = player2.getBuilder(player2.getBuilderSize() - 1); //controlliamo che getBuilder funzioni, accertandoci inoltre che l'ultimo builder aggiunto sia il nostro.
        assertEquals(square, builder.getPosition()); // controlliamo che il builder restituito abbia la posizione che avevamo richiesto
        assertEquals(builder.getColour(), player2.colour); //controlliamo che la pedina abbia il colore del giocatore
        player2.removeBuilder(0);
    }

    /** checks that the first worker created is a male and that the second one is female.
     * checks that when there are no female worker associated to the player, getFemale() returns null
     */
    @Test
    public void checkForSex(){
        player2.addBuilder(new Square(3,3));
        player2.addBuilder(new Square(1,2));
        assertEquals(2, player2.getBuilderSize());  //controlla che builders sia di dimensione due
        assertEquals(Player.SEX1, player2.builders.get(0).sex);   //controlla che il primo builder sia maschio
        assertEquals(Player.SEX2, player2.builders.get(1).sex);   // controlla che il secondo builder sia femnmina
        assertEquals(player2.builders.get(1), player2.getFemale()); //controlla che getFemale restituisca il secondo female
        assertEquals(Player.SEX2, player2.getFemale().sex);  //controlla che il sesso di getFemale sia F

        player2.removeBuilder(0);
        assertEquals(Player.SEX2, player2.builders.get(0).sex);  //controlla che getFemale funzioni anche quando togliamo un builder
        assertEquals(player2.builders.get(0), player2.getFemale());

        player2.removeBuilder(0);
        assertEquals(null, player2.getFemale());   //controlla che nel caso di pedine assenti getFemale ritorni null

        player2.addBuilder(new Square (3,3));
        assertEquals(null, player2.getFemale());   //controlla che nel caso di pedine femmine assenti getFemale() ritorni null

        player2.removeBuilder(0);

    }

    /** checks the behaviour of removeBuilder() */
    @Test
    public void checkRemoveBuilder(){
        player1.addBuilder(new Square(2,2));
        player1.removeBuilder(0);
        assertEquals(0, player1.builders.size());  //controlla che venga rimosso un builder

        player1.addBuilder(new Square(2,2));
        player1.addBuilder(new Square (3,1));
        Builder builder = player1.builders.get(1);
        player1.removeBuilder(1);
        assertEquals(1, player1.builders.size());
        assertNotEquals(builder, player1.builders.get(0));    //controlla che il builder rimasto non sia quello che doveva essere rimosso

        player1.removeBuilder(0);
    }

    /** checks the behaviour of chooseCard(). In particular, it checks if the card chosen by the player has been assigned to
     * the player and that the card has been removed from the cards chosen by the Challenger
     */
    @Test
    public void checkChooseCard(){

        game.addChosenCard(3);
        game.addChosenCard(8);
        game.addChosenCard(14);
        ArrayList<Card> possibleCards = game.getChosenCards();
        assertEquals(3, possibleCards.size());
        possibleCards = player1.chooseCard(possibleCards, 1);
        assertEquals(2, possibleCards.size());
        assertEquals(8, player1.card.getNumber());
        assertEquals("Pan", player1.card.getName());

        for(Card card: possibleCards){
            assertNotEquals(player1.card, card);
        }

    }

}
