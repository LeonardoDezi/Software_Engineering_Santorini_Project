package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase2 {

    private final Game game;
    private final Board board;
    private final BasicRules basicRules;
    private HashMap<String, Runnable> movesCommands;
    private HashMap<String, Runnable> actionCommands;

    private Builder builder;
    private Square position;
    private ArrayList<Square> possibleMoves;
    private Player player;



    public SpecialPhase2(Game game){
        basicRules = game.getRules();   // assicurarsi che siano sempre le stesse rules
        this.game = game;
        this.board = game.getBoard();
        map();
    }


    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();

        //getMoves
        movesCommands.put("doubleNotSameMove", this::doubleNotSameMove);  //Artemide
        movesCommands.put(null, ()->{possibleMoves = new ArrayList<>();});

        //actionMethod
        actionCommands.put("doubleNotSame", ()->{basicRules.move(player, builder.getPosition(), position);});
        actionCommands.put(null, ()->{});
    }

    public ArrayList<Square> getMoves(Player player, Builder builder, Square position){

        this.player = player;
        this.builder = builder;
        this.position = position;

        movesCommands.get(player.getCard().parameters.specialPhase2Moves).run();
        return possibleMoves;
    }

    public void doubleNotSameMove(){

        possibleMoves = basicRules.proximity(builder);
        possibleMoves = basicRules.removeBuilderSquare(possibleMoves);
        possibleMoves = basicRules.removeTooHighPlaces(possibleMoves, builder);
        possibleMoves = basicRules.removeDomeSquare(possibleMoves);    //considerare se sia il caso di accorpare queste due

        for(int i =0; i<possibleMoves.size(); i++) {
            if (possibleMoves.get(i).equals(position)) {
                possibleMoves.remove(i);
                i--;
            }
        }
    }

    public void actionMethod(Builder builder, Square position){

        this.builder = builder;
        this.position = position;

        actionCommands.get(player.getCard().parameters.specialPhase2Action).run();
    }



}
