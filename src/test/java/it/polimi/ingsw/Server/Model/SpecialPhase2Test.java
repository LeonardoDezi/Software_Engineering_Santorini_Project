package it.polimi.ingsw.Server.Model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SpecialPhase2Test {
    private SpecialPhase2 specialPhase2;
    private Player player1;
    private Player player2;
    private Game game;
    private Builder builder;
    private ArrayList<Square> possibleMoves;


    @Before
    public void create(){
        game = new Game(2);
        specialPhase2 = new SpecialPhase2(game);
        game.addPlayer(new Player("Marco", "Red", game, 0));
        game.addPlayer(new Player("Luca", "Blue", game, 1));
        player1 = game.playerList.get(0);
        player2 = game.playerList.get(1);
        player1.setCard(game.getDeckCard(8));
        player2.setCard(game.getDeckCard(2));   //Artemis
    }


    @Test
    public void checkDoubleNotSameMove(){
        game.deployBuilder(player2, game.gameBoard.fullMap[4][2]);
        game.gameBoard.build(game.gameBoard.fullMap[3][3], true);  //cupola
        possibleMoves = specialPhase2.getMoves(player2, player2.getBuilder(0),game.gameBoard.fullMap[4][3]);
        assertEquals(3, possibleMoves.size());

        for(Square possibleMove : possibleMoves)
            assertNotEquals(game.gameBoard.fullMap[4][3], possibleMove);   // si accerta che position non sia in possibleMoves
        specialPhase2.actionMethod(player2.getBuilder(0), game.gameBoard.fullMap[4][1]);
        assertEquals(game.gameBoard.fullMap[4][1].getBuilder(), player2.getBuilder(0));
    }
}
