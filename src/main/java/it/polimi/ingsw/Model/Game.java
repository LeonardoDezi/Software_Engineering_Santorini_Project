package it.polimi.ingsw.Model;
/*17/04/2020
Ho modificato addPlayer in modo che venissero assegnati direttamente i colori ai vari giocatori. in questo modo non correremo il rischio che due giocatori
abbiano lo stesso colore assegnato.
Red = Giocatore 1
Green = Giocatore 2
Blue = Giocatore 3
 */


//ci manca la rimozione delle pedine


import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * represents the game. this class is used as root of the model to connect every entity inside of it.
 * @version 1.0
 * @since 1.5
 */
public class Game {
    private Board gameBoard;
    protected ArrayList<Player> playerList;
    private ArrayList<Card> chosenCards;
    private Deck deck;     //lo lasciamo?
    private Dealer dealer;
    // identificatore numerico per differenziare le partite?
    private Square position;
    private BasicRules rules;

    /**
     * creates a new game.
     */
    public Game(){     //da mettere un identificativo partita?
        gameBoard = new Board();
        playerList = new ArrayList<>();
        chosenCards = new ArrayList<>();
        rules = new BasicRules(gameBoard, this);
        //dobbiamo decidere se togliere o meno deck. Se lo lasciamo lo dobbiamo implementare
    }

    /**
     * adds a new player to the game.
     * @param name is the id of the player.
     */
    public void addPlayer(String name){
        // E se mettessimo enumerazioni?
        switch(playerList.size()){
            case 0:   //ancora nessun giocatore presente
                playerList.add(new Dealer(name, "Red", this)); //il primo giocatore sarà sempre il dealer
                // dealer = (Dealer) playerList.get(0);  nel caso fosse comodo tenerlo da parte: probabilmente dovremo aggiungere un altro test nel caso
                break;
            case 1:
                playerList.add(new Player(name, "Green", this));
                break;
            case 2:
                playerList.add(new Player(name, "Blue", this));
                break;
            default:
                //se playerList.size() >= 3 non fa niente forse dovremmo mettere qualcosa;
        }

        //Il controller dovrà avere un contatore che tenga conto di quanti giocatori sono presenti nella partita/lobby

    }

    /**
     * removes a player from the game.
     * @param player is the player id.
     */
    public void removePlayer(Player player){

   //     try {
            player.removeBuilders();
            playerList.remove(player);

            //player = null; necessario?
/*        }catch(NoSuchElementException e){
            System.out.println("There's no player registered with the name " + player.getPlayerID());
        }   mi sa che non servirà*/

        return;

    }

    /**
     * is used to get the deck of all the god cards.
     * @return the deck of cards.
     */
    public Deck getDeck() {      // da aggiungere  nell'UML se vogliamo mettere la variabile private
        return deck;
    }

    /**
     * calls the move metod of the board to move a builder.
     * @param playerIndex is the player id of that builder.
     * @param builderId is the id of the builder that is moving.
     * @param x is the x coordinate where the builder is going to move.
     * @param y is the y coordinate where the builder is going to move.
     */
    public void move(int playerIndex, int builderId, int x, int y){
        Player player = playerList.get(playerIndex);   // recupera giocatore: è garantito che playerIndex sia un indice valido
        gameBoard.move(player.getBuilder(builderId), x, y); // stessa cosa qui
    }

    /**
     * calls the move method of the board to build something.
     * @param x is the x coordinate of the square where the player wants to build.
     * @param y is the y coordinate of the square where the player wants to build.
     * @param isDome is set to 1 when the player builds a dome that is not on the 4th floor.
     */
    public void build ( int x, int y, boolean isDome){
        gameBoard.build(x, y, isDome);
    }

    /**
     * adds the card chosen by the dealer at the beginning of the game to the list of the available cards for the other players.
     * @param card is the card choosen by the dealer.
     */
    public void addChosenCard(Card card){    // da aggiungere all'UML
        chosenCards.add(card);
        return;
    }


    // arrivati a questo punto siamo certi che la posizione data è possibile?

    /**
     * puts a new builder on the board.
     * @param player is the player that is deploying the builder.
     * @param x is the x coordinate of the square where the player wants to put the builder
     * @param y is the y coordinate of the square where the player wants to put the builder
     */
    public void deployBuilder(Player player, int x, int y){


        //if(isPlayerTurn == true) {   serve?

        if(player.getBuilderSize() == 2){   //ATTENZIONE: DOBBIAMO ACCERTARCI CHE size() sarà sempre o 0 o 2 in qualche modo
            System.out.println("Error:" + player.getPlayerID() + "has already deployed all the builders");   //non possiamo lasciare questo
            //    isPlayerTurn = false;   Serve? compito del controller magari?
        } else {
            position = gameBoard.fullMap[x][y];  //mettere fullMap public o private?
            if(position.getValue() == 1)
                System.out.println("Error: Square already occupied by another player");
            else
                player.addBuilder(position);   //aggiungi alla lista dei builder del giocatore
            System.out.println("Builder deployed");  //non possiamo lasciare questo
        }

    }
    public Card getChoosenCard(int cardNumber){
        return chosenCards.get(cardNumber);
    }

}
