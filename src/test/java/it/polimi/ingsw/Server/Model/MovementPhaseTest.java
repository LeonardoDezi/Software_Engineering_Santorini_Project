package it.polimi.ingsw.Server.Model;


import it.polimi.ingsw.Server.Controller.Context;
import it.polimi.ingsw.Server.VirtualView.NetInterface;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;


/**
 * this class is used to test the behaviour of MovementPhase
 */
public class MovementPhaseTest {
    /** the object tested */
    MovementPhase movementPhase;
    /** the object tested */
    MovementPhase movementPhase1;
    /** the two-player game used for the test */
    Game game;
    /** the three-player game used for the test */
    Game game1;
    /** the player used for the test*/
    Player player1;
    /** the player used for the test*/
    Player player2;
    /** the player used for the test*/
    Player player3;
    /** the player used for the test*/
    Player player4;
    /** the player used for the test*/
    Player player5;
    /** the ArrayList of Squares used for the test*/
    ArrayList<Square> possibleMoves;
    /** the context used for the test*/
    private Context context = new Context(new NetInterface(game));
    /** the context used for the test*/
    private Context context1 = new Context(new NetInterface(game1));


    /**
     * creates the players and the games used in every test
     */
    @Before
    public void create(){
        game = new Game(2);
        game1 = new Game(3);

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


    /**
     * tests the behaviour of getMoves() when the card is Apollo
     */
    @Test
    public void checkSwapMoves(){
        player1.setCard(game.getDeckCard(1));  //Apollo
        player2.setCard(game.getDeckCard(7));   //Minotauro

        game.deployBuilder(player1, game.gameBoard.fullMap[0][0]);  // soggetto: il giocatore si trova a terra

        //Test 1: pedina di giocatore avversario
        game.deployBuilder(player2, game.gameBoard.fullMap[0][1]);

        game.gameBoard.build(game.gameBoard.fullMap[1][0], false);
        game.gameBoard.build(game.gameBoard.fullMap[1][0], false);  //torre di due piani

        movementPhase = new MovementPhase(game, context, player1, player1.getBuilder(0), null);
        possibleMoves = movementPhase.getMoves(player1.getBuilder(0));
        assertEquals(2, possibleMoves.size());  //si accerta che il giocatore ha due mosse disponibili


        //Test 2: pedina dello stesso giocatore
        game.deployBuilder(player1, game.gameBoard.fullMap[1][1]);  // pedina dello stesso giocatore
        possibleMoves = movementPhase.getMoves(player1.getBuilder(0));
        assertEquals(1, possibleMoves.size());

    }

    /**
     * tests the behaviour of getMoves() when the card is Minotaur
     */
    @Test
    public void checkSwapMoves2(){

        player3.setCard(game1.getDeckCard(1));  //Apollo
        player4.setCard(game1.getDeckCard(7));   //Minotauro

        game1.deployBuilder(player4, game1.gameBoard.fullMap[2][2]);  //soggetto

        game1.deployBuilder(player3, game1.gameBoard.fullMap[3][2]);   //pedina valida

        game1.deployBuilder(player3, game1.gameBoard.fullMap[3][3]);   //pedina bloccata
        game1.gameBoard.build(game1.gameBoard.fullMap[4][4], true); //cupola bloccante la pedina

        game1.deployBuilder(player5, game1.gameBoard.fullMap[1][3]);    //pedina bloccata
        game1.deployBuilder(player4, game1.gameBoard.fullMap[0][4]);   //pedina bloccante la pedina

        game1.gameBoard.build(game1.gameBoard.fullMap[3][1], false);  //casella non valida
        game1.gameBoard.build(game1.gameBoard.fullMap[3][1], false);

        movementPhase1 = new MovementPhase(game1, context, player4, player4.getBuilder(0), null);
        possibleMoves = movementPhase1.getMoves(player4.getBuilder(0));  //verifica
        assertEquals(5, possibleMoves.size());

    }

    /**
     * other tests of the behaviour of getMoves() when the card is Minotaur
     */
    @Test
    public void checkSwapMoves3(){
        player3.setCard(game1.getDeckCard(1));  //Apollo
        player4.setCard(game1.getDeckCard(7));   //Minotauro

        //la pedina si trova a una casella di distanza dal confine della plancia
        game1.deployBuilder(player4, game1.gameBoard.fullMap[3][2]);  //soggetto

        game1.deployBuilder(player3, game1.gameBoard.fullMap[4][2]);  //pedina al confine della plancia
        game1.deployBuilder(player4, game1.gameBoard.fullMap[3][3]); //pedina dello stesso giocatore

        game1.gameBoard.build(game1.gameBoard.fullMap[2][1], true);
        game1.gameBoard.build(game1.gameBoard.fullMap[3][1], true);
        game1.gameBoard.build(game1.gameBoard.fullMap[4][1], true);   //occupa le restanti caselle con cupole
        game1.gameBoard.build(game1.gameBoard.fullMap[2][2], true);
        game1.gameBoard.build(game1.gameBoard.fullMap[2][3], true);
        game1.gameBoard.build(game1.gameBoard.fullMap[4][3], true);

        movementPhase1 = new MovementPhase(game1, context, player4, player4.getBuilder(0), null);
        possibleMoves = movementPhase1.getMoves(player4.getBuilder(0));
        assertTrue(possibleMoves.isEmpty());
    }

    /**
     * tests the behaviour of getMoves() when the card has no special effect regarding MovementPhase
     */
    @Test
    public void checkNull(){

        player1.setCard(game.getDeckCard(6));
        player2.setCard(game.getDeckCard(3));

        game.gameBoard.build(game.gameBoard.fullMap[1][3], false); //torre di un blocco

        game.deployBuilder(player1, game.gameBoard.fullMap[1][3]);  //soggetto: pedina su torre

        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);  //torre di tre inaccessibile
        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);

