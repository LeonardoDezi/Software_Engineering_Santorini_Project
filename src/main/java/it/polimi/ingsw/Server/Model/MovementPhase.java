package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class MovementPhase {


    private final Game game;
    private final Board board;
    private final BasicRules basicRules;
    private HashMap<String, Runnable> movesCommands;
    private HashMap<String, Runnable> actionCommands;

    private Builder builder;
    private Square position;
    private ArrayList<Square> possibleMoves;
    private Player player;

    private int i;

    public BasicRules getBasicRules(){return this.basicRules;}

    public MovementPhase(Game game){
        basicRules = game.getRules();
        this.game = game;
        this.board = game.getBoard();
        map();
    }



    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();

        //getMoves
        movesCommands.put("swap", this::swapMoves);   //Apollo
        movesCommands.put("push", this::swapMoves);   //Minotauro
        movesCommands.put(null, () ->{possibleMoves = basicRules.removeBuilderSquare(possibleMoves);});  //qua non dovrebbe fare niente(?)


        //Movement
        actionCommands.put("jumpUp", this::jumpUp);    //Athena
        actionCommands.put("pushAction", this::pushAction); //Minotauro
        actionCommands.put("restore", this :: restore);  //Prometeo
        actionCommands.put(null, () -> {});

    }

//getMoves
    public ArrayList<Square> getMoves( Player player, Builder builder){

        if(builder == null)   // nel caso di builder non esistente
            return new ArrayList<>();     //ritorna lista vuota  (necessario mettere Square?)

        this.player = player;
        this.builder = builder;

        possibleMoves = basicRules.proximity(builder);
        possibleMoves = basicRules.removeDomeSquare(possibleMoves);
        possibleMoves = basicRules.removeTooHighPlaces(possibleMoves, builder);

        movesCommands.get(player.getCard().parameters.movementPhaseMoves).run();

        return possibleMoves;
    }

    public void swapMoves() {

        HashMap<String, Runnable> cardMap = new HashMap<>();
        cardMap.put("swap",()->{});
        cardMap.put("push", this::pushMoves);
        cardMap.put(null, ()->{});


        for (i = 0; i < possibleMoves.size(); i++) {
            if (possibleMoves.get(i).getValue() == 1) {
                if (possibleMoves.get(i).getBuilder().getColour().equals(player.getColour())) {   //pedine dello stesso giocatore
                    possibleMoves.remove(i);
                    i--;
                }
                else {
                    position = possibleMoves.get(i);
                    cardMap.get(player.getCard().parameters.movementPhaseMoves).run();
                }
            }
        }
    }



    public void pushMoves(){
        int builderX = builder.getPosition().x;
        int builderY = builder.getPosition().y;

        int positionX = position.x;
        int positionY = position.y;

        int a = 2 * positionX - builderX;
        int b = 2 * positionY - builderY;

        try{
            if(board.fullMap[a][b].getValue() != 0) {
                possibleMoves.remove(i);
                i--;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            possibleMoves.remove(i);
            i--;
        }


    }



//movement
    public void actionMethod(Builder builder, Square arrival){
        this.builder = builder;
        this.position = arrival;
        actionCommands.get(player.getCard().parameters.movementPhaseAction).run();
        Square lastPosition = builder.getPosition();
        basicRules.move(player, lastPosition, position);  //movimento effettivo
    }

    public void jumpUp(){
        int level1 = builder.getPosition().getLevel();
        int level2 = position.getLevel();
        if(level2 - level1 > 0)
            basicRules.setMaxHeight(0);
    }


    public void pushAction(){

        if(position.getValue() == 1){

            int builderX = builder.getPosition().x;
            int builderY = builder.getPosition().y;

            int positionX = position.x;
            int positionY = position.y;

            int a = 2 * positionX - builderX;
            int b = 2 * positionY - builderY;

            board.move(position ,board.fullMap[a][b]);  //questa funzione dovrà limitarsi a spostare la pedina avversaria

        }

    }

    public void restore(){
        basicRules.setMaxHeight(basicRules.getPreviousMaxHeight());
        basicRules.setPreviousMaxHeight(BasicRules.BASICMAXHEIGHT);
    }




}

