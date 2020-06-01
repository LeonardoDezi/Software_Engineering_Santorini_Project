package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;
import it.polimi.ingsw.Server.Model.BasicRules;
import it.polimi.ingsw.Server.Model.Board;
import it.polimi.ingsw.Server.Model.Game;

import java.io.IOException;

/**
 * This abstract class is used to describe every possible state in which a turn can find itself in. It represents the class State in a State Pattern
 * Here is a list of every possible phase:
 * SpecialPhase1: Moment of the game in which the player can make an extra move (according to its card) before the standard movement
 * MovementPhase: Moment of the game in which the player makes its standard movement
 * SpecialPhase2: Moment of the game in which the player can make an extra move (according to its card) before the standard building move
 * BuildingPhase: Moment of the game in which the player makes its standard building move
 * SpecialPhase3: Moment of the game in which the player can make an extra move (according to its card) after the standard building move
 */
public abstract class Phase {

    /** represents the basic rules of the game */
    protected final BasicRules basicRules;
    /** represents the game*/
    protected final Game game;
    /** represents th board of the game */
    protected final Board board;
    /** represents the context of the game */
    protected final Context context;

    /** Constructor used to create a new Phase
     * @param game represents the game
     * @param context represents the context of the game
     * */
    public Phase(Game game, Context context){
        this.basicRules = game.getRules();
        this.game = game;
        this.board = game.getBoard();
        this.context = context;
        map();
    }

    /** method used by the phase to develop the part of the game logic assigned. In the State Pattern, it represents the main method that has to be implemented */
    public abstract void handle() throws IOException;

    /** used to initialize the hashMaps related to the phase */
    public abstract void map();

}
