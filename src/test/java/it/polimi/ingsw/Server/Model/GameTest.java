package it.polimi.ingsw.Server.Model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


//2) test che controlla che non ci siano due giocatori con lo stesso builderId  ??

//Da testare: deployBuilder
public class GameTest {
    Game game;
    @Before
    public void createGame(){
        game = new Game();
    }

    //DA RILEGGERE
    //addPlayer
    //@requires playerList != null     (controllare)
    //@ensures playerList.size() <=3
    //@ensures if playerList.size() = 3 && addPlayer(new, new) il comando non avrà alcun effetto
    //@ensures (\forall int i; 0 <= i < playerList.size(); (\forall int j; i != j && 0 <= i < playerList.size();
    // playerList.get(i).getColour() != playerList.get(j).getColour));

    @Test
    public void playerListCheck(){
        assertEquals(0, game.playerList.size());
        game.addPlayer("Marco");
        game.addPlayer("Luca");
        game.addPlayer("Fra");
        game.addPlayer("error");

        assertEquals("Marco", game.playerList.get(0).getPlayerID());
        assertEquals("Red", game.playerList.get(0).getColour());


        assertEquals("Luca", game.playerList.get(1).getPlayerID());
        assertEquals("Green", game.playerList.get(1).getColour());

        assertEquals("Fra", game.playerList.get(2).getPlayerID());
        assertEquals("Blue", game.playerList.get(2).getColour());


    }

    @After
    public void maximumThree(){
        assertEquals(3, game.playerList.size());
    }

    // è necessario fare un test anche per quando ci saranno solo due giocatori?

}


// bisogna fare Test per removePlayer?
// non reputo necessario un test di move() dal momento che l'unica cosa che fa è utilizzare metodi già testati di altre classi.
//Accertarsene.
//build(): vedi move()