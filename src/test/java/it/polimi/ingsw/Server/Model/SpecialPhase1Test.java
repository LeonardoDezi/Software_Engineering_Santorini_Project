package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;
import it.polimi.ingsw.Server.VirtualView.NetInterface;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

//ATTENZIONE: prima di eseguire i test, commentare updateBoard() in Board.move() e Board.build() (righe 96 e 73)

/**
 * this class is used to test the behaviour of SpecialPhase1
 */
public class SpecialPhase1Test {

    /** the object tested */
    private SpecialPhase1 specialPhase1;

    /** the game used for the test */
    private Game game;

    /** the context used for the test*/
    private Context context = new Context(new NetInterface());

    /** the player used for the test*/
    private Player player1;
    /** the player used for the test*/
    private Player player2;
    /** the player used for the test*/
    private Player player3;
    /** the worker used for the test*/
    private Builder builder;
    /** the ArrayList of Squares used for the test*/
    private ArrayList<Square> possibleMoves;

    private NetInterface netInterface = new NetInterface();


    /**
     * creates the players used in every test and sets the cards of player2 and player3 to Pan and Chronus
     */
    @Before
    public void create(){
        game = new Game(3, netInterface);
        game.addPlayer(new Player("Marco", "Red", game, 0));
        game.addPlayer(new Player("Luca", "Blue", game, 1));
        game.addPlayer(new Player("Fra", "Green", game, 2));
        player1 = game.playerList.get(0);
        player2 = game.playerList.get(1);
        player3 = game.playerList.get(2);
        player2.setCard(game.getDeckCard(8));
        player3.setCard(game.getDeckCard(11));
    }



    /**
     * tests the fact that when the player does not have a card with a special effect in specialPhase1,
     * getMoves() will return an empty ArrayList. Also, if the worker passed is null, getMoves() returns an empty arrayList
     */
    @Test
    public void testNull() throws IOException {

        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        builder = player1.getBuilder(0);
        player1.setCard(game.getDeckCard(2));  //Artemide: nessun effetto special in questa fase

        specialPhase1 = new SpecialPhase1(game, context, player1, builder, null);
        possibleMoves = specialPhase1.getMoves(builder);
        assertTrue(possibleMoves.isEmpty());

        possibleMoves = specialPhase1.getMoves(null);
        assertTrue(possibleMoves.isEmpty());
    }

    /**
     * tests the behaviour of getMoves() when the card is Prometheus
     */
    @Test
    public void testAdditionalBuild() throws IOException {
        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        builder = player1.getBuilder(0);
        player1.setCard(game.getDeckCard(9));  //Prometeo
        specialPhase1 = new SpecialPhase1(game, context, player1, builder, null);
        possibleMoves = specialPhase1.getMoves(builder);
        assertEquals(8, possibleMoves.size());
    }

    /**
     * tests the behaviour of getMoves() when the card is Athena
     */
    @Test
    public void checkRestore() throws IOException {
        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        builder = player1.getBuilder(0);
        player1.setCard(game.getDeckCard(3));
        specialPhase1 = new SpecialPhase1(game, context, player1, builder, null);
        possibleMoves = specialPhase1.getMoves(builder);
        assertTrue(possibleMoves.isEmpty());
    }

    /**
     * tests the behaviour of getMoves() when the card is Charon
     */
    @Test
    public void checkOppositeSideMoves() throws IOException {
        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        builder = player1.getBuilder(0);
        player1.setCard(game.getDeckCard(10));
        game.deployBuilder(player1, game.gameBoard.fullMap[3][3]);   //pedina dello stesso giocatore
        game.deployBuilder(game.playerList.get(1), game.gameBoard.fullMap[3][2]);  //pedina valida
        game.deployBuilder(game.playerList.get(1), game.gameBoard.fullMap[3][1]);  //pedina con spazio opposto occupato da pedina
        game.deployBuilder(game.playerList.get(2), game.gameBoard.fullMap[1][3]);
        specialPhase1 = new SpecialPhase1(game, context, player1, player1.getBuilder(0), player1.getBuilder(1));
        possibleMoves = specialPhase1.getMoves(builder);
        assertEquals(1, possibleMoves.size());
        assertEquals(possibleMoves.get(0), game.gameBoard.fullMap[3][2]);
    }


