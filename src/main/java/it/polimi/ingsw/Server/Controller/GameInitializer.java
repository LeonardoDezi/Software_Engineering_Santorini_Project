package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Client;
import it.polimi.ingsw.Server.Model.Dealer;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Server.Model.Square;

import java.util.ArrayList;

import static it.polimi.ingsw.Server.ServerApp.*;

public class GameInitializer implements Runnable{

    private Client firstPlayer;
    private String firstPlayerName;
    private Integer clientID;
    private Game game;

    public GameInitializer(Client client){
        this.firstPlayer = client;
        firstPlayerName = firstPlayer.getUsername();
        this.clientID = client.clientID;
        this.run();
    }


    @Override
    public void run() {
        //TODO ask the player the number of players
        // virtualView. sendmessage("Please insert the number of player")
        // wait for virtualView
        Integer numberOfPlayers;
        Game game = new Game(numberOfPlayers);
        this.game = game;
        Dealer player1 = new Dealer(firstPlayerName, RED, game, clientID);
        Integer outcome = game.addPlayer(player1);
        if(outcome == 0){
            //TODO send to the client "error in match creation, please retry"
            return;
        }
        //TODO ask the first player which cards he wants to choose
        TurnManager myGameManager = new TurnManager(game);
    }

    public Integer addPlayer(Client client){
        Integer outcome;
        Integer playersInGame = game.getPlayerList().size();
        if(playersInGame==1){
            Player player = new Player(client.getUsername(), BLUE, game, client.clientID);
            outcome = game.addPlayer(player);
        }
        else{
            Player player = new Player(client.getUsername(), WHITE, game, client.clientID);
            outcome = game.addPlayer(player);
        }
        if(outcome == 0){
            return 0;
        }
        return 1;
    }

    public boolean checkStatus(){
        if(game.getPlayerList().size()==game.numberOfPlayers){
            return true;
        }
        return false;
    }

    public void dealCards(){

    //TODO implement the method to choose and deal the cards



        this.setBuilders();
    }

    public void setBuilders(){
        ArrayList<Square> possibleSquares;
        for(int i=0; i<game.numberOfPlayers; i++){
            Player player = game.getPlayerList().get(game.numberOfPlayers-i);
            possibleSquares=game.getBasic().getFreeSquares();
            //TODO ask the player where he wants to place the builders
            //Square square1 = netinteface.getBuilderplacement
            //Square square2 = netinteface.getBuilderplacement
            game.deployBuilder(player, square1);
            game.deployBuilder(player, square2);
        }
    }


}
