package it.polimi.ingsw.Model;

import static org.junit.Assert.*;

import org.junit.Test;

public class BuilderTest {

    @Test
    public void test(){
        Square position = new Square (2, 3);
        Builder builder = new Builder (position , "red" );
        assertTrue(builder.getPosition().equals(position) );
        assertTrue(builder.getPosition().x == 2);
        assertTrue(builder.getPosition().y == 3);
    }
}
