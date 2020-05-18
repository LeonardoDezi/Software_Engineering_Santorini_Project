package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase3 {

    private final Game game;
    private final Board board;
    private final BasicRules basicRules;
    private HashMap<String, Runnable> movesCommands;
    private HashMap<String, Runnable> actionCommands;

    private Card card;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    private Square position;

    private Player player;



    public SpecialPhase3(Game game){
        basicRules = game.getRules();   // assicurarsi che siano sempre le stesse rules
        this.game = game;
        this.board = game.getBoard();
        map();
    }




    public void map(){
        movesCommands = new HashMap<>();
        actionCommands = new HashMap<>();
//getMoves
        movesCommands.put(null, ()->{possibleMoves = new ArrayList<>();});
        movesCommands.put("notSameSquare", this::notSameSquare); //Demeter
        movesCommands.put("sameSquareNotDome", this::sameSquareNotDome); //hephaestus
        movesCommands.put("notPerimeter", this::notPerimeter);
//actionMethod
        actionCommands.put(null, ()->{});
    }


    public ArrayList<Square> getMoves(Player player, Builder builder, Square lastPosition){

        this.player = player;
        this.builder = builder;
        this.card = player.getCard();
        this.position = lastPosition;

        possibleMoves= basicRules.getBuildingRange(builder);
        movesCommands.get(card.parameters.specialPhase3Moves).run();

        return possibleMoves;
    }

    //getMoves
    public void notSameSquare(){
        for(int i = 0; i < possibleMoves.size(); i++) {
            if (possibleMoves.get(i).equals(position)) {
                possibleMoves.remove(i);
                i--;
            }
        }
    }

    public void sameSquareNotDome(){
        possibleMoves = new ArrayList<>();
        if(position.getLevel() < 3)   // per rispetto dell'atomicità dovremmo dividere questo metodo in due dato che magari qualche carta potrebbe costruire la cupola
            possibleMoves.add(position);
    }

    public void notPerimeter(){
        for(int i =0; i < possibleMoves.size(); i++) {
            Square pos = possibleMoves.get(i);
            if (pos.x == 0 || pos.y == 0 || pos.x == 5 || pos.y == 5) {
                possibleMoves.remove(i);
                i--;
            }
        }
    }

    public void actionMethod(Builder builder, Square position, boolean isDome){
        this.builder = builder;
        this.position = position;

        actionCommands.get(card.parameters.specialPhase3Action).run();
        basicRules.build(player, position, isDome);
    }



}
