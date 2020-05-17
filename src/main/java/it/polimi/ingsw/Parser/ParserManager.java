package it.polimi.ingsw.Parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Server.Model.Card;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class ParserManager {

    private ArrayList<Card> cards;

    public Card getCard(int num) {
        return cards.get(num);
    }

    public void update() {

        Gson gson = new GsonBuilder().serializeNulls().create();

        try (Reader reader = new FileReader("Cards.json")) {
            Card card = gson.fromJson(reader, Card.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
