package it.polimi.ingsw.Server.Model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*Tre casi di test:
* 1)Un builder a terra si muove su un altro square a terra
  2)Un builder al primo piano sale di un livello
  3) Due builder fanno uno swap

  Cose da controllare:
  I value degli square vengano aggiornati
  I position dei builders vengano aggiornati ( sia in caso di swap che tradizionale)
 */

//Da testare/ ritestare: costruttore, build(cupole)

public class BoardTest {
    Board board;
    Builder builder1;
    Builder builder2;
    Builder builder3;
    Builder builder4;
    Square square;

    @Before
    public void createElements() {
        board = new Board();

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

    @Test
    public void groundToGround() {
        board.move(builder1.getPosition(), board.fullMap[2][3]);

        assertEquals(0, board.fullMap[2][2].getValue()); //controlla che la casella di partenza adesso risulti libera
        assertEquals(1, board.fullMap[2][3].getValue()); //controlla che la casella di arrivo risulti adesso occupata
        assertNull(board.fullMap[2][2].getBuilder()); // controlla che il campo builder sia ora null
        assertSame(board.fullMap[2][3].getBuilder(), builder1); // controlla che il campo builder sia uguale a builder1
        assertSame(builder1.getPosition(), board.fullMap[2][3]);
    }


    @Test
    public void differentFloor() {

        int levelA = board.fullMap[0][4].getLevel();  //livelli di partenza e destinazione prima della mossa
        int levelB = board.fullMap[1][4].getLevel();

        board.move(builder2.getPosition(), board.fullMap[1][4]);

        assertEquals(board.fullMap[0][4].getLevel(), levelA);  //controlla che i livelli di partenza e di destinazione siano rimasti inalterati
        assertEquals(board.fullMap[1][4].getLevel(), levelB);
    }

    @Test
    public void swap() {

        int levelA = board.fullMap[1][3].getLevel();
        int levelB = board.fullMap[1][2].getLevel();

        board.move(builder3.getPosition(), board.fullMap[1][2]);

        assertSame(board.fullMap[1][2].getBuilder(), builder3);
        assertSame(board.fullMap[1][3].getBuilder(), builder4);

        assertEquals(1, board.fullMap[1][2].getValue());  //controlla che il value sia ancora impostato a 1
        assertEquals(1, board.fullMap[1][3].getValue());

        assertSame(builder3.getPosition(), board.fullMap[1][2]);
        assertSame(builder4.getPosition(), board.fullMap[1][3]);

        assertEquals(levelB, builder3.getPosition().getLevel());
        assertEquals(levelA, builder4.getPosition().getLevel());
    }


    @Test
    public void createBuilding() {
        Square position = board.fullMap[0][0];
        assertEquals(1, board.fullMap[0][0].getLevel());
        assertEquals(0, board.fullMap[0][0].getValue());
        assertNull(board.fullMap[0][0].getBuilder());
        board.build(position, false);  // in quella casella è già presente un blocco
        assertEquals(2, board.fullMap[0][0].getLevel());
        assertEquals(0, board.fullMap[0][0].getValue());  //controlla che gli altri valori non vengano modificati
        assertNull(board.fullMap[0][0].getBuilder());
    }

    @Test
    public void createDome() {
        Square position = board.fullMap[0][1];
        int num = board.completedTowers;
        assertEquals(3, board.fullMap[0][1].getLevel());
        assertEquals(0, board.fullMap[0][1].getValue());
        board.build(position, true);
        assertEquals(4, board.fullMap[0][1].getLevel());   //controlla che il livello sia aumentato
        assertEquals(2, board.fullMap[0][1].getValue());   //controlla che il value sia posto a 2
        assertEquals(num + 1, board.completedTowers);   //controlla che completedTowers sia stato aggiornato

        position = board.fullMap[2][4];
        num = board.completedTowers;
        assertEquals(0, board.fullMap[2][4].getLevel());
        assertEquals(0, board.fullMap[2][4].getValue());
        board.build(position, true);
        assertEquals(1, board.fullMap[2][4].getLevel());
        assertEquals(2, board.fullMap[2][4].getValue());
        assertEquals(num, board.completedTowers);   // controlla che il valore di completedTowers non sia cambiato
    }

}