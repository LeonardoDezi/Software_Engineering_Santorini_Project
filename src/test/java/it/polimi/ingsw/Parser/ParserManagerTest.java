package it.polimi.ingsw.Parser;

import static org.junit.Assert.*;
import it.polimi.ingsw.Server.Model.Card;
import org.junit.Test;
import java.util.ArrayList;


public class ParserManagerTest {

    @Test
    public void checkCardUpLoad(){
        ParserManager parserManager = new ParserManager();

        parserManager.uploadCards();
        ArrayList<Card> deck = parserManager.getDeck();
        assertEquals(14, deck.size());


    }

}