package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Server.Model.Card;
import it.polimi.ingsw.Server.Model.Square;

import java.util.ArrayList;

public class ViewPhase1 implements Phase {

    //cards are Charon and Prometheus
    @Override
    public Square chooseMove(ArrayList<Square> moves, Card card) {
        Square choosenMove = null;
        if(card.name.equals("Prometheus")){
            //stampare a schermo
        }

        return choosenMove;
    }

}
