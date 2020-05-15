package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialPhase3 {

    private final Game game;
    private final Board board;
    private final BasicRules basicRules;
    private HashMap<String, Runnable> commands;


    private Card card;
    private Builder builder;
    private ArrayList<Square> possibleMoves;

    private Square position;



    public SpecialPhase3(Game game){
        basicRules = game.getRules();   // assicurarsi che siano sempre le stesse rules
        this.game = game;
        this.board = game.getBoard();
        map();
    }




    public void map(){
        commands = new HashMap<>();
//getMoves
        commands.put(null, ()->{possibleMoves = new ArrayList<>();});
        commands.put("notSameSquare", this::notSameSquare);
        commands.put("sameSquareNotDome", this::sameSquareNotDome);
        commands.put("notPerimeter", this::notPerimeter);
    }

    public ArrayList<Square> getMoves(Player player, Builder builder, Square lastPosition){

        this.builder = builder;
        this.card = player.getCard();
        this.position = lastPosition;

        possibleMoves= basicRules.getBuildingRange(builder);
        commands.get(card.parameters.specialPhase3Moves).run();

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



}
