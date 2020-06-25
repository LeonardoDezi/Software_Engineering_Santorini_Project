package it.polimi.ingsw.Server.Model;

import static org.junit.Assert.*;

import it.polimi.ingsw.Server.VirtualView.NetInterface;
import org.junit.Before;
import org.junit.Test;

//ATTENZIONE: prima di eseguire i test, commentare updateBoard() in Board.move() e Board.build() (righe 96 e 73)

/**
 * this class is used to test the behaviour of Dealer
 */
public class DealerTest {
    /** the Challenger of game 1*/
    private Dealer player1;
    /** the Challenger in game2 */
    private Dealer player2;
    /** the two-player game used for the test */
    private Game game1;
    /** the three-player game used for the test */
    private Game game2;
    /** the netInterface used by game1 */
    private NetInterface netInterface = new NetInterface();
    /** the netInterface used by game2 */
    private NetInterface netInterface2 = new NetInterface();

    /** creates the games and the Challenger */
    @Before
    public void createGame(){
        game1 = new Game(2, netInterface);
        game2 = new Game(3, netInterface2);
        player1 = new Dealer("Marco", "RED", game1, 0);
        player2 = new Dealer("Leonardo", "Green", game2, 1);
        game1.addPlayer(player1);
        game2.addPlayer(player2);

    }

    /** checks the behaviour of drawCards() methods */
    @Test
    public void checkDrawCards(){

        //tre giocatori
        player1.drawCards(5, 8, 2);
        assertEquals(3, game1.getChosenCardsSize());
        assertEquals("Demeter", game1.getChosenCard(0).getName());
        assertEquals("Pan", game1.getChosenCard(1).getName());
        assertEquals("Artemis", game1.getChosenCard(2).getName());

        //due giocatori
        player2.drawCards(9, 11);
        assertEquals(2, game2.getChosenCardsSize());
        assertEquals("Prometheus", game2.getChosenCard(0).getName());
        assertEquals("Chronus", game2.getChosenCard(1).getName());

    }

}
