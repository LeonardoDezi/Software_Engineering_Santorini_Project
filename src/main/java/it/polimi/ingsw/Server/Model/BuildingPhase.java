package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the moment of the game in which the player makes its standard building move
 */
public class BuildingPhase extends Phase {

    /** represents the hashMap containing the keys related to the method getMoves() */
    private HashMap<String, Runnable> movesCommands;
    /** represents the hashMap containing the keys related to the method actionMethod() */
    private HashMap<String, Runnable> actionCommands;
    /** represents the current player's playing worker */
    private Builder playingBuilder;
    /** generic parameter used by actionMethod() to save the position where the building move is going to be performed */
    private Square position;
    /** represents the possible moves that a worker can make */
    private ArrayList<Square> possibleMoves;
    /** represents the player that is currently playing */
    private final Player player;
    /** represents the worker who makes the actual building */
    private Builder actionBuilder;
    /** represents the type of construction that is going to be made. True = dome, False = block */
    private boolean isDome;


    /**
     * used to create a new MovementPhase
     * @param game represents the game
     * @param context represents the context of the game
     * @param player represents the current player
     * @param builder represents the current player's playing worker
     */
    public BuildingPhase(Game game, Context context, Player player, Builder builder){
        super(game, context);
        this.player = player;
        this.playingBuilder = builder;
        map();
    }


    /** method used by the phase to develop the part of the game logic assigned. In the State Pattern, it represents the main method that has to be implemented.
     */
    public void handle() throws IOException{

        ArrayList<Square> moves1 = getMoves(playingBuilder);
        Boolean canBuildDome = player.getCard().getParameters().buildDome;
        Envelope received;

        String BPmoves = player.getCard().getParameters().buildingPhaseMoves;

        if(BPmoves != null && BPmoves.equals("askForFemale")){   //se la carta è Selene

            if (playingBuilder.sex.equals(Player.SEX2)) {  //se il worker giocante è femmina
                received=context.getNetInterface().getBuildMove(moves1, playingBuilder, true, player, false);
            }else if(player.getBuilderSize() ==2){      //se il worker è maschio e il giocatore ha due workers
                Builder female = player.getFemale();
                ArrayList<Square> moves2 = basicRules.getBuildingRange(female);
                received = context.getNetInterface().getBothBuildMove(moves1, playingBuilder, moves2, female, true, player,false);   //TODO usiamo canBuildADome per indicare che la femmina può costruire solo cupole. Ricordarselo quando si implementa getBothBuildMove
            }else  //unico worker ed è maschio
                received =context.getNetInterface().getBuildMove(moves1, playingBuilder, false, player,false);   //il worker è maschio e non può costruire cupole

        }else  //la carta non è selene
            received = context.getNetInterface().getBuildMove(moves1, playingBuilder, canBuildDome, player,false);

        if (received.getMove().x == 20){
            game.setGameEnded(true);
            game.setDisconnect(true);

        }else {
            actionMethod(received.getBuilder(), received.getMove(), received.getIsDome());
        }
        
        if(!(game.getGameEnded()))
            context.setPhase(new SpecialPhase3(game, context, player, actionBuilder, position));

    }

    /** initializes the hashMaps related to the phase */
    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();

//getMoves
        movesCommands.put(null, () ->{});
        movesCommands.put("askForFemale", () ->{});
        movesCommands.put("addBuilderPosition", this::addBuilderPosition);  //Zeus
//building
        actionCommands.put("buildBelowYou", this::buildBelowYou);
        actionCommands.put("maleOrFemale", this::maleOrFemale);
        actionCommands.put(null, () ->{});

    }

    /**
     * obtains the possible moves that a worker can make
     * @param builder is the worker examined
     * @return the arrayList of possible moves that the worker can make
     */
    public ArrayList<Square> getMoves(Builder builder){

         this.playingBuilder = builder;
         possibleMoves = basicRules.getBuildingRange(builder);
         movesCommands.get(player.getCard().parameters.buildingPhaseMoves).run();
         return possibleMoves;
    }

    /** adds the playing worker's current position to the possible moves */
    public void addBuilderPosition(){
        Square currentPosition = playingBuilder.getPosition();
        if (currentPosition.getLevel() < 3)   //dobbiamo accertarci di questo casomai zeus si muovesse da una torre di tre a un' altra
            possibleMoves.add(currentPosition);
    }

    /**
     * executes the move sent by the worker, according to the rules assigned by the game and/or the player's card
     * @param builder is the worker who makes the move
     * @param position is the position where the move is going to be performed
     * @param isDome indicates whether the construction is going to be a block or a dome
     */
    public void actionMethod(Builder builder, Square position, boolean isDome) throws IOException {
        this.actionBuilder = builder;
        this.position = position;
        this.isDome = isDome;
        actionCommands.get(player.getCard().parameters.buildingPhaseAction).run();
        basicRules.build(player, this.position, this.isDome);
    }


    /**
     * lets the playing worker build under itself, as long as the construction does not produce a complete tower.
     * This special move can never resolve in the player's victory
     */
    public void buildBelowYou() {
        if(position.equals(actionBuilder.getPosition())) {
            if (position.getLevel() < 3) {
                try{
                    board.build(position, false);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                position = null;
            }
        }
    }

    /** If actionBuilder is not equal to playingBuilder, then the worker is a female. At this point of the phase, the female worker can only
     * build domes, so the method sets isDome to true
      */
    public void maleOrFemale(){
        if(!(actionBuilder.equals(playingBuilder)))
            this.isDome = true;
    }



}
