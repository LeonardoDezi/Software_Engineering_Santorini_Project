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
    /** represents the list of the players that play in the game */
    private final ArrayList<Player> playerList;
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
        this.playerList = game.getPlayerList();
    }


    /**
     * manages the beginning of the game and its regular execution until its ending
     */
    public void letsPlay() throws IOException {

        Builder builder1;
        Builder builder2;

        netInterface.startGame();

        while(!(game.getGameEnded())) {

            for (Player player : playerList) {  //TODO testare se succederà qualcosa se nel mentre rimuoviamo un giocatore?   cambierà la playerList di game?

                builder1 = player.getBuilder(0);

                builder2 = player.getBuilder(1);

                Context context = new Context(netInterface);
                context.setPhase(new SpecialPhase1(game, context, player, builder1, builder2));

                while (!(game.getGameEnded()) && context.getPhase() != null)
                    context.request();

                if (game.getGameEnded())
                    break;

            }
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

