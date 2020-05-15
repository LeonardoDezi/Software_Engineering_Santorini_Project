package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase1 {
    //mi sa che conviene passare il giocatore facciamo prima

    private final Game game;
    private final Board board;
    private final BasicRules basicRules;
    private HashMap<String, Runnable> movesCommands;
    private HashMap<String, Runnable> actionCommands;

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
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();
    //getMoves
        movesCommands.put("additionalBuild", () -> {possibleMoves = basicRules.getBuildingRange(builder);});
        movesCommands.put("oppositeSideMoves", this::oppositeSideMoves);  //caronte
        movesCommands.put(null, () ->{possibleMoves = new ArrayList<>();});    //controllare maxHeight
    //actionMethod
        actionCommands.put("restore", this::restore);  //Athena
    }


    public ArrayList<Square> getMoves (Player player, Builder builder){

        if(builder == null)   // nel caso di builder non esistente
            return new ArrayList<>();     //ritorna lista vuota  (necessario mettere Square?)

        this.builder = builder;
        this.card = player.getCard();
        movesCommands.get(card.parameters.specialPhase1Action).run();
        return possibleMoves;
    }


    public void oppositeSideMoves(){

        possibleMoves = basicRules.proximity(builder);

        for(int i =0; i < possibleMoves.size(); i++) {

            Square position = possibleMoves.get(i);
            Builder opponentBuilder = position.getBuilder();

            //al posto di getValue() controlliamo getBuilder() ma prima vorrei fare più test sugli square
            if(position.getValue() == 1 && !(opponentBuilder.getColour().equals(builder.getColour()))){

                    int builderX = builder.getPosition().x;
                    int builderY = builder.getPosition().y;

                    int positionX = position.x;
                    int positionY = position.y;

                    int a = 2 * builderX - positionX;
                    int b = 2 * builderY - positionY;

                    try{
                        if(board.fullMap[a][b].getValue() != 0) {
                            possibleMoves.remove(i);
                            i--;
                        }
                    }catch(ArrayIndexOutOfBoundsException e){   //controllare che sia l'exception giusto'
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
        basicRules.setMaxHeight(1);
        possibleMoves = new ArrayList<>();
    }
}
