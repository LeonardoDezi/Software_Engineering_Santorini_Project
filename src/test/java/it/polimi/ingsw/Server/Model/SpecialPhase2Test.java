package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;
import it.polimi.ingsw.Server.VirtualView.NetInterface;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

//ATTENZIONE: prima di eseguire i test, commentare updateBoard() in Board.move() e Board.build() (righe 96 e 73)

/**
 * this class is used to test the behaviour of SpecialPhase2
 */
public class SpecialPhase2Test {

    /** the object tested */
    private SpecialPhase2 specialPhase2;
    /** the player used for the test*/
    private Player player1;
    /** the player used for the test*/
    private Player player2;
    /** the game used for the test */
    private Game game;
    /** the worker used for the test */
    private Builder builder;
    /** the ArrayList of Squares used for the test*/
    private ArrayList<Square> possibleMoves;
    /** the context used for the test*/
    private Context context = new Context(new NetInterface());

    private NetInterface netInterface = new NetInterface();

    /**
     * creates the players and the game used in every test
     */
    @Before
    public void create(){
        game = new Game(2, netInterface);
        game.addPlayer(new Player("Marco", "Red", game, 0));
        game.addPlayer(new Player("Luca", "Blue", game, 1));
        player1 = game.playerList.get(0);
        player2 = game.playerList.get(1);
        player1.setCard(game.getDeckCard(8));   //Pan
        player2.setCard(game.getDeckCard(2));   //Artemis
    }

    /**
     * tests the behaviour of specialPhase2 when the card is Artemis
     */
    @Test
    public void checkDoubleNotSameMove() throws IOException {
        game.deployBuilder(player2, game.gameBoard.fullMap[4][2]);
        game.gameBoard.build(game.gameBoard.fullMap[3][3], true);  //cupola
        specialPhase2 = new SpecialPhase2(game, context, player2, builder, null);
        possibleMoves = specialPhase2.getMoves(player2.getBuilder(0), game.gameBoard.fullMap[4][3]);
        assertEquals(3, possibleMoves.size());

        for(Square possibleMove : possibleMoves)
            assertNotEquals(game.gameBoard.fullMap[4][3], possibleMove);   // si accerta che position non sia in possibleMoves

        specialPhase2.actionMethod(player2.getBuilder(0), game.gameBoard.fullMap[4][1]);  //generico movimento
        assertEquals(game.gameBoard.fullMap[4][1].getBuilder(), player2.getBuilder(0));
    }


    /**
     * tests the behaviour of specialPhase2 when the card has no special effect regarding SpecialPhase2
     */
    @Test
    public void checkNull() throws IOException {
        game.deployBuilder(player1, game.gameBoard.fullMap[4][2]);
        game.gameBoard.build(game.gameBoard.fullMap[3][3], true);  //cupola
        specialPhase2 = new SpecialPhase2(game, context, player1, builder, null);
        possibleMoves = specialPhase2.getMoves(player1.getBuilder(0), game.gameBoard.fullMap[4][3]);
        assertTrue(possibleMoves.isEmpty());

    }

}
