package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the moment of the game in which the player can make an extra move (according to its card) after the standard building move
 */
public class SpecialPhase3 extends Phase {

    /** represents the hashMap containing the keys related to the method getMoves() */
    private HashMap<String, Runnable> movesCommands;
    /** represents the hashMap containing the keys related to the method actionMethod() */
    private HashMap<String, Runnable> actionCommands;
    /** represents the current player's playing worker */
    private Builder playingBuilder;
    /** generic parameter used by actionMethod() to save the position where the extra move is going to be performed,
     * or by getMoves() to save where the worker built during BuildingPhase */
    private Square position;
    /** represents the possible moves that a worker can make */
    private ArrayList<Square> possibleMoves;
    /** represents the player that is currently playing */
    private final Player player;


    /**
     * used to create a new MovementPhase
     * @param game represents the game
     * @param context represents the context of the game
     * @param player represents the current player
     * @param builder represents the current player's playing worker
     * @param position represents the position where the playing worker built during buildingPhase
     */
    public SpecialPhase3(Game game, Context context, Player player, Builder builder, Square position){
        super(game, context);
        this.player = player;
        this.playingBuilder = builder;
        this.position = position;
        map();
    }

    /** method used by the phase to develop the part of the game logic assigned. In the State Pattern, it represents the main method that has to be implemented.
     */
    public void handle() throws IOException{

        ArrayList<Square> moves1 = getMoves(playingBuilder);
        boolean buildDome = player.getCard().getParameters().buildDome;

        if(!(moves1.isEmpty())) {   // il giocatore può fare mosse

            Envelope received = context.getNetInterface().getBuildMove(moves1, playingBuilder, buildDome, player, true);

            if (received != null) {
                if (received.getMove().x == 20) {
                    game.setGameEnded(true);
                    game.setDisconnect(true);
                } else {
                    actionMethod(received.getBuilder(), received.getMove(), received.getIsDome());
                }
            }
        }

        if(!(game.getGameEnded()))
            context.setPhase(null);

    }

    /** initializes the hashMaps related to the phase */
    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();
//getMoves
        movesCommands.put(null, ()->{possibleMoves = new ArrayList<>();});
        movesCommands.put("notSameSquare", this::notSameSquare); //Demeter
        movesCommands.put("sameSquareNotDome", this::sameSquareNotDome); //Hephaestus
        movesCommands.put("notPerimeter", this::notPerimeter); //Hestia
//actionMethod
        actionCommands.put(null, ()->{});
    }


    /**
     * obtains the possible moves that a worker can make
     * @param builder is the worker examined
     * @return the arrayList of possible moves that the worker can make
     */
    public ArrayList<Square> getMoves(Builder builder){

        this.playingBuilder = builder;

        possibleMoves= basicRules.getBuildingRange(builder);
        movesCommands.get(player.getCard().parameters.specialPhase3Moves).run();

        return possibleMoves;
    }

//getMoves
    /** removes from the list of possible moves the place where the playing worker built during buildingPhase */
    public void notSameSquare(){
        for(int i = 0; i < possibleMoves.size(); i++) {
            if (possibleMoves.get(i).equals(position)) {
                possibleMoves.remove(i);
                i--;
            }
        }
    }

    /**
     * sets possibleMoves as an arrayList containing only the position where the playing worker built during buildingPhase,
     * and only if in that square there aren't already three blocks
     */
    public void sameSquareNotDome(){
        possibleMoves = new ArrayList<>();
        if(position.getLevel() < 3)
            possibleMoves.add(position);
    }

    /**
     * removes from possibleMoves the squares that are on the perimeter of the board
     */
    public void notPerimeter(){
        for(int i =0; i < possibleMoves.size(); i++) {
            Square pos = possibleMoves.get(i);
            if (pos.x == 0 || pos.y == 0 || pos.x == Board.BOARDSIZEX - 1 || pos.y == Board.BOARDSIZEY -1 ) {   //casella perimetrale
                possibleMoves.remove(i);
                i--;
            }
        }
    }

    /**
     * executes the move sent by the worker, according to the rules assigned by the game and/or the player's card
     * @param builder is the worker who makes the move
     * @param position is the position where the move is going to be performed
     * @param isDome indicates whether the construction is going to be a block or a dome
     */
    public void actionMethod(Builder builder, Square position, boolean isDome) throws IOException {
        this.playingBuilder = builder;
        this.position = position;

        actionCommands.get(player.getCard().parameters.specialPhase3Action).run();
        basicRules.build(player, position, isDome);
    }



}
