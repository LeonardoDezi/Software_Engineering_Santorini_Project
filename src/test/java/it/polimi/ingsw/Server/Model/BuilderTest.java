package it.polimi.ingsw.Server.Model;

import static org.junit.Assert.*;

import org.junit.Test;

public class BuilderTest {

    //Il costruttore?
    @Test
    public void test(){
        Square position = new Square (2, 3);
        Builder builder = new Builder (position , "red" );
        assertEquals(builder.getPosition(), position);
        assertEquals(2, builder.getPosition().x);
        assertEquals(3, builder.getPosition().y);
    }
}
