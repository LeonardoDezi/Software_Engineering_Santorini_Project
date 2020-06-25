package it.polimi.ingsw.Parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Server.Model.Card;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ParserManager {

    private ArrayList<Card> cards;

    public Card getCard(int num) {
        return cards.get(num);
    }

    public ArrayList<Card> getDeck(){ return cards;}

    public int getCardSize(){return cards.size();}

    public void uploadCards() {

        Gson gson = new GsonBuilder().serializeNulls().create();

        Type founderlistType = new TypeToken<ArrayList<Card>>() {}.getType();

        String fileName = "src/main/resources/Cards.json";

        try (Reader reader = new FileReader(fileName)) {

            cards = gson.fromJson(reader, founderlistType);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


