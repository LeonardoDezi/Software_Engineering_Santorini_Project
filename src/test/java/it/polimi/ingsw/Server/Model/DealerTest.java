package it.polimi.ingsw.Server.Model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DealerTest {
    Dealer player1;
    Dealer player2;
    Game game1;
    Game game2;

    @Before
    public void createGame(){
        game1 = new Game(2);
        game2 = new Game(3);
        player1 = new Dealer("Marco", "RED", game1, 0);
        player2 = new Dealer("Leonardo", "Green", game2, 1);
        game1.addPlayer(player1);
        game2.addPlayer(player2);

    }

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
