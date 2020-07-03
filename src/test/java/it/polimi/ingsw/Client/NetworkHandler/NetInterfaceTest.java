package it.polimi.ingsw.Client.NetworkHandler;

import it.polimi.ingsw.Server.Model.Card;
import it.polimi.ingsw.Server.Model.Square;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class NetInterfaceTest {
    private NetInterface netInterface;
    private ArrayList<Square> array = new ArrayList<Square>();
    private Square square1 = new Square(3, 5);
    private Square square2 = new Square(7, 2);
    private String cardString = new String();
    private Card card = new Card();
    private String arraySquaretoString = new String("3,5:7,2:3,5:7,2:@");
    private Boolean itstrue = Boolean.TRUE;
    private Boolean itsfalse = Boolean.FALSE;


    @Before
    public void create() {
        netInterface = new NetInterface(null);
        cardString = "Franco_in realta si chiama Beppe:";
        card.name = "Franco";
        card.setDescription("in realta si chiama Beppe:");
        array.add(square1);
        array.add(square2);
        array.add(square1);
        array.add(square2);
    }


    @Test
    public void stringToArrayListSquare() {
        assertEquals(array.get(0).x, netInterface.stringToArrayListSquare(arraySquaretoString).get(0).x);
        assertEquals(array.get(0).y, netInterface.stringToArrayListSquare(arraySquaretoString).get(0).y);
        assertEquals(array.get(2).x, netInterface.stringToArrayListSquare(arraySquaretoString).get(2).x);
        assertEquals(array.get(1).y, netInterface.stringToArrayListSquare(arraySquaretoString).get(1).y);
        assertEquals(array.get(3).x, netInterface.stringToArrayListSquare(arraySquaretoString).get(3).x);
        assertEquals(array.get(3).y, netInterface.stringToArrayListSquare(arraySquaretoString).get(3).y);
    }

    @Test
    public void booleanToString() {
        assertEquals("1", netInterface.booleanToString(itstrue));
        assertEquals("0", netInterface.booleanToString(itsfalse));

    }

    @Test
    public void stringToCard() {
        assertEquals(card.name, netInterface.stringToCard(cardString).name);
        assertEquals(card.getDescription(), netInterface.stringToCard(cardString).getDescription());

    }



    public String arrayListSquareToString(ArrayList<Square> moves) {
        if (moves.isEmpty()) {
            return null;
        }
        StringBuilder stringMoves = new StringBuilder();
        String partial;
        for (Square move : moves) {
            partial = netInterface.squareToString(move);
            stringMoves.append(partial);
        }
        stringMoves.append("@");
        return stringMoves.toString();

    }
}