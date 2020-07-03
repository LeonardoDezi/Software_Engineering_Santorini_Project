package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Server.Model.SpecialPhase1;
import it.polimi.ingsw.Server.VirtualView.NetInterface;

import java.io.IOException;
import java.util.ArrayList;


/**
 * this class manages the players' turns and the turn's current phase during the game
 */
public class TurnManager {
    /** represents the game that is currently being played */
    private final Game game;

    /** represents the interface used by the server to communicate with the clients */
    private final NetInterface netInterface;


    /**
     * creates a new TurnManager for the game currently played
     * @param game is the game currently played
     * @param netInterface is the interface used by the server to communicate with the clients
     */
    public TurnManager(Game game, NetInterface netInterface){
        this.game = game;
        this.netInterface = netInterface;
    }


    /**
     * manages the beginning of the game and its regular execution until its ending
     */
    public void letsPlay() throws IOException {

        Builder builder1;
        Builder builder2;

        netInterface.startGame();

        Player player = game.getPlayerList().get(0);

        while(!(game.getGameEnded())) {

            builder1 = player.getBuilder(0);

            builder2 = player.getBuilder(1);

            Context context = new Context(netInterface);
            context.setPhase(new SpecialPhase1(game, context, player, builder1, builder2));

            while (!(game.getGameEnded()) && context.getPhase() != null)
                context.request();

            if (!game.getGameEnded())
                player = game.getNextPlayer(player);


        }


        if (game.getDisconnect()){
            game.disconnectClients();
        }
        else {
            endGame();
        }
    }

    /**
     * manages the ending of the game
     */
    public void endGame() throws IOException {
        for(int i=0; i<game.getPlayerList().size(); i++){
            if(game.getPlayerList().get(i).equals(game.getWinningPlayer())){
                netInterface.winner(game.getPlayerList().get(i));
            }
            else{
                netInterface.loseMethod(game.getPlayerList().get(i), game.getWinningPlayer().playerID);
            }
        }
    }



}

