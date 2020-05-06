package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class MovementPhase {

    //revisionare gli attributi
    private Board board;
    private Card card;
    private HashMap<String, Runnable> commands;
    private Rules basicRules;
    private Builder builder;
    private Square position;
    private ArrayList<Square> possibleMoves;
    private String colour;


    public MovementPhase(Card card, Rules rules, Board board){
        basicRules = rules;
        this.card = card;
        this.board = board;
        map();
    }

    public void map(){
        commands = new HashMap<>();
        commands.put("swap", this::swap);   //rivedere lambda functions
        commands.put("basic", () -> basic());  //qua non dovrebbe fare niente(?)
        commands.put("push", () -> minotauro()); //cambiare TUTTI i nomi

    }


    public ArrayList<Square> getMoves( Builder builder){
        possibleMoves = basicRules.proximity(builder); // mappa il circondario del builder
        possibleMoves = basicRules.removeDomeSquare(possibleMoves); // queste condizioni vengono rispettate da tutte le carte FORSE POSSIAMO SEMPLIFICARLA
        possibleMoves = basicRules.removeTooHighPlaces(possibleMoves, builder); // queste condizioni vengono rispettate da tutte le carte
        colour = builder.getColour();
        commands.get(Card.MovementPhase).run();

        return possibleMoves;
    }

    public void swap() {
        int i;
        HashMap cardMap = new HashMap<String, Runnable>();
        cardMap.put("basic",/* come si scrive che non deve fare niente?*/);
        cardMap.put("push", this::push());  //non so perchè non va qui
        for (i = 0; i < possibleMoves.size(); i++) {
            if (possibleMoves.get(i).getValue() == 1) {
                if (possibleMoves.get(i).getBuilder().getColour().equals(colour))   //da riscrivere
                    possibleMoves.remove(i);
                else
                    cardMap.get(Card.altroAttributo).run();
            }
        }
    }

    public void push(){


        //non so come fare bene nè push del minotauro nè caronte. come fai a decidere i giusti square?
    }

    public void movement(Builder builder, Square arrival){
        commands.get(Card.sottoAttributodadefinire).run();
    }

    public void minotauro(){

        if(position.getValue() == 1)
            //non so come trovare lo square di destinazione
            board.move(arrival ,pointB); //questa funzione dovrà limitarsi a spostare la pedina avversaria
    }
}

