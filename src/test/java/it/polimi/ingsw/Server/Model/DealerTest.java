package it.polimi.ingsw.Server.Model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DealerTest {
    Dealer dealer;
    Player player;
    Game game;

    @Before
    public void createGame(){
        game = new Game();

    }

    @Test
    public void checkDealer(){
        game.addPlayer("Marco");
        game.addPlayer("Luca");
        assertTrue(game.playerList.get(0) instanceof Dealer);   // controlla che il tipo dinamico del primo giocatore sia Dealer
        player = game.playerList.get(1);
        assertFalse(game.playerList.get(1) instanceof Dealer); // controlla che l'altro elemento salvato non sia un dealer
        dealer = (Dealer) game.playerList.get(0); //cast di verifica (forse non è un test adeguato)
        assertEquals("Marco", dealer.getPlayerID());  // verifica che i metodi della classe player siano attivi (necessario?)
    }
}
