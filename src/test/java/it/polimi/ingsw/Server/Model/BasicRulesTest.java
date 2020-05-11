package it.polimi.ingsw.Server.Model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class BasicRulesTest {

    private Game game;
    private Board board;
    private Player player1;
    private BasicRules rules;

    @Before
    public void createGameAndRules(){

        game = new Game();
        board = game.getBoard();
        game.addPlayer("Marco");
        game.addPlayer("leonardo");
        game.addPlayer("Fra");
        rules = new BasicRules(board, game);

    }

    @Test
    public void testWinCondition(){
        board.build(1, 2, false);
        board.build(1,2, false);  //costruiamo un edificio di due piani
        assertEquals(0, board.fullMap[1][2].getValue());
        assertEquals(2, board.fullMap[1][2].getLevel());  // verifichiamo che l'edificio sia costruito correttamente

        board.build(2,2, false);
        board.build(2,2, false);
        board.build(2,2, false);  // costruiamo un edifico di tre piani
        assertEquals(0, board.fullMap[2][2].getValue());
        assertEquals(3, board.fullMap[2][2].getLevel());  // verifichiamo che l'edificio sia costruito correttamente


        player1 = game.playerList.get(2);
        game.deployBuilder(player1, 1, 2);  // poniamo una pedina su la torre di due piani
        assertEquals(1, board.fullMap[1][2].getValue());  // verifichiamo che sia stata posta
        assertFalse(rules.testFlag);  // ci accertiamo che testFlag sia false
        rules.winCondition(player1.builders.get(0), 2, 2);
        assertTrue(rules.testFlag);  // ci accertiamo che testFlag sia true


        game = new Game();
        board = game.getBoard();
        game.addPlayer("Marco");
        game.addPlayer("leonardo");
        game.addPlayer("Fra");
        rules = new BasicRules(board, game);   // reinizializza per il prossimo metodo di test


    }

    //Testiamo quando la pedina si sposta in posizioni diverse dalla condizione di vittoria
    @Test
    public void testLosingConditions(){

        //primo caso: il giocatore non si trovo su un edificio di due piani
        game.deployBuilder(game.playerList.get(0), 0,1);
        board.build(1,1, false);

        assertFalse(rules.testFlag); // accertiamoci che testFlag sia falso all'inizio
        rules.winCondition(game.playerList.get(0).builders.get(0), 1 ,1);
        assertFalse(rules.testFlag); //accertiamoci che testFlag sia ancora falso in quanto non è una winCondition


        //secondo caso: il giocatore si trova su un edificio di due piani ma la sua destinazione non è un edificio di 3
        board.build(4,4, false);
        board.build(4,4, false);
        game.deployBuilder(game.playerList.get(1),4,4);
        board.build(3,1, false);
        assertFalse(rules.testFlag);
        rules.winCondition(game.playerList.get(1).builders.get(0), 3 ,1);
        assertFalse(rules.testFlag);


        game = new Game();
        board = game.getBoard();
        game.addPlayer("Marco");
        game.addPlayer("Leonardo");
        game.addPlayer("Fra");
        rules = new BasicRules(board, game);   // reinizializza per il prossimo metodo di test

    }

    @Test
    public void testPossibleMoves(){
        game.deployBuilder(game.playerList.get(0), 2,2);
        //testiamo getPossibleMoves nel caso di una pedina circondata da caselle vuote
        rules.getMovementRange(game.playerList.get(0));
        ArrayList<Square> moves = rules.getFirstPossibleMoves();
        assertEquals(8, moves.size()); // verifichiamo che ci siano 8 mosse disponibili

        for(int i = 0; i < 8; i++) {
            assertNotSame(moves.get(i), board.fullMap[2][2]);  // verifichiamo che nessuna delle caselle sia quella in cui la pedina è contenuta
            assertTrue(Math.abs(moves.get(i).x - 2) <= 1);  // verifichiamo che le mosse contenute nell'array siano solo di caselle adiacenti
            assertTrue(Math.abs(moves.get(i).y - 2) <= 1);
        }
        
        assertSame(moves.get(0), board.fullMap[1][1]);    //non so se può essere considerato un buon caso di test
        assertSame(moves.get(1), board.fullMap[1][2]);
        assertSame(moves.get(2), board.fullMap[1][3]);
        assertSame(moves.get(3), board.fullMap[2][1]);
        assertSame(moves.get(4), board.fullMap[2][3]);
        assertSame(moves.get(5), board.fullMap[3][1]);
        assertSame(moves.get(6), board.fullMap[3][2]);
        assertSame(moves.get(7), board.fullMap[3][3]);




    }
}
