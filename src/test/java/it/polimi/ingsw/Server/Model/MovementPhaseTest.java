package it.polimi.ingsw.Server.Model;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class MovementPhaseTest {
    MovementPhase movementPhase;
    MovementPhase movementPhase1;
    Game game;
    Game game1;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    Player player5;
    ArrayList<Square> possibleMoves;

    @Before
    public void create(){
        game = new Game(2);
        game1 = new Game(3);
        movementPhase = new MovementPhase(game);
        movementPhase1 = new MovementPhase((game1));
        player1 = new Player("Marco", "Red", game, 0);
        player2 = new Player("Luca", "Blue", game, 1);

        player3 = new Player("Fra", "Black", game1, 2);
        player4 = new Player("Luca", "Blue", game1, 1);
        player5 = new Player("Marco", "Red", game1, 0);
        game.addPlayer(player1);
        game.addPlayer(player2);

        game1.addPlayer(player3);
        game1.addPlayer(player4);
        game1.addPlayer(player5);
    }

    @Test
    public void checkSwapMoves(){
        player1.setCard(game.getDeckCard(1));  //Apollo
        player2.setCard(game.getDeckCard(7));   //Minotauro

        game.deployBuilder(player1, game.gameBoard.fullMap[0][0]);  // il giocatore si trova a terra
        //1)pedina di giocatore avversario
        game.deployBuilder(player2, game.gameBoard.fullMap[0][1]);

        game.gameBoard.build(game.gameBoard.fullMap[1][0], false);
        game.gameBoard.build(game.gameBoard.fullMap[1][0], false);  //torre di due piani

        possibleMoves = movementPhase.getMoves(player1, player1.getBuilder(0));
        assertEquals(2, possibleMoves.size());  //si accerta che il giocatore ha due mosse disponibili

        //2) pedina dello stesso giocatore
        game.deployBuilder(player1, game.gameBoard.fullMap[1][1]);  // pedina dello stesso giocatore
        possibleMoves = movementPhase.getMoves(player1, player1.getBuilder(0));
        assertEquals(1, possibleMoves.size());

    }

    @Test
    public void checkSwapMoves2(){

        player3.setCard(game1.getDeckCard(1));  //Apollo
        player4.setCard(game1.getDeckCard(7));   //Minotauro

        game1.deployBuilder(player4, game1.gameBoard.fullMap[2][2]);  //soggetto

        game1.deployBuilder(player3, game1.gameBoard.fullMap[3][2]);   //pedina valida

        game1.deployBuilder(player3, game1.gameBoard.fullMap[3][3]);   //pedina bloccata

        game1.gameBoard.build(game1.gameBoard.fullMap[4][4], true); //bloccata da cupola

        game1.deployBuilder(player5, game1.gameBoard.fullMap[1][3]);    //pedina bloccata
        game1.deployBuilder(player4, game1.gameBoard.fullMap[0][4]);   //pedina bloccante

        game1.gameBoard.build(game1.gameBoard.fullMap[3][1], false);  //casella non valida
        game1.gameBoard.build(game1.gameBoard.fullMap[3][1], false);

        possibleMoves = movementPhase1.getMoves(player4, player4.getBuilder(0));  //verifica
        assertEquals(5, possibleMoves.size());


    }


    @Test
    public void checkSwapMoves3(){
        player3.setCard(game1.getDeckCard(1));  //Apollo
        player4.setCard(game1.getDeckCard(7));   //Minotauro

        //la pedina si trova al confine della plancia
        game1.deployBuilder(player4, game1.gameBoard.fullMap[3][2]);  //soggetto

        game1.deployBuilder(player3, game1.gameBoard.fullMap[4][2]);  //pedina al confine della plancia
        game1.deployBuilder(player4, game1.gameBoard.fullMap[3][3]); //pedina dello stesso giocatore

        game1.gameBoard.build(game1.gameBoard.fullMap[2][1], true);
        game1.gameBoard.build(game1.gameBoard.fullMap[3][1], true);
        game1.gameBoard.build(game1.gameBoard.fullMap[4][1], true);   //recinto di cupole
        game1.gameBoard.build(game1.gameBoard.fullMap[2][2], true);
        game1.gameBoard.build(game1.gameBoard.fullMap[2][3], true);
        game1.gameBoard.build(game1.gameBoard.fullMap[4][3], true);

        possibleMoves = movementPhase1.getMoves(player4, player4.getBuilder(0));
        assertTrue(possibleMoves.isEmpty());
    }

    @Test
    public void checkNull(){

        player1.setCard(game.getDeckCard(6));
        player2.setCard(game.getDeckCard(3));

        game.gameBoard.build(game.gameBoard.fullMap[1][3], false); //torre di un blocco
        game.deployBuilder(player1, game.gameBoard.fullMap[1][3]);  //soggetto pedina su torre

        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);  //torre di tre inaccessibile
        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);

        game.deployBuilder(player1, game.gameBoard.fullMap[2][3]);   //altra pedina

        game.gameBoard.build(game.gameBoard.fullMap[1][2], false);   //torre di due accessibile
        game.gameBoard.build(game.gameBoard.fullMap[1][2], false);

        game.gameBoard.build(game.gameBoard.fullMap[2][2], true);    //cupola inaccessibile

        possibleMoves = movementPhase.getMoves(player1, player1.getBuilder(0));
        assertEquals(5, possibleMoves.size());
    }

    @Test
    public void checkJumpUp(){
        player1.setCard(game.getDeckCard(3));
        player2.setCard(game.getDeckCard(5));


        game.gameBoard.build(game.gameBoard.fullMap[2][3], false);   //torre di un blocco
        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        game.deployBuilder(player1, game.gameBoard.fullMap[3][3]);
        movementPhase.getMoves(player1, player1.getBuilder(0));

        //1) mossa normale
        assertEquals(1, movementPhase.getBasicRules().getMaxHeight());  // controlla che sia 1
        movementPhase.actionMethod(player1.getBuilder(1), game.gameBoard.fullMap[4][3]);
        assertEquals(1, movementPhase.getBasicRules().getMaxHeight());



        //2) mossa speciale
        assertEquals(1, movementPhase.getBasicRules().getMaxHeight());  // controlla che sia 1
        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[2][3]);
        assertEquals(0, movementPhase.getBasicRules().getMaxHeight());
    }


    @Test
    public void checkPushAction() {
        player3.setCard(game1.getDeckCard(7));
        player4.setCard(game1.getDeckCard(5));
        player5.setCard(game1.getDeckCard(10));

        game1.deployBuilder(player3, game1.gameBoard.fullMap[3][3]);     //soggetto

        game1.deployBuilder(player4, game1.gameBoard.fullMap[4][4]);
        game1.deployBuilder(player4, game1.gameBoard.fullMap[2][3]);   //pedina spostabile
        game1.deployBuilder(player5, game1.gameBoard.fullMap[3][2]);   //pedina spostabile
        game1.deployBuilder(player5, game1.gameBoard.fullMap[2][2]);    //pedina spostabile

        possibleMoves = movementPhase1.getMoves(player3, player3.getBuilder(0));
        assertEquals(7, possibleMoves.size());

        movementPhase1.actionMethod(player3.getBuilder(0), game1.gameBoard.fullMap[2][3]);  //pedina 1
        assertEquals(player4.getBuilder(1), game1.gameBoard.fullMap[1][3].getBuilder()); // controlla che la pedina sia stata spostata
        assertEquals(player3.getBuilder(0), game1.gameBoard.fullMap[2][3].getBuilder());   //controlla che il soggetto sia stato spostato nella posizione corretta

        game1.gameBoard.move(game1.gameBoard.fullMap[2][3], game1.gameBoard.fullMap[3][3]);    //riporta il soggetto alla condizione originale
        movementPhase1.actionMethod(player3.getBuilder(0), game1.gameBoard.fullMap[3][2]);  //pedina 2
        assertEquals(player5.getBuilder(0), game1.gameBoard.fullMap[3][1].getBuilder()); // controlla che la pedina sia stata spostata
        assertEquals(player3.getBuilder(0), game1.gameBoard.fullMap[3][2].getBuilder());

        game1.gameBoard.move(game1.gameBoard.fullMap[3][2], game1.gameBoard.fullMap[3][3]);
        movementPhase1.actionMethod(player3.getBuilder(0), game1.gameBoard.fullMap[2][2]);  //pedina 3
        assertEquals(player5.getBuilder(1), game1.gameBoard.fullMap[1][1].getBuilder()); // controlla che la pedina sia stata spostata
        assertEquals(player3.getBuilder(0), game1.gameBoard.fullMap[2][2].getBuilder());

        game1.gameBoard.move(game1.gameBoard.fullMap[3][2], game1.gameBoard.fullMap[3][3]);
        movementPhase1.actionMethod(player3.getBuilder(0), game1.gameBoard.fullMap[3][4]);   //movimento standard
        assertEquals(player3.getBuilder(0), game1.gameBoard.fullMap[3][4].getBuilder());
        assertEquals(0, game1.gameBoard.fullMap[3][3].getValue());
    }

    @Test
    public void checkNullAction(){
        player1.setCard(game.getDeckCard(1));
        player2.setCard(game.getDeckCard(13));

        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);

        game.gameBoard.build(game.gameBoard.fullMap[3][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[3][3], false);   // torre di due piani
        game.deployBuilder(player2, game.gameBoard.fullMap[3][3]);

        movementPhase.getMoves(player1, player1.getBuilder(0));
        movementPhase.getMoves(player2, player2.getBuilder(0));

        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[2][3]);   // condizione base
        assertEquals(player1.getBuilder(0), game.gameBoard.fullMap[2][3].getBuilder());
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());



        game.gameBoard.build(game.gameBoard.fullMap[4][4], false);
        game.gameBoard.build(game.gameBoard.fullMap[4][4], false);    //condizione di vittoria
        game.gameBoard.build(game.gameBoard.fullMap[4][4], false);

        movementPhase.actionMethod(player2.getBuilder(0), game.gameBoard.fullMap[4][4]);

        assertEquals(player2.getBuilder(0), game.gameBoard.fullMap[4][4].getBuilder());
        assertTrue(game.getGameEnded());
        assertEquals(player2, game.getWinningPlayer());
    }


    @Test
    public void checkRestore(){
        player1.setCard(game.getDeckCard(9));
        player2.setCard(game.getDeckCard(13));
        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        movementPhase.getMoves(player1, player1.getBuilder(0));

        movementPhase.getBasicRules().setPreviousMaxHeight(1);    //primo caso
        movementPhase.getBasicRules().setMaxHeight(0);
        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[4][4]);

        assertEquals(1, game.getRules().getMaxHeight());   //in questo modo ci accertiamo anche che game.basicRules = movementphase.basicRULES
        assertEquals(1, game.getRules().getPreviousMaxHeight());

        movementPhase.getBasicRules().setPreviousMaxHeight(0);    //secondo caso
        movementPhase.getBasicRules().setMaxHeight(0);
        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[4][3]);
        assertEquals(0, game.getRules().getMaxHeight());   //caso in cui Athena abbia modificato maxHeight
        assertEquals(1, game.getRules().getPreviousMaxHeight());
    }

    @Test
    public void checkWinPan(){
        player1.setCard(game.getDeckCard(8));
        player2.setCard(game.getDeckCard(13));

        game.gameBoard.build(game.gameBoard.fullMap[2][2], false);
        game.gameBoard.build(game.gameBoard.fullMap[2][2], false);   //torre di due

        game.gameBoard.build(game.gameBoard.fullMap[3][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[3][3], false);     //torre di tre
        game.gameBoard.build(game.gameBoard.fullMap[3][3], false);

        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        movementPhase.getMoves(player1, player1.getBuilder(0));


        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[3][3]);   // condizione di vincita normale
        assertTrue(game.getGameEnded());
        assertEquals(player1, game.getWinningPlayer());


        game.setWinningPlayer(null);  //reinizializzazione
        game.setGameEnded(false);
        game.gameBoard.move(game.gameBoard.fullMap[3][3],game.gameBoard.fullMap[2][2]);

        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[2][3]);   //caso di vincita speciale
        assertTrue(game.getGameEnded());
        assertEquals(player1, game.getWinningPlayer());

        game.setWinningPlayer(null);  //reinizializzazione
        game.setGameEnded(false);

        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[2][4]);  //caso di non vincita
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());


    }

}
