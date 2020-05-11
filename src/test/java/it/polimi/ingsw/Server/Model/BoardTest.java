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
    public void createElements(){
        board = new Board();

        //builder1 è a terra
        square= board.fullMap[2][2];
        builder1 = new Builder(square, "Red");

        //builder2 è sopra un primo blocco e la sua destinazione è un edificio di due blocchi
        square= board.fullMap[0][4];
        square.setLevel(1);
        builder2= new Builder(square, "Green");
        square = board.fullMap[1][4];
        square.setLevel(2);

        //builder3 è a terra e builder 4 è al primo piano
        square= board.fullMap[1][3];
        builder3 = new Builder(square, "Red");

        square = board.fullMap[1][2];
        square.setLevel(1);
        builder4 = new Builder(square, "Green");

        square = board.fullMap[0][0];
        square.setLevel(1);

        square = board.fullMap[0][1];
        square.setLevel(4);


    }

    @Test
    public void groundToGround(){
        board.move(builder1, 2, 3);

        assertEquals(0, board.fullMap[2][2].getValue()); //controlla che la casella di partenza adesso risulti libera
        assertEquals(1, board.fullMap[2][3].getValue()); //controlla che la casella di arrivo risulti adesso occupata
        assertNull(board.fullMap[2][2].getBuilder()); // controlla che il campo builder sia ora null
        assertSame(board.fullMap[2][3].getBuilder(), builder1); // controlla che il campo builder sia uguale a builder 1
        assertSame(builder1.getPosition(), board.fullMap[2][3]);
    }

    //non sono sicuro riguardo la necessità di questo test, dato che il metodo non va a modificare il valore di level
    @Test
    public void differentFloor(){

        int levelA = board.fullMap[0][4].getLevel();  //livelli di partenza e destinazione prima della mossa
        int levelB = board.fullMap[1][4].getLevel();

        board.move(builder2, 1, 4);

        assertEquals(board.fullMap[0][4].getLevel(), levelA);
        assertEquals(board.fullMap[1][4].getLevel(), levelB);
    }

    @Test
    public void swap(){
        board.move(builder3, 1, 2);

        assertSame(board.fullMap[1][2].getBuilder(), builder3);
        assertSame(board.fullMap[1][3].getBuilder(), builder4);

        assertEquals(1, board.fullMap[1][2].getValue());
        assertEquals(1, board.fullMap[1][3].getValue());

        assertSame(builder3.getPosition(), board.fullMap[1][2]);
        assertSame(builder4.getPosition(), board.fullMap[1][3]);
    }

    @After  // serve?
    public void clear(){    // reset everything
        board = null;
        builder1 = null;
        builder2 = null;
        builder3 = null;
        square = null;
    }

    @Test
    public void createBuilding(){
        Square position = board.fullMap[0][0];
        board.build( position, false);  // in quella casella era già presente un blocco
        assertEquals(2, board.fullMap[0][0].getLevel());
        assertEquals(0, board.fullMap[0][0].getValue());
    }

    @Test
    public void createDome(){
        Square position = board.fullMap[0][1];
        board.build(position, true);
        assertEquals(4, board.fullMap[0][1].getLevel());  // decidere quale implementazione usare 4 o 5?
        assertEquals(2, board.fullMap[0][1].getValue());
    }

    @Test
    public void checkNull(){
        Square position = null;
        //dovrebbe controllare che fullMap non venga modificata
    }
}
