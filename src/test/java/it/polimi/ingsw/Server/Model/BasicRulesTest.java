package it.polimi.ingsw.Server.Model;

import static java.lang.Math.abs;
import static org.junit.Assert.*;

import it.polimi.ingsw.Server.VirtualView.NetInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

//ATTENZIONE: prima di eseguire i test, commentare updateBoard() in Board.move() e Board.build() (righe 96 e 73)


/**
 * this class is used to test the behaviour of MovementPhase
 */
public class BasicRulesTest {

    /** the three-player game used for the test */
    private Game game;
    /** the board used by the game */
    private Board board;
    /** the player used for the test*/
    private Player player1;
    /** the basic rules used in the game */
    private BasicRules rules;
    /** the netInterface used in the game */
    private NetInterface netInterface = new NetInterface();


    /**
     * creates the players and the games used in every test
     */
    @Before
    public void createGameAndRules(){
        game = new Game(3, netInterface );
        board = game.getBoard();
        int num1 =game.addPlayer(new Player("Marco", "Red", game, 0));
        int num2 =game.addPlayer(new Player("Luca", "Blue", game, 1));
        int num3 =game.addPlayer(new Player("Fra", "Green", game, 2));
        rules = new BasicRules(board, game);

    }


    /** checks the basic winning condition according to the general rules of the game: a worker moving from a
     * two-floor building to a three-floor building */
    @Test
    public void testWinCondition() throws IOException {
        board.build(board.fullMap[1][2], false);
        board.build(board.fullMap[1][2], false);  //costruiamo un edificio di due piani
        assertEquals(0, board.fullMap[1][2].getValue());
        assertEquals(2, board.fullMap[1][2].getLevel());  // verifichiamo che l'edificio sia costruito correttamente

        board.build(board.fullMap[2][2], false);
        board.build(board.fullMap[2][2], false);
        board.build(board.fullMap[2][2], false);  // costruiamo un edifico di tre piani
        assertEquals(0, board.fullMap[2][2].getValue());
        assertEquals(3, board.fullMap[2][2].getLevel());  // verifichiamo che l'edificio sia costruito correttamente


        player1 = game.playerList.get(2);
        game.deployBuilder(player1, board.fullMap[2][2]);  // poniamo una pedina sulla torre di tre piani
        assertEquals(1, board.fullMap[2][2].getValue());  // verifichiamo che sia stata posta
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());
        rules.winCondition(player1, board.fullMap[1][2], board.fullMap[2][2]);  //chiamiamo winCondition() ponendo come parametri la torre di due piani e quella di tre
        assertTrue(game.getGameEnded());
        assertEquals(player1, game.getWinningPlayer());

    }

    /** checks for unexpected behaviours in winCondition() that could result in the player's wrong victory
     * 1) The worker hasn't moved from initialPosition
     * 2) There's a worker in finalPosition, but the examined worker has stayed in initialPosition
     * 3) finalPosition has not the right level in order to win
     * 4) initialPosition has not the right level in order to win
     * 5) the worker in finalPosition does not belong to the examined player
     * */
    @Test
    public void testNotWinningConditions() throws IOException {


        //1) il giocatore si trova ancora sulla posizione iniziale
        player1 = game.playerList.get(0);
        board.build(board.fullMap[0][1], false);   //torre di due piani
        board.build(board.fullMap[0][1], false);
        game.deployBuilder(player1, board.fullMap[0][1]);

        board.build(board.fullMap[1][1], false);
        board.build(board.fullMap[1][1], false);
        board.build(board.fullMap[1][1], false);   // torre di tre piani

        assertEquals(2, board.fullMap[0][1].getLevel());  //accertarsi che la pedina si trova sulla torre di due piani
        assertEquals(1, board.fullMap[0][1].getValue());

        assertEquals(3, board.fullMap[1][1].getLevel());   //accertarsi che la torre sia di tre piani è libera
        assertEquals(0, board.fullMap[1][1].getValue());

        assertFalse(game.getGameEnded());                         // accertarsi che nessuno abbia ancora vinto
        assertNull(game.getWinningPlayer());

        rules.winCondition(player1, board.fullMap[0][1], board.fullMap[1][1]);

        assertFalse(game.getGameEnded());                         // accertarsi che non sia cambiato niente
        assertNull(game.getWinningPlayer());


        //2) c'è una pedina in finalPosition ma il giocatore è ancora nella posizione iniziale
        game.deployBuilder(player1, board.fullMap[1][1]);   //posizionare una pedina su finalPosition
        assertEquals(3, board.fullMap[1][1].getLevel());
        assertEquals(1, board.fullMap[1][1].getValue());    //accertarsi che la torre sia occupata

        assertEquals(2, board.fullMap[0][1].getLevel());  //accertarsi che la pedina si trova sulla torre di due piani
        assertEquals(1, board.fullMap[0][1].getValue());

        assertFalse(game.getGameEnded());                         // accertarsi che nessuno abbia ancora vinto
        assertNull(game.getWinningPlayer());

        rules.winCondition(player1, board.fullMap[0][1], board.fullMap[1][1]);

        assertFalse(game.getGameEnded());                         // accertarsi che non sia cambiato niente
        assertNull(game.getWinningPlayer());



        //3) finalPosition non è del livello corretto
        board.build(board.fullMap[0][2], false);    // torre di due piani
        board.build(board.fullMap[0][2], false);
        board.move(board.fullMap[0][1], board.fullMap[0][2]);   // spostamento del builder da una torre di due piani a un' altra di due piani

        assertEquals(2, board.fullMap[0][1].getLevel());  //accertarsi che la posizione ora è libera
        assertEquals(0, board.fullMap[0][1].getValue());

        assertEquals(2, board.fullMap[0][2].getLevel());   //accertarsi che la posizione è occupata
        assertEquals(1, board.fullMap[0][2].getValue());

        assertFalse(game.getGameEnded());                         // accertarsi che nessuno abbia ancora vinto
        assertNull(game.getWinningPlayer());

        rules.winCondition(player1, board.fullMap[0][1], board.fullMap[0][2]);

        assertFalse(game.getGameEnded());                         // accertarsi che non sia cambiato niente
        assertNull(game.getWinningPlayer());


        //4) initialPosition non è del livello corretto
        board.build(board.fullMap[2][3], false);
        board.build(board.fullMap[2][3], false);
        board.build(board.fullMap[2][3], false);    // torre di tre piani

        Player player3 = game.playerList.get(2);
        game.deployBuilder(player3, board.fullMap[2][2]);
        board.move(board.fullMap[2][2], board.fullMap[2][3]);   // spostamento del builder da terra a una torre di tre piani

        assertEquals(0, board.fullMap[2][2].getLevel());  //accertarsi che la posizione ora è libera
        assertEquals(0, board.fullMap[2][2].getValue());

        assertEquals(3, board.fullMap[2][3].getLevel());   //accertarsi che la posizione è occupata
        assertEquals(1, board.fullMap[2][3].getValue());

        assertFalse(game.getGameEnded());                         // accertarsi che nessuno abbia ancora vinto
        assertNull(game.getWinningPlayer());

        rules.winCondition(player3, board.fullMap[2][2], board.fullMap[2][3]);

        assertFalse(game.getGameEnded());                         // accertarsi che non sia cambiato niente
        assertNull(game.getWinningPlayer());


        //5) il builder in finalPosition non è del player giocante
        Player player2 = game.playerList.get(1);

        board.build(board.fullMap[0][4], false);
        board.build(board.fullMap[0][4], false);   //torre di due piani

        board.build(board.fullMap[1][4], false);
        board.build(board.fullMap[1][4], false);   //torre di tre piani
        board.build(board.fullMap[1][4], false);

        game.deployBuilder(player2, board.fullMap[1][4]);   //mettere la pedina sulla torre di tre piani

        assertEquals(2, board.fullMap[0][4].getLevel());  //accertarsi che la posizione ora è libera
        assertEquals(0, board.fullMap[0][4].getValue());

        assertEquals(3, board.fullMap[1][4].getLevel());   //accertarsi che la posizione è occupata
        assertEquals(1, board.fullMap[1][4].getValue());

        assertFalse(game.getGameEnded());                         // accertarsi che nessuno abbia ancora vinto
        assertNull(game.getWinningPlayer());

        rules.winCondition(player1, board.fullMap[0][4], board.fullMap[1][4]);

        assertFalse(game.getGameEnded());                         // accertarsi che non sia cambiato niente
        assertNull(game.getWinningPlayer());

    }


    /** checks the correct behaviour of the method proximity.
     * 1) Empty board. Checks that all the squares in possibleMoves are actually neighboring the examined worker.
     * 2) Adding a dome, a worker and a three-level building in the proximity of the worker
     * 3) The examined worker is placed at a corner of the board
     * 4) The examined worker is placed on the border side of the board
     */
    @Test
    public void checkProximity() throws IOException {

        //1) Plancia vuota
        player1 = game.playerList.get(0);
        game.deployBuilder(player1, board.fullMap[2][2]);
        Builder builder = player1.getBuilder(0);
        ArrayList<Square> possibleMoves = rules.proximity(builder);
        assertEquals(8, possibleMoves.size()); // controlla che ci siano tutte le caselle

        for (Square possibleMove : possibleMoves) {
            assertNotEquals(player1.getBuilder(0).getPosition(), possibleMove);  // controlla che la posizione del giocatore non sia presente
            assertEquals(0, possibleMove.getValue());  //controlla che siano tutti square liberi
            assertTrue(abs(builder.getPosition().x - possibleMove.x) <= 1);  //controlla che siano tutte caselle adiacenti al builder
            assertTrue(abs(builder.getPosition().y - possibleMove.y) <= 1);
        }

        //2) Aggiunta di un edificio di tre piani in (1,3) una cupola in (3,1) una pedina in (2,3)
        board.build(board.fullMap[1][3], false);
        board.build(board.fullMap[1][3], false);
        board.build(board.fullMap[1][3], false);   // torre di tre piani

        board.build(board.fullMap[3][1], true);   //cupola

        game.deployBuilder(game.playerList.get(1), board.fullMap[2][3]);    // pedina in (2,3)

        possibleMoves = rules.proximity(builder);
        assertEquals(8, possibleMoves.size()); // controlla che ci siano tutte le caselle

        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        for (Square possibleMove : possibleMoves) {
            if (possibleMove.getLevel() == 3 && possibleMove.equals(board.fullMap[1][3]))
                flag1 = true;
            if (possibleMove.getValue() == 2 && possibleMove.equals(board.fullMap[3][1]))
                flag2 = true;
            if (possibleMove.getValue() == 1 && possibleMove.equals(board.fullMap[2][3]))
                flag3 = true;
        }
        assertTrue(flag1);
        assertTrue(flag2);
        assertTrue(flag3);


        //3)proximity di un builder in un angolo
        game.deployBuilder(game.playerList.get(0), board.fullMap[0][4]);
        builder = game.playerList.get(0).getBuilder(1);
        possibleMoves = rules.proximity(builder);
        assertEquals(3, possibleMoves.size());  // si accerta che la dimensione sia tre

        //4)proximity di un builder in un lato
        game.deployBuilder(game.playerList.get(2), board.fullMap[4][2]);
        builder = game.playerList.get(2).getBuilder(0);
        possibleMoves = rules.proximity(builder);
        assertEquals(5, possibleMoves.size());

    }


    /** checks the correct behaviour of the method removeBuilderSquare().
     * 1) In the worker's surroundings there are a dome, a three-floor building, an opponent worker and a worker belonging
     * to the examined player
     */
    @Test
    public void checkRemoveBuilderSquare() throws IOException {

        player1 = game.playerList.get(0);
        game.deployBuilder(player1, board.fullMap[2][2]);
        Builder builder = player1.getBuilder(0);   //soggetto

        game.deployBuilder(player1, board.fullMap[2][3]);   //aggiunta di due pedine
        game.deployBuilder(game.playerList.get(1), board.fullMap[3][2]);

        board.build(board.fullMap[1][3], false);
        board.build(board.fullMap[1][3], false);
        board.build(board.fullMap[1][3], false);   // torre di tre piani

        board.build(board.fullMap[3][1], true);   //cupola

        ArrayList<Square> posssibleMoves = rules.proximity(builder);
        posssibleMoves = rules.removeBuilderSquare(posssibleMoves);
        assertEquals(6, posssibleMoves.size());


    }

    /** checks the correct behaviour of the method removeDomeSquare().
     * 1) In the worker's surroundings there are a dome, a three-floor building, an opponent worker, a worker belonging
     * to the examined player and a complete tower.
     */
    @Test
    public void checkRemoveDomeSquare() throws IOException {

        player1 = game.playerList.get(0);
        game.deployBuilder(player1, board.fullMap[2][2]);
        Builder builder = player1.getBuilder(0);   //soggetto

        game.deployBuilder(player1, board.fullMap[2][3]);   //aggiunta di due pedine
        game.deployBuilder(game.playerList.get(1), board.fullMap[3][2]);

        board.build(board.fullMap[1][3], false);
        board.build(board.fullMap[1][3], false);
        board.build(board.fullMap[1][3], false);   // torre di tre piani con cupola
        board.build(board.fullMap[1][3], false);

        board.build(board.fullMap[3][1], true);   //cupola

        board.build(board.fullMap[1][1], false);
        board.build(board.fullMap[1][1], false);
        board.build(board.fullMap[1][1], false);   // torre di tre piani

        ArrayList<Square> posssibleMoves = rules.proximity(builder);
        posssibleMoves = rules.removeDomeSquare(posssibleMoves);
        assertEquals(6, posssibleMoves.size());
    }


