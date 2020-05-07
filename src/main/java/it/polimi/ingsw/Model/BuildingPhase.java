package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class BuildingPhase{

    private Board board;
    private Card card;
    private HashMap<String, Runnable> commands;
    private Rules basicRules;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    public BuildingPhase (Card card, Rules rules, Board board){
        basicRules = rules;
        this.card = card;
        this.board = board;
        map();
    }


    public void map(){
        commands = new HashMap<>();
        commands.put("basic", () -> return null);  //boh);
        commands.put("Zeus", ()-> addBuilderPosition());
        commands.put("Zeus2", () -> buildBelowYou());

    }

    public void getMoves(Builder builder){
         this.builder = builder;
         possibleMoves = basicRules.getBuildingRange(builder);  //potrebbe darci problemi a lungo andare?
         commands.get(Card.BuildingPhase).run();
    }


    public void building(Square position){
        if(position.equals(builder.getPosition()))   // questo if mi sembra legittimo dato che credo che l'unico modo
           commands.get(Card.AltroParametroBuildingPhase).run(); // in cui possiamo infrangere le regole arrivati a questo punto è che i due square coincidano
        else
           board.build(/*ecc...*/);
    }


    public void addBuilderPosition(){
        Square position = builder.getPosition();
        if(position.getLevel() < 3)   // caso mai zeus si muovesse da una torre di tre a un' altra
            possibleMoves.add(position);
    }

    public void buildBelowYou(){
        Square position = builder.getPosition();
        position.setLevel(position.getLevel() +1);
    }
}