    /**
     * other tests of the behaviour of getMoves() when the card is Charon
     */
    @Test
    public void checkOppositeSideMoves2() throws IOException {
        player1.setCard(game.getDeckCard(10));
        game.deployBuilder(player1, game.gameBoard.fullMap[4][2]);  //test di una pedina in un lato
        game.deployBuilder(game.playerList.get(1), game.gameBoard.fullMap[4][3]);

        specialPhase1 = new SpecialPhase1(game, context, player1, builder, null);
        possibleMoves = specialPhase1.getMoves(player1.getBuilder(0));
        assertEquals(1, possibleMoves.size());
        assertEquals(possibleMoves.get(0), game.gameBoard.fullMap[4][3]);

        game.deployBuilder(game.playerList.get(1), game.gameBoard.fullMap[1][2]);
        game.deployBuilder(game.playerList.get(2), game.gameBoard.fullMap[0][2]);  // test con torre completa

        game.gameBoard.build(game.gameBoard.fullMap[2][2], false);
        game.gameBoard.build(game.gameBoard.fullMap[2][2], false);  //torre completa
        game.gameBoard.build(game.gameBoard.fullMap[2][2], false);
        game.gameBoard.build(game.gameBoard.fullMap[2][2], false);

        game.playerList.get(1).setCard(game.getDeckCard(10));
        specialPhase1 = new SpecialPhase1(game, context, player2, player2.getBuilder(0), player2.getBuilder(1));
        possibleMoves = specialPhase1.getMoves(player2.getBuilder(1));
        assertTrue(possibleMoves.isEmpty());

        game.deployBuilder(player1, game.gameBoard.fullMap[0][0]);   // test di una pedina in angolo
        game.deployBuilder(player3,game.gameBoard.fullMap[1][1]);
        specialPhase1 = new SpecialPhase1(game, context, player1, player1.getBuilder(0), player1.getBuilder(1));

        possibleMoves = specialPhase1.getMoves(player1.getBuilder(1));
        assertTrue(possibleMoves.isEmpty());

    }

    /**
     * tests the behaviour of actionMethod() when the card is Prometheus.
     * Checks that the building has been made
     * Checks that basicRules.maxHeight is now 0 nad previousMaxHeight 1.
     */
    @Test
    public void checkSpecialBuild() throws IOException {
        player1.setCard(game.getDeckCard(9));

        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        specialPhase1 = new SpecialPhase1(game, context, player1, player1.getBuilder(0), null);
        possibleMoves = specialPhase1.getMoves(player1.getBuilder(0));

        assertEquals(0, game.gameBoard.fullMap[3][3].getLevel());

        specialPhase1.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[3][3]);

        assertEquals(1, game.gameBoard.fullMap[3][3].getLevel());

        assertEquals(0, game.getRules().getMaxHeight());
        assertEquals(1, game.getRules().getPreviousMaxHeight());
    }

    /**
     * tests the behaviour of actionMethod() when the card is Charon.
     * checks that the opponent worker has been moved
     */
    @Test
    public void checkMoveOpposite() throws IOException {
        player1.setCard(game.getDeckCard(10));
        game.deployBuilder(player1, game.gameBoard.fullMap[4][2]);  //scenario preso da checkOppositeSideMoves2
        game.deployBuilder(game.playerList.get(1), game.gameBoard.fullMap[4][3]);
        builder = player1.getBuilder(0);

        specialPhase1 = new SpecialPhase1(game, context, player1, player1.getBuilder(0), null);

        possibleMoves = specialPhase1.getMoves(player1.getBuilder(0));
        specialPhase1.actionMethod(builder, possibleMoves.get(0));
        assertNull(game.gameBoard.fullMap[4][3].getBuilder());  //controlla che il worker non si trovi più qui
        assertEquals(game.playerList.get(1).getBuilder(0), game.gameBoard.fullMap[4][1].getBuilder());  //controlla che il worker si trovi qui

    }
}

