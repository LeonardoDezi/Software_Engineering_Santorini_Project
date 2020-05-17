package it.polimi.ingsw.Server;

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
            GameInitializer gameInitializer = new GameInitializer(client);
            gameInitializers.add(gameInitializer);
        }
        else {
            Client client = lobby.getFirstClient();
            Integer outcome = gameInitializers.get(0).addPlayer(client);
            if(outcome == 0){
                gameInitializers.remove(0);
                lobby.addClient(client);
            }
        }

        if(gameInitializers.get(0).checkStatus()){
            gameInitializers.get(0).dealCards();
        }

    }
}
