package it.polimi.ingsw.Model;

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
    private String playerColour;



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
        commands.put("basic", this::basicMoves);  //qua non dovrebbe fare niente(?)   //BASIC = NULL
        commands.put("push", this::minotauro); //cambiare TUTTI i nomi
        commands.put("Zeus", this::zeus);
        commands.put("restore", this::restore);  //da usare quando Prometeo termina il suo turno e non poteva salire

        //Movement
        commands.put("jumpUp", this::jumpUp);

    }


    public ArrayList<Square> getMoves( Player player, Builder builder){

        this.builder = builder;

        possibleMoves = basicRules.proximity(builder); // mappa il circondario del builder
        possibleMoves = basicRules.removeDomeSquare(possibleMoves); // queste condizioni vengono rispettate da tutte le carte FORSE POSSIAMO SEMPLIFICARLA
        possibleMoves = basicRules.removeTooHighPlaces(possibleMoves, builder); // queste condizioni vengono rispettate da tutte le carte
        playerColour = builder.getColour();
        commands.get(Card.MovementPhase).run();

        return possibleMoves;
    }

    public void swapMoves() {
        int i;
        HashMap<String, Runnable> cardMap = new HashMap<>();  // cambiare il nome
        cardMap.put("basic",()->{});      //BASIC = NULL
        cardMap.put("push2", this::pushMoves);  //cambiare i nomi
        for (i = 0; i < possibleMoves.size(); i++) {
            if (possibleMoves.get(i).getValue() == 1) {
                if (possibleMoves.get(i).getBuilder().getColour().equals(playerColour))   //pedine dello stesso giocatore
                    possibleMoves.remove(i);
                else
                    cardMap.get(Card.altroAttributo).run();
            }
        }
    }

    public void basicMoves(){
        possibleMoves = basicRules.removeBuilderSquare(possibleMoves);
    }

    public void pushMoves(){
        //non so come fare bene nè push del minotauro nè caronte. come fai a decidere i giusti square?
    }

    public void zeus(){
        possibleMoves = basicRules.removeBuilderSquare(possibleMoves);
        possibleMoves.add(builder.getPosition());
    }





    public void movement(Builder builder, Square arrival){

        this.builder = builder;
        this.position = arrival;
        commands.get(Card.sottoAttributodadefinire).run();
    }

    public void jumpUp(){
        int level1 = builder.getPosition().getLevel();
        int level2 = position.getLevel();    //verificare che position non dia misunderstandings

        if(level2 - level1 != 0)  //controllare i valori negativi
            basicRules.setMaxHeight(0);
    }


    public void minotauro(){

        if(position.getValue() == 1)
            //non so come trovare lo square di destinazione
            board.move(arrival ,pointB); //questa funzione dovrà limitarsi a spostare la pedina avversaria
    }




}

