package Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Card {
    private String name;
    private CardConditions conditions;
    private CardEffects effects;

    public void upload() throws IOException {

        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("Cards.json"));
        List<Card> cards = new Gson().fromJson(reader, new TypeToken<List<Card>>() {}.getType());
        cards.forEach(System.out::println);
        reader.close();

    }

}
