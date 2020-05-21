package it.polimi.ingsw.Server.Model;

import static org.junit.Assert.*;

//import it.polimi.ingsw.Server.ServerApp;
import org.junit.Test;

public class BuilderTest {


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
