package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class BuildingPhase{

    private final Game game;
    private final Board board;
    private final BasicRules basicRules;
    private HashMap<String, Runnable> movesCommands;
    private HashMap<String, Runnable> actionCommands;


    private Card card;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    private Square position;

    private Player player;
    private Builder actionBuilder;
    private boolean isDome;

    public BuildingPhase(Game game){
        basicRules = game.getRules();   // assicurarsi che siano sempre le stesse rules
        this.game = game;
        this.board = game.getBoard();
        map();
    }



    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();

//getMoves
        movesCommands.put(null, () ->{});
        movesCommands.put("addBuilderPosition", this::addBuilderPosition);  //zeus
        movesCommands.put("askForDome", this::askForDome);
        movesCommands.put("askForFemale", this::askForFemale);
//building
        actionCommands.put("buildBelowYou", this::buildBelowYou);
        actionCommands.put("maleOrFemale", this::maleOrFemale);
        actionCommands.put(null, () ->{});



    }

    public ArrayList<Square> getMoves(Player player, Builder builder){

         this.builder = builder;
         this.player = player;
         this.card = player.getCard();
         possibleMoves = basicRules.getBuildingRange(builder);
         movesCommands.get(card.parameters.buildingPhaseMoves).run();
         return possibleMoves;
    }

    public void addBuilderPosition(){
        Square position = builder.getPosition();
        if(position.getLevel() < 3)   //dobbiamo accertarci di questo caso mai zeus si muovesse da una torre di tre a un' altra
            possibleMoves.add(position);
    }

    public void askForDome(){
        //sendrequest  "vuoi costruire una cupola?" ancora non so come
    }

    public void askForFemale(){
        if(builder.sex == "male" && player.builders.size() == 2){
            ArrayList<Square> tmp = basicRules.getBuildingRange(player.getFemale());
            //sendBuildingPhase(femaleBuilder, tmp);
        }else
            System.out.println("ciao0"); //solo per i test
            //askForDome

    }


    public void actionMethod(Builder builder, Square position, boolean isDome){
        this.actionBuilder = builder;
        this.position = position;
        this.isDome = isDome;

        actionCommands.get(card.parameters.buildingPhaseAction).run();
        basicRules.build(player, position, isDome);
    }



    public void buildBelowYou(){
        if(position.equals(actionBuilder.getPosition())) {
            if (position.getLevel() < 3) {
                position.setLevel(position.getLevel() + 1);
                position = null;
            }
        }
    }

    public void maleOrFemale(){
        if(!(actionBuilder.equals(builder)))
            isDome = true;
    }



}
