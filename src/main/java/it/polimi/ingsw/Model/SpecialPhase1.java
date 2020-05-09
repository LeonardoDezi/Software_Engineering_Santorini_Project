package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase1 {
    //mi sa che conviene passare il giocatore facciamo prima
    private Board board;
    private Card card;
    private HashMap<String, Runnable> commands;
    private Rules basicRules;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    public SpecialPhase1(Card card, Rules rules, Board board){
        basicRules = rules;   // assicurarsi che siano sempre le stesse rules
        this.card = card;
        this.board = board;
        map();
    }

    public void map(){
        commands = new HashMap<>();
        commands.put("Prometeo", this::prometeo);
        commands.put("Caronte", this::caronte);
        commands.put("Basic", () ->{});    //controllare maxHeight
        commands.put("restore", this::restore);  //Athena
    }


    public ArrayList<Square> genericMethod (Builder builder, Card card){

        if(builder == null)   // nel caso di builder non esistente
            return null;

        this.builder = builder;
        this.card = card;
        commands.get(card.specialPhase1).run();
        return possibleMoves;
    }

    public void prometeo(){

        possibleMoves = basicRules.getBuildingRange(builder);
        return;

    }

    public void caronte(){

        possibleMoves = basicRules.proximity(builder);

        for(int i =0; i < possibleMoves.size(); i++) {

            Square position = possibleMoves.get(i);   // cambiare il nome
            Builder opponentBuilder = position.getBuilder();

            //al posto di getValue() controlliamo getBuilder() ma prima vorrei fare più test sugli square
            if (position.getValue() != 1 || opponentBuilder.getColour().equals(builder.getColour()))   // qualunque casella che non contenga una pedina o una pedina appartenente allo stesso giocatore
                possibleMoves.remove(i);

            else {
                // non so come mettere gli square opposti
            }
        }

    }


    public void restore(){   //se athena aveva modificato maxHeight questo lo ristabilirà
        basicRules.setMaxHeight(1);
        possibleMoves = null;
    }
}
