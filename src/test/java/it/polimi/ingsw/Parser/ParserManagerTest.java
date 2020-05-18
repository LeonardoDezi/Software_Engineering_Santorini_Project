package it.polimi.ingsw.Parser;

import com.google.gson.JsonElement;
import org.junit.Test;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ParserManagerTest {


    @Test
    public void carduploader() {

        JsonParser parser = new JsonParser();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Cards.json");
        Reader reader = new InputStreamReader(inputStream);
        JsonElement rootelement = parser.parse(reader);
    }

}