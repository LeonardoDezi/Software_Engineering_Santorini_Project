package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase3 {

    private Board board;
    private Card card;
    private HashMap<String, Runnable> commands;
    private Rules basicRules;
    private Builder builder;
    private ArrayList<Square> possibleMoves;
    private Square position;


    public SpecialPhase3(Card card, Rules rules, Board board){
        basicRules = rules;   // assicurarsi che siano sempre le stesse rules
        this.card = card;
        this.board = board;
        map();
    }

    public void map(){
        commands = new HashMap<>();
        commands.put(null, ()->{possibleMoves = null;});   //decidere bene
        commands.put("notSameSquare", this::notSameSquare);
        commands.put("sameSquare", this::sameSquare);
        commands.put("notPerimeter", ()->notPerimeter());
    }

    public ArrayList<Square> getMoves(Builder builder /*Card*/, Square lastPosition){
        this.builder = builder;
        this.position = position;
        possibleMoves= basicRules.getBuildingRange(builder);
        commands.get(Card.specialPhase3).run();
        return possibleMoves;
    }

    //getMoves
    public void notSameSquare(){
        for(int i = 0; i < possibleMoves.size(); i++) {
            if (possibleMoves.get(i).equals(position))
                possibleMoves.remove(i);
        }
    }

    public void sameSquare(){
        possibleMoves = new ArrayList<>();
        if(position.getLevel() < 3)   // per rispetto dell'atomicità dovremmo dividere questo metodo in due
            possibleMoves.add(position);
    }

    public void notPerimeter(){
        for(int i =0; i < possibleMoves.size(); i++) {
            Square pos = possibleMoves.get(i);
            if (pos.x == 0 || pos.y == 0 || pos.x == 5 || pos.y == 5)
                possibleMoves.remove(i);
        }
    }



}
