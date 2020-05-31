package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;
import it.polimi.ingsw.Server.Controller.Phase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BuildingPhase extends Phase {


    private HashMap<String, Runnable> movesCommands;
    private HashMap<String, Runnable> actionCommands;

    private Builder playingBuilder;
    private ArrayList<Square> possibleMoves;

    private Square position;

    private final Player player;
    private Builder actionBuilder;
    private boolean isDome;   //necessario per far funzionare Selene

    public BuildingPhase(Game game, Context context, Player player, Builder builder){
        super(game, context);
        this.player = player;
        this.playingBuilder = builder;
        map();
    }


    public void handle() throws IOException{

        ArrayList<Square> moves1 = getMoves(playingBuilder);
        Boolean canBuildDome = player.getCard().getParameters().buildDome;
        Envelope received;


        if(player.getCard().getParameters().buildingPhaseMoves.equals("askForFemale")){   //se la carta è Selene

            if (playingBuilder.sex.equals(Player.SEX2)) {
                received=context.getNetInterface().getBuildMove(moves1, playingBuilder, true, player);
            }else if(player.getBuilderSize() ==2){
                Builder female = player.getFemale();
                ArrayList<Square> moves2 = basicRules.getBuildingRange(female);
                received = context.getNetInterface().getBothBuildMove(moves1, playingBuilder, moves2, female, true, player);   //TODO usiamo canBuildADome per indicare che la femmina può costruire. Ricordarselo quando si implementa getBothBuildMove
            }else
                received =context.getNetInterface().getBuildMove(moves1, playingBuilder, false, player);   //il worker è maschio e non può costruire cupole

        }else

            received = context.getNetInterface().getBuildMove(moves1, playingBuilder, canBuildDome, player);


        actionMethod(received.getBuilder(), received.getMove(), received.getIsDome());
        //updateBoard(game.getBoard);  ????

        if(!(game.getGameEnded()))
            context.setPhase(new SpecialPhase3(game, context, player, actionBuilder, position));

    }

    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();

//getMoves
        movesCommands.put(null, () ->{});
        movesCommands.put("addBuilderPosition", this::addBuilderPosition);  //Zeus
//building
        actionCommands.put("buildBelowYou", this::buildBelowYou);
        actionCommands.put("maleOrFemale", this::maleOrFemale);
        actionCommands.put(null, () ->{});

    }

    public ArrayList<Square> getMoves(Builder builder){

         this.playingBuilder = builder;
         possibleMoves = basicRules.getBuildingRange(builder);
         movesCommands.get(player.getCard().parameters.buildingPhaseMoves).run();
         return possibleMoves;
    }

    public void addBuilderPosition(){
        Square currentPosition = playingBuilder.getPosition();
        if (position.getLevel() < 3)   //dobbiamo accertarci di questo casomai zeus si muovesse da una torre di tre a un' altra
            possibleMoves.add(currentPosition);
    }



    public void actionMethod(Builder builder, Square position, boolean isDome){
        this.actionBuilder = builder;
        this.position = position;
        this.isDome = isDome;

        actionCommands.get(player.getCard().parameters.buildingPhaseAction).run();
        basicRules.build(player, position, this.isDome);
    }



    public void buildBelowYou(){
        if(position.equals(actionBuilder.getPosition())) {
            if (position.getLevel() < 3) {
                position.setLevel(position.getLevel() + 1);   //TODO come aggiornare la GUI in questo caso?
                //updateBoard()?????
                position = null;
            }
        }
    }

    public void maleOrFemale(){
        if(!(actionBuilder.equals(playingBuilder)))  //se non corrispondono allora sarà il builder donna che potrà solo costruire cupole a questo punto
            this.isDome = true;
    }



}