        game.deployBuilder(player1, game.gameBoard.fullMap[2][3]);   //altra pedina

        game.gameBoard.build(game.gameBoard.fullMap[1][2], false);   //torre di due accessibile
        game.gameBoard.build(game.gameBoard.fullMap[1][2], false);

        game.gameBoard.build(game.gameBoard.fullMap[2][2], true);    //cupola inaccessibile

        movementPhase = new MovementPhase(game, context, player1, player1.getBuilder(0), player1.getBuilder(1));
        possibleMoves = movementPhase.getMoves(player1.getBuilder(0));
        assertEquals(5, possibleMoves.size());
    }


    /**
     * tests the behaviour of actionMethod() when the card is Athena
     */
    @Test
    public void checkJumpUp(){
        player1.setCard(game.getDeckCard(3)); //Athena
        player2.setCard(game.getDeckCard(5)); //Demeter

        game.gameBoard.build(game.gameBoard.fullMap[2][3], false);   //torre di un blocco
        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        game.deployBuilder(player1, game.gameBoard.fullMap[3][3]);
        movementPhase = new MovementPhase(game, context, player1, player1.getBuilder(0), player1.getBuilder(1));

        movementPhase.getMoves(player1.getBuilder(0));

        //1) mossa normale
        assertEquals(1, movementPhase.getBasicRules().getMaxHeight());  // controlla che sia 1
        movementPhase.actionMethod(player1.getBuilder(1), game.gameBoard.fullMap[4][3]);
        assertEquals(1, movementPhase.getBasicRules().getMaxHeight());  //controlla che rimanga 1



        //2) mossa speciale
        assertEquals(1, movementPhase.getBasicRules().getMaxHeight());  // controlla che sia 1
        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[2][3]);
        assertEquals(0, movementPhase.getBasicRules().getMaxHeight());  //controlla che diventi 0
    }

    /**
     * tests the behaviour of actionMethod() when the card is Minotaur
     */
    @Test
    public void checkPushAction() {
        player3.setCard(game1.getDeckCard(7));
        player4.setCard(game1.getDeckCard(5));
        player5.setCard(game1.getDeckCard(10));

        game1.deployBuilder(player3, game1.gameBoard.fullMap[3][3]);     //soggetto

        game1.deployBuilder(player4, game1.gameBoard.fullMap[4][4]);    //pedina non spostabile : confine della plancia
        game1.deployBuilder(player4, game1.gameBoard.fullMap[2][3]);   //pedina spostabile 1
        game1.deployBuilder(player5, game1.gameBoard.fullMap[3][2]);   //pedina spostabile 2
        game1.deployBuilder(player5, game1.gameBoard.fullMap[2][2]);    //pedina spostabile 3

        movementPhase1 = new MovementPhase(game1, context, player3, player3.getBuilder(0), null);
        possibleMoves = movementPhase1.getMoves(player3.getBuilder(0));
        assertEquals(7, possibleMoves.size());

        movementPhase1.actionMethod(player3.getBuilder(0), game1.gameBoard.fullMap[2][3]);  //push pedina 1
        assertEquals(player4.getBuilder(1), game1.gameBoard.fullMap[1][3].getBuilder()); // controlla che la pedina sia stata spostata
        assertEquals(player3.getBuilder(0), game1.gameBoard.fullMap[2][3].getBuilder());   //controlla che il soggetto sia stato spostato nella posizione corretta

        game1.gameBoard.move(game1.gameBoard.fullMap[2][3], game1.gameBoard.fullMap[3][3]);    //riporta il soggetto alla condizione originale
        movementPhase1.actionMethod(player3.getBuilder(0), game1.gameBoard.fullMap[3][2]);  //push pedina 2
        assertEquals(player5.getBuilder(0), game1.gameBoard.fullMap[3][1].getBuilder()); // controlla che la pedina sia stata spostata
        assertEquals(player3.getBuilder(0), game1.gameBoard.fullMap[3][2].getBuilder());

        game1.gameBoard.move(game1.gameBoard.fullMap[3][2], game1.gameBoard.fullMap[3][3]);   //riporta il soggetto alla condizione originale
        movementPhase1.actionMethod(player3.getBuilder(0), game1.gameBoard.fullMap[2][2]);  //push pedina 3
        assertEquals(player5.getBuilder(1), game1.gameBoard.fullMap[1][1].getBuilder()); // controlla che la pedina sia stata spostata
        assertEquals(player3.getBuilder(0), game1.gameBoard.fullMap[2][2].getBuilder());

        game1.gameBoard.move(game1.gameBoard.fullMap[3][2], game1.gameBoard.fullMap[3][3]);    //riporta il soggetto alla condizione originale
        movementPhase1.actionMethod(player3.getBuilder(0), game1.gameBoard.fullMap[3][4]);   //movimento standard
        assertEquals(player3.getBuilder(0), game1.gameBoard.fullMap[3][4].getBuilder());
        assertEquals(0, game1.gameBoard.fullMap[3][3].getValue());
    }


    /**
     * tests the behaviour of actionMethod() when the card has no special effect regarding MovementPhase
     */
    @Test
    public void checkNullAction(){
        player1.setCard(game.getDeckCard(1));   //Apollo
        player2.setCard(game.getDeckCard(13));  //Selene

        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);

        game.gameBoard.build(game.gameBoard.fullMap[3][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[3][3], false);   // torre di due piani
        game.deployBuilder(player2, game.gameBoard.fullMap[3][3]);

        movementPhase = new MovementPhase(game, context, player1, player1.getBuilder(0), null);
        movementPhase.getMoves(player1.getBuilder(0));

        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[2][3]);   // condizione base
        assertEquals(player1.getBuilder(0), game.gameBoard.fullMap[2][3].getBuilder());
        assertFalse(game.getGameEnded());
        assertNull(game.getWinningPlayer());

        movementPhase = new MovementPhase(game, context, player2, player2.getBuilder(0), null);
        movementPhase.getMoves(player2.getBuilder(0));

        game.gameBoard.build(game.gameBoard.fullMap[4][4], false);
        game.gameBoard.build(game.gameBoard.fullMap[4][4], false);    //condizione di vittoria
        game.gameBoard.build(game.gameBoard.fullMap[4][4], false);

        movementPhase.actionMethod(player2.getBuilder(0), game.gameBoard.fullMap[4][4]);

        assertEquals(player2.getBuilder(0), game.gameBoard.fullMap[4][4].getBuilder());
        assertTrue(game.getGameEnded());  //controlla che il giocatore risulti vincitore
        assertEquals(player2, game.getWinningPlayer());
    }


    /**
     * tests the behaviour of actionMethod() when the card is Prometheus
     */
    @Test
    public void checkRestore(){
        player1.setCard(game.getDeckCard(9));
        player2.setCard(game.getDeckCard(13));
        game.deployBuilder(player1, game.gameBoard.fullMap[2][2]);
        movementPhase = new MovementPhase(game, context, player1, player1.getBuilder(0), null);
        movementPhase.getMoves(player1.getBuilder(0));

        movementPhase.getBasicRules().setPreviousMaxHeight(1);    //simuliamo il caso in cui Prometeo abbia fatto la mossa speciale in SpecialPhase1
        movementPhase.getBasicRules().setMaxHeight(0);
        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[4][4]);

        assertEquals(1, game.getRules().getMaxHeight());   //in questo modo ci accertiamo anche che game.basicRules = movementphase.basicRules
        assertEquals(1, game.getRules().getPreviousMaxHeight());

        movementPhase.getBasicRules().setPreviousMaxHeight(0);    //simulazione secondo caso: precedente modifica di maxHeight da parte di Atena
        movementPhase.getBasicRules().setMaxHeight(0);
        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[4][3]);
        assertEquals(0, game.getRules().getMaxHeight());   //caso in cui Athena abbia modificato maxHeight
        assertEquals(1, game.getRules().getPreviousMaxHeight());

        movementPhase.getBasicRules().setPreviousMaxHeight(1);    //simulazione terzo caso: Prometeo non aveva effettuato la mossa speciale
        movementPhase.getBasicRules().setMaxHeight(1);
        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[4][3]);
        assertEquals(1, game.getRules().getMaxHeight());
        assertEquals(1, game.getRules().getPreviousMaxHeight());

    }

    /**
     * tests the behaviour of actionMethod() when the card is Pan
     */
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
        movementPhase = new MovementPhase(game, context, player1, player1.getBuilder(0), null);
        movementPhase.getMoves(player1.getBuilder(0));


        movementPhase.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[3][3]);   // condizione di vittoria normale
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
