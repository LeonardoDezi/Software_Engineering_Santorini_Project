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
}
