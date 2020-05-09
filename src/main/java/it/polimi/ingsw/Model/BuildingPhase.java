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
        commands.put("basic", () ->{});
        commands.put("ZeusMove", this::addBuilderPosition);
        commands.put("ZeusBuild", this::buildBelowYou);
        commands.put("dome", this::askforDome);
        commands.put("female", this::askforFemale);

    }

    public ArrayList<Square> getMoves(Builder builder){
         this.builder = builder;
         possibleMoves = basicRules.getBuildingRange(builder);  //potrebbe darci problemi a lungo andare?
         commands.get(Card.BuildingPhase).run();
         return possibleMoves;
    }

    public void addBuilderPosition(){
        Square position = builder.getPosition();
        if(position.getLevel() < 3)   // caso mai zeus si muovesse da una torre di tre a un' altra
            possibleMoves.add(position);
    }

    public void askforDome(){
        //sendrequest  "vuoi costruire una cupola?" ancora non so come
    }

    public void askForFemale(){
        //sendrequest "ti sto mandando anche il range del tuo worker femmina vuoi usare questo
        //piuttosto che l'altro?"
    }


    public void building(Square position, boolean isDome){
        if(position.equals(builder.getPosition()))   // questo if mi sembra legittimo dato che credo che l'unico modo
           commands.get(Card.AltroParametroBuildingPhase).run(); // in cui possiamo infrangere le regole arrivati a questo punto è che i due square coincidano

           board.build(position, isDome);
    }

    //Zeus: se deve costruire su se stesso lo farà nella building Phase e metterà lo square di build a null: modificare build() perchè non faccia niente
    //in questo caso


    public void buildBelowYou(){
        Square position = builder.getPosition();
        if(position.getLevel() < 3) {
            position.setLevel(position.getLevel() + 1);
            position = null;
        }
    }



}
