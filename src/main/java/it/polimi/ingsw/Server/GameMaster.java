package it.polimi.ingsw.Server;

import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.Server.Controller.GameInitializer;

import java.io.IOException;
import java.util.List;

public class GameMaster implements Observer {
private List<GameInitializer> gameInitializers;

    @Override
    public Integer update(Client newClient) throws IOException {

        if (gameInitializers.isEmpty()){
            GameInitializer gameInitializer = new GameInitializer(newClient);
            gameInitializers.add(gameInitializer);
        }
        else {
            Integer outcome = gameInitializers.get(0).addPlayer(newClient);
            if(outcome == 0){
                gameInitializers.remove(0);
                return 0;
            }
        }

        if(gameInitializers.get(0).checkStatus()){
            gameInitializers.get(0).dealCards();
        }

        return 1;
    }
}
