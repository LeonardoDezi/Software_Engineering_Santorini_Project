package it.polimi.ingsw.Model;

import java.util.HashMap;
import java.util.Map;


public class CardManager {

    private  Rules rules;
    private Builder builder;

    public CardManager(Rules rules, Builder builder){
        this.rules = rules;
        this.builder = builder;
    }

    private void mappa(String[] args) {

        Map<String, Runnable> commands = new HashMap<>();

        commands.put("swap", () -> rules.getPossibleMoves(builder));
    }

}