/** checks the correct behaviour of the method getFreeSquares().
 * 1) Inside the board there are three workers, a complete tower and a dome
 * */

    @Test
    public void checkGetFreeSquares() throws IOException {
        player1 = game.playerList.get(0);
        game.deployBuilder(player1, board.fullMap[2][2]);
        Builder builder = player1.getBuilder(0);

        game.deployBuilder(player1, board.fullMap[2][3]);   //aggiunta di due pedine
        game.deployBuilder(game.playerList.get(1), board.fullMap[3][2]);

        board.build(board.fullMap[1][3], false);
        board.build(board.fullMap[1][3], false);
        board.build(board.fullMap[1][3], false);   // torre di tre piani con cupola
        board.build(board.fullMap[1][3], false);

        board.build(board.fullMap[3][1], true);   //cupola

        board.build(board.fullMap[1][1], false);
        board.build(board.fullMap[1][1], false);
        board.build(board.fullMap[1][1], false);   // torre di tre piani

        board.build(board.fullMap[0][0], false);
        board.build(board.fullMap[4][4], false);
        board.build(board.fullMap[0][4], false);
        board.build(board.fullMap[4][0], false);
        board.build(board.fullMap[4][4], false);

        ArrayList <Square> possibleMoves = rules.getFreeSquares();
        assertEquals(20, possibleMoves.size()); // tre pedine, un edificio con cupola e una cupola

        for(Square possibleMove: possibleMoves)
            assertEquals(0, possibleMove.getValue());

    }



    /** checks the correct behaviour of the method getBuildingRange().
     * 1) In the worker's surroundings there are two workers, a complete tower, a three-floor tower and
     * a dome.
     * */
    @Test
    public void checkGetBuildingRange() throws IOException {
        player1 = game.playerList.get(0);
        game.deployBuilder(player1, board.fullMap[2][2]);
        Builder builder = player1.getBuilder(0);   //soggetto

        game.deployBuilder(player1, board.fullMap[2][3]);   //aggiunta di due pedine
        game.deployBuilder(game.playerList.get(1), board.fullMap[3][2]);

        board.build(board.fullMap[1][3], false);
        board.build(board.fullMap[1][3], false);
        board.build(board.fullMap[1][3], false);   // torre di tre piani con cupola
        board.build(board.fullMap[1][3], false);

        board.build(board.fullMap[3][1], true);   //cupola

        board.build(board.fullMap[1][1], false);
        board.build(board.fullMap[1][1], false);
        board.build(board.fullMap[1][1], false);   // torre di tre piani

        ArrayList<Square> possibleMoves = rules.getBuildingRange(builder);
        assertEquals(4, possibleMoves.size());

        for(Square possibleMove: possibleMoves)
            assertEquals(0, possibleMove.getValue());

    }


    /** checks the correct behaviour of the method removeTooHighPlaces().
     * 1) The worker is on a one-floor building and there are a three-floor building, a two_floor building in its surroundings
     * 2) The worker is on the ground and there are a worker a three-floor tower and a two-floor tower in its surroundings
     * 3) The worker is on a three-floor building and a three-floor building and a one-floor building in its surroundings
     */
    @Test
    public void checkRemoveTooHighPlaces() throws IOException {

        //1) poniamo il builder sulla torre da un piano
        board.build(board.fullMap[2][2], false);
        player1 = game.playerList.get(0);
        game.deployBuilder(player1, board.fullMap[2][2]);   // la pedina si trova sulla torre di un piano
        Builder builder = player1.getBuilder(0);

        board.build(board.fullMap[3][3], false);   //torre di tre piani
        board.build(board.fullMap[3][3], false);
        board.build(board.fullMap[3][3], false);

        board.build(board.fullMap[1][3], false);   //torre di due piani
        board.build(board.fullMap[1][3], false);

        ArrayList<Square> possibleMoves = rules.proximity(builder);
        possibleMoves = rules.removeTooHighPlaces(possibleMoves,builder);

        assertEquals(7, possibleMoves.size());
        for(Square possibleMove : possibleMoves)
            assertNotEquals(possibleMove, board.fullMap[3][3]);


        //2)poniamo il builder a terra
        player1 = game.playerList.get(1);
        game.deployBuilder(player1, board.fullMap[0][4]); // la pedina si trova sulla torre di un piano
        builder = player1.getBuilder(0);

        board.build(board.fullMap[0][3], false); //torre di tre piani
        board.build(board.fullMap[0][3], false);
        board.build(board.fullMap[0][3], false);

        board.build(board.fullMap[1][4], false);  //torre di due piani
        board.build(board.fullMap[1][4], false);

        possibleMoves = rules.proximity(builder);
        possibleMoves = rules.removeTooHighPlaces(possibleMoves, builder);
        assertTrue(possibleMoves.isEmpty());



        //3) poniamo il builder su una torre di tre piani     (1,1):torre di tre piani     (0,1):torre di un piano    (1,0):terra
        board.build(board.fullMap[0][0], false); //torre di tre piani
        board.build(board.fullMap[0][0], false);
        board.build(board.fullMap[0][0], false);

        board.build(board.fullMap[1][1], false); //torre di tre piani
        board.build(board.fullMap[1][1], false);
        board.build(board.fullMap[1][1], false);

        board.build(board.fullMap[0][1], false);  //torre di un piano

        player1 = game.playerList.get(2);
        game.deployBuilder(player1, board.fullMap[0][0]);
        builder = player1.getBuilder(0);

        possibleMoves = rules.proximity(builder);
        possibleMoves = rules.removeTooHighPlaces(possibleMoves, builder);

        assertEquals(3, possibleMoves.size());


    }
}


