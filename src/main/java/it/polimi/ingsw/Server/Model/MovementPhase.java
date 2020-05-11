package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class MovementPhase {

    //revisionare gli attributi
    private final Game game;
    private final Board board;
    private final Rules basicRules;
    private HashMap<String, Runnable> commands;


    private Card card;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    //servono?
    private Square position;
    private String playerColour; // al posto di player colour magari mettere per tutti un player?



    public MovementPhase(Game game){
        basicRules = game.getRules();   // assicurarsi che siano sempre le stesse rules
        this.game = game;
        this.board = game.getBoard();
        map();
    }



    public void map(){
        commands = new HashMap<>();

        //getMoves
        commands.put("swap", this::swapMoves);
        commands.put("push", this::swapMoves);
        commands.put(null, () ->{possibleMoves = basicRules.removeBuilderSquare(possibleMoves);});  //qua non dovrebbe fare niente(?)


        //Movement
        commands.put("jumpUp", this::jumpUp);
        commands.put("push2", this::minotauro); //cambiare TUTTI i nomi
        commands.put("restore", () -> {basicRules.setMaxHeight(1);});  //da usare quando Prometeo termina il suo turno e non poteva salire


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
        commands.get(card.MovementPhase).run();

        return possibleMoves;
    }

    public void swapMoves() {

        HashMap<String, Runnable> cardMap = new HashMap<>();  // cambiare il nome
        cardMap.put("swap",()->{});
        cardMap.put("push", this::pushMoves);  //cambiare i nomi
        cardMap.put(null, ()->{}); //non necessaria la metto per eventuali casi futuri


        for (int i = 0; i < possibleMoves.size(); i++) {
            if (possibleMoves.get(i).getValue() == 1) {
                if (possibleMoves.get(i).getBuilder().getColour().equals(playerColour)) {   //pedine dello stesso giocatore
                    possibleMoves.remove(i);
                    i--;
                }
                else
                    cardMap.get(card.MovementPhase).run();
            }
        }
    }



    public void pushMoves(){
        //non so come fare bene nè push del minotauro nè caronte. come fai a decidere i giusti square?
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
        commands.get(Card.MovementPhase(movement)).run();
    }

    public void jumpUp(){
        int level1 = builder.getPosition().getLevel();
        int level2 = position.getLevel();
        if(level2 - level1 > 0)
            basicRules.setMaxHeight(0);
    }


    public void minotauro(){

        if(position.getValue() == 1)
            //non so come trovare lo square di destinazione
            board.move(position ,pointB); //questa funzione dovrà limitarsi a spostare la pedina avversaria
    }




}

