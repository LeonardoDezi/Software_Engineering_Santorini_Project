package it.polimi.ingsw.Server;

import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.Server.Controller.GameInitializer;

import java.io.IOException;
import java.util.*;

/**
 * this class manages the multiple games initialization.
 */
public class GameMaster implements Observer {

    /**
     * list of games waiting for players.
     */
    private ArrayList<GameInitializer> gameInitializers = new ArrayList<GameInitializer>();

    /**
     * update method from the observer pattern, receives the newly connected clients.
     * @param newClient is the newly connected client.
     * @return "1" if the client is correctly added to the game, "0" otherwise.
     * @throws IOException from the methods called.
     */
    @Override
    public Integer update(Client newClient) throws IOException{

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
            GameInitializer game =gameInitializers.get(0);
            gameInitializers.remove(0);
            if (gameInitializers.isEmpty()){
                System.out.println("Match started");
            }
            game.dealCards();
            if (game.getGame().getDisconnect()){
                game.disconnectAll();
            }else {
                game.setPlayers();
                if (game.getGame().getDisconnect()){
                    game.disconnectAll();
                }
                game.setBuilders();
                if (game.getGame().getDisconnect()){
                    game.disconnectAll();
                }else {
                    game.startGame();
                }
            }
        }

        return 1;
    }
}
