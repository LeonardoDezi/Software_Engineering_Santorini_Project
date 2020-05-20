package it.polimi.ingsw.Server.VirtualView;

import com.sun.tools.doclint.Env;
import it.polimi.ingsw.Server.Client;
import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Envelope;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Server.Model.Square;

import java.util.ArrayList;

public class NetInterface {
    private ArrayList<Client> clients;


public Envelope getBothMovementMove(ArrayList<Square> moves1, Builder builder1,ArrayList<Square> moves2, Builder builder2, Player player){
    String message = serialize(moves1, builder1, moves2, builder2);
    //message ->invio

}

public Envelope getMovementMove(){

}

public Envelope getBothBuildingMove(){

}

public Ennvelope



}
