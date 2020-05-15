package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class MovementPhase {

    //revisionare gli attributi
    private final Game game;
    private final Board board;
    private final BasicRules basicRules;
    private HashMap<String, Runnable> movesCommands;
    private HashMap<String, Runnable> actionCommands;


    private Card card;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    //servono?
    private Square position;
    private String playerColour; // al posto di player colour magari mettere per tutti un player?
    private int i;



    public MovementPhase(Game game){
        basicRules = game.getRules();   // assicurarsi che siano sempre le stesse rules
        this.game = game;
        this.board = game.getBoard();
        map();
    }



    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();

        //getMoves
        movesCommands.put("swap", this::swapMoves);
        movesCommands.put("push", this::swapMoves);
        movesCommands.put(null, () ->{possibleMoves = basicRules.removeBuilderSquare(possibleMoves);});  //qua non dovrebbe fare niente(?)


        //Movement
        actionCommands.put("jumpUp", this::jumpUp);
        actionCommands.put("push2", this::minotauro); //cambiare TUTTI i nomi
        actionCommands.put("restore", () -> {basicRules.setMaxHeight(1);});  //da usare quando Prometeo termina il suo turno e non poteva salire
        actionCommands.put(null, () -> {});

    }

//getMoves
    public ArrayList<Square> getMoves( Player player, Builder builder){

        if(builder == null)   // nel caso di builder non esistente
            return new ArrayList<>();     //ritorna lista vuota  (necessario mettere Square?)

        //è necessaria?
        this.builder = builder;
        this.card = player.getCard();

        possibleMoves = basicRules.proximity(builder); // mappa il circondario del builder
        possibleMoves = basicRules.removeDomeSquare(possibleMoves); // queste condizioni vengono rispettate da tutte le carte FORSE POSSIAMO SEMPLIFICARLA
        possibleMoves = basicRules.removeTooHighPlaces(possibleMoves, builder); // queste condizioni vengono rispettate da tutte le carte

        playerColour = builder.getColour();
        movesCommands.get(card.parameters.movementPhaseMoves).run();

        return possibleMoves;
    }

    public void swapMoves() {

        HashMap<String, Runnable> cardMap = new HashMap<>();  // cambiare il nome
        cardMap.put("swap",()->{});
        cardMap.put("push", this::pushMoves);  //cambiare i nomi
        cardMap.put(null, ()->{}); //non necessaria la metto per eventuali casi futuri


        for (i = 0; i < possibleMoves.size(); i++) {
            if (possibleMoves.get(i).getValue() == 1) {
                if (possibleMoves.get(i).getBuilder().getColour().equals(playerColour)) {   //pedine dello stesso giocatore
                    possibleMoves.remove(i);
                    i--;
                }
                else {
                    position = possibleMoves.get(i);
                    cardMap.get(card.parameters.movementPhaseAction).run();
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

    //cancellare
   /* public void zeus(){
        possibleMoves = basicRules.removeBuilderSquare(possibleMoves);
        possibleMoves.add(builder.getPosition());
    }*/



//movement
    public void movement(Builder builder, Square arrival){
        // qui player e card non vengono aggiornati. rimangono quelli impostati precedentemente
        this.builder = builder;
        this.position = arrival;
        actionCommands.get(card.parameters.movementPhaseAction).run();
    }

    public void jumpUp(){
        int level1 = builder.getPosition().getLevel();
        int level2 = position.getLevel();
        if(level2 - level1 > 0)
            basicRules.setMaxHeight(0);
    }


    public void minotauro(){

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




}

