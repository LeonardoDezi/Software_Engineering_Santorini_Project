package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.BasicRules;
import it.polimi.ingsw.Server.Model.Board;
import it.polimi.ingsw.Server.Model.Game;

import java.io.IOException;

public abstract class Phase {

    protected final BasicRules basicRules;
    protected final Game game;
    protected final Board board;
    protected final Context context;


    public Phase(Game game, Context context){
        this.basicRules = game.getRules();
        this.game = game;
        this.board = game.getBoard();
        this.context = context;
        map();
    }

    public abstract void handle() throws IOException;

    public abstract void map();
}
