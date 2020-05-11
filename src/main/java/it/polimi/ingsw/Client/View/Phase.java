package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Server.Model.Card;
import it.polimi.ingsw.Server.Model.Square;

import java.util.ArrayList;

public interface Phase {

    public Square chooseMove(ArrayList<Square> moves, Card card);

}
