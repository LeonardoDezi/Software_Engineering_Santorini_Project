package it.polimi.ingsw.Server.Model;

import static org.junit.Assert.*;

import it.polimi.ingsw.Server.VirtualView.NetInterface;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


//ATTENZIONE: prima di eseguire i test, commentare updateBoard() in Board.move() e Board.build() (righe 96 e 73)

/**
 * this class is used to test the behaviour of Board
 */
public class BoardTest {
    /** the board used by the game */
    private Board board;
    /** the worker used in the test */
    private Builder builder1;
    /** the worker used in the test */
    private Builder builder2;
    /** the worker used in the test */
    private Builder builder3;
    /** the worker used in the test */
    private Builder builder4;
    /** a square used in the test */
    private Square square;
    /** the game used in the test */
    private Game game;
    /** the netInterface used in the test */
    private NetInterface netInterface = new NetInterface();

    /** creates the board and the workers used during the tests*/
    @Before
    public void createElements() {
        game = new Game(3, netInterface);
        board = new Board(netInterface);

        //builder1 è a terra
        square = board.fullMap[2][2];
        builder1 = new Builder(square, "Red", Player.SEX1);

        //builder2 è sopra un primo blocco e la sua destinazione è un edificio di due blocchi
        square = board.fullMap[0][4];
        square.setLevel(1);

        builder2 = new Builder(square, "Green", Player.SEX2);
        square = board.fullMap[1][4];
        square.setLevel(2);

        //builder3 è a terra e builder 4 è al primo piano
        square = board.fullMap[1][3];
        builder3 = new Builder(square, "Red", Player.SEX1);

        square = board.fullMap[1][2];
        square.setLevel(1);
        builder4 = new Builder(square, "Green", Player.SEX2);

        square = board.fullMap[0][0];
        square.setLevel(1);

        square = board.fullMap[0][1];
        square.setLevel(3);


    }

    /** checks the behaviour of method move() when a worker moves from a ground level into another ground level */
    @Test
    public void groundToGround() throws IOException {
        board.move(builder1.getPosition(), board.fullMap[2][3]);

        assertEquals(0, board.fullMap[2][2].getValue()); //controlla che la casella di partenza adesso risulti libera
        assertEquals(1, board.fullMap[2][3].getValue()); //controlla che la casella di arrivo risulti adesso occupata
        assertNull(board.fullMap[2][2].getBuilder()); // controlla che il campo builder sia ora null
        assertSame(board.fullMap[2][3].getBuilder(), builder1); // controlla che il campo builder sia uguale a builder1
        assertSame(builder1.getPosition(), board.fullMap[2][3]);
    }

    /** checks the behaviour of method move() when a worker moves on a tower of a different level */
    @Test
    public void differentFloor() throws IOException {

        int levelA = board.fullMap[0][4].getLevel();  //livelli di partenza e destinazione prima della mossa
        int levelB = board.fullMap[1][4].getLevel();

        board.move(builder2.getPosition(), board.fullMap[1][4]);

        assertEquals(board.fullMap[0][4].getLevel(), levelA);  //controlla che i livelli di partenza e di destinazione siano rimasti inalterati
        assertEquals(board.fullMap[1][4].getLevel(), levelB);
    }


    /** checks the behaviour of method move() when it has to swap two workers */
    @Test
    public void swap() throws IOException {

        int levelA = board.fullMap[1][3].getLevel();
        int levelB = board.fullMap[1][2].getLevel();

        board.move(builder3.getPosition(), board.fullMap[1][2]);

        assertSame(board.fullMap[1][2].getBuilder(), builder3);  // controlla che negli square risultino i builder scambiati
        assertSame(board.fullMap[1][3].getBuilder(), builder4);

        assertEquals(1, board.fullMap[1][2].getValue());  //controlla che il value sia ancora impostato a 1
        assertEquals(1, board.fullMap[1][3].getValue());

        assertSame(builder3.getPosition(), board.fullMap[1][2]);  // controlla che nei builder sia registrata la nuova posizione
        assertSame(builder4.getPosition(), board.fullMap[1][3]);

        assertEquals(levelB, builder3.getPosition().getLevel());  // controlla che il livello degli square sia rimasto inalterato
        assertEquals(levelA, builder4.getPosition().getLevel());
    }

    /** checks the behaviour of method build() when it has to build blocks */
    @Test
    public void createBuilding() throws IOException {
        Square position = board.fullMap[0][0];
        assertEquals(1, board.fullMap[0][0].getLevel());
        assertEquals(0, board.fullMap[0][0].getValue());
        assertNull(board.fullMap[0][0].getBuilder());
        board.build(position, false);  // in quella casella è già presente un blocco
        assertEquals(2, board.fullMap[0][0].getLevel());
        assertEquals(0, board.fullMap[0][0].getValue());  //controlla che gli altri valori non vengano modificati
        assertNull(board.fullMap[0][0].getBuilder());
    }

    /** checks the behaviour of method build() when it has to build domes, complete towers included, and that
     * the parameter completedTowers is updated only when a complete tower is built */
    @Test
    public void createDome() throws IOException {
        Square position = board.fullMap[0][1];
        int num = board.completedTowers;
        assertEquals(3, board.fullMap[0][1].getLevel());
        assertEquals(0, board.fullMap[0][1].getValue());
        board.build(position, false);
        assertEquals(4, board.fullMap[0][1].getLevel());   //controlla che il livello sia aumentato
        assertEquals(2, board.fullMap[0][1].getValue());   //controlla che il value sia posto a 2
        assertEquals(num + 1, board.completedTowers);   //controlla che completedTowers sia stato aggiornato

        position = board.fullMap[2][4];
        num = board.completedTowers;
        assertEquals(0, board.fullMap[2][4].getLevel());
        assertEquals(0, board.fullMap[2][4].getValue());
        board.build(position, true);
        assertEquals(0, board.fullMap[2][4].getLevel());
        assertEquals(2, board.fullMap[2][4].getValue());
        assertEquals(num, board.completedTowers);   // controlla che il valore di completedTowers non sia cambiato
    }

    /**
     * tests when Zeus builds below its worker
     */
    @Test
    public void buildSamePosition() throws IOException {

        builder2 = new Builder(square, "Green", Player.SEX2);
        square = board.fullMap[1][4];
        square.setBuilder(builder2);
        square.setValue(1);
        square.setLevel(2);   //builder2 è al secondo livello, sopra due blocchi
        board.build(board.fullMap[1][4], false);
        assertEquals(3, square.getLevel());  //si accerta che il livello sia aumentato
        assertEquals(1, square.getValue());
        assertEquals(builder2, square.getBuilder());



    }

}