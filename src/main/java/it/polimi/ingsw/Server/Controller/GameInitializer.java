package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Client;
import it.polimi.ingsw.Server.Model.Dealer;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Server.Model.Square;

import java.util.ArrayList;

import static it.polimi.ingsw.Server.ServerApp.*;



//sendMessage("Sei il dealer. scegli tre carte) ???
//ArrayList<Integer> cards = netInterface.getCards(player?);
//int chosenCard = netInterface.getChosenCard(possibleCard, player);

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
        int numberOfPlayers;
        Game game = new Game(numberOfPlayers);
        this.game = game;
        Dealer player1 = new Dealer(firstPlayerName, COLOUR1, game, clientID);
        int outcome = game.addPlayer(player1);  //testare che non ci dia problemi quando facciamo addPlayer
        if(outcome == 0){
            //TODO send to the client "error in match creation, please retry"
            return;
        }
        //TODO ask the first player which cards he wants to choose
        TurnManager myGameManager = new TurnManager(game);
    }

    public int addPlayer(Client client){
        int outcome;
        int playersInGame = game.getPlayerList().size();
        if(playersInGame==1){
            Player player = new Player(client.getUsername(), COLOUR2, game, client.clientID);
            outcome = game.addPlayer(player);
        }
        else{
            Player player = new Player(client.getUsername(), COLOUR3, game, client.clientID);
            outcome = game.addPlayer(player);
        }
        if(outcome == 0){
            return 0;
        }
        return 1;
    }

    public boolean checkStatus(){
        return game.getPlayerList().size() == game.numberOfPlayers;
    }

    public void dealCards(){

        //Il client avrà già il mazzo di carte?
        //sendMessage("Sei il dealer. scegli tre carte) ???
        //ArrayList<Integer> cards = netInterface.getCards(player?);
        Dealer dealer = (Dealer)game.getPlayerList().get(0);
        dealer.chooseCards(cards.get(0), cards.get(1), cards.get(2));
        //possibleCards = copy of game.chosenCards;

        for(Player player: game.getPlayerList()){
            //int chosenCard = netInterface.getChosenCard(possibleCards, player);


        }


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
