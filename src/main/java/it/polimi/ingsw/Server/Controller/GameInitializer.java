package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Parser.Sender;
import it.polimi.ingsw.Server.Client;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.VirtualView.NetInterface;

import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw.Server.SantoriniApp.*;



//sendMessage("Sei il dealer. scegli tre carte) ???
//ArrayList<Integer> cards = netInterface.getCards(player?);
//int chosenCard = netInterface.getChosenCard(possibleCard, player);
//messaggio per confermare che la carta è stata scelta?

public class GameInitializer implements Runnable {

    private Client firstPlayer;
    private String firstPlayerName;
    private Integer clientID;
    private NetInterface netInterface;
    private Game game;
    public static final String COLOUR1 = "red";
    public static final String COLOUR2 = "blue";
    public static final String COLOUR3 = "white";

    public GameInitializer(Client client) throws IOException {
        this.firstPlayer = client;
        firstPlayerName = firstPlayer.getUsername();
        this.clientID = client.clientID;
        this.run();
    }

    /**
     * Run method to start the class.
     */
    @Override
    public void run() {
        this.netInterface = new NetInterface();
        netInterface.addClient(firstPlayer);
        Integer numberOfPlayers = 0;
        try {
            numberOfPlayers = netInterface.getNumberOfPlayers(firstPlayer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (numberOfPlayers == -1){
             game = new Game(numberOfPlayers,netInterface);
             game.setGameEnded(true);
             game.setDisconnect(true);
             return;
        }
        game = new Game(numberOfPlayers, netInterface);
        netInterface.setGame(game);
        Dealer player1 = new Dealer(firstPlayerName, COLOUR1, game, clientID);
        int outcome = game.addPlayer(player1);  //testare che non ci dia problemi quando facciamo addPlayer
        if (outcome == 0) {
            //TODO send to the client "error in match creation, please retry"
            return;
        }
        return;
    }

    /**
     * Adds a new player to the game and links it to the users client.
     * @param client is the new client logged for the match.
     * @return "1" if the operation is successful.
     * @throws IOException
     */
    public int addPlayer(Client client) throws IOException {
        int outcome;
        int playersInGame = game.getPlayerList().size();
        if (playersInGame == 1) {
            Player player = new Player(client.getUsername(), COLOUR2, game, client.clientID);
            for (int i=0; i<game.getPlayerList().size(); i++){
                if (player.playerID.equals(game.getPlayerList().get(i).playerID)){
                    Sender.send("89@",client.getSocket());
                    return 0;
                }
            }

            outcome = game.addPlayer(player);
            if (outcome == 1) {
                netInterface.addClient(client);
            }
        }
        else {

            Player player = new Player(client.getUsername(), COLOUR3, game, client.clientID);
            for (int i=0; i<game.getPlayerList().size(); i++) {
                if (player.playerID.equals(game.getPlayerList().get(i).playerID)) {
                    Sender.send("89@", client.getSocket());
                    return 0;
                }
            }
            outcome = game.addPlayer(player);
            if (outcome == 1) {
                netInterface.addClient(client);
            }
        }
        if (outcome == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * Checks if the players that have logged are enough to start the match.
     * @return "1" if the number of players is correct, "0" otherwise.
     */
    public boolean checkStatus() {
        return game.getPlayerList().size() == game.numberOfPlayers;
    }

    /**
     * Sets as first player the player that is going to be the first to move.
     * @throws IOException
     */
    public void setPlayers() throws IOException {

        Player player = netInterface.askFirstPlayer(firstPlayer, game.getPlayerList());    //questo deve essere il giocatore ritornato lo inizializzo per compilare
        if (player.clientID == -1){
            game.setDisconnect(true);
            game.setGameEnded(true);
        }
        game.getPlayerList().remove(player);  //rimuovi il giocatore da playerList. posso anche farlo se mi mandi solo il nome
        game.getPlayerList().add(0, player); //inserisce il giocatore in testa a playerList

    }

    /**
     * Makes the Challenger choose the GodCards that are going to be used for the match and the other players choose the one that they want.
     * @throws IOException
     */
    public void dealCards() throws IOException {

        netInterface.sendNumber();
        // TODO sendMessage("Sei stato scelto dagli Dei per decidere chi parteciperà al gioco. scegli " + game.numberOfPlayers + " carte", firstPlayer)
        ArrayList<Integer> cards = netInterface.getCards(firstPlayer, game.getDeck());

        if (cards.get(0) == -1){
            game.setGameEnded(true);
            game.setDisconnect(true);
            return;
        }

        game.setDealer(game.getPlayerList().get(0));

        //Dealer dealer = (Dealer)game.getPlayerList().get(0);   //l'ho modificato

        if (game.numberOfPlayers == 3)
            game.getDealer().drawCards(cards.get(0), cards.get(1), cards.get(2));    //modificato
        else
            game.getDealer().drawCards(cards.get(0), cards.get(1));   //modificato

        ArrayList<Card> possibleCards = new ArrayList<>();
        for (int i = 0; i < game.getChosenCardsSize(); i++) {
            possibleCards.add(game.getChosenCard(i));
        }


        for (int i = game.numberOfPlayers - 1; i >= 0; i--) {

            int chosenCard = netInterface.getChosenCard(possibleCards, game.getPlayerList().get(i).clientID);
            //TODO da riprovare
            if (netInterface.getClients().size() == game.numberOfPlayers-1){
                game.setGameEnded(true);
                game.setDisconnect(true);
            }else if(chosenCard == -1){
                game.setGameEnded(true);
                game.setDisconnect(true);
            }
            else {
                possibleCards = game.getPlayerList().get(i).chooseCard(possibleCards, chosenCard);
                //messaggio per confermare che la carta è stata scelta?
            }
        }

    }

    /**
     * Makes each Player choose where he wants to place its Workers.
     * @throws IOException
     */
    public void setBuilders() throws IOException {
        ArrayList<Square> possibleSquares;
        for (int i = 0; i < game.numberOfPlayers; i++) {
            Player player = game.getPlayerList().get(i);
            possibleSquares = game.getBasic().getFreeSquares();
            Square square1 = netInterface.getBuilderPlacement(possibleSquares, player.clientID, 1);
            if (square1.x == -1) {
                System.out.print("There has been an error with the recognition of the client, the player has not set the builders");
                return;
            }
            if (square1.x == 20){
                game.setGameEnded(true);
                game.setDisconnect(true);
            }
            square1 = game.getBoard().getSquare(square1.x, square1.y);
            game.deployBuilder(player, square1);

            possibleSquares = game.getBasic().getFreeSquares();
            Square square2 = netInterface.getBuilderPlacement(possibleSquares, player.clientID, 2);
            if (square2.x == 20){
                game.setGameEnded(true);
                game.setDisconnect(true);
            }
            else {
                square2 = game.getBoard().getSquare(square2.x, square2.y);
                game.deployBuilder(player, square2);
            }
        }

    }

    /**
     * Starts the game.
     * @throws IOException
     */
    public void startGame() throws IOException {
        TurnManager myGameManager = new TurnManager(game, netInterface);
        myGameManager.letsPlay();
    }

    public Game getGame(){
        return game;
    }

    //TODO da fare
    public void disconnectAll() throws IOException {
        game.disconnectClients();
    }
}
