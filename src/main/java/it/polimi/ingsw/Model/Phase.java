package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.Map;

public abstract class Phase {

    private Rules basicRules;
    protected Map<String, Runnable> commands;
    private Card card;
    ArrayList<Square> possibleMoves;

    public Phase (Card card, Rules rules){
        basicRules = rules;
        this.card = card;
        map();
    }

    public void map(){}
}
