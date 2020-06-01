package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;
import it.polimi.ingsw.Server.VirtualView.NetInterface;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * this class is used to test the behaviour of SpecialPhase3
 */
public class SpecialPhase3Test {
    /** the object tested */
    SpecialPhase3 specialPhase3;
    /** the object tested */
    SpecialPhase3 specialPhase31;
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
     * tests the behaviour of SpecialPhase3 when the card has no special effect regarding SpecialPhase3
     */
    @Test
    public void checkNull(){

        player1.setCard(game.getDeckCard(14)); //Zeus
        player2.setCard(game.getDeckCard(10)); //Charon

        game.deployBuilder(player1, game.gameBoard.fullMap[1][3]);  //soggetto
        specialPhase3 = new SpecialPhase3(game, context, player1, player1.getBuilder(0), game.gameBoard.fullMap[1][2]);  //lastPosition messa casualmente
        possibleMoves = specialPhase3.getMoves(player1.getBuilder(0));
        assertTrue(possibleMoves.isEmpty());   //si accerta che possibleMoves sia vuota
    }

    /**
     * tests the behaviour of SpecialPhase3 when the card is Demeter
     */
    @Test
    public void checkDemeterBehaviour(){

        player1.setCard(game.getDeckCard(5)); //Demeter
        player2.setCard(game.getDeckCard(14)); //Zeus

        game.gameBoard.build(game.gameBoard.fullMap[1][3], false); //torre di un blocco

        game.deployBuilder(player1, game.gameBoard.fullMap[1][3]);  //soggetto: pedina su torre con lastPosition su (0,4)

        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);  //torre completa
        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);

        game.deployBuilder(player1, game.gameBoard.fullMap[2][3]);   //altra pedina

        game.gameBoard.build(game.gameBoard.fullMap[1][2], false);   //torre di due
        game.gameBoard.build(game.gameBoard.fullMap[1][2], false);

        game.gameBoard.build(game.gameBoard.fullMap[2][2], true);    //cupola

        specialPhase3 = new SpecialPhase3(game, context, player1, player1.getBuilder(0), game.gameBoard.fullMap[0][4]);
        possibleMoves = specialPhase3.getMoves(player1.getBuilder(0));
        assertEquals(4, possibleMoves.size());  // si accerta della dimensione di possibleMoves

        for(Square square: possibleMoves)   //si accerta che in possibleMoves non sia contenuto lastPosition
            assertNotEquals(game.gameBoard.fullMap[0][4], square);

        int level = game.gameBoard.fullMap[2][4].getLevel();
        specialPhase3.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[2][4], false);  //generica costruzione
        assertEquals(level + 1 , game.gameBoard.fullMap[2][4].getLevel());

    }


    /**
     * tests the behaviour of SpecialPhase3 when the card is Hephaestus
     */
    @Test
    public void checkHephaestusBehaviour(){

        player1.setCard(game.getDeckCard(6)); //Hephaestus
        player2.setCard(game.getDeckCard(14)); //Zeus

        game.gameBoard.build(game.gameBoard.fullMap[1][3], false); //torre di un blocco

        game.deployBuilder(player1, game.gameBoard.fullMap[1][3]);  //soggetto: pedina su torre

        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);
        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);  //torre di tre
        game.gameBoard.build(game.gameBoard.fullMap[0][3], false);


        game.gameBoard.build(game.gameBoard.fullMap[1][2], false);   //torre di due
        game.gameBoard.build(game.gameBoard.fullMap[1][2], false);


        //Test 1: l'ultima posizione corrisponde a una torre di tre blocchi  (0,3)
        specialPhase3 = new SpecialPhase3(game, context, player1, player1.getBuilder(0), game.gameBoard.fullMap[0][3]);
        possibleMoves = specialPhase3.getMoves(player1.getBuilder(0));
        assertTrue(possibleMoves.isEmpty());  //si accerta che possibleMoves sia vuoto


        //Test 2: l'ultima posizione corrisponde a una torre di due blocchi  (1,2)
        specialPhase3 = new SpecialPhase3(game, context, player1, player1.getBuilder(0), game.gameBoard.fullMap[1][2]);
        possibleMoves = specialPhase3.getMoves(player1.getBuilder(0));
        assertEquals(1,possibleMoves.size());  //si accerta che possibleMoves sia di dimensioni 1
        assertEquals(game.gameBoard.fullMap[1][2], possibleMoves.get(0)); //si accerta che l'elemento contenuto in possibleMoves sia lastPosition

        int level = game.gameBoard.fullMap[1][2].getLevel();
        specialPhase3.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[1][2], false);  //costruzione effettiva
        assertEquals(level + 1, game.gameBoard.fullMap[1][2].getLevel());  //si accerta che la costruzione è stata effettuata


        level = game.gameBoard.fullMap[2][4].getLevel();
        specialPhase3.actionMethod(player1.getBuilder(0), game.gameBoard.fullMap[2][4], false);  //generica costruzione
        assertEquals(level + 1 , game.gameBoard.fullMap[2][4].getLevel());

    }


    /**
     * tests the behaviour of buildingPhase when the card is Hestia
     */
    @Test
    public void checkHestiaBehaviour(){

        player3.setCard(game1.getDeckCard(1));  //Apollo
        player4.setCard(game1.getDeckCard(14));   //Zeus
        player5.setCard(game1.getDeckCard(12));//Hestia


        //Test 1: Il worker è al confine della plancia
        game1.deployBuilder(player5, game1.gameBoard.fullMap[0][0]);

        game1.gameBoard.build(game1.gameBoard.fullMap[1][1], false);
        game1.gameBoard.build(game1.gameBoard.fullMap[1][1], false);  //torre di tre
        game1.gameBoard.build(game1.gameBoard.fullMap[1][1], false);

        game1.gameBoard.build(game1.gameBoard.fullMap[1][0], false);  //torre di uno

        specialPhase31 = new SpecialPhase3(game1, context1, player5, player5.getBuilder(0), game1.gameBoard.fullMap[1][1]);

        possibleMoves = specialPhase31.getMoves(player5.getBuilder(0));
        assertEquals(1, possibleMoves.size());
        assertEquals(game1.gameBoard.fullMap[1][1], possibleMoves.get(0));


        //Test 2: il worker è a una casella di distanza dal confine con la plancia
        game1.deployBuilder(player5, game1.gameBoard.fullMap[3][3]);

        game1.deployBuilder(player4, game1.gameBoard.fullMap[3][2]);  // pedina avversaria

        possibleMoves = specialPhase31.getMoves(player5.getBuilder(1));

        assertEquals(2, possibleMoves.size());

        int level = game1.gameBoard.fullMap[2][3].getLevel();
        specialPhase31.actionMethod(player5.getBuilder(1), game1.gameBoard.fullMap[2][3], false);
        assertEquals(level + 1, game1.gameBoard.fullMap[2][3].getLevel());  // si accerta che la costruzione è stata eseguita

    }




}
