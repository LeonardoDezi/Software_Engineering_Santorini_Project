package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.Builder;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Server.Model.SpecialPhase1;
import it.polimi.ingsw.Server.VirtualView.NetInterface;

import java.io.IOException;
import java.util.ArrayList;

// received != null: quando il giocatore non fa alcuna mossa getMove mi deve restituire received= null



//public Envelope getBothMovementMove(ArrayList<Square> moves1, Builder builder1, ArrayList<Square> moves2, Builder builder2, Player player)
//public Envelope getMovementMove(ArrayList<Square> moves, Builder builder, Player player); P.S. potremmo accorparlo a
//                                                                          getBothMovementMove e mettere
//                                                                             moves2 e builder 2 a null
//public Envelope getBuildMove(ArrayList<Square> moves1, Builder builder, boolean buildDome, Player player);
//public Envelope getBothBuildMove(ArrayList<Square> moves1, Builder builder1, boolean buildDome1, ArrayList<Square> moves2, Builder builder2, boolean buildDome2);


//loseMethod(): questo metodo dovrebbe inviare un messaggio al giocatore che ha perso comunicandogli la sconfitta,
//per poi chiudere la connessione. Non so come chiudere la connessione quindi non so specificare il prototipo

//broadcastMessage(String message): manda in broadcast il messaggio contenuto in message
//updateBoard(Board board): aggiorna in broadcast tutte le board dei giocatori

public class TurnManager {

    private final Game game;
    private final ArrayList<Player> playerList;
    private final NetInterface netInterface;


    public TurnManager(Game game, NetInterface netInterface){
        this.game = game;
        this.netInterface = netInterface;
        this.playerList = game.getPlayerList();
    }

    public void letsPlay() throws IOException {

        Builder builder1;
        Builder builder2;

        netInterface.startGame();


        while(!(game.getGameEnded())) {

            for (Player player : playerList) {  //succederà qualcosa se nel mentre rimuoviamo un giocatore?   cambierà la playerList di game?

                builder1 = player.getBuilder(0);

                try {
                    builder2 = player.getBuilder(1);
                } catch (ArrayIndexOutOfBoundsException e) {
                    builder2 = null;
                }


                Context context = new Context(netInterface);
                context.setPhase(new SpecialPhase1(game, context, player, builder1, builder2));

                while (!(game.getGameEnded()) && context.getPhase() != null)
                    context.request();

                if(game.getGameEnded())
                    break;

            }
        }

        endGame();
    }


    public void endGame(){
        //sendMessage("La partita è finita. Ha vinto game.currentPlayer!!!, null);
    }



}

