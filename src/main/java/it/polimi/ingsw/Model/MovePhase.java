package it.polimi.ingsw.Model;

import java.util.HashMap;

public class MovePhase {
    private Rules basicRules;
    protected HashMap <String, Runnable> commands;
    private Card card;


    public Phase1(Card card){
        basicRules = new BasicRules(board, game);
        this.card = card;
        mappa();
    }


    private void mappa() {

        commands = new HashMap<>();

        commands.put("basic", (builder)-> board.move(square1, square2));
        commands.put("athena", ()-> athena() );
        //commands.put("basic", () -> basicRules.getPossibleMoves(builder));
    }

    public void athena(){
        board.move(square1, square2);
        //se si è mossa verso l'alto setta il flag degli altri personaggi a 1
    }


    //nextPhase()
}
