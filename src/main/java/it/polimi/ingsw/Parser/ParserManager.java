package it.polimi.ingsw.Parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Server.Model.Card;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * this class reads the json file of all the cards
 */
public class ParserManager {

    /** list of all the cards */
    private ArrayList<Card> cards;

    public Card getCard(int num) {
        return cards.get(num);
    }

    public ArrayList<Card> getDeck() {
        return cards;
    }

    public int getCardSize() {
        return cards.size();
    }

    /** method used to read the json file of the cards and add them to the cards ArrayList   */
    public void uploadCards() {

        Gson gson = new GsonBuilder().serializeNulls().create();

        Type founderlistType = new TypeToken<ArrayList<Card>>() {}.getType();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Cards.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        cards = gson.fromJson(reader, founderlistType);

    }

}

