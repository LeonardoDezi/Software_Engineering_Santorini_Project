package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Server.Model.Card;
import it.polimi.ingsw.Server.Model.Phase;
import it.polimi.ingsw.Server.Model.Square;

import java.util.ArrayList;

public class ViewLogic {
    private Card card;


    public ViewLogic(Card card) {
        this.card = card;
    }

    public Square chooseMove(Phase phase, ArrayList<Square> moves){
        Square choosenMove = null;
        if(phase.equals("phase1")){
            choosenMove=ViewPhase1.chooseMove();
        }
        if (phase.equals("movementPhase")){

        }
        if (phase.equals("phase2")){

        }
        if (phase.equals("buildingPhase")){

        }
        if(phase.equals("phase3")){

        }
        return choosenMove;
    }
}
