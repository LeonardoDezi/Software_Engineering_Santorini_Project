package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * This class represents the Moment of the game in which the player can make an extra move (according to its card) before the standard building move
 */
public class SpecialPhase2 extends Phase {

    /** represents the hashMap containing the keys related to the method getMoves() */
    private HashMap<String, Runnable> movesCommands;
    /** represents the hashMap containing the keys related to the method actionMethod() */
    private HashMap<String, Runnable> actionCommands;
    /** represents the current player's playing worker */
    private Builder playingBuilder;
    /** generic parameter used by actionMethod() to save the position where the extra move is going to be performed,
     * or by getMoves() to save the worker's position before movementPhase */
    private Square position;
    /** represents the possible moves that a worker can make */
    private ArrayList<Square> possibleMoves;
    /** represents the player that is currently playing */
    private final Player player;


    /**
     * creates a new MovementPhase
     * @param game represents the game
     * @param context represents the context of the game
     * @param player represents the current player
     * @param playingBuilder represents the current player's playing worker
     * @param lastPosition represents the worker's position before movementPhase
     */
    public SpecialPhase2(Game game, Context context, Player player, Builder playingBuilder, Square lastPosition){
        super(game, context);
        this.player = player;
        this.playingBuilder = playingBuilder;
        this.position = lastPosition;
        map();
    }

    /** method used by the phase to develop the part of the game logic assigned. In the State Pattern, it represents the main method that has to be implemented.
     */
    public void handle() throws IOException {

        ArrayList<Square> moves1 = getMoves(playingBuilder, position);

        Envelope received = context.getNetInterface().getMovementMove(moves1, playingBuilder, player);

        if(received != null)
            actionMethod(received.getBuilder(), received.getMove());   // TODO controllare che received.getBuilder() è sempre = playingBuilder?

        if(!(game.getGameEnded()))
            context.setPhase(new BuildingPhase(game, context, player, playingBuilder));


    }

    /** initializes the hashMaps related to the phase */
    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();

        //getMoves
        movesCommands.put("doubleNotSameMove", this::doubleNotSameMove);  //Artemide
        movesCommands.put(null, ()->{possibleMoves = new ArrayList<>();});

        //actionMethod
        actionCommands.put("doubleNotSame", ()->{
            try {
                basicRules.move(player, playingBuilder.getPosition(), position);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        actionCommands.put(null, ()->{});
    }

    /**
     * obtains the possible moves that a worker can make
     * @param builder is the worker examined
     * @param lastPosition is the worker's position before movementPhase
     * @return the arrayList of possible moves that the worker can make
     */
    public ArrayList<Square> getMoves(Builder builder, Square lastPosition){

        this.playingBuilder = builder;
        this.position = lastPosition;

        movesCommands.get(player.getCard().parameters.specialPhase2Moves).run();
        return possibleMoves;
    }

    /** returns a list of all the places where the playing worker can be moved, except for the place where the worker used to be before movementPhase */
    public void doubleNotSameMove(){
        //position è l'ultima posizione in cui si trovava la pedina
        possibleMoves = basicRules.proximity(playingBuilder);
        possibleMoves = basicRules.removeBuilderSquare(possibleMoves);
        possibleMoves = basicRules.removeTooHighPlaces(possibleMoves, playingBuilder);
        possibleMoves = basicRules.removeDomeSquare(possibleMoves);

        for(int i =0; i<possibleMoves.size(); i++) {
            if (possibleMoves.get(i).equals(position)) {   //rimuove l'ultima casella in cui si trovava
                possibleMoves.remove(i);
                i--;
            }
        }
    }

    /**
     * executes the move sent by the worker, according to the rules assigned by the game and/or the player's card
     * @param builder is the worker who makes the move
     * @param position is the position where the move is going to be performed
     */
    public void actionMethod(Builder builder, Square position){

        this.playingBuilder = builder;
        //ora position sarà lo square di destinazione
        this.position = position;

        actionCommands.get(player.getCard().parameters.specialPhase2Action).run();
    }



}
