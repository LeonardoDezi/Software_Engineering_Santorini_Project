package it.polimi.ingsw.Server.Model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

//TODO magari fare più test con carte speciali move e carte speciali build insieme

public class WinPhaseTest {
    private WinPhase winPhase;
    private Game game;
    private Player player1;
    private Player player2;
    private Player player3;


    @Before
    public void create(){
        game = new Game(3);
        winPhase = new WinPhase(game);
        player1 = new Player("Marco", "RED", game, 0 );
        game.addPlayer(player1);
        player2 = new Player("Leonardo", "RED", game, 1 );
        game.addPlayer(player2);
        player3 = new Player("Fra", "RED", game, 2 );
        game.addPlayer(player3);
    }

    @Test
    public void checkAtLeastFiveTowers(){
        game.gameBoard.build(game.gameBoard.fullMap[0][0], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][0], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][0], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][0], false);

        game.gameBoard.build(game.gameBoard.fullMap[0][1], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][1], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][1], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][1], false);

        game.gameBoard.build(game.gameBoard.fullMap[0][2], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][2], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][2], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][2], false);   //quattro torri complete

        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);

        //1) solo quattro torri
        player1.setCard(game.getDeckCard(7));     //Minotauro
        player2.setCard(game.getDeckCard(11));    //Crono
        player3.setCard(game.getDeckCard(8));     //Pan

        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());   // si accerta che nessuno abbia ancora vinto

        winPhase.checkBuild(player1);
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());   // si accerta che niente sia cambiato

        //2) cinque torri e il giocatore passato in ingresso non ha la carta speciale
        game.gameBoard.build(game.gameBoard.fullMap[1][0], false);
        game.gameBoard.build(game.gameBoard.fullMap[1][0], false);
        game.gameBoard.build(game.gameBoard.fullMap[1][0], false);
        game.gameBoard.build(game.gameBoard.fullMap[1][0], false);   // quinta torre

        winPhase.checkBuild(player1);
        assertTrue(game.getGameEnded());
        assertEquals(player2, game.getWinningPlayer());   // si accerta che player2 abbia vinto

        game.setGameEnded(false);
        game.setWinningPlayer(null);    //reinizializzazione

        //3) cinque torri e il giocatore passato in ingresso ha la carta speciale
        player1.setCard(game.getDeckCard(11));    //Crono
        player2.setCard(game.getDeckCard(13));     //Selene
        player3.setCard(game.getDeckCard(8));     //Pan

        winPhase.checkBuild(player1);
        assertTrue(game.getGameEnded());
        assertEquals(player1, game.getWinningPlayer());   // si accerta che player1 abbia vinto

        game.setGameEnded(false);
        game.setWinningPlayer(null);    //reinizializzazione


        //4) cinque torri e nessun giocatore ha la carta speciale
        player1.setCard(game.getDeckCard(5));    //Demeter
        player2.setCard(game.getDeckCard(1));    //Apollo
        player3.setCard(game.getDeckCard(8));    //Pan

        winPhase.checkBuild(player1);
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());   // si accerta che nessuno abbia vinto

    }


    @Test
    public void checkJumpDownCondition(){

        //1) caso non vincente  //torre di tre piani - torre di due piani
        game.gameBoard.build(game.gameBoard.fullMap[3][3], false);   //torre di tre piani
        game.gameBoard.build(game.gameBoard.fullMap[3][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[3][3], false);

        game.gameBoard.build(game.gameBoard.fullMap[1][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[1][3], false);   //torre di due piani


        player1.setCard(game.getDeckCard(5));    //Demeter
        player2.setCard(game.getDeckCard(1));    //Apollo
        player3.setCard(game.getDeckCard(8));    //Pan

        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());   // si accerta che nessuno abbia ancora vinto

        winPhase.checkMovement(player3, game.gameBoard.fullMap[3][3],game.gameBoard.fullMap[1][3] );

        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());   // si accerta che nessuno abbia vinto


        //2) caso non vincente  torre di due piani - torre di tre piani
        winPhase.checkMovement(player3, game.gameBoard.fullMap[3][3],game.gameBoard.fullMap[1][3] );

        //3)caso vincente ma il giocatore non ha la carta speciale     //torre di 3 piani - terra
        winPhase.checkMovement(player1, game.gameBoard.fullMap[3][3],game.gameBoard.fullMap[4][4]);
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());   // si accerta che nessuno abbia vinto



        //3) caso vincente e il giocatore ha la carta speciale      //torre di 3 piani - torre di un piano
        game.gameBoard.build(game.gameBoard.fullMap[4][4], false);   //torre di un piano
        winPhase.checkMovement(player3, game.gameBoard.fullMap[3][3],game.gameBoard.fullMap[4][4]);
        assertTrue(game.getGameEnded());
        assertEquals(game.getWinningPlayer(), player3);

        game.setGameEnded(false);
        game.setWinningPlayer(null);    //reinizializzazione

        //4) caso non vincente e il giocatore ha la carta speciale      //torre di un piano  - torre di tre piani
        winPhase.checkMovement(player3, game.gameBoard.fullMap[4][4],game.gameBoard.fullMap[3][3]);
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());   // si accerta che nessuno abbia vinto


        //5) caso vincente e nessuno ha la carta speciale
        player3.setCard(game.getDeckCard(4));    //Apollo
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());   // si accerta che nessuno abbia vinto
        winPhase.checkMovement(player2, game.gameBoard.fullMap[3][3],game.gameBoard.fullMap[4][4]);
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());   // si accerta che nessuno abbia vinto

    }

}
