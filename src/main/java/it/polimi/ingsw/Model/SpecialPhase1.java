package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase1 {
    //mi sa che conviene passare il giocatore facciamo prima

    private final Game game;
    private final Board board;
    private final Rules basicRules;
    private HashMap<String, Runnable> commands;

    private Card card;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    //terminare la seconda parte di specialPhase1

    public SpecialPhase1(Game game){
        basicRules = game.getRules();   // assicurarsi che siano sempre le stesse rules
        this.game = game;
        this.board = game.getBoard();
        map();
    }

    /*public SpecialPhase1(){
        map();
    }*/     //altra possibilità: passare il player contenente il game. in questo modo potremmo fare il multi partita?

    public void map(){
        commands = new HashMap<>();
        commands.put("Prometeo", this::prometeo);
        commands.put("Caronte", this::caronte);
        commands.put("Basic", () ->{possibleMoves = new ArrayList<>();});    //controllare maxHeight
        commands.put("restore", this::restore);  //Athena
    }


    public ArrayList<Square> genericMethod (Player player, Builder builder){

        if(builder == null)   // nel caso di builder non esistente
            return new ArrayList<>();     //ritorna lista vuota  (necessario mettere Square?)

        this.builder = builder;
        this.card = player.getCard();
        commands.get(card.specialPhase1).run();
        return possibleMoves;
    }

    public void prometeo(){

        possibleMoves = basicRules.getBuildingRange(builder);

    }

    public void caronte(){

        possibleMoves = basicRules.proximity(builder);

        for(int i =0; i < possibleMoves.size(); i++) {

            Square position = possibleMoves.get(i);   // cambiare il nome
            Builder opponentBuilder = position.getBuilder();

            //al posto di getValue() controlliamo getBuilder() ma prima vorrei fare più test sugli square
            if (position.getValue() != 1 || opponentBuilder.getColour().equals(builder.getColour())) {   // qualunque casella che non contenga una pedina o una pedina appartenente allo stesso giocatore
                possibleMoves.remove(i);
                i--;
            }else {
                // non so come mettere gli square opposti
            }
        }

    }


    public void restore(){   //se athena aveva modificato maxHeight questo lo ristabilirà
        basicRules.setMaxHeight(1);
        possibleMoves = new ArrayList<>();
    }
}
