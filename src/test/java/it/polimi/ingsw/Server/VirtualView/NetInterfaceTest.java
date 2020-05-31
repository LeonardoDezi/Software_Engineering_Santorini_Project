package it.polimi.ingsw.Server.VirtualView;

import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Card;
import it.polimi.ingsw.Server.Model.Square;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class NetInterfaceTest {

    private NetInterface netInterface;
    private String string1 = new String();
    private String string2 = new String();
    private Square square1 = new Square(3,5);
    private Square square2 = new Square(7,2);
    private ArrayList<Square> array = new ArrayList<Square>();
    private String arraySquaretoString = new String();
    private Builder builder = new Builder(square1, "blue", "female");
    private String builderString = new String();
    private Boolean itstrue = Boolean.TRUE;
    private Boolean itsfalse = Boolean.FALSE;
    private Card card = new Card();
    private String cardString = new String();
    private String integer = new String();
    private Integer x = 7;


    @Before
    public void create(){
        netInterface=new NetInterface(null);
        string1 = "3,5:";
        string2 = "7,2:";
        for(int i=0; i<=1; i++){
            array.add(square1);
            array.add(square2);
        }
        builderString = "3,5:@";
        card.name = "Franco";
        card.setDescription("in realta si chiama Beppe");
        cardString="Franco_in realta si chiama Beppe:";
        integer="7";
        x=7;
        arraySquaretoString="3,5:7,2:3,5:7,2:@";
    }


    @Test
    public void squareToStringTest() {
        assertEquals("3,5:", netInterface.squareToString(square1));
    }

    @Test
    public void arrayListSquareToStringTest() {
        assertEquals(arraySquaretoString, netInterface.arrayListSquareToString(array));
    }

    @Test
    public void builderToStringTest() { //probabilmente da errore, spostare la chiocciola davanti
        assertEquals(builderString, netInterface.builderToString(builder));
    }

    @Test
    public void wantsToBuildADomeTest() {
        assertEquals("1", netInterface.wantsToBuildADome(itstrue));
        assertEquals("0", netInterface.wantsToBuildADome(itsfalse));
    }

    @Test
    public void stringToBuilderTest() {
        assertEquals(builder.getPosition().x, netInterface.stringToBuilder(builderString).getPosition().x);
        assertEquals(builder.getPosition().y, netInterface.stringToBuilder(builderString).getPosition().y);
    }

    @Test
    public void stringToBoolTest() {
        String string1 = "0";
        String string2 = "1";
        assertEquals(false, netInterface.stringToBool(string1));
        assertEquals(true, netInterface.stringToBool(string2));
    }

    @Test
    public void cardToString() {
        assertEquals(cardString, netInterface.cardToString(card));
    }

    @Test
    public void stringToInt() {
        assertEquals(x, netInterface.stringToInt(integer));
    }

    @Test
    public void stringToSquare() {
        assertEquals(square1.x, netInterface.stringToSquare(string1).x);
        assertEquals(square2.y, netInterface.stringToSquare(string2).y);
    }
}