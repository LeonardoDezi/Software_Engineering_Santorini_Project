package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Controller.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the moment of the game in which the player makes its standard movement
 */
public class MovementPhase extends Phase {

    /** represents the hashMap containing the keys related to the method getMoves() */
    private HashMap<String, Runnable> movesCommands;
    /** represents the hashMap containing the keys related to the method actionMethod() */
    private HashMap<String, Runnable> actionCommands;
    /** represents the current player's first worker */
    private Builder builder1;
    /** represents the current player's second worker */
    private Builder builder2;
    /** represents the current player's playing worker */
    private Builder playingBuilder;
    /** generic parameter used by actionMethod() to save the position where the extra move is going to be performed,
     * or by getMoves() when examining the worker's possible moves */
    private Square position;
    /** represents the possible moves that a worker can make */
    private ArrayList<Square> possibleMoves;
    /** represents the player that is currently playing */
    private final Player player;
    /** generic parameter used by pushMoves() to remove a Square from possibleMoves*/
    private int i;


    /**
     * used to create a new MovementPhase
     * @param game represents the game
     * @param context represents the context of the game
     * @param player represents the current player
     * @param builder1 represents the current player's first worker
     * @param builder2 represents the current player's second worker
     */
    public MovementPhase(Game game, Context context, Player player, Builder builder1, Builder builder2) {
        super(game, context);
        this.player = player;
        this.builder1 = builder1;
        this.builder2 = builder2;

    }

   // public BasicRules getBasicRules(){return this.basicRules;}

    /** method used by the phase to develop the part of the game logic assigned. In the State Pattern, it represents the main method that has to be implemented.
     *  It also includes the game's losing condition.
     */
    public void handle() throws IOException {

        ArrayList<Square> moves1 = getMoves(builder1);
        ArrayList<Square> moves2 = getMoves(builder2);


        if (!(moves1.isEmpty()) || !(moves2.isEmpty())) {

            Envelope received = context.getNetInterface().getBothMovementMove(moves1, builder1, moves2, builder2, player);

            Square lastPosition = received.getBuilder().getPosition();
            actionMethod(received.getBuilder(), received.getMove());
            //updateBoard(game.getBoard);

            if(!(game.getGameEnded()))
                context.setPhase(new SpecialPhase2(game, context, player, playingBuilder, lastPosition));


        }else{    //entrambi i worker sono incapaci di muoversi

            game.removePlayer(player);
            //loseMethod();
            //sendMessage("Il giocatore" + player + "ha perso", null); //per mandare in broadcast il campo player è null

            if(game.getPlayerList().size() == 1) {   //è rimasto solo un giocatore
                game.setWinningPlayer(game.playerList.get(0));
                game.setGameEnded(true);
            }

            context.setPhase(null);
        }
    }


    /** initializes the hashMaps related to the phase */
    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();

        //getMoves
        movesCommands.put("swap", this::swapMoves);   //Apollo
        movesCommands.put("push", this::swapMoves);   //Minotauro
        movesCommands.put(null, () -> possibleMoves = basicRules.removeBuilderSquare(possibleMoves));


        //Movement
        actionCommands.put("jumpUp", this::jumpUp);    //Athena
        actionCommands.put("pushAction", this::pushAction); //Minotauro
        actionCommands.put("restore", this :: restore);  //Prometeo
        actionCommands.put(null, () -> {});

    }

    /**
     * obtains the possible moves that a worker can make
     * @param builder is the worker examined
     * @return the arrayList of possible moves that the worker can make
     */
    public ArrayList<Square> getMoves(Builder builder){

        if(builder == null)   // nel caso di builder non esistente
            return new ArrayList<>();     //ritorna lista vuota  (necessario mettere Square?)

        this.playingBuilder = builder;

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
        int builderX = playingBuilder.getPosition().x;
        int builderY = playingBuilder.getPosition().y;

        int positionX = position.x;
        int positionY = position.y;

        int a = 2 * positionX - builderX;    //coordinate della casella retrostante il worker esaminato
        int b = 2 * positionY - builderY;

        try{
            if(board.fullMap[a][b].getValue() != 0) {
                possibleMoves.remove(i);
                i--;
            }
        }catch(ArrayIndexOutOfBoundsException e){  // nel caso in cui la casella sia inesistente
            possibleMoves.remove(i);
            i--;
        }


    }



    /**
     * executes the move sent by the worker, according to the rules assigned by the game and/or the player's card
     * @param builder is the worker who makes the move
     * @param arrival is the position where the move is going to be performed
     */
    public void actionMethod(Builder builder, Square arrival){
        this.playingBuilder = builder;
        this.position = arrival;
        actionCommands.get(player.getCard().parameters.movementPhaseAction).run();
        Square lastPosition = playingBuilder.getPosition();
        basicRules.move(player, lastPosition, position);  //movimento effettivo
    }

    public void jumpUp(){
        int level1 = playingBuilder.getPosition().getLevel();
        int level2 = position.getLevel();
        if(level2 - level1 > 0)
            basicRules.setMaxHeight(0);
    }


    public void pushAction(){

        if(position.getValue() == 1){

            int builderX = playingBuilder.getPosition().x;
            int builderY = playingBuilder.getPosition().y;

            int positionX = position.x;
            int positionY = position.y;

            int a = 2 * positionX - builderX;
            int b = 2 * positionY - builderY;

            board.move(position ,board.fullMap[a][b]);  //questa funzione dovrà limitarsi a spostare la pedina avversaria

        }

    }

    public void restore(){
        basicRules.setMaxHeight(basicRules.getPreviousMaxHeight());  //ristabilisce maxHeight all'altezza precedente
        basicRules.setPreviousMaxHeight(BasicRules.BASICMAXHEIGHT);  //pone previousMaxHeight all'altezza base
    }




}

