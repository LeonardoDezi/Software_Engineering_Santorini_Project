package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase2 {

    private final Game game;
    private final Board board;
    private final Rules basicRules;
    private HashMap<String, Runnable> commands;


    private Card card;   //le carte devono essere sempre aggiornateeee
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
        commands = new HashMap<>();

        //getMoves
        commands.put("doubleNotSameMove", this::artemis);  //cambiare i nomi
        commands.put(null, ()->{possibleMoves = new ArrayList<>();});

        //generic
        commands.put("doubleNotSame", ()->{board.move(builder.getPosition(), position);});
    }

    public ArrayList<Square> getMoves(Player player, Builder builder, Square position){

        this.builder = builder;
        this.card = player.getCard();
        this.position = position;

        commands.get(Card.specialPhase2-getMoves).run();
        return possibleMoves;
    }

    public void artemis(){

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

    public void genericMethod(Builder builder, Square position){

        this.builder = builder;  //forse non serve?
        this.position = position;

        commands.get(card.specialPhase2-generic).run();
    }


    /*artemide
    public void doubleNotSame(){

    }*/

}
