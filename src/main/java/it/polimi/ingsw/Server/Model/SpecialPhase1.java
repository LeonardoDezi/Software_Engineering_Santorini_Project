package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the moment of the game in which the player can make an extra move (according to its card) before the standard movement
 */
public class SpecialPhase1 extends Phase {

    /** represents the hashMap containing the keys related to the method getMoves() */
    private HashMap<String, Runnable> movesCommands;
    /** represents the hashMap containing the keys related to the method actionMethod() */
    private HashMap<String, Runnable> actionCommands;
    /** represents the current player's first worker */
    private Builder builder1;
    /** represents the current player's second worker */
    private Builder builder2;
    /** represents the current player's playing worker */
    private Builder playingBuilder;
    /** generic parameter used by actionMethod() to save the position where the extra move is going to be performed */
    private Square position;
    /** represents the possible moves that a worker can make */
    private ArrayList<Square> possibleMoves;
    /** represents the player that is currently playing */
    private final Player player;


    /**
     * used to create a new specialPhase1
     * @param game represents the game
     * @param context represents the context of the game
     * @param player represents the current player
     * @param builder1 represents the current player's first worker
     * @param builder2 represents the current player's second worker
     */
    public SpecialPhase1(Game game, Context context, Player player, Builder builder1, Builder builder2) {
        super(game, context);
        this.player = player;
        this.builder1 = builder1;
        this.builder2 = builder2;
    }

    /** method used by the phase to develop the part of the game logic assigned. In the State Pattern, it represents the main method that has to be implemented */
    public void handle() throws IOException {


        ArrayList<Square> moves1 = getMoves(builder1);
        ArrayList<Square> moves2 = getMoves(builder2);

        if(!(moves1.isEmpty()) || !(moves2.isEmpty())) {   // il giocatore può fare mosse

            Envelope received = context.getNetInterface().getBothMovementMove(moves1, builder1, moves2, builder2, player, true);

            if (received != null) {
                actionMethod(received.getBuilder(), received.getMove());
            }

        }

        if(!(game.getGameEnded()))
            context.setPhase(new MovementPhase(game, context, player, builder1, builder2));

    }


    /** initializes the hashMaps related to the phase */
    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();

    //getMoves
        movesCommands.put("additionalBuild", () -> possibleMoves = basicRules.getBuildingRange(playingBuilder)); //Prometeo
        movesCommands.put("oppositeSideMoves", this::oppositeSideMoves);  //Caronte
        movesCommands.put(null, () ->{possibleMoves = new ArrayList<>();});
        movesCommands.put("restore", this::restore);  //Athena

    //actionMethod
        actionCommands.put(null, ()->{});
        actionCommands.put("specialBuild", this::specialBuild);  //Prometeo
        actionCommands.put("moveOpposite", this::moveOpposite);  //Caronte
    }

    /**
     * obtains the possible moves that a worker can make
     * @param builder is the worker examined
     * @return the arrayList of possible moves that the worker can make
     */
    public ArrayList<Square> getMoves (Builder builder){

        if(builder == null)   // nel caso di builder non esistente
            return new ArrayList<>();     //ritorna lista vuota

        this.playingBuilder = builder;
        movesCommands.get(player.getCard().parameters.specialPhase1Moves).run();
        return possibleMoves;
    }


    /**
     * returns the list of neighboring opponent workers with an unoccupied opposite Square
     */
    public void oppositeSideMoves(){

        possibleMoves = basicRules.proximity(playingBuilder);

        for(int i =0; i < possibleMoves.size(); i++) {

            Square position = possibleMoves.get(i);
            Builder opponentBuilder = position.getBuilder();


            if(position.getValue() == 1 && !(opponentBuilder.getColour().equals(playingBuilder.getColour()))){

                    int builderX = playingBuilder.getPosition().x;
                    int builderY = playingBuilder.getPosition().y;

                    int positionX = position.x;
                    int positionY = position.y;

                    int a = 2 * builderX - positionX;   //coordinate della casella opposta
                    int b = 2 * builderY - positionY;

                    try{
                        if(board.fullMap[a][b].getValue() != 0) {    //casella non libera
                            possibleMoves.remove(i);
                            i--;
                        }
                    }catch(ArrayIndexOutOfBoundsException e){   //TODO controllare che sia l'exception giusto: caso in cui la casella non esiste
                        possibleMoves.remove(i);
                        i--;
                    }

            }else{
                possibleMoves.remove(i);
                i--;
            }
        }

    }


    /** restores the parameter maxHeight of basicRules to BasicRules.BASICMAXHEIGHT */
    public void restore(){   //se athena aveva modificato maxHeight questo lo ristabilirà
        basicRules.setMaxHeight(BasicRules.BASICMAXHEIGHT);
        possibleMoves = new ArrayList<>();
    }


    /**
     * executes the move sent by the worker, according to the rules assigned by the game and/or the player's card
     * @param builder is the worker who makes the move
     * @param position is the position where the move is going to be performed
     */
    public void actionMethod(Builder builder, Square position){
        this.playingBuilder = builder;
        this.position = position;
        actionCommands.get(player.getCard().parameters.specialPhase1Action).run();
    }

    /** lets the worker make an extra building move, as long as the next movement phase is performed by the same worker and it doesn't involve moving to a higher tower */
    public void specialBuild() {
        if (position != null){
            try {
                basicRules.build(player, position, false);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            basicRules.setPreviousMaxHeight( basicRules.getMaxHeight());   //necessario in quanto maxHeight potrebbe essere != 1
            basicRules.setMaxHeight(0);

            if(!(playingBuilder.equals(builder1)))     //la fase successiva deve necessariamente essere eseguita dal worker che ha fatto la mossa speciale
                builder1 = null;
            else
                builder2 = null;

        }
    }

    /**
     * moves the worker set in @param position to the other side of the surroundings of the playing worker.
     * This special movement can never resolve in the moved worker's victory
     */

    public void moveOpposite() {

        int builderX = playingBuilder.getPosition().x;
        int builderY = playingBuilder.getPosition().y;

        int positionX = position.x;
        int positionY = position.y;

        int a = 2 * builderX - positionX;
        int b = 2 * builderY - positionY;

        Square destination = board.fullMap[a][b];
        try {
            board.move(position, destination);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        if(!(playingBuilder.equals(builder1)))     //la fase successiva deve necessariamente essere eseguita dal worker che ha fatto la mossa speciale
            builder1 = null;
        else
            builder2 = null;

    }






}
