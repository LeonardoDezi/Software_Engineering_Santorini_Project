package it.polimi.ingsw.Model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

// da testare: costruttore, chooseCard, removeBuilders
public class PlayerTest {
    Player player1;
    Player player2;
    Game game = new Game();

    // il costruttore?   test su remove builder?
    @Before
    public void createPlayers(){
        player1 = new Player("Marco", "Red", game);
        player2 = new Player("Leonardo", "Green", game);
    }


    @Test
    public void checkEmptyBuilders(){

        Builder builder = player1.getBuilder(0);
        int num = player1.getBuilderSize();
    }

    @Test
    // forse devo aggiungere qualcosa
    public void checkAddAndGetBuilder(){   //li fondo per convenienza
        int num = player2.getBuilderSize();
        Square square = new Square(1, 2);
        player2.addBuilder(square);
        assertEquals(num + 1, player2.getBuilderSize() ); // controlla che un nuovo builder sia stato aggiunto

        Builder builder = player2.getBuilder(player2.getBuilderSize() - 1); //controlliamo che getBuilder funzioni, accertandoci inoltre che l'ultimo builder aggiunto sia il nostro.
        assertEquals(square, builder.getPosition()); // controlliamo che il builder restituito abbia la posizione che avevamo richiesto
        assertEquals(builder.getColour(), player2.colour); //controlliamo che la pedina abbia il colore del giocatore
    }


}
