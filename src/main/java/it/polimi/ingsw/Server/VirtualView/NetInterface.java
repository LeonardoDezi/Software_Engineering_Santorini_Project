package it.polimi.ingsw.Server.VirtualView;

import com.sun.tools.doclint.Env;
import it.polimi.ingsw.Server.Client;
import it.polimi.ingsw.Server.Model.*;
import sun.nio.ch.Net;

import java.util.ArrayList;

public class NetInterface {
    private ArrayList<Client> clients;
    private Game game;

    public NetInterface(Game game){
        this.game = game;
    }


public Envelope getBothMovementMove(ArrayList<Square> moves1, Builder builder1,ArrayList<Square> moves2, Builder builder2, Player player){
    String message = serialize(moves1, builder1, moves2, builder2);
    //message ->invio

}

public Envelope getMovementMove(){

}

public Envelope getBothBuildingMove(){

}

public Envelope getBuildingMove(){

}

public void sendMessage(String message, Client client){
    if(client == null){
        for(int i=0; i<)
    }
}

public void addClient(Client client){
        this.clients.add(client);
}



}
