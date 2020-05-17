package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Map;

public abstract class Phase {

    private BasicRules basicRules;
    protected Map<String, Runnable> commands;
    private Card card;
    ArrayList<Square> possibleMoves;

    public Phase (Card card, BasicRules rules){
        basicRules = rules;
        this.card = card;
        map();
    }

    public void map(){}
}
