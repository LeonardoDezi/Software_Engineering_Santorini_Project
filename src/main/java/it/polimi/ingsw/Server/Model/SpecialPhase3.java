package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;
import it.polimi.ingsw.Server.Controller.Phase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase3 extends Phase {


    private HashMap<String, Runnable> movesCommands;
    private HashMap<String, Runnable> actionCommands;

    private Builder playingBuilder;
    private ArrayList<Square> possibleMoves;

    //può rappresentare sia l'ultima posizione in cui avevamo costruito sia il punto dove costruiremo
    private Square position;

    private final Player player;



    public SpecialPhase3(Game game, Context context, Player player, Builder builder, Square position){
        super(game, context);
        this.player = player;
        this.playingBuilder = builder;
        this.position = position;
        map();
    }


    public void handle() throws IOException{

        ArrayList<Square> moves1 = getMoves(playingBuilder , position);
        boolean buildDome = player.getCard().getParameters().buildDome;
        Envelope received = context.getNetInterface().getBuildMove(moves1, playingBuilder, buildDome ,player);

        if(received.getMove() != null)  //TODO received != null ???
            actionMethod(received.getBuilder(), received.getMove(), received.getIsDome());
            //updateBoard(game.getBoard);

        if(!(game.getGameEnded()))
            context.setPhase(null);

    }

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


    public ArrayList<Square> getMoves(Builder builder, Square lastPosition){

        this.playingBuilder = builder;
        this.position = lastPosition;

        possibleMoves= basicRules.getBuildingRange(builder);
        movesCommands.get(player.getCard().parameters.specialPhase3Moves).run();

        return possibleMoves;
    }

    //getMoves
    public void notSameSquare(){
        for(int i = 0; i < possibleMoves.size(); i++) {
            if (possibleMoves.get(i).equals(position)) {
                possibleMoves.remove(i);
                i--;
            }
        }
    }

    public void sameSquareNotDome(){
        possibleMoves = new ArrayList<>();
        if(position.getLevel() < 3)
            possibleMoves.add(position);
    }

    public void notPerimeter(){
        for(int i =0; i < possibleMoves.size(); i++) {
            Square pos = possibleMoves.get(i);
            if (pos.x == 0 || pos.y == 0 || pos.x == Board.BOARDSIZEX - 1 || pos.y == Board.BOARDSIZEY -1 ) {   //casella perimetrale
                possibleMoves.remove(i);
                i--;
            }
        }
    }

    public void actionMethod(Builder builder, Square position, boolean isDome){
        this.playingBuilder = builder;
        this.position = position;

        actionCommands.get(player.getCard().parameters.specialPhase3Action).run();
        basicRules.build(player, position, isDome);
    }



}
