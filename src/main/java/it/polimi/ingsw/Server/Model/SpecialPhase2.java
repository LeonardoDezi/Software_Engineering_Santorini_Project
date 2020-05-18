package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase2 {

    private final Game game;
    private final Board board;
    private final BasicRules basicRules;
    private HashMap<String, Runnable> movesCommands;
    private HashMap<String, Runnable> actionCommands;


    private Card card;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    private Square position;


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
        actionCommands.put("doubleNotSame", ()->{board.move(builder.getPosition(), position);});
        actionCommands.put(null, ()->{});
    }

    public ArrayList<Square> getMoves(Player player, Builder builder, Square position){

        this.builder = builder;
        this.card = player.getCard();
        this.position = position;

        movesCommands.get(card.parameters.specialPhase2Moves).run();
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

        this.builder = builder;  //forse non serve?
        this.position = position;

        actionCommands.get(card.parameters.specialPhase2Action).run();
    }



}
