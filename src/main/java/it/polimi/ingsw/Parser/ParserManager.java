package it.polimi.ingsw.Parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Model.Card;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ParserManager {

    private ArrayList<Card> cards;

    public Card getCard(int num){
        return cards.get(num);
    }

    public void upload() throws IOException {

        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("Cards.json"));
        ArrayList<Card> cards = new Gson().fromJson(reader, new TypeToken<List<Card>>() {}.getType());
        cards.forEach(System.out::println);
        reader.close();

    }

}
