package it.polimi.ingsw.Server.Model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class SpecialPhase1Test {
    private SpecialPhase1 specialPhase1;
    private Player player1;
    private Player player2;
    private Player player3;
    private Game game;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    @Before
    public void create(){
        game = new Game(3);
        specialPhase1 = new SpecialPhase1(game);
        game.addPlayer(new Player("Marco", "Red", game, 0));
        game.addPlayer(new Player("Luca", "Blue", game, 1));
        game.addPlayer(new Player("Fra", "Green", game, 2));
        player1 = game.playerList.get(0);
        player2 = game.playerList.get(1);
        player3 = game.playerList.get(2);
        player2.setCard(game.getDeckCard(8));
        player3.setCard(game.getDeckCard(11));
    }

    @Test
    public void testNull(){
        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        builder = player1.getBuilder(0);
        player1.setCard(game.getDeckCard(2));
        possibleMoves = specialPhase1.getMoves(player1, builder);
        assertTrue(possibleMoves.isEmpty());
    }

    @Test
    public void testAdditionalBuild(){
        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        builder = player1.getBuilder(0);
        player1.setCard(game.getDeckCard(9));
        possibleMoves = specialPhase1.getMoves(player1, builder);
        assertEquals(8, possibleMoves.size());
    }

    @Test
    public void checkRestore(){
        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        builder = player1.getBuilder(0);
        player1.setCard(game.getDeckCard(3));
        possibleMoves = specialPhase1.getMoves(player1, builder);
        assertTrue(possibleMoves.isEmpty());
    }

    @Test
    public void checkOppositeSideMoves(){
        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        builder = player1.getBuilder(0);
        player1.setCard(game.getDeckCard(10));
        game.deployBuilder(player1, game.gameBoard.fullMap[3][3]);   //pedina dello stesso giocatore
        game.deployBuilder(game.playerList.get(1), game.gameBoard.fullMap[3][2]);  //pedina valida
        game.deployBuilder(game.playerList.get(1), game.gameBoard.fullMap[3][1]);  //pedina con spazio opposto occupato da pedina
        game.deployBuilder(game.playerList.get(2), game.gameBoard.fullMap[1][3]);
        possibleMoves = specialPhase1.getMoves(player1, builder);
        assertEquals(1, possibleMoves.size());
        assertEquals(possibleMoves.get(0), game.gameBoard.fullMap[3][2]);
    }

    @Test
    public void checkOppositeSideMoves2(){
        player1.setCard(game.getDeckCard(10));
        game.deployBuilder(player1, game.gameBoard.fullMap[4][2]);  //test di una pedina in un lato
        game.deployBuilder(game.playerList.get(1), game.gameBoard.fullMap[4][3]);

        possibleMoves = specialPhase1.getMoves(player1, player1.getBuilder(0));
        assertEquals(1, possibleMoves.size());
        assertEquals(possibleMoves.get(0), game.gameBoard.fullMap[4][3]);

        game.deployBuilder(game.playerList.get(1), game.gameBoard.fullMap[1][2]);
        game.deployBuilder(game.playerList.get(2), game.gameBoard.fullMap[0][2]);  // test con torre completa

        game.gameBoard.build(game.gameBoard.fullMap[2][2], false);
        game.gameBoard.build(game.gameBoard.fullMap[2][2], false);  //torre completa
        game.gameBoard.build(game.gameBoard.fullMap[2][2], false);
        game.gameBoard.build(game.gameBoard.fullMap[2][2], false);

        game.playerList.get(1).setCard(game.getDeckCard(10));
        possibleMoves = specialPhase1.getMoves(game.playerList.get(1), game.playerList.get(1).getBuilder(1));
        assertTrue(possibleMoves.isEmpty());

        game.deployBuilder(player1, game.gameBoard.fullMap[0][0]);   // test di una pedina in angolo
        game.deployBuilder(game.playerList.get(2),game.gameBoard.fullMap[1][1]);
        possibleMoves = specialPhase1.getMoves(player1, player1.getBuilder(1));
        assertTrue(possibleMoves.isEmpty());

    }

    @Test
    public void checkSpecialBuild(){
        player1.setCard(game.getDeckCard(9));
        //1) casella presente in possibleMoves
        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        possibleMoves = specialPhase1.getMoves(player1, player1.getBuilder(0));
        specialPhase1.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[3][3]);
        assertEquals(1, game.gameBoard.fullMap[3][3].getLevel());
        assertEquals(0, game.getRules().getMaxHeight());
    }

    @Test
    public void checkMoveOpposite(){
        player1.setCard(game.getDeckCard(10));
        game.deployBuilder(player1, game.gameBoard.fullMap[4][2]);  //scenario preso da checkOppositeSideMoves2
        game.deployBuilder(game.playerList.get(1), game.gameBoard.fullMap[4][3]);
        builder = player1.getBuilder(0);

        possibleMoves = specialPhase1.getMoves(player1, player1.getBuilder(0));
        specialPhase1.actionMethod(builder, possibleMoves.get(0));
        assertNull(game.gameBoard.fullMap[4][3].getBuilder());
        assertEquals(game.playerList.get(1).getBuilder(0), game.gameBoard.fullMap[4][1].getBuilder() );

    }
}

