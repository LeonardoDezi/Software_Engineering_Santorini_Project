package it.polimi.ingsw.Server.Model;

import static org.junit.Assert.*;

//import it.polimi.ingsw.Server.SantoriniApp;
import org.junit.Test;

//ATTENZIONE: prima di eseguire i test, commentare updateBoard() in Board.move() e Board.build() (righe 96 e 73)

/**
 * this class is used to test the behaviour of Builder
 */
public class BuilderTest {
    /**
     * A general test of the builder's constructor.
     */
    @Test
    public void generalTest(){
        Square position = new Square (2, 3);
        position.setLevel(2);
        Builder builder = new Builder (position , "red", Player.SEX1 );
        assertEquals(builder.getPosition(), position);  // posizione corretta?
        assertEquals(2, builder.getPosition().x);  //coordinate corrette?
        assertEquals(3, builder.getPosition().y);
        assertEquals(1, builder.getPosition().getValue());  //value del position settato a 1?
        assertEquals("red", builder.getColour());  //colore corretto?
        assertEquals(2, position.getLevel());    //level Invariato?
    }
}
