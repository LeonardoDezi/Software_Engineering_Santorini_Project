package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase2 {
    private Board board;
    private Card card;   //le carte devono essere sempre aggiornateeee
    private HashMap<String, Runnable> commands;
    private Rules basicRules;
    private Builder builder;
    private ArrayList<Square> possibleMoves;
    private Square position;

    public SpecialPhase2(Board board){
        this.board = board;
        map();
    }

    public void map(){
        commands = new HashMap<>();

        //getMoves
        commands.put("doubleMove", () -> artemis());  //cambiare i nomi
        commands.put(null, ()->{});

        //generic
        commands.put("doubleNotSame", gameBoard.move(builder.getPosition(), position));
    }

    public ArrayList<Square> getMoves(Builder builder, Square position){
        this.builder = builder;
        this.position = position;
        commands.get(Card.specialPhase2-getMoves).run();
        return possibleMoves;
    }

    public void artemis(){

        this.builder = builder;

        possibleMoves = basicRules.proximity(builder);
        possibleMoves = basicRules.removeBuilderSquare(possibleMoves);
        possibleMoves = basicRules.removeTooHighPlaces(possibleMoves, builder);
        possibleMoves = basicRules.removeDomeSquare(possibleMoves);    //considerare se sia il caso di accorpare queste due

        for(int i =0; i<possibleMoves.size(); i++) {
            if (possibleMoves.get(i).equals(position))
                possibleMoves.remove(i);
        }
    }

    public void genericMethod(Builder builder, Square position){
        this.builder = builder;
        this.position = position;
        commands.get(Card.specialPhase2-generic).run();
    }


    /*artemide
    public void doubleNotSame(){

    }*/

}
