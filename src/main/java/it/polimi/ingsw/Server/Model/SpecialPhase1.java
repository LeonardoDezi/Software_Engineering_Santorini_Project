package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;
import it.polimi.ingsw.Server.Controller.Phase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class SpecialPhase1 extends Phase {

    private HashMap<String, Runnable> movesCommands;
    private HashMap<String, Runnable> actionCommands;

    private Builder builder1;
    private Builder builder2;

    private Builder playingBuilder;
    private Square position;
    private ArrayList<Square> possibleMoves;
    private final Player player;



    public SpecialPhase1(Game game, Context context, Player player, Builder builder1, Builder builder2) {
        super(game, context);
        this.player = player;
        this.builder1 = builder1;
        this.builder2 = builder2;
    }


    public void handle() throws IOException {

        //IMPORTANTE : TUTTE LE CARTE DEVONO PASSARE PER OGNI GETMOVES
        ArrayList<Square> moves1 = getMoves(builder1);
        ArrayList<Square> moves2 = getMoves(builder2);

        //netInterface.sendMessage()
        Envelope received = context.getNetInterface().getBothMovementMove(moves1, builder1, moves2, builder2, player);

        if(received != null ) {   //TODO received sarà null?
            actionMethod(received.getBuilder(), received.getMove());
            //updateBoard(game.getBoard);
        }

        if(!(game.getGameEnded()))
            context.setPhase(new MovementPhase(game, context, player, builder1, builder2));

    }



    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();

    //getMoves
        movesCommands.put("additionalBuild", () -> possibleMoves = basicRules.getBuildingRange(playingBuilder)); //Prometeo
        movesCommands.put("oppositeSideMoves", this::oppositeSideMoves);  //Caronte
        movesCommands.put(null, () ->{possibleMoves = new ArrayList<>();});
        movesCommands.put("restore", this::restore);  //Athena

    //actionMethod
        actionCommands.put(null, ()->{});
        actionCommands.put("specialBuild", this::specialBuild);  //Prometeo
        actionCommands.put("moveOpposite", this::moveOpposite);  //Caronte
    }


    public ArrayList<Square> getMoves (Builder builder){

        if(builder == null)   // nel caso di builder non esistente
            return new ArrayList<>();     //ritorna lista vuota

        this.playingBuilder = builder;
        movesCommands.get(player.getCard().parameters.specialPhase1Moves).run();
        return possibleMoves;
    }


    public void oppositeSideMoves(){

        possibleMoves = basicRules.proximity(playingBuilder);

        for(int i =0; i < possibleMoves.size(); i++) {

            Square position = possibleMoves.get(i);
            Builder opponentBuilder = position.getBuilder();


            if(position.getValue() == 1 && !(opponentBuilder.getColour().equals(playingBuilder.getColour()))){

                    int builderX = playingBuilder.getPosition().x;
                    int builderY = playingBuilder.getPosition().y;

                    int positionX = position.x;
                    int positionY = position.y;

                    int a = 2 * builderX - positionX;   //coordinate della casella opposta
                    int b = 2 * builderY - positionY;

                    try{
                        if(board.fullMap[a][b].getValue() != 0) {    //casella non libera
                            possibleMoves.remove(i);
                            i--;
                        }
                    }catch(ArrayIndexOutOfBoundsException e){   //TODO controllare che sia l'exception giusto: caso in cui la casella non esiste
                        possibleMoves.remove(i);
                        i--;
                    }

            }else{
                possibleMoves.remove(i);
                i--;
            }
        }

    }

    public void restore(){   //se athena aveva modificato maxHeight questo lo ristabilirà
        basicRules.setMaxHeight(BasicRules.BASICMAXHEIGHT);
        possibleMoves = new ArrayList<>();
    }




    public void actionMethod(Builder builder, Square position){
        this.playingBuilder = builder;
        this.position = position;
        actionCommands.get(player.getCard().parameters.specialPhase1Action).run();
    }

    //position deve essere legittima
    public void specialBuild() {
        if (position != null){  //TODO forse non servirà per via di received = null
            basicRules.build(player, position, false);
            basicRules.setPreviousMaxHeight( basicRules.getMaxHeight());   //necessario in quanto maxHeight potrebbe essere != 1
            basicRules.setMaxHeight(0);

            if(!(playingBuilder.equals(builder1)))     //la fase successiva deve necessariamente essere eseguita dal worker che ha fatto la mossa speciale
                builder1 = null;
            else
                builder2 = null;

        }
    }

    //position deve essere legittima
    public void moveOpposite(){

        int builderX = playingBuilder.getPosition().x;
        int builderY = playingBuilder.getPosition().y;

        int positionX = position.x;
        int positionY = position.y;

        int a = 2 * builderX - positionX;
        int b = 2 * builderY - positionY;

        Square destination = board.fullMap[a][b];
        board.move(position, destination);

        if(!(playingBuilder.equals(builder1)))     //la fase successiva deve necessariamente essere eseguita dal worker che ha fatto la mossa speciale
            builder1 = null;
        else
            builder2 = null;

    }






}
