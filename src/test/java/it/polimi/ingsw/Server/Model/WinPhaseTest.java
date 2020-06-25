package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.VirtualView.NetInterface;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

//ATTENZIONE: prima di eseguire i test, commentare updateBoard() in Board.move() e Board.build() (righe 96 e 73)

/**  this class is used to test the behaviour of Game  */
 public class WinPhaseTest {
    /** the winPhase tested */
    private WinPhase winPhase;
    /** the three-player game used by the test */
    private Game game;
    /** the player used in the test */
    private Player player1;
    /** the player used in the test */
    private Player player2;
    /** the player used in the test */
    private Player player3;
    /** the netInterface used in the test */
    private NetInterface netInterface = new NetInterface();

    /** creates the game and the players */
    @Before
    public void create(){
        game = new Game(3, netInterface);
        winPhase = new WinPhase(game);
        player1 = new Player("Marco", "RED", game, 0 );
        game.addPlayer(player1);
        player2 = new Player("Leonardo", "RED", game, 1 );
        game.addPlayer(player2);
        player3 = new Player("Fra", "RED", game, 2 );
        game.addPlayer(player3);
    }

    /** checks the behaviour of WinPhase when
     * 1) There are less than five towers
     * 2) There are five towers and one of the players has the Chronus card
     * 3) There are five towers and the player currently playing has the Chronus card
     * 4) There are five towers and no player has the Chronus card
     * */
    @Test
    public void checkAtLeastFiveTowers() throws IOException {
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

/** checks the behaviour of WinPhase when
    1) The move made cannot result in a special victory. (From a three-block tower to a two-block tower)
    2) The move made cannot result in a special victory. (From a two-block tower to a three-block tower)
    3) The move made can result in a special victory but the current player doesn't have the Pan card. (From a three-block tower to the ground)
    4) The move made can result in a special victory and the current player has the Pan card. (From a three-block tower to the ground)
    5) The move made cannot result in a special victory and the current player has the Pan card. (From a one-block tower to a three-block tower)
    6) The move made can result in a special victory but no player has the Pan card.
 */
    @Test
    public void checkJumpDownCondition() throws IOException {

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



        //4) caso vincente e il giocatore ha la carta speciale      //torre di 3 piani - torre di un piano
        game.gameBoard.build(game.gameBoard.fullMap[4][4], false);   //torre di un piano
        winPhase.checkMovement(player3, game.gameBoard.fullMap[3][3],game.gameBoard.fullMap[4][4]);
        assertTrue(game.getGameEnded());
        assertEquals(game.getWinningPlayer(), player3);

        game.setGameEnded(false);
        game.setWinningPlayer(null);    //reinizializzazione

        //5) caso non vincente e il giocatore ha la carta speciale      //torre di un piano  - torre di tre piani
        winPhase.checkMovement(player3, game.gameBoard.fullMap[4][4],game.gameBoard.fullMap[3][3]);
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());   // si accerta che nessuno abbia vinto


        //6) caso vincente e nessuno ha la carta speciale
        player3.setCard(game.getDeckCard(4));    //Apollo
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());   // si accerta che nessuno abbia vinto
        winPhase.checkMovement(player2, game.gameBoard.fullMap[3][3],game.gameBoard.fullMap[4][4]);
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());   // si accerta che nessuno abbia vinto

    }

}
