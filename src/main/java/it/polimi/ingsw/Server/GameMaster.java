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
            if (gameInitializer.getGame().getDisconnect()){
                gameInitializer.getGame().disconnectClients();
                return 0;
            }
            gameInitializers.add(gameInitializer);
        }
        else {
            Integer outcome = gameInitializers.get(0).addPlayer(newClient);
            if(outcome == 0){
                System.out.print("Problem in adding player\n");
                return 0;
            }
        }

        if(gameInitializers.get(0).checkStatus()){
            gameInitializers.get(0).dealCards();
            if (gameInitializers.get(0).getGame().getDisconnect()){
                gameInitializers.get(0).disconnectAll();
                gameInitializers.remove(0);
            }else {
                gameInitializers.get(0).setPlayers();
                if (gameInitializers.get(0).getGame().getDisconnect()){
                    gameInitializers.get(0).disconnectAll();
                    gameInitializers.remove(0);
                }
                gameInitializers.get(0).setBuilders();
                if (gameInitializers.get(0).getGame().getDisconnect()){
                    gameInitializers.get(0).disconnectAll();
                    gameInitializers.remove(0);
                }else {
                    gameInitializers.get(0).startGame();
                }
            }
        }

        return 1;
    }
}
