package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.Server.Controller.GameInitializer;

import java.util.List;

public class GameMaster implements Observer {
public Lobby lobby;
private List<GameInitializer> gameInitializers;


    @Override
    public void update() {

        if (gameInitializers.isEmpty()){

            Client client = lobby.getFirstClient();
            GameInitializer gameInitializer = new GameInitializer();


        }

    }
}
