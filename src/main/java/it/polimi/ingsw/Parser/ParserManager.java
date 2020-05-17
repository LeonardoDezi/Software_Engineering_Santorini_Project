package it.polimi.ingsw.Parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Server.Model.Card;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ParserManager {

    private ArrayList<Card> cards;

    public Card getCard(int num) {
        return cards.get(num);
    }

    public void update() {

        Gson gson = new GsonBuilder().serializeNulls().create();

        try (Reader reader = new FileReader("Cards.json")) {

            // Convert JSON File to Java Object
            Card card = gson.fromJson(reader, Card.class);

            // print staff
            System.out.println(card);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
