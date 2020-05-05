package it.polimi.ingsw.Model;

import java.util.HashMap;

public class BuildPhase extends Phase {

    private Rules basicRules;
    protected HashMap<String, Runnable> commands;
    private Card card;

    public BuildPhase(Card card){
        basicRules = new BasicRules(board, game);
        this.card = card;
        mappa();
    }



    //non mi vengono in mente nuovi metodi
}
