package it.polimi.ingsw.Server.Model;

import static org.junit.Assert.*;

import it.polimi.ingsw.Server.VirtualView.NetInterface;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;





public class GameTest {
    private Game game1;
    private Game game2;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private NetInterface netInterface = new NetInterface();
    private NetInterface netInterface2 = new NetInterface();


    @Before          //pensare anche alle partite a due giocatori
    public void createGame() {
        game1 = new Game(3, netInterface);
        game2 = new Game(2, netInterface2);
    }


    @Rule
    public final ExpectedException exception = ExpectedException.none();

    //DA RILEGGERE
    //addPlayer
    //@requires playerList != null     (controllare)
    //@ensures playerList.size() <=3
    //@ensures if playerList.size() = 3 && addPlayer(new, new) il comando non avrà alcun effetto
    //@ensures (\forall int i; 0 <= i < playerList.size(); (\forall int j; i != j && 0 <= i < playerList.size();
    // playerList.get(i).getColour() != playerList.get(j).getColour));

    @Test
    public void addPlayerCheck(){

        //partita a tre giocatori
        game1.playerList = new ArrayList<>();  //reinizializza playerList
        assertEquals(0, game1.playerList.size());
        int num1 =game1.addPlayer(new Player("Marco", "Red", game1, 0));
        int num2 =game1.addPlayer(new Player("Luca", "Blue", game1, 1));
        int num3 =game1.addPlayer(new Player("Fra", "Green", game1, 2));
        int num4 =game1.addPlayer(new Player("error", "White", game1, 3));

        assertEquals(3, game1.playerList.size());
        assertEquals("Marco", game1.playerList.get(0).playerID);
        assertEquals("Red", game1.playerList.get(0).getColour());
        assertEquals(1, num1);


        assertEquals("Luca", game1.playerList.get(1).playerID);
        assertEquals("Blue", game1.playerList.get(1).getColour());
        assertEquals(1, num2);

        assertEquals("Fra", game1.playerList.get(2).playerID);
        assertEquals("Green", game1.playerList.get(2).getColour());
        assertEquals(1, num3);

        assertEquals(0, num4);


        //partita a due giocatori
        game2.playerList = new ArrayList<>();    //reinizializza playerList
        assertEquals(0, game2.playerList.size());
        num1 =game2.addPlayer(new Player("Marco", "Red", game2, 0));
        num2 =game2.addPlayer(new Player("Luca", "Blue", game2, 1));
        num3 =game2.addPlayer(new Player("Fra", "Green", game2, 2));
        num4 =game2.addPlayer(new Player("error", "White", game2, 3));

        assertEquals(2, game2.playerList.size());
        assertEquals("Marco", game2.playerList.get(0).playerID);
        assertEquals("Red", game2.playerList.get(0).getColour());
        assertEquals(1, num1);


        assertEquals("Luca", game2.playerList.get(1).playerID);
        assertEquals("Blue", game2.playerList.get(1).getColour());
        assertEquals(1, num2);
        assertEquals(0, num3);

        exception.expect(IndexOutOfBoundsException.class);
        Player player = game2.playerList.get(2);

        assertEquals(0, num4);

    }

    @Test
    public void deployBuilderCheck() throws IOException {
        game1.playerList = new ArrayList<>(); // reinizializza playerList
        game1.addPlayer(new Player("Marco", "Red", game1, 0));
        game1.addPlayer(new Player("Luca", "Blue", game1, 1));
        game1.gameBoard = new Board(netInterface);


        //deployBuilder standard
        int num1 = game1.playerList.get(0).getBuilderSize();
        String message = game1.deployBuilder(game1.playerList.get(0), game1.gameBoard.fullMap[2][2]);
        assertEquals(num1+1, game1.playerList.get(0).getBuilderSize());  // controlla che builders.size() sia aumentato
        assertEquals("Builder deployed", message);

        //deployBuilder errato
        Square square = game1.gameBoard.fullMap[2][3];
        square.setValue(1);
        num1 = game1.playerList.get(0).getBuilderSize();
        message = game1.deployBuilder(game1.playerList.get(0), game1.gameBoard.fullMap[2][3]);
        assertEquals(num1, game1.playerList.get(0).getBuilderSize());  //check builders.size() invariato
        assertEquals("Error: Square already occupied by another player", message);

        //builders.size() al massimo
        message = game1.deployBuilder(game1.playerList.get(0), game1.gameBoard.fullMap[2][1]);
        assertEquals("Builder deployed", message);

        assertEquals(2, game1.playerList.get(0).getBuilderSize());
        message = game1.deployBuilder(game1.playerList.get(0), game1.gameBoard.fullMap[3][3]);
        assertEquals("Error:" + game1.playerList.get(0).playerID + "has already deployed all the builders", message);
        assertEquals(2, game1.playerList.get(0).getBuilderSize());
    }

    @Test
    public void removePlayerCheck() throws IOException {
        //reinizializza variabili
        game1.playerList = new ArrayList<>();
        game1.gameBoard = new Board(netInterface);

        //aggiungi giocatori e builders
        int num1 =game1.addPlayer(new Player("Marco", "Red", game1, 0));
        int num2 =game1.addPlayer(new Player("Luca", "Blue", game1, 1));
        int num3 =game1.addPlayer(new Player("Fra", "Green", game1, 2));

        game1.deployBuilder(game1.playerList.get(0), game1.gameBoard.fullMap[0][0]);
        game1.deployBuilder(game1.playerList.get(0), game1.gameBoard.fullMap[0][1]);

        game1.deployBuilder(game1.playerList.get(1), game1.gameBoard.fullMap[0][2]);
        game1.deployBuilder(game1.playerList.get(1), game1.gameBoard.fullMap[0][3]);

        game1.deployBuilder(game1.playerList.get(2), game1.gameBoard.fullMap[0][4]);
        game1.deployBuilder(game1.playerList.get(2), game1.gameBoard.fullMap[1][0]);

        game1.removePlayer(game1.playerList.get(1));
        assertEquals(2, game1.playerList.size());  //controlla che sia stato eliminato dalla lista
        assertEquals("Marco", game1.playerList.get(0).playerID);
        assertEquals("Fra", game1.playerList.get(1).playerID);

        assertNull(game1.gameBoard.fullMap[0][2].getBuilder());  //controlla che le pedine del player siano state eliminate
        assertEquals(0, game1.gameBoard.fullMap[0][2].getValue());

        assertNull(game1.gameBoard.fullMap[0][3].getBuilder());  //controlla che le pedine del player siano state eliminate
        assertEquals(0, game1.gameBoard.fullMap[0][3].getValue());

    }


    @Test
    public void checkAddChosenCard(){
        assertEquals(0, game1.getChosenCardsSize());
        game1.addChosenCard(5);
        Card card = game1.getChosenCard(0);
        assertEquals("Demeter", card.name);
    }
}


