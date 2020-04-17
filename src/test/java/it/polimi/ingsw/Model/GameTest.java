package it.polimi.ingsw.Model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


//2) test che controlla che non ci siano due giocatori con lo stesso builderId  ??


public class GameTest {
    Game game;
    @Before
    public void createGame(){
        game = new Game();
    }

    //addPlayer
    //@requires playerList != null     (controllare)
    //@ensures playerList.size() <=3
    //@ensures if playerList.size() = 3 && addPlayer(new, new) il comando non avrà alcun effetto
    //@ensures (\forall int i; 0 <= i < playerList.size(); (\forall int j; i != j && 0 <= i < playerList.size();
    // playerList.get(i).getColour() != playerList.get(j).getColour));

    @Test
    public void playerListCheck(){
        assertTrue(game.playerList.size() == 0);
        game.addPlayer("Marco");
        game.addPlayer("Luca");
        game.addPlayer("Fra");
        game.addPlayer("errore");

        assertTrue(game.playerList.get(0).getPlayerID().equals("Marco"));
        assertTrue(game.playerList.get(0).getColour().equals("Red"));



        assertTrue(game.playerList.get(1).getPlayerID().equals("Luca"));
        assertTrue(game.playerList.get(1).getColour().equals("Green"));

        assertTrue(game.playerList.get(2).getPlayerID().equals("Fra"));
        assertTrue(game.playerList.get(2).getColour().equals("Blue"));


    }

    @After
    public void maximumThree(){
        assertTrue(game.playerList.size() == 3);
    }

    // è necessario fare un test anche per quando ci saranno solo due giocatori?

}


// bisogna fare Test per removePlayer?