package it.polimi.ingsw.Server.Model;
//penso che dovremmo mettere un falg che comunichi quando una partita è stata vinta

//ci manca la rimozione delle pedine


import java.util.ArrayList;

/**
 * represents the game. this class is used as root of the model to connect every entity inside of it.
 * @version 1.0
 * @since 1.5
 */
public class Game {
    //rivedere tutti i private, protected e public
    private boolean gameEnded;
    protected Board gameBoard;  //lo lasciamo private?
    protected ArrayList<Player> playerList;
    private ArrayList<Card> chosenCards;
    private ArrayList<Card> deck;     //lo lasciamo?
    private Dealer dealer;
    public final Integer numberOfPlayers;
    protected BasicRules basic;

    /**
     * creates a new game.
     */
    public Game(Integer numberOfPlayers){     //da mettere un identificativo partita?
        gameBoard = new Board();
        gameEnded = false;
        playerList = new ArrayList<>();
        chosenCards = new ArrayList<>();
        basic = new BasicRules(gameBoard, this);
        this.numberOfPlayers=numberOfPlayers;

    }


    public ArrayList<Player> getPlayerList() {
       return this.playerList;
    }

    public boolean getGameEnded(){return this.gameEnded;}

    public void setGameEnded(boolean value){this.gameEnded = value;}

    /**
     * adds a new player to the game.
     * @param player is the player object.
     */
    public Integer addPlayer(Player player){

        if(this.playerList.size()<numberOfPlayers){
            this.playerList.add(player);
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * removes a player from the game.
     * @param player is the player id.
     */
    public void removePlayer(Player player){

   //     try {
            player.removeBuilder(0);
            player.removeBuilder(1);
            playerList.remove(player);

            //player = null; necessario?
/*        }catch(NoSuchElementException e){
            System.out.println("There's no player registered with the name " + player.getPlayerID());
        }   mi sa che non servirà*/

    }

    /**
     * is used to get the deck of all the god cards.
     * @return the deck of cards.
     */
    public ArrayList<Card> getDeck() {
        return deck;
    }

    public BasicRules getBasic(){
        return basic;
    }



    /**
     * adds the card chosen by the dealer at the beginning of the game to the list of the available cards for the other players.
     * @param cardNumber is the number that identifies the card choosen by the dealer.
     */
    public void addChosenCard(Integer cardNumber){    // da aggiungere all'UML
        Card card = deck.get(cardNumber);
        chosenCards.add(card);
    }


    // arrivati a questo punto siamo certi che la posizione data è possibile?

    /**
     * puts a new builder on the board.
     * @param player is the player that is deploying the builder.
     * @param placement is the square where the player wants to put the builder
     */
    public void deployBuilder(Player player, Square placement){
        Integer x = placement.x;
        Integer y = placement.y;
        if(player.getBuilderSize() == 2){
            System.out.println("Error:" + player.getPlayerID() + "has already deployed all the builders");
        } else {
            if(gameBoard.fullMap[x][y].getValue() == 1)
                System.out.println("Error: Square already occupied by another player");
            else
                player.addBuilder(placement);   //aggiungi alla lista dei builder del giocatore
            System.out.println("Builder deployed");  //non possiamo lasciare questo
        }

    }

    public Card getChosenCard(int cardNumber){
        return chosenCards.get(cardNumber);
    }

    public Board getBoard(){ return this.gameBoard;}   // immagino serva anche questo

    public BasicRules getRules(){
        return this.basic;
    }
}
