package it.polimi.ingsw.Server;

import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.Server.Controller.GameInitializer;

import java.io.IOException;
import java.util.*;

public class GameMaster implements Observer {
private ArrayList<GameInitializer> gameInitializers = new ArrayList<GameInitializer>();

    @Override
    public Integer update(Client newClient) throws IOException, InterruptedException {

        if (gameInitializers.size()==0){
            GameInitializer gameInitializer = new GameInitializer(newClient);
            gameInitializers.add(gameInitializer);
        }
        else {
            Integer outcome = gameInitializers.get(0).addPlayer(newClient);
            if(outcome == 0){
                gameInitializers.remove(0);
                System.out.print("Problem in adding player");
                return 0;
            }
        }

        if(gameInitializers.get(0).checkStatus()){
            gameInitializers.get(0).dealCards();
            gameInitializers.get(0).setPlayers();
            gameInitializers.get(0).setBuilders();
            gameInitializers.get(0).startGame();
        }

        return 1;
    }
}
