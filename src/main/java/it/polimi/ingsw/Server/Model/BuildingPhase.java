package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class BuildingPhase{

    private final Game game;
    private final Board board;
    private final Rules basicRules;
    private HashMap<String, Runnable> commands;


    private Card card;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    private Square position;


    public BuildingPhase(Game game){
        basicRules = game.getRules();   // assicurarsi che siano sempre le stesse rules
        this.game = game;
        this.board = game.getBoard();
        map();
    }



    public void map(){
        commands = new HashMap<>();

        commands.put(null, () ->{});
//getMoves
        commands.put("ZeusMove", this::addBuilderPosition);
        commands.put("dome", this::askForDome);
        commands.put("female", this::askForFemale);
//building
        commands.put("ZeusBuild", this::buildBelowYou);



    }

    public ArrayList<Square> getMoves(Player player, Builder builder){

         this.builder = builder;
         this.card = player.getCard();
         possibleMoves = basicRules.getBuildingRange(builder);
         commands.get(card.BuildingPhase).run();
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
        //sendrequest "ti sto mandando anche il range del tuo worker femmina vuoi usare questo
        //piuttosto che l'altro?"
    }


    public void building(Builder builder, Square position, boolean isDome){
        this.builder = builder;
        this.position = position;
        if(position.equals(builder.getPosition()))   // questo if mi sembra legittimo dato che credo che l'unico modo
           commands.get(Card.AltroParametroBuildingPhase).run(); // in cui possiamo infrangere le regole arrivati a questo punto è che i due square coincidano

           board.build(position, isDome);
    }

    //Zeus: se deve costruire su se stesso lo farà nella building Phase e metterà lo square di build a null: modificare build() perchè non faccia niente
    //in questo caso


    public void buildBelowYou(){
        if(position.getLevel() < 3) {
            position.setLevel(position.getLevel() + 1);
            position = null;
        }
    }



}
