package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Client;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.VirtualView.NetInterface;

import java.util.ArrayList;

import static it.polimi.ingsw.Server.ServerApp.*;



//sendMessage("Sei il dealer. scegli tre carte) ???
//ArrayList<Integer> cards = netInterface.getCards(player?);
//int chosenCard = netInterface.getChosenCard(possibleCard, player);
//messaggio per confermare che la carta è stata scelta?

public class GameInitializer implements Runnable{

    private Client firstPlayer;
    private String firstPlayerName;
    private Integer clientID;
    private NetInterface netInterface;
    private Game game;

    public GameInitializer(Client client){
        this.firstPlayer = client;
        firstPlayerName = firstPlayer.getUsername();
        this.clientID = client.clientID;
        this.run();
    }


    @Override
    public void run() {
        this.netInterface = new NetInterface(game);
        netInterface.addClient(firstPlayer);
        //TODO ask the player the number of players
        // virtualView.sendmessage("Please insert the number of player")
        // wait for virtualView
        int numberOfPlayers = 2; //get this from client
        Game game = new Game(numberOfPlayers);
        this.game = game;
        Dealer player1 = new Dealer(firstPlayerName, COLOUR1, game, clientID);
        int outcome = game.addPlayer(player1);  //testare che non ci dia problemi quando facciamo addPlayer
        if(outcome == 0){
            //TODO send to the client "error in match creation, please retry"
            return;
        }

        // Wait until game.getPlayerList().size() == game.numberOfPlayers;

        dealCards();
        setBuilders();
        TurnManager myGameManager = new TurnManager(game, netInterface);   //possiamo spostarlo? se no in questo punto è inutile
        myGameManager.letsPlay();
    }

    public int addPlayer(Client client){
        int outcome;
        int playersInGame = game.getPlayerList().size();
        if(playersInGame==1){
            Player player = new Player(client.getUsername(), COLOUR2, game, client.clientID);
            outcome = game.addPlayer(player);
            if(outcome==1){
                netInterface.addClient(client);
            }
        }
        else{
            Player player = new Player(client.getUsername(), COLOUR3, game, client.clientID);
            outcome = game.addPlayer(player);
            if(outcome==1){
                netInterface.addClient(client);
            }
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

        //sendMessage("Sei stato scelto dagli Dei per decidere chi parteciperà al gioco. scegli " + game.numberOfPlayers + " carte", firstPlayer)
        ArrayList<Integer> cards = netInterface.getCards(firstPlayer, game.getDeck());
        Dealer dealer = (Dealer)game.getPlayerList().get(0);
        if(game.numberOfPlayers == 3)
            dealer.drawCards(cards.get(0), cards.get(1), cards.get(2));
        else
            dealer.drawCards(cards.get(0), cards.get(1));

        ArrayList<Card> possibleCards = new ArrayList<>();
        for(int i=0; i < game.getChosenCardsSize(); i++) {
            possibleCards.add(game.getChosenCard(i));
        }


        for(int i=game.numberOfPlayers; i>0; i--){
            int chosenCard = netInterface.getChosenCard(possibleCards, game.getPlayerList().get(i).clientID);
            possibleCards = game.getPlayerList().get(i).chooseCard(possibleCards, chosenCard);
            //messaggio per confermare che la carta è stata scelta?
        }

    }

    public void setBuilders(){
        ArrayList<Square> possibleSquares;
        for(int i=0; i<game.numberOfPlayers; i++){
            Player player = game.getPlayerList().get(game.numberOfPlayers-i);
            possibleSquares=game.getBasic().getFreeSquares();
            //TODO ask the player where he wants to place the builders
            Square square1 = netInterface.getBuilderPlacement(possibleSquares, player.clientID, 1);
            if(square1.x == -1){
                System.out.print("There has been an error with the recognition of the client, the player has not set the builders");
                return;
            }
            game.deployBuilder(player, square1);
            possibleSquares=game.getBasic().getFreeSquares();
            Square square2 = netInterface.getBuilderPlacement(possibleSquares, player.clientID, 2);
            game.deployBuilder(player, square2);
        }

    }


}
