package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;
import it.polimi.ingsw.Server.Controller.Phase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase2 extends Phase {

    private HashMap<String, Runnable> movesCommands;
    private HashMap<String, Runnable> actionCommands;

    private Builder playingBuilder;
    //position è usato sia per registrare lastPosition che per salvare la posizione dove avverrà il movimento
    private Square position;
    private ArrayList<Square> possibleMoves;
    private final Player player;



    public SpecialPhase2(Game game, Context context, Player player, Builder playingBuilder, Square lastPosition){
        super(game, context);
        this.player = player;
        this.playingBuilder = playingBuilder;
        this.position = lastPosition;
        map();
    }

    public void handle() throws IOException {

        ArrayList<Square> moves1 = getMoves(playingBuilder, position);

        Envelope received = context.getNetInterface().getMovementMove(moves1, playingBuilder, player);

        if(received != null)
            actionMethod(received.getBuilder(), received.getMove());   // TODO controllare che received.getBuilder() è sempre = playingBuilder
            //updateBoard(game.getBoard);

        if(!(game.getGameEnded()))
            context.setPhase(new BuildingPhase(game, context, player, playingBuilder));


    }


    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();

        //getMoves
        movesCommands.put("doubleNotSameMove", this::doubleNotSameMove);  //Artemide
        movesCommands.put(null, ()->{possibleMoves = new ArrayList<>();});

        //actionMethod
        actionCommands.put("doubleNotSame", ()->{basicRules.move(player, playingBuilder.getPosition(), position);});
        actionCommands.put(null, ()->{});
    }

    public ArrayList<Square> getMoves(Builder builder, Square lastPosition){

        this.playingBuilder = builder;
        this.position = lastPosition;

        movesCommands.get(player.getCard().parameters.specialPhase2Moves).run();
        return possibleMoves;
    }

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

    public void actionMethod(Builder builder, Square position){

        this.playingBuilder = builder;
        //ora position sarà lo square di destinazione
        this.position = position;

        actionCommands.get(player.getCard().parameters.specialPhase2Action).run();
    }



}
